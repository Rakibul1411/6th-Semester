package com.example;

public class UserRepository {
    public User findByEmail(String email) {

        throw new RuntimeException("User not found.");
    }
}
