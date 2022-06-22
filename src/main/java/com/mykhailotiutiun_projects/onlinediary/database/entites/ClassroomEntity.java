package com.mykhailotiutiun_projects.onlinediary.database.entites;

import javax.persistence.*;

@Entity
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassroomTeacher() {
        return classroomTeacher;
    }

    public void setClassroomTeacher(String classroomTeacher) {
        this.classroomTeacher = classroomTeacher;
    }
}