package com.example;

public class AuthManager {
    private final UserRepository userRepository;
    private final HashLibrary hashLibrary;

    public AuthManager(UserRepository userRepository, HashLibrary hashLibrary) {
        this.userRepository = userRepository;
        this.hashLibrary = hashLibrary;
    }


    public boolean login(String email, String password) {
        User user;
        try{
            user = userRepository.findByEmail(email);
        } catch (Exception e) {
            throw e;
        }

        return hashLibrary.verify(password, user.getHashedPassword());

    }
}