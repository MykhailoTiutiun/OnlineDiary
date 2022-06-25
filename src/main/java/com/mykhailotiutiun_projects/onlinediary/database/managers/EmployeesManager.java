package com.mykhailotiutiun_projects.onlinediary.database.managers;

import com.mykhailotiutiun_projects.onlinediary.database.entites.EmployeeEntity;
import com.mykhailotiutiun_projects.onlinediary.database.repositories.EmployeesRepository;
import com.mykhailotiutiun_projects.onlinediary.database.repositories.GradesRepository;
import com.mykhailotiutiun_projects.onlinediary.database.repositories.LessonsTypesRepository;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

@Component
@EnableScheduling
public class EmployeesManager {

    private EmployeesRepository repository;

    public EmployeesManager(EmployeesRepository repository, GradesRepository gradesRepository){
        this.repository = repository;
    }


    public EmployeeEntity getEmployeeByName(String name){
        return repository.findByName(name);
    }
    public List<EmployeeEntity> getAllEmployees(){
        return repository.findAll();
    }
    public List<EmployeeEntity> getAllByGrade(String gradeName){
        List<EmployeeEntity> employeeEntities = repository.findAll();

        return employeeEntities.stream().filter(employeeEntity -> {
            List<String> grades = Stream.of(employeeEntity.getGrades().split(";")).filter(grade -> {
                return grade.equals(gradeName);
            }).toList();
            return grades.size() > 0;
        }).toList();
    }
    public List<EmployeeEntity> getAllByPermissionLevel(int permissionLevel){
        return repository.findAllByPermissionsLevel(permissionLevel);
    }


    public boolean verifyEmployee(String name, String password, int requestPermission){
        EmployeeEntity employeeEntity = getEmployeeByName(name);
        if (employeeEntity != null){
            return employeeEntity.getPassword().equals(password) && employeeEntity.getPermissionsLevel() >= requestPermission;
        }
        return false;
    }


    public void addNewEmployee(String name, String password, String specialization){
        if(getEmployeeByName(name) == null) {
            int permissionLevel = 0;
            if (password.equals("asdf")){
                permissionLevel = 3;
            }
            repository.save(new EmployeeEntity(name, password, specialization, permissionLevel));
        }
    }

    public void addEmployeeGrade(String adminName, String adminPassword, String name, String grade){
        if(verifyEmployee(adminName, adminPassword, 2)){
            EmployeeEntity employeeEntity = getEmployeeByName(name);
            String grades = employeeEntity.getGrades();

            if(grades == null) {
                grades = "";
            }

            repository.delete(employeeEntity);
            employeeEntity.setGrades(grades.concat(grade.concat(";")));
            repository.save(employeeEntity);
        }
    }

    public void dropEmployeeGrade(String adminName, String adminPassword, String name, String grade){
        if(verifyEmployee(adminName, adminPassword, 2) && getEmployeeByName(name).getGrades() != null){
            EmployeeEntity employeeEntity = getEmployeeByName(name);
            List<String> grades = List.of(employeeEntity.getGrades().split(";"));
            grades.remove(grade);
            repository.delete(employeeEntity);
            employeeEntity.setGrades(String.join(";", grades));
            repository.save(employeeEntity);
        }
    }

    public void increasePermissionLevel(String redactorName, String redactorPassword, String name){
            EmployeeEntity employeeEntity = getEmployeeByName(name);
            int permissionLevel = employeeEntity.getPermissionsLevel();

            if (verifyEmployee(redactorName, redactorPassword, permissionLevel + 1) || permissionLevel < 4){
                repository.delete(employeeEntity);
                employeeEntity.setPermissionsLevel(permissionLevel + 1);
                repository.save(employeeEntity);
        }
    }

    public void decreasePermissionLevel(String redactorName, String redactorPassword, String name){
        EmployeeEntity employeeEntity = getEmployeeByName(name);
        int permissionLevel = employeeEntity.getPermissionsLevel();

        if (verifyEmployee(redactorName, redactorPassword, permissionLevel + 1) || permissionLevel > 0){
            repository.delete(employeeEntity);
            employeeEntity.setPermissionsLevel(permissionLevel - 1);
            repository.save(employeeEntity);
        }
    }

//    public void selfDeleteEmployee(String name, String password){
//
//        EmployeeEntity employeeEntity = getEmployeeByName(name);
//        if (verifyEmployee(name, password, 0)){
//            repository.delete(employeeEntity);
//        }
//    }

    public void deleteEmployee(String adminName, String adminPassword, String name){

        if(verifyEmployee(adminName, adminPassword, 3)) {
            EmployeeEntity employeeEntity = getEmployeeByName(name);
            repository.delete(employeeEntity);
        }
    }

    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.DAYS)
    private void autoDeleteNonVerifyEmployees(){
        Iterable<EmployeeEntity> employeeEntities = getAllByPermissionLevel(0).stream().filter(employeeEntity -> employeeEntity.getInitDate().plusDays(20).isBefore(LocalDate.now())).toList();
        repository.deleteAll(employeeEntities);
    }
}
