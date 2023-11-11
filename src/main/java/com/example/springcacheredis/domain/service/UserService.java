package com.example.springcacheredis.domain.service;

import com.example.springcacheredis.domain.entity.RedisHashUser;
import com.example.springcacheredis.domain.entity.User;
import com.example.springcacheredis.domain.repository.RedisHashUserRepository;
import com.example.springcacheredis.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

import static com.example.springcacheredis.config.CacheConfig.CACHE1;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RedisHashUserRepository redisHashUserRepository;
    private final RedisTemplate<String, User> userRedisTemplate;
    private final RedisTemplate<String, Object> objectRedisTemplate;

    public User getUser(final Long id) {
        var key = "users:%d".formatted(id);
        // 1. cache get
        var cachedUser = objectRedisTemplate.opsForValue().get(key);
        if (cachedUser != null) {
            return (User) cachedUser;
        }
        // 2. else db -> cache set
        var user = userRepository.findById(id).orElseThrow();
        objectRedisTemplate.opsForValue().set(key, user, Duration.ofSeconds(30));

        return user;
    }

    public RedisHashUser getRedisHashUser(final Long id) {
        return redisHashUserRepository.findById(id).orElseGet(() -> {
            var user = userRepository.findById(id).orElseThrow();
            return redisHashUserRepository.save(RedisHashUser.builder()
                    .id(user.getId())
                    .name(user.getName())
                    .email(user.getEmail())
                    .createdAt(user.getCreatedAt())
                    .updatedAt(user.getUpdatedAt())
                    .build());
        });
    }

    @Cacheable(cacheNames = CACHE1, key = "'user:' + #id")
    public User getUser2(final Long id) {
        return userRepository.findById(id).orElseThrow();
    }
}
