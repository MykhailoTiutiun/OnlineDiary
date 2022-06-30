package com.mykhailotiutiun_projects.onlinediary.data.repositories;

import com.mykhailotiutiun_projects.onlinediary.data.entites.LessonTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface LessonsTypesRepository extends JpaRepository<LessonTypeEntity, Long> {

    LessonTypeEntity findById(long id);
    LessonTypeEntity findByName(String name);
    List<LessonTypeEntity> findAll();
}
