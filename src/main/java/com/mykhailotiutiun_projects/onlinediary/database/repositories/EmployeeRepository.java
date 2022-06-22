package com.mykhailotiutiun_projects.onlinediary.database.repositories;

import com.mykhailotiutiun_projects.onlinediary.database.entites.EmployeeEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EmployeeRepository extends CrudRepository<EmployeeEntity, Long> {

    EmployeeEntity findById(long id);
    EmployeeEntity findByName(String name);
    List<EmployeeEntity> findAllBySpecialization(String specialization);

}
