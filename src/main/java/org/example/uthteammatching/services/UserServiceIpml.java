package org.example.uthteammatching.services;

import org.example.uthteammatching.models.UthUser;
import org.example.uthteammatching.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceIpml implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Override
    public UthUser findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
