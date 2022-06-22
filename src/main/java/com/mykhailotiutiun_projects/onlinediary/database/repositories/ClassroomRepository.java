package com.mykhailotiutiun_projects.onlinediary.database.repositories;

import com.mykhailotiutiun_projects.onlinediary.database.entites.ClassroomEntity;
import org.springframework.data.repository.CrudRepository;

public interface ClassroomRepository extends CrudRepository<ClassroomEntity, Long> {

    ClassroomEntity findById(long id);
    ClassroomEntity findByName(String name);

}
