package com.mykhailotiutiun_projects.onlinediary.data.repositories;

import com.mykhailotiutiun_projects.onlinediary.data.entites.EmployeeEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EmployeesRepository extends CrudRepository<EmployeeEntity, Long> {

    EmployeeEntity findById(long id);
    EmployeeEntity findByName(String name);
    List<EmployeeEntity> findAll();
    List<EmployeeEntity> findAllBySpecialization(String specialization);

}
