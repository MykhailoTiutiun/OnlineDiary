package com.mykhailotiutiun_projects.onlinediary.database.repositories;

import com.mykhailotiutiun_projects.onlinediary.database.entites.StudentEntity;
import org.springframework.data.repository.CrudRepository;

public interface StudentsRepository extends CrudRepository<StudentEntity, Long> {

    StudentEntity findById(long id);
    StudentEntity findByName(String name);
}
