package com.mykhailotiutiun_projects.onlinediary.data.entites;

import lombok.Data;
import org.hibernate.annotations.LazyCollection;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "lessons_types")
public class LessonTypeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long id;
    @Column
    String name;
    @CollectionTable
    @ElementCollection
    Set<String> grades = new HashSet<>();

    public LessonTypeEntity(){}

    public LessonTypeEntity(String name) {
        this.name = name;
    }

    public boolean isGradesContain(String grade){
        return grades.contains(grade);
    }
}
