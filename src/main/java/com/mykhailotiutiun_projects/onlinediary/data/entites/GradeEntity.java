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

    protected GradeEntity() {}

    public GradeEntity(String name, String gradesTeacherName) {
        this.name = name;
        this.gradeTeacherName = gradesTeacherName;
    }
}