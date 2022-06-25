package com.mykhailotiutiun_projects.onlinediary.database.managers;


import com.mykhailotiutiun_projects.onlinediary.database.entites.GradeEntity;
import com.mykhailotiutiun_projects.onlinediary.database.entites.LessonTypeEntity;
import com.mykhailotiutiun_projects.onlinediary.database.repositories.EmployeesRepository;
import com.mykhailotiutiun_projects.onlinediary.database.repositories.GradesRepository;

import java.util.List;

public class GradesManager {

    private GradesRepository repository;
    private EmployeesManager employeeManager;

    public GradesManager(GradesRepository repository, EmployeesRepository employeesRepository) {
        this.repository = repository;
        this.employeeManager = new EmployeesManager(employeesRepository, repository);
    }


    public GradeEntity getGradeByName(String name) {
        return repository.findByName(name);
    }

    public List<GradeEntity> getAllGrades(){
        return repository.findAll();
    }


    public void createNewGrade(String employeeName, String employeePassword, String gradeName, String gradeTeacherName) {
        if (employeeManager.verifyEmployee(employeeName, employeePassword, 2)) {
            if (employeeManager.getEmployeeByName(gradeTeacherName) != null && getGradeByName(gradeName) == null){
                repository.save(new GradeEntity(gradeName, gradeTeacherName));
            }
        }

    }

    public void changeGradeTeacher(String adminName, String adminPassword, String gradeName, String newGradeTeacherName){
        if(employeeManager.verifyEmployee(adminName, adminPassword, 2) && getGradeByName(gradeName) != null) {
            GradeEntity gradeEntity = getGradeByName(gradeName);
            repository.delete(gradeEntity);
            gradeEntity.setGradeTeacherName(newGradeTeacherName);
            repository.save(gradeEntity);
        }
    }

    public void deleteGrade(String adminName, String adminPassword, String gradeName){
        if(employeeManager.verifyEmployee(adminName, adminPassword, 2) && getGradeByName(gradeName) != null) {
            repository.delete(getGradeByName(gradeName));
        }
    }

}
