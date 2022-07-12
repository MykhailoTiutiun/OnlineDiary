package com.mykhailotiutiun_projects.onlinediary.services;

import com.mykhailotiutiun_projects.onlinediary.data.entites.EmployeeEntity;
import com.mykhailotiutiun_projects.onlinediary.data.entites.RoleEntity;
import com.mykhailotiutiun_projects.onlinediary.data.entites.StudentEntity;
import com.mykhailotiutiun_projects.onlinediary.data.entites.UserEntity;
import com.mykhailotiutiun_projects.onlinediary.data.repositories.EmployeesRepository;
import com.mykhailotiutiun_projects.onlinediary.data.repositories.RolesRepository;
import com.mykhailotiutiun_projects.onlinediary.data.repositories.StudentsRepository;
import com.mykhailotiutiun_projects.onlinediary.data.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@EnableScheduling
public class UsersService implements UserDetailsService {
    public static final String TEMPORAL_DIRECROR_PASSWORD = "12345";
    @PersistenceContext
    private EntityManager em;
    @Autowired
    UsersRepository userRepository;
    @Autowired
    RolesRepository roleRepository;
    @Autowired
    StudentsRepository studentsRepository;
    @Autowired
    EmployeesRepository employeesRepository;

    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByName(username);

        return user;
    }

    @Bean
    public BCryptPasswordEncoder getBCryptPasswordEncoder(){
        return bCryptPasswordEncoder;
    }
    public UserEntity getUserByName(String name){return userRepository.findByName(name);}
    public UserEntity getUserById(Long userId) {
        Optional<UserEntity> userFromDb = userRepository.findById(userId);
        return userFromDb.orElse(new UserEntity());
    }

    public boolean verifyUser(String name, String password, RoleEntity requestRole){
        return bCryptPasswordEncoder.matches(password, getUserByName(name).getPassword()) && getUserByName(name).getRoles().contains(requestRole);
    }

    public boolean changePassword(String name, String prevPassword, String newPassword){
        if(verifyUser(name, prevPassword, roleRepository.findByName("ROLE_USER"))){
            UserEntity user = userRepository.findByName(name);
            userRepository.delete(user);
            user.setPassword(newPassword);
            user.setPasswordConfirm(newPassword);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    public boolean addUser(UserEntity user) {
        UserEntity userFromDB = userRepository.findByName(user.getUsername());

        if (userFromDB != null) {
            return false;
        }

        user.setInitDate(LocalDate.now());
        user.addRole(roleRepository.findByName("ROLE_USER"));
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        if(!user.isEmployee()) {
            studentsRepository.save(new StudentEntity(user.getName()));
        } else {
            employeesRepository.save(new EmployeeEntity(user.getName()));

            if (bCryptPasswordEncoder.matches(TEMPORAL_DIRECROR_PASSWORD, user.getPassword())) {
                user.addRole(roleRepository.findByName("ROLE_EMPLOYEE"));
                user.addRole(roleRepository.findByName("ROLE_HEAD_TEACHER"));
                user.addRole(roleRepository.findByName("ROLE_DIRECTOR"));
                user.setVerify(true);
            }
        }

        userRepository.save(user);
        return true;
    }

    public void addRoleToUser(Long id, String roleName){
        UserEntity user = getUserById(id);
        userRepository.delete(user);
        user.addRole(roleRepository.findByName(roleName));
        userRepository.save(user);
    }

    public void removeRoleToUser(Long id, String roleName){
        UserEntity user = getUserById(id);
        userRepository.delete(user);
        user.removeRole(roleRepository.findByName(roleName));
        userRepository.save(user);
    }

    public boolean deleteUser(Long userId) {
        if (userRepository.findById(userId).isPresent()) {
            String name = userRepository.findById(userId).get().getName();
            if(!userRepository.findById(userId).get().isEmployee()){
                studentsRepository.delete(studentsRepository.findByName(name));
            } else {
                employeesRepository.delete(employeesRepository.findByName(name));
            }

            userRepository.deleteById(userId);
            return true;
        }
        return false;
    }

    public boolean checkUser(Long userId) {
        if(getUserById(userId) != null){
            UserEntity user = getUserById(userId);
            userRepository.delete(user);
            user.setVerify(true);
            if(user.isEmployee()){
                user.addRole(roleRepository.findByName("ROLE_EMPLOYEE"));
            } else {
                user.addRole(roleRepository.findByName("ROLE_STUDENT"));
            }
            userRepository.save(user);
            return true;
        }
        return false;
    }

//    public List<UserEntity> usergtList(Long idMin) {
//        return em.createQuery("SELECT u FROM UserEntity u WHERE u.id > :paramId", UserEntity.class)
//                .setParameter("paramId", idMin).getResultList();
//    }

    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.DAYS)
    private void autoDeleteNonVerifyUsers(){
        List<UserEntity> userEntities = getAllUsers().stream().filter(userEntity -> !userEntity.isVerify() && userEntity.getInitDate().plusDays(30).isBefore(LocalDate.now())).toList();
        userEntities.forEach(user -> deleteUser(user.getId()));
    }

    @Scheduled(fixedRate = 12, timeUnit = TimeUnit.HOURS)
    private void autoDeleteTemporalUsers(){
        List<UserEntity> userEntities = getAllUsers().stream().filter(userEntity -> bCryptPasswordEncoder.matches(TEMPORAL_DIRECROR_PASSWORD, userEntity.getPassword()) && userEntity.getInitDate().plusDays(1).isBefore(LocalDate.now())).toList();
        userEntities.forEach(user -> deleteUser(user.getId()));
    }
}
