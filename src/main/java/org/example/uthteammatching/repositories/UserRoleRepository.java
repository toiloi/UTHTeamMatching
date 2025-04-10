package org.example.uthteammatching.repositories;

import org.example.uthteammatching.models.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    List<UserRole> findByRoleMaSo(Long roleId);
    List<UserRole> findByUserMaSo(Long userId);
    boolean existsByUserMaSoAndRoleMaSo(Long userId, Long roleId);
    void deleteByUserMaSoAndRoleMaSo(Long userId, Long roleId);
    
    @Modifying
    @Query("DELETE FROM UserRole ur WHERE ur.user.maSo = :userId")
    void deleteByUserMaSo(Long userId);
} 