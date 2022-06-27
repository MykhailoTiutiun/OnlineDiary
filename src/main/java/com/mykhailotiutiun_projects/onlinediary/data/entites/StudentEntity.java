package com.mykhailotiutiun_projects.onlinediary.data.entites;

import lombok.Data;

import javax.persistence.*;

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
    @Column
    private String lessons;
    @Column(length = 2000)
    private String marks;
    @Column(length = 1000)
    private String semesterMarks;
    @Column(length = 500)
    private String yearlyMarks;

    protected StudentEntity() {
    }

    public StudentEntity(String name) {
        this.name = name;
    }
}
