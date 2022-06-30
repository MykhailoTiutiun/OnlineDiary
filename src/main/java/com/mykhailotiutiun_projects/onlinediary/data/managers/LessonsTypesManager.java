package com.mykhailotiutiun_projects.onlinediary.data.managers;

import com.mykhailotiutiun_projects.onlinediary.data.entites.LessonTypeEntity;
import com.mykhailotiutiun_projects.onlinediary.data.entites.RoleEntity;
import com.mykhailotiutiun_projects.onlinediary.data.repositories.LessonsTypesRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;
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

        return lessonTypeEntities.stream().filter(lessonTypeEntity -> lessonTypeEntity.isGradesContain(gradeName)).toList();
    }


    public void createNewLessonType(String employeeName, String employeePassword, String lessonTypeName) {
        if (usersManager.verifyUser(employeeName, employeePassword, new RoleEntity(5L, "ROLE_DIRECTOR")) && getLessonTypeByName(lessonTypeName) == null) {
            repository.save(new LessonTypeEntity(lessonTypeName));
        }
    }

    public void deleteLessonType(long id) {
        if (repository.findById(id) != null) {
            if (repository.findById(id).getGrades() != null) {
                Set<String> grades = repository.findById(id).getGrades();
                grades.forEach(grade -> studentsManager.updateLessons(grade));
            }
            repository.delete(repository.findById(id));
        }
    }

    @Transactional
    public void addLessonTypeGrade(long lessonId, String gradeName) {
            LessonTypeEntity lessonTypeEntity = repository.findById(lessonId);

            Set<String> grades = lessonTypeEntity.getGrades();
            grades.add(gradeName);
            lessonTypeEntity.setGrades(grades);

            studentsManager.updateLessons(gradeName);
        }

    public void dropLessonTypeGrade(long id, String gradeName) {
            LessonTypeEntity lessonTypeEntity = repository.findById(id);

            Set<String> grades = lessonTypeEntity.getGrades();
            grades.remove(gradeName);
            lessonTypeEntity.setGrades(grades);

            studentsManager.updateLessons(gradeName);
    }

}
