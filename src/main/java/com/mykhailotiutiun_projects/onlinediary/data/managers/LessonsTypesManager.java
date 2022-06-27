package com.mykhailotiutiun_projects.onlinediary.data.managers;

import com.mykhailotiutiun_projects.onlinediary.data.entites.LessonTypeEntity;
import com.mykhailotiutiun_projects.onlinediary.data.entites.RoleEntity;
import com.mykhailotiutiun_projects.onlinediary.data.repositories.LessonsTypesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
public class LessonsTypesManager {

    @Autowired
    private LessonsTypesRepository repository;
    @Autowired
    private EmployeesManager employeeManager;
    @Autowired
    private UsersManager usersManager;
    @Autowired
    private GradesManager gradesManager;
    @Autowired
    private StudentsManager studentsManager;


    public LessonTypeEntity getLessonTypeByName(String name) {
        return repository.findByName(name);
    }

    public List<LessonTypeEntity> getAllLessonType() {
        return repository.findAll();
    }

    public List<LessonTypeEntity> getAllByGrade(String gradeName) {
        List<LessonTypeEntity> lessonTypeEntities = repository.findAll();

        return lessonTypeEntities.stream().filter(lessonTypeEntity -> {
            List<String> grades = Stream.of(lessonTypeEntity.getGrades().split(";")).filter(grade -> {
                return grade.equals(gradeName);
            }).toList();
            return grades.size() > 0;
        }).toList();
    }


    public void createNewLessonType(String employeeName, String employeePassword, String lessonTypeName) {
        if (usersManager.verifyUser(employeeName, employeePassword, new RoleEntity(5L, "ROLE_MAIN_ADMIN")) && getLessonTypeByName(lessonTypeName) == null) {
            repository.save(new LessonTypeEntity(lessonTypeName));
        }
    }

    public void deleteLessonType(String employeeName, String employeePassword, String lessonTypeName) {
        if (usersManager.verifyUser(employeeName, employeePassword, new RoleEntity(5L, "ROLE_MAIN_ADMIN")) && getLessonTypeByName(lessonTypeName) != null) {
            repository.delete(getLessonTypeByName(lessonTypeName));
        }
    }

    public void addLessonTypeGrade(String adminName, String adminPassword, String name, String grade) {
        if (usersManager.verifyUser(adminName, adminPassword, new RoleEntity(4L, "ROLE_ADMIN")) && gradesManager.getGradeByName(grade) != null) {
            LessonTypeEntity lessonTypeEntity = getLessonTypeByName(name);
            String grades = lessonTypeEntity.getGrades();

            if (grades == null) {
                grades = "";
            }

            repository.delete(lessonTypeEntity);
            lessonTypeEntity.setGrades(grades.concat(grade.concat(";")));
            repository.save(lessonTypeEntity);

            studentsManager.updateLessons(getAllByGrade(grade), grade);
        }
    }

    public void dropLessonTypeGrade(String adminName, String adminPassword, String name, String grade) {
        if (usersManager.verifyUser(adminName, adminPassword, new RoleEntity(4L, "ROLE_ADMIN")) && employeeManager.getEmployeeByName(name).getGrades() != null && gradesManager.getGradeByName(grade) != null) {
            LessonTypeEntity lessonTypeEntity = getLessonTypeByName(name);
            List<String> grades = List.of(lessonTypeEntity.getGrades().split(";"));
            grades.remove(grade);
            repository.delete(lessonTypeEntity);
            lessonTypeEntity.setGrades(String.join(";", grades));
            repository.save(lessonTypeEntity);

            studentsManager.updateLessons(getAllByGrade(grade), grade);
        }
    }

}
