package com.mykhailotiutiun_projects.onlinediary.data.services;

import com.mykhailotiutiun_projects.onlinediary.data.entites.LessonTypeEntity;
import com.mykhailotiutiun_projects.onlinediary.data.entites.RoleEntity;
import com.mykhailotiutiun_projects.onlinediary.data.entites.StudentEntity;
import com.mykhailotiutiun_projects.onlinediary.data.repositories.LessonsTypesRepository;
import com.mykhailotiutiun_projects.onlinediary.data.repositories.StudentsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class StudentsService {

    @Autowired
    private StudentsRepository repository;
    @Autowired
    private GradesService gradesService;
    @Autowired
    private LessonsTypesRepository lessonsTypesRepository;
    @Autowired
    private UsersService usersService;

    public StudentEntity getStudentByName(String name){
        return repository.findByName(name);
    }
    public List<StudentEntity> getAllStudents(){
        return repository.findAll();
    }
    public List<StudentEntity> getAllByGrade(String gradeName){
        return getAllStudents().stream().filter(studentEntity -> studentEntity.isLessonsContain(gradeName)).toList();
    }
    public Map<String, List<Integer>> getMapByStudentId(long studentId, int typeOfMarks){
        switch (typeOfMarks) {
            case (0) -> {
                return repository.findById(studentId).getMarks();
            }
            case (1) -> {
                return repository.findById(studentId).getSemesterMarks();
            }
            case (2) -> {
                return repository.findById(studentId).getYearlyMarks();
            }
            default -> throw new IllegalStateException("Unexpected value: " + typeOfMarks);
        }
    }

    private Map<String, List<Integer>> updateMarks(Map<String, List<Integer>> marks, Set<String> lessons){
        lessons.forEach(lesson -> {
            if(!marks.containsKey(lesson)){
                marks.put(lesson, new ArrayList<>());
            }
        });
        return marks;
    }
    public void addNewStudent(String name) {
        if (getStudentByName(name) == null) {
            repository.save(new StudentEntity(name));
            updateLessons(repository.findByName(name).getGrade());
        }
    }

    public void setGrade(String adminName, String adminPassword, String name, String grade) {
        if (usersService.verifyUser(adminName, adminPassword, new RoleEntity(4L, "ROLE_HEAD_TEACHER")) && getStudentByName(name) != null && gradesService.getGradeByName(grade) != null) {
            StudentEntity studentEntity = getStudentByName(name);
            repository.delete(studentEntity);
            studentEntity.setGrade(grade);
            repository.save(studentEntity);
        }
    }

    public List<LessonTypeEntity> getAllLessonsByGrade(String gradeName) {
        List<LessonTypeEntity> lessonTypeEntities = lessonsTypesRepository.findAll();

        return lessonTypeEntities.stream().filter(lessonTypeEntity -> lessonTypeEntity.isGradesContain(gradeName)).toList();
    }


    public void updateLessons(String grade) {
        Set<String> lessons = new HashSet<>();
        getAllLessonsByGrade(grade).forEach(lessonTypeEntity -> lessons.add(lessonTypeEntity.getName()));

        List<StudentEntity> studentEntities = getAllByGrade(grade);

        studentEntities.forEach(entity -> {
            entity.setLessons(lessons);

            entity.setMarks(updateMarks(entity.getMarks(), lessons));
            entity.setSemesterMarks(updateMarks(entity.getSemesterMarks(), lessons));
            entity.setYearlyMarks(updateMarks(entity.getYearlyMarks(), lessons));
        });

    }

    public void addMarks(long studentId, String lesson, int mark, int typeOfMarks){
        StudentEntity studentEntity = repository.findById(studentId);

        Map<String, List<Integer>> marksMap = getMapByStudentId(studentId, typeOfMarks);
        List<Integer> marks = marksMap.get(lesson);
        marks.add(mark);
        marksMap.put(lesson, marks);

        repository.delete(studentEntity);

        switch (typeOfMarks) {
            case (0) -> studentEntity.setMarks(marksMap);
            case (1) -> studentEntity.setSemesterMarks(marksMap);
            case (2) -> studentEntity.setYearlyMarks(marksMap);
            default -> throw new IllegalStateException("Unexpected value: " + typeOfMarks);
        }

        repository.save(studentEntity);
    }

    public void deleteStudent(String name){
        if(getStudentByName(name) != null){
            repository.delete(getStudentByName(name));
        }
    }

}
