package com.mykhailotiutiun_projects.onlinediary.data.managers;


import com.mykhailotiutiun_projects.onlinediary.data.entites.GradeEntity;
import com.mykhailotiutiun_projects.onlinediary.data.entites.RoleEntity;
import com.mykhailotiutiun_projects.onlinediary.data.repositories.GradesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GradesManager {

    @Autowired
    private GradesRepository repository;
    @Autowired
    private EmployeesManager employeeManager;

    @Autowired
    private UsersManager usersManager;




    public GradeEntity getGradeByName(String name) {
        return repository.findByName(name);
    }

    public List<GradeEntity> getAllGrades(){
        return repository.findAll();
    }


    public void createNewGrade(String employeeName, String employeePassword, String gradeName, String gradeTeacherName) {
        if (usersManager.verifyUser(employeeName, employeePassword, new RoleEntity(4L, "ROLE_ADMIN"))) {
            if (employeeManager.getEmployeeByName(gradeTeacherName) != null && getGradeByName(gradeName) == null){
                repository.save(new GradeEntity(gradeName, gradeTeacherName));
            }
        }

    }

    public void changeGradeTeacher(String adminName, String adminPassword, String gradeName, String newGradeTeacherName){
        if(usersManager.verifyUser(adminName, adminPassword, new RoleEntity(4L, "ROLE_ADMIN")) && getGradeByName(gradeName) != null) {
            GradeEntity gradeEntity = getGradeByName(gradeName);
            repository.delete(gradeEntity);
            gradeEntity.setGradeTeacherName(newGradeTeacherName);
            repository.save(gradeEntity);
        }
    }

    public void deleteGrade(String adminName, String adminPassword, String gradeName){
        if(usersManager.verifyUser(adminName, adminPassword, new RoleEntity(4L, "ROLE_ADMIN")) && getGradeByName(gradeName) != null) {
            repository.delete(getGradeByName(gradeName));
        }
    }

}
