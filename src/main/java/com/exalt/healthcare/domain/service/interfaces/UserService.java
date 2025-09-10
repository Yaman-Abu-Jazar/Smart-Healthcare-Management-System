package com.exalt.healthcare.domain.service.interfaces;

import com.exalt.healthcare.domain.model.entity.User;


public interface UserService {
    User findByEmail(String email);
    boolean isExistEmail(String email);
    void deleteUser(Long id);
}
