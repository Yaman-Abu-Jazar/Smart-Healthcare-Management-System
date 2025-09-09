package com.exalt.healthcare.domain.service.implementations;

import com.exalt.healthcare.common.exception.UserNotFoundException;
import com.exalt.healthcare.domain.model.entity.User;
import com.exalt.healthcare.domain.repository.jpa.UserRepository;
import com.exalt.healthcare.domain.service.interfaces.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository repository, PasswordEncoder passwordEncoder){
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return repository.save(user);
    }

    @Override
    public User findByEmail(String email) {
        return this.repository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email : " + email));
    }

    @Override
    public boolean isExistEmail(String email) {
        return this.repository.existsByEmail(email);
    }

    @Override
    public void deleteUser(Long id){
        User user = this.repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id : " + id));

        this.repository.deleteById(id);
    }

    @Override
    public List<User> findAllUsers(){
        return this.repository.findAll();
    }
}
