package org.example.uthteammatching.repositories;

import org.example.uthteammatching.models.LecturerRequest;
import org.example.uthteammatching.models.UthUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LecturerRequestRepository extends JpaRepository<LecturerRequest, Long> {
    List<LecturerRequest> findByStatusOrderByCreatedAtDesc(String status);
    List<LecturerRequest> findByUserOrderByCreatedAtDesc(UthUser user);
    boolean existsByUserAndStatus(UthUser user, String status);
} 