package com.exalt.healthcare.domain.service.interfaces;

import com.exalt.healthcare.domain.model.entity.User;

public interface UserService {
    User createUser(User user);
    User findByUsername(String username);
    User findByEmail(String email);
    boolean isExistUsername(String username);
    boolean isExistEmail(String email);
}
