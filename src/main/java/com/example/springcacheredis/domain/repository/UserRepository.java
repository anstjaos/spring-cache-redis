package com.example.springcacheredis.domain.repository;

import com.example.springcacheredis.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
