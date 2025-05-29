package com.nailsalon.service;

import com.nailsalon.entity.User;
import java.util.List;
import java.util.Optional;

public interface UserService {

    // CRUD operations
    User createUser(User user);

    User updateUser(Long id, User user);

    void deleteUser(Long id);

    Optional<User> getUserById(Long id);

    List<User> getAllUsers();

    // Additional methods we'll need
    Optional<User> getUserByEmail(String email);

    List<User> getUsersByRole(User.Role role);

    boolean existsByEmail(String email);
}