package com.mykhailotiutiun_projects.onlinediary.data.repositories;

import com.mykhailotiutiun_projects.onlinediary.data.entites.GradeEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface GradesRepository extends CrudRepository<GradeEntity, Long> {

    GradeEntity findById(long id);
    GradeEntity findByName(String name);
    List<GradeEntity> findAll();

}
