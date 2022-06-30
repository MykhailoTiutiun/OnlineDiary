package com.mykhailotiutiun_projects.onlinediary.data.entites;

import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

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

    @CollectionTable
    @ElementCollection
    private Set<String> grades = new HashSet<>();

    public EmployeeEntity() {
    }

    public EmployeeEntity(String name) {
        this.name = name;
    }
}
