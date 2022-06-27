package com.mykhailotiutiun_projects.onlinediary.data.repositories;

import com.mykhailotiutiun_projects.onlinediary.data.entites.LessonTypeEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface LessonsTypesRepository extends CrudRepository<LessonTypeEntity, Long> {

    LessonTypeEntity findByName(String name);
    List<LessonTypeEntity> findAll();

}
