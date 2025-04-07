package org.example.uthteammatching.repositories;

import org.example.uthteammatching.models.UthUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UthUser, Long> {
    UthUser findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}

