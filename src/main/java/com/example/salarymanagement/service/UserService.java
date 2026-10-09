package com.example.salarymanagement.service;

import com.example.salarymanagement.entity.User;
import com.example.salarymanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User saveUser(User user) throws Exception {
        Optional<User> existingUser = userRepository.findByName(user.getName());
        if (existingUser.isPresent() && (user.getId() == null || !existingUser.get().getId().equals(user.getId()))) {
            throw new Exception("A User with name " + user.getName() + " already exist.");
        }
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}