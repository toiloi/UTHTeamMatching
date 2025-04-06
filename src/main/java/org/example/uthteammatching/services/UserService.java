package org.example.uthteammatching.services;

import org.example.uthteammatching.models.UthUser;

public interface UserService {
    UthUser findByUsername(String username);
}
