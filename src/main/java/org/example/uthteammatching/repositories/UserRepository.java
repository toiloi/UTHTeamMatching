package org.example.uthteammatching.repositories;

import org.example.uthteammatching.models.UthUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

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
    @Query("SELECT u FROM UthUser u JOIN u.userRoles ur JOIN ur.role r WHERE r.ten = :roleName")
    List<UthUser> findByRoleName(String roleName);

    @Query("SELECT u FROM UthUser u WHERE LOWER(u.ho) LIKE LOWER(CONCAT('%', :term, '%')) " +
            "OR LOWER(u.ten) LIKE LOWER(CONCAT('%', :term, '%')) " +
            "OR LOWER(u.sdt) LIKE LOWER(CONCAT('%', :term, '%'))")
    List<UthUser> findByHoContainingIgnoreCaseOrTenContainingIgnoreCaseOrSdtContainingIgnoreCase(@Param("term") String term);
}