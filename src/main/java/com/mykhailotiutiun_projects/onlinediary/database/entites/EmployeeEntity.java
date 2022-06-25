package com.mykhailotiutiun_projects.onlinediary.database.entites;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

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
    private String password;
    @Column
    private LocalDate initDate;
    @Column
    private String specialization;
    @Column
    private String grades;
    @Column
    private int permissionsLevel;

    protected EmployeeEntity() {
    }

    public EmployeeEntity(String name, String password, String specialization, int permissionsLevel) {
        this.name = name;
        this.password = password;
        this.initDate = LocalDate.now();
        this.specialization = specialization;
        this.permissionsLevel = permissionsLevel;
    }
}
