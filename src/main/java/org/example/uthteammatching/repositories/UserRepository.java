package org.example.uthteammatching.repositories;

import org.example.uthteammatching.models.UthUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UthUser, Long> {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    List<UthUser> findByHoContainingOrTenContainingOrEmailContaining(String ho, String ten, String email);
    Optional<UthUser> findByUsername(String username);
    UthUser findBySdt(String sdt);
    Optional<UthUser> findFirstBySdt(String sdt);
    boolean existsBySdt(String sdt);
}