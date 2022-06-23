package com.mykhailotiutiun_projects.onlinediary.database.repositories;

import com.mykhailotiutiun_projects.onlinediary.database.entites.GradeEntity;
import org.springframework.data.repository.CrudRepository;

public interface GradeRepository extends CrudRepository<GradeEntity, Long> {

    GradeEntity findById(long id);
    GradeEntity findByName(String name);

}
