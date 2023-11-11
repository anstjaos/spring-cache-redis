package com.example.springcacheredis.domain.service;

import com.example.springcacheredis.domain.entity.User;
import com.example.springcacheredis.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RedisTemplate<String, User> userRedisTemplate;

    public User getUser(final Long id) {
        var key = "users:%d".formatted(id);
        // 1. cache get
        var cachedUser = userRedisTemplate.opsForValue().get(key);
        if (cachedUser != null) {
            return cachedUser;
        }
        // 2. else db -> cache set
        var user = userRepository.findById(id).orElseThrow();
        userRedisTemplate.opsForValue().set(key, user, Duration.ofSeconds(30));

        return user;
    }
}
