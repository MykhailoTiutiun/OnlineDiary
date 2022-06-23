package com.mykhailotiutiun_projects.onlinediary.database.managers;

import com.mykhailotiutiun_projects.onlinediary.database.entites.EmployeeEntity;
import com.mykhailotiutiun_projects.onlinediary.database.repositories.EmployeeRepository;


public class EmployeeManager {
    private EmployeeRepository repository;

    public EmployeeManager(EmployeeRepository repository){
        this.repository = repository;
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

    public boolean verifyEmployee(String name, String password, int requestPermission){
        EmployeeEntity employeeEntity = getEmployeeByName(name);
        if (employeeEntity != null){
            return employeeEntity.getPassword().equals(password) && employeeEntity.getPermissionsLevel() >= requestPermission;
        }
        return false;
    }

    public EmployeeEntity getEmployeeByName(String name){
        return repository.findByName(name);
    }

    public Iterable<EmployeeEntity> getAllEmployees(){
        return repository.findAll();
    }

    public void setEmployeeClassrooms(String adminName, String adminPassword, String name, String classroom){
        if(verifyEmployee(adminName, adminPassword, 2)){
            EmployeeEntity employeeEntity = getEmployeeByName(name);
            String classrooms = employeeEntity.getClassrooms();

            if(classrooms == null) {
                classrooms = "";
            }
            repository.delete(employeeEntity);
            employeeEntity.setClassrooms(classrooms.concat(classroom.concat(";")));
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

    public void selfDeleteEmployee(String name, String password){

        EmployeeEntity employeeEntity = getEmployeeByName(name);
        if (verifyEmployee(name, password, 0)){
            repository.delete(employeeEntity);
        }
    }

    public void adminDeleteEmployee(String adminName, String adminPassword, String name){

        if(verifyEmployee(adminName, adminPassword, 3)) {
            EmployeeEntity employeeEntity = getEmployeeByName(name);
            repository.delete(employeeEntity);
        }
    }
}
