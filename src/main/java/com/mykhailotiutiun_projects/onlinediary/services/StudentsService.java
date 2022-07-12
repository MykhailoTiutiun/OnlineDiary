package com.mykhailotiutiun_projects.onlinediary.services;

import com.mykhailotiutiun_projects.onlinediary.data.entites.LessonTypeEntity;
import com.mykhailotiutiun_projects.onlinediary.data.entites.StudentEntity;
import com.mykhailotiutiun_projects.onlinediary.data.repositories.LessonsTypesRepository;
import com.mykhailotiutiun_projects.onlinediary.data.repositories.StudentsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class StudentsService {

    @Autowired
    private StudentsRepository repository;
    @Autowired
    private GradesService gradesService;
    @Autowired
    private LessonsTypesRepository lessonsTypesRepository;

    public StudentEntity getStudentById(long id){return repository.findById(id);}
    public StudentEntity getStudentByName(String name){
        return repository.findByName(name);
    }
    public List<StudentEntity> getAllStudents(){
        return repository.findAll();
    }
    public List<StudentEntity> getAllByGrade(String gradeName){
        return getAllStudents().stream().filter(studentEntity -> studentEntity.getGrade().equals(gradeName)).toList();
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
        Map<String, List<Integer>> marksMap = new HashMap<>();

        lessons.forEach(lesson -> {
            if(marks.containsKey(lesson)){
                marksMap.put(lesson, marks.get(lesson));
            } else {
                marksMap.put(lesson, new ArrayList<>());
            }
        });
        return marksMap;
    }
    public void addNewStudent(String name) {
            repository.save(new StudentEntity(name));
            updateLessons(repository.findByName(name).getGrade());
    }

    @Transactional
    public void setGrade(long id, String grade) {
            StudentEntity studentEntity = repository.findById(id);
            studentEntity.setGrade(grade);
            updateLessons(grade);

    }

    public List<LessonTypeEntity> getAllLessonsByGrade(String gradeName) {
        List<LessonTypeEntity> lessonTypeEntities = lessonsTypesRepository.findAll();

        return lessonTypeEntities.stream().filter(lessonTypeEntity -> lessonTypeEntity.isGradesContain(gradeName)).toList();
    }


    @Transactional
    public void updateLessons(String grade) {
        Set<String> lessons = new HashSet<>();
        getAllLessonsByGrade(grade).forEach(lessonTypeEntity -> lessons.add(lessonTypeEntity.getName()));

        List<StudentEntity> studentEntities = getAllByGrade(grade);

        if (studentEntities.isEmpty()){
            return;
        }
        studentEntities.forEach(entity -> {
            entity.setLessons(lessons);

            entity.setMarks(updateMarks(entity.getMarks(), lessons));
            entity.setSemesterMarks(updateMarks(entity.getSemesterMarks(), lessons));
            entity.setYearlyMarks(updateMarks(entity.getYearlyMarks(), lessons));
        });

    }

    @Transactional
    public void addMarks(long studentId, String lesson, int mark, int typeOfMarks){
        StudentEntity studentEntity = repository.findById(studentId);

        Map<String, List<Integer>> marksMap = getMapByStudentId(studentId, typeOfMarks);
        List<Integer> marks = marksMap.get(lesson);
        if (marks == null) {
            marks = new ArrayList<>();
        }
        marks.add(mark);
        marksMap.put(lesson, marks);

        switch (typeOfMarks) {
            case (0) -> studentEntity.setMarks(marksMap);
            case (1) -> studentEntity.setSemesterMarks(marksMap);
            case (2) -> studentEntity.setYearlyMarks(marksMap);
            default -> throw new IllegalStateException("Unexpected value: " + typeOfMarks);
        }
    }

    @Transactional
    public void removeMark(long studentId, String lesson, int typeOfMarks){
        StudentEntity studentEntity = getStudentById(studentId);

        Map<String, List<Integer>> marksMap = getMapByStudentId(studentId, typeOfMarks);
        List<Integer> marks = marksMap.get(lesson);
        marks.remove(marks.size() - 1);
        marksMap.put(lesson, marks);

        switch (typeOfMarks) {
            case (0) -> studentEntity.setMarks(marksMap);
            case (1) -> studentEntity.setSemesterMarks(marksMap);
            case (2) -> studentEntity.setYearlyMarks(marksMap);
            default -> throw new IllegalStateException("Unexpected value: " + typeOfMarks);
        }
    }

    public void deleteStudent(String name){
        if(getStudentByName(name) != null){
            repository.delete(getStudentByName(name));
        }
    }

}
