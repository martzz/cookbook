package com.misha.cookbook.service;

import com.misha.cookbook.model.User;

import java.util.List;

public interface UserService {

    User findById(Long id);

    User findByEmail(String email);

    List<User> findAll();

    void save(User user);

    void deleteById(Long id);

    boolean alreadyExists(User user);
}
