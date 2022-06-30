package com.mykhailotiutiun_projects.onlinediary.data.services;

import com.mykhailotiutiun_projects.onlinediary.data.entites.EmployeeEntity;
import com.mykhailotiutiun_projects.onlinediary.data.repositories.EmployeesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

@Service
public class EmployeesService {

    @Autowired
    private EmployeesRepository repository;

    @Autowired
    private UsersService usersService;


    public EmployeeEntity getEmployeeByName(String name) {
        return repository.findByName(name);
    }

    public List<EmployeeEntity> getAllEmployees() {
        return repository.findAll();
    }

    public List<EmployeeEntity> getAllByGrade(String gradeName) {
        List<EmployeeEntity> employeeEntities = repository.findAll();

        return employeeEntities.stream().filter(employeeEntity -> employeeEntity.getGrades().contains(gradeName)).toList();
    }

    public void addNewEmployee(String name) {
        if (getEmployeeByName(name) == null) {
            repository.save(new EmployeeEntity(name));
        }
    }

    @Transactional
    public void setSpecialization(long id, String specialization){
        EmployeeEntity employeeEntity = repository.findById(id);
        employeeEntity.setSpecialization(specialization);
    }

    @Transactional
    public void addEmployeeGrade(long id, String gradeName) {
        EmployeeEntity employeeEntity = repository.findById(id);

        Set<String> grades = employeeEntity.getGrades();
        grades.add(gradeName);
        employeeEntity.setGrades(grades);
    }

    @Transactional
    public void dropEmployeeGrade(long id, String gradeName) {
        EmployeeEntity employeeEntity = repository.findById(id);

        Set<String> grades = employeeEntity.getGrades();
        grades.remove(gradeName);
        employeeEntity.setGrades(grades);
    }

    public void deleteEmployee(String name) {
        if (getEmployeeByName(name) != null) {
            repository.delete(getEmployeeByName(name));
        }
    }

}
