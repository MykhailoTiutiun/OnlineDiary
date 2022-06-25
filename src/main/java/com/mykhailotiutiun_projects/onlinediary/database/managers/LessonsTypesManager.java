package com.mykhailotiutiun_projects.onlinediary.database.managers;

import com.mykhailotiutiun_projects.onlinediary.database.entites.LessonTypeEntity;
import com.mykhailotiutiun_projects.onlinediary.database.repositories.EmployeesRepository;
import com.mykhailotiutiun_projects.onlinediary.database.repositories.GradesRepository;
import com.mykhailotiutiun_projects.onlinediary.database.repositories.LessonsTypesRepository;
import com.mykhailotiutiun_projects.onlinediary.database.repositories.StudentsRepository;

import java.util.List;
import java.util.stream.Stream;

public class LessonsTypesManager {

    private LessonsTypesRepository repository;
    private EmployeesManager employeeManager;
    private GradesManager gradesManager;
    private StudentsManager studentsManager;

    public LessonsTypesManager(LessonsTypesRepository repository, EmployeesRepository employeesRepository, GradesRepository gradesRepository, StudentsRepository studentsRepository) {
        this.repository = repository;
        this.employeeManager = new EmployeesManager(employeesRepository, gradesRepository);
        this.gradesManager = new GradesManager(gradesRepository, employeesRepository);
        this.studentsManager = new StudentsManager(studentsRepository, employeesRepository, gradesRepository);
    }

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
        if (employeeManager.verifyEmployee(employeeName, employeePassword, 3) && getLessonTypeByName(lessonTypeName) == null) {
            repository.save(new LessonTypeEntity(lessonTypeName));
        }
    }

    public void deleteLessonType(String employeeName, String employeePassword, String lessonTypeName) {
        if (employeeManager.verifyEmployee(employeeName, employeePassword, 3) && getLessonTypeByName(lessonTypeName) != null) {
            repository.delete(getLessonTypeByName(lessonTypeName));
        }
    }

    public void addLessonTypeGrade(String adminName, String adminPassword, String name, String grade) {
        if (employeeManager.verifyEmployee(adminName, adminPassword, 2) && gradesManager.getGradeByName(grade) != null) {
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
        if (employeeManager.verifyEmployee(adminName, adminPassword, 2) && employeeManager.getEmployeeByName(name).getGrades() != null && gradesManager.getGradeByName(grade) != null) {
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
