package com.mykhailotiutiun_projects.onlinediary.database.repositories;

import com.mykhailotiutiun_projects.onlinediary.database.entites.StudentEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface StudentsRepository extends CrudRepository<StudentEntity, Long> {

    StudentEntity findById(long id);
    StudentEntity findByName(String name);
    List<StudentEntity> findAll();
    List<StudentEntity> findByGrade(String grade);

    void deleteAllByGrade(String grade);

}
