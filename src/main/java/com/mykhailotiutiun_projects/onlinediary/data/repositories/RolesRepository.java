package com.mykhailotiutiun_projects.onlinediary.data.repositories;

import com.mykhailotiutiun_projects.onlinediary.data.entites.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface RolesRepository extends CrudRepository<RoleEntity, Long> {

    RoleEntity findByName(String name);

}
