package com.mykhailotiutiun_projects.onlinediary.data.managers;

import com.mykhailotiutiun_projects.onlinediary.data.entites.LessonTypeEntity;
import com.mykhailotiutiun_projects.onlinediary.data.entites.RoleEntity;
import com.mykhailotiutiun_projects.onlinediary.data.entites.StudentEntity;
import com.mykhailotiutiun_projects.onlinediary.data.repositories.StudentsRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class StudentsManager {

    @Autowired
    private StudentsRepository repository;
    @Autowired
    private GradesManager gradesManager;
    @Autowired
    private UsersManager usersManager;

    public StudentEntity getStudentByName(String name){
        return repository.findByName(name);
    }
    public List<StudentEntity> getAllStudents(){
        return repository.findAll();
    }
    public List<StudentEntity> getAllByGrade(String gradeName){
        return repository.findByGrade(gradeName);
    }

    public Map<String, List<String>> getMarksByStName(String name, int typeOfMarks) {
        switch (typeOfMarks){
            case (0) -> {return getMapFromString(getStudentByName(name).getMarks());}
            case (1) -> {return getMapFromString(getStudentByName(name).getSemesterMarks());}
            case (2) -> {return getMapFromString(getStudentByName(name).getYearlyMarks());}
            default -> {return null;}
        }
    }
    private Map<String, List<String>> getMapFromString(String mapString){
        Map<String, List<String>> map = new HashMap<>();
        Arrays.stream(mapString.split(";")).map(s -> s.split("=")).forEach(keyValueString -> {
            List<String> values = List.of(keyValueString[1].split(","));
            map.put(keyValueString[0], values);
        });

        return map;
    }

    private String getStringFromMap(Map<String,List<String>> map){
        StringBuilder mapString = new StringBuilder();

        map.forEach((key, value) -> {
            mapString.append(key).append("=");
            value.forEach(s -> mapString.append(s).append(","));
            mapString.append(";");
        });

        return String.valueOf(mapString);
    }

    public void addNewStudent(String name) {
        if (getStudentByName(name) == null) {
            repository.save(new StudentEntity(name));
        }
    }

    public void setGrade(String adminName, String adminPassword, String name, String grade) {
        if (usersManager.verifyUser(adminName, adminPassword, new RoleEntity(4L, "ROLE_ADMIN")) && getStudentByName(name) != null && gradesManager.getGradeByName(grade) != null) {
            StudentEntity studentEntity = getStudentByName(name);
            repository.delete(studentEntity);
            studentEntity.setGrade(grade);
            repository.save(studentEntity);
        }
    }

    public void updateLessons(@NotNull List<LessonTypeEntity> lessonTypeEntities, String grade) {
        StringBuilder lessons = new StringBuilder();

        lessonTypeEntities.forEach(lessonTypeEntity -> lessons.append(lessonTypeEntity.getName()).append(";"));

        List<StudentEntity> studentEntities = getAllByGrade(grade);
        studentEntities.forEach(studentEntity -> {studentEntity.setLessons(String.valueOf(lessons));});

        studentEntities.forEach(entity -> {
            Map<String, List<String>> marksMap = getMapFromString(entity.getMarks());
            lessonTypeEntities.stream().filter(lessonTypeEntity -> !marksMap.containsKey(lessonTypeEntity.getName())).forEach(lessonTypeEntity -> marksMap.put(lessonTypeEntity.getName(), new LinkedList<>()));
            repository.delete(entity);
            entity.setMarks(getStringFromMap(marksMap));
            repository.save(entity);
        });

    }

    public void changeMarks(String name, List<String> lessons, List<List<String>> marks, int typeOfMarks ){
        StudentEntity studentEntity = repository.findByName(name);
        Map<String, List<String>> map;

        repository.delete(studentEntity);


        switch (typeOfMarks) {
            case (0) -> {
                map = getMapFromString(studentEntity.getMarks());
                for (int i = 0; i < lessons.size(); i++) map.put(lessons.get(i), marks.get(i));
                studentEntity.setMarks(getStringFromMap(map));
            }
            case (1) -> {
                map = getMapFromString(studentEntity.getSemesterMarks());
                for (int i = 0; i < lessons.size(); i++) map.put(lessons.get(i), marks.get(i));
                studentEntity.setSemesterMarks(getStringFromMap(map));
            }
            case (2) -> {
                map = getMapFromString(studentEntity.getYearlyMarks());
                for (int i = 0; i < lessons.size(); i++) map.put(lessons.get(i), marks.get(i));
                studentEntity.setYearlyMarks(getStringFromMap(map));
            }
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
