package com.example.springcacheredis.domain.service;

import com.example.springcacheredis.domain.entity.User;
import com.example.springcacheredis.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User getUser(final Long id) {
        return userRepository.findById(id).orElseThrow();
    }
}
