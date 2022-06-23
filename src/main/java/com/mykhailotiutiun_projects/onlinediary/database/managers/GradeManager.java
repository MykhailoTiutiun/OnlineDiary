package com.mykhailotiutiun_projects.onlinediary.database.managers;


import com.mykhailotiutiun_projects.onlinediary.database.entites.EmployeeEntity;
import com.mykhailotiutiun_projects.onlinediary.database.entites.GradeEntity;
import com.mykhailotiutiun_projects.onlinediary.database.repositories.EmployeeRepository;
import com.mykhailotiutiun_projects.onlinediary.database.repositories.GradeRepository;

public class GradeManager {

    private GradeRepository repository;
    private EmployeeManager employeeManager;

    public GradeManager(GradeRepository repository, EmployeeManager employeeManager) {
        this.repository = repository;
        this.employeeManager = employeeManager;
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

    public GradeEntity getGradeByName(String name) {
        return repository.findByName(name);
    }
    public Iterable<GradeEntity> getAllGrades(){
        return repository.findAll();
    }
}
