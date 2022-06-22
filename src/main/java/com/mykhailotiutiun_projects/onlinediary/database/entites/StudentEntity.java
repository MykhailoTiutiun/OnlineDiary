package com.mykhailotiutiun_projects.onlinediary.database.entites;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "students")
@Data
public class StudentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    private String name;
    @Column
    private String password;
    @Column
    private String grades;
    @Column
    private String semesterGrades;
    @Column
    private String yearlyGrades;

    protected StudentEntity() {
    }
}
