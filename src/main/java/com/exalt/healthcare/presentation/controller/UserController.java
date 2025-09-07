package com.exalt.healthcare.presentation.controller;

import com.exalt.healthcare.domain.model.entity.User;
import com.exalt.healthcare.domain.service.implementations.UserServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserServiceImpl service;

    @Autowired
    public UserController(UserServiceImpl service){
        this.service = service;
    }

    @PostMapping("/add")
    public ResponseEntity<User> addNewUser(@Valid @RequestBody User user){
        return ResponseEntity.ok(this.service.createUser(user));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<User> addNewUser(@PathVariable Long id){
        this.service.deleteUser(id);
        return ResponseEntity.ok().build();
    }
}
