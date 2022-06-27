package com.mykhailotiutiun_projects.onlinediary.data.repositories;

import com.mykhailotiutiun_projects.onlinediary.data.entites.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByName(String name);
}
