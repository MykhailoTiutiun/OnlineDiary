package com.mykhailotiutiun_projects.onlinediary.data.entites;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "grades")
public class GradeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column
    private String name;
    @Column
    private String gradeTeacherName;

    public GradeEntity() {}

    public GradeEntity(String name) {
        this.name = name;
    }
}