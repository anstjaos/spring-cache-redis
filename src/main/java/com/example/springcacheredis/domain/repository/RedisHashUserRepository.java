package com.example.springcacheredis.domain.repository;

import com.example.springcacheredis.domain.entity.RedisHashUser;
import org.springframework.data.repository.CrudRepository;

public interface RedisHashUserRepository extends CrudRepository<RedisHashUser, Long> {
}
