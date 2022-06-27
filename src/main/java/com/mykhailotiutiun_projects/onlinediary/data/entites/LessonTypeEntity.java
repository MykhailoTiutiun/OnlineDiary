package com.mykhailotiutiun_projects.onlinediary.data.entites;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "lessons_types")
public class LessonTypeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long id;
    @Column
    String name;
    @Column
    String grades;

    protected LessonTypeEntity(){}

    public LessonTypeEntity(String name) {
        this.name = name;
    }
}
