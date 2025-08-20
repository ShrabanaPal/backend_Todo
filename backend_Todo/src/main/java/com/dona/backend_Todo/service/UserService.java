package com.dona.backend_Todo.service;


import com.dona.backend_Todo.model.User;
import com.dona.backend_Todo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository repo;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public List<User> getUser() {
        return repo.findAll();
    }

    public User getUserById(int id) {
        return repo.findById(id).orElse(new User());
    }

    public String addUser(User user) {
        Optional<User> existingUser = repo.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            return "exists";
        }

        user.setPassword(passwordEncoder.encode(user.getPassword())); // encrypt password
        repo.save(user);
        return "success";
    }

    public boolean loginUser(String email, String rawPassword) {
        Optional<User> userOpt = repo.findByEmail(email);
        if (userOpt.isPresent()) {
            return passwordEncoder.matches(rawPassword, userOpt.get().getPassword());
        }
        return false;
    }

}


