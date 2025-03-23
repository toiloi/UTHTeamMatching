package org.example.uthteammatching.repositories;

import org.example.uthteammatching.models.UthUser;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UthUser, String> {
    Optional<UthUser> findByUsername(String username);
} 