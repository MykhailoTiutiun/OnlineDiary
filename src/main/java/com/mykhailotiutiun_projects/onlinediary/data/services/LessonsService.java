package com.mykhailotiutiun_projects.onlinediary.data.services;

import com.mykhailotiutiun_projects.onlinediary.data.entites.LessonTypeEntity;
import com.mykhailotiutiun_projects.onlinediary.data.entites.RoleEntity;
import com.mykhailotiutiun_projects.onlinediary.data.repositories.LessonsTypesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

@Service
public class LessonsService {

    @Autowired
    private LessonsTypesRepository repository;
    @Autowired
    private EmployeesService employeeManager;
    @Autowired
    private UsersService usersService;
    @Autowired
    private GradesService gradesService;
    @Autowired
    private StudentsService studentsService;


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
        if (usersService.verifyUser(employeeName, employeePassword, new RoleEntity(5L, "ROLE_DIRECTOR")) && getLessonTypeByName(lessonTypeName) == null) {
            repository.save(new LessonTypeEntity(lessonTypeName));
        }
    }

    public void deleteLessonType(long id) {
        if (repository.findById(id) != null) {
            if (repository.findById(id).getGrades() != null) {
                Set<String> grades = repository.findById(id).getGrades();
                grades.forEach(grade -> studentsService.updateLessons(grade));
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

        studentsService.updateLessons(gradeName);
    }

    @Transactional
    public void dropLessonTypeGrade(long id, String gradeName) {
        LessonTypeEntity lessonTypeEntity = repository.findById(id);

        Set<String> grades = lessonTypeEntity.getGrades();
        grades.remove(gradeName);
        lessonTypeEntity.setGrades(grades);

        studentsService.updateLessons(gradeName);
    }

}
