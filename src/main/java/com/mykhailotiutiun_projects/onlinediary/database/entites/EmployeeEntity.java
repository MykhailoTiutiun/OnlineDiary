package com.mykhailotiutiun_projects.onlinediary.database.entites;

import javax.persistence.*;

@Entity
@Table(name = "employees")
public class EmployeeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    private String name;
    @Column
    private String password;
    @Column
    private String specialization;
    @Column
    private String classrooms;
    @Column
    private int permissionsLevel;
    @Column
    private boolean isChecked;

    protected EmployeeEntity() {
    }

    public EmployeeEntity(String name, String password, String specialization, String classrooms, int permissionsLevel, boolean isChecked) {
        this.name = name;
        this.password = password;
        this.specialization = specialization;
        this.classrooms = classrooms;
        this.permissionsLevel = permissionsLevel;
        this.isChecked = isChecked;
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

    public void setName(String firstName) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getClassrooms() {
        return classrooms;
    }

    public void setClassrooms(String classrooms) {
        this.classrooms = classrooms;
    }

    public int getPermissionsLevel() {
        return permissionsLevel;
    }

    public void setPermissionsLevel(int permissionsLevel) {
        this.permissionsLevel = permissionsLevel;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
