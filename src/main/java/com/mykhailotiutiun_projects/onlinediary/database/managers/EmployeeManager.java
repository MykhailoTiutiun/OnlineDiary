package com.mykhailotiutiun_projects.onlinediary.database.managers;

import com.mykhailotiutiun_projects.onlinediary.database.entites.EmployeeEntity;
import com.mykhailotiutiun_projects.onlinediary.database.repositories.EmployeeRepository;
import org.springframework.stereotype.Component;

@Component
public class EmployeeManager {
    private EmployeeRepository employeeRepository;

    public EmployeeManager(EmployeeRepository employeeRepository){
        this.employeeRepository = employeeRepository;
    }

    public void addNewEmployee(String name, String password, String specialization){
        if(employeeRepository.findByName(name) == null) {
            int permissionLevel = 0;
            if (password.equals("asdf")){
                permissionLevel = 3;
            }
            employeeRepository.save(new EmployeeEntity(name, password, specialization, permissionLevel));
        }
    }

    private boolean verifyEmployee(String name, String password, int requestPermission){
        EmployeeEntity employeeEntity = employeeRepository.findByName(name);
        if (employeeEntity != null){
            return employeeEntity.getPassword().equals(password) && employeeEntity.getPermissionsLevel() >= requestPermission;
        }
        return false;
    }

    public EmployeeEntity getEmployeeByName(String name){
        return employeeRepository.findByName(name);
    }

    public Iterable<EmployeeEntity> getAllEmployees(){
        return employeeRepository.findAll();
    }

    public void setEmployeeClassrooms(String adminName, String adminPassword, String name, String classroom){
        if(verifyEmployee(adminName, adminPassword, 2)){
            EmployeeEntity employeeEntity = employeeRepository.findByName(name);
            String classrooms = employeeEntity.getClassrooms();

            if(classrooms == null) {
                classrooms = "";
            }
            employeeRepository.delete(employeeEntity);
            employeeEntity.setClassrooms(classrooms.concat(classroom.concat(";")));
            employeeRepository.save(employeeEntity);
        }
    }

    public void increasePermissionLevel(String redactorName, String redactorPassword, String name){
            EmployeeEntity employeeEntity = employeeRepository.findByName(name);
            int permissionLevel = employeeEntity.getPermissionsLevel();

            if (verifyEmployee(redactorName, redactorPassword, permissionLevel + 1) || permissionLevel < 4){
                employeeRepository.delete(employeeEntity);
                employeeEntity.setPermissionsLevel(permissionLevel + 1);
                employeeRepository.save(employeeEntity);
        }
    }

    public void decreasePermissionLevel(String redactorName, String redactorPassword, String name){
        EmployeeEntity employeeEntity = employeeRepository.findByName(name);
        int permissionLevel = employeeEntity.getPermissionsLevel();

        if (verifyEmployee(redactorName, redactorPassword, permissionLevel + 1) || permissionLevel > 0){
            employeeRepository.delete(employeeEntity);
            employeeEntity.setPermissionsLevel(permissionLevel - 1);
            employeeRepository.save(employeeEntity);
        }
    }

    public void selfDeleteEmployee(String name, String password){

        EmployeeEntity employeeEntity = employeeRepository.findByName(name);
        if (verifyEmployee(name, password, 0)){
            employeeRepository.delete(employeeEntity);
        }
    }

    public void adminDeleteEmployee(String adminName, String adminPassword, String name){

        if(verifyEmployee(adminName, adminPassword, 3)) {
            EmployeeEntity employeeEntity = employeeRepository.findByName(name);
            employeeRepository.delete(employeeEntity);
        }
    }
}
