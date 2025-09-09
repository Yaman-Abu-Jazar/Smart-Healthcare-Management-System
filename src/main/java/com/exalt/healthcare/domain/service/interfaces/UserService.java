package com.exalt.healthcare.domain.service.interfaces;

import com.exalt.healthcare.domain.model.entity.User;

import java.util.List;

public interface UserService {
    User createUser(User user);
    User findByEmail(String email);
    boolean isExistEmail(String email);
    void deleteUser(Long id);
    List<User> findAllUsers();
}
