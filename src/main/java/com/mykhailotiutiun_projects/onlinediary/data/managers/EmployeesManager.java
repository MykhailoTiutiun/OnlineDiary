package com.mykhailotiutiun_projects.onlinediary.data.managers;

import com.mykhailotiutiun_projects.onlinediary.data.entites.EmployeeEntity;
import com.mykhailotiutiun_projects.onlinediary.data.entites.RoleEntity;
import com.mykhailotiutiun_projects.onlinediary.data.repositories.EmployeesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
public class EmployeesManager {

    @Autowired
    private EmployeesRepository repository;

    @Autowired
    private UsersManager usersManager;


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

    public void addNewEmployee(String name){
        if(getEmployeeByName(name) == null) {
            repository.save(new EmployeeEntity(name));
        }
    }

    public void addEmployeeGrade(String adminName, String adminPassword, String name, String grade){
        if(usersManager.verifyUser(adminName, adminPassword, new RoleEntity(4L, "ROLE_ADMIN"))){
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
        if(usersManager.verifyUser(adminName, adminPassword, new RoleEntity(4L, "ROLE_ADMIN")) && getEmployeeByName(name).getGrades() != null){
            EmployeeEntity employeeEntity = getEmployeeByName(name);
            List<String> grades = List.of(employeeEntity.getGrades().split(";"));
            grades.remove(grade);
            repository.delete(employeeEntity);
            employeeEntity.setGrades(String.join(";", grades));
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

    public void deleteEmployee(String name){
        if(getEmployeeByName(name) != null) {
            repository.delete(getEmployeeByName(name));
        }
    }

}
