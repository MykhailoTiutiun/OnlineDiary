package com.mykhailotiutiun_projects.onlinediary.data.entites;

import com.mykhailotiutiun_projects.onlinediary.data.entites.converters.ListIntegerStringConverter;
import lombok.Data;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "students")
@Data
public class StudentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column
    private String name;
    @Column
    private String grade;
    @CollectionTable
    @ElementCollection
    private Set<String> lessons = new HashSet<>();

    @CollectionTable
    @ElementCollection
    @Convert(attributeName="value",converter= ListIntegerStringConverter.class)
    private Map<String, List<Integer>> marks = new HashMap<>();

    @CollectionTable
    @ElementCollection
    @Convert(attributeName="value",converter=ListIntegerStringConverter.class)
    private Map<String, List<Integer>> semesterMarks = new HashMap<>();

    @CollectionTable
    @ElementCollection
    @Convert(attributeName="value",converter=ListIntegerStringConverter.class)
    private Map<String, List<Integer>> yearlyMarks = new HashMap<>();

    protected StudentEntity() {
    }

    public StudentEntity(String name) {
        this.name = name;
    }

    public void addLesson(String lesson){
        lessons.add(lesson);
    }

    public void removeLesson(String lesson){
        lessons.remove(lesson);
    }

    public boolean isLessonsContain(String lesson){
        return lessons.contains(lesson);
    }
}
