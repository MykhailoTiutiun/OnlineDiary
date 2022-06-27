package com.mykhailotiutiun_projects.onlinediary.data.entites;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "employees")
public class EmployeeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column
    private String name;
    @Column
    private String specialization;
    @Column
    private String grades;

    public EmployeeEntity() {
    }

    public EmployeeEntity(String name) {
        this.name = name;
    }
}
