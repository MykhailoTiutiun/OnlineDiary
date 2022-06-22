package com.mykhailotiutiun_projects.onlinediary.database.entites;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "classroom")
public class ClassroomEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    private String name;
    @Column
    private String classroomTeacher;

    protected ClassroomEntity() {}

    public ClassroomEntity(long id, String name, String classroomTeacher) {
        this.id = id;
        this.name = name;
        this.classroomTeacher = classroomTeacher;
    }
}