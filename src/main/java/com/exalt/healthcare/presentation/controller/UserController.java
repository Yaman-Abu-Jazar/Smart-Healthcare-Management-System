package com.exalt.healthcare.presentation.controller;

import com.exalt.healthcare.domain.model.entity.User;
import com.exalt.healthcare.domain.service.implementations.UserServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers(){
        return ResponseEntity.ok(this.service.findAllUsers());
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email){
        return ResponseEntity.ok(this.service.findByEmail(email));
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username){
        return ResponseEntity.ok(this.service.findByUsername(username));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @Valid @RequestBody User user){
        return ResponseEntity.ok(this.service.updateUser(id, user));
    }
}
