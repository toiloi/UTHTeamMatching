package org.example.uthteammatching.repositories;


import org.example.uthteammatching.models.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findAll();
    Project findByMaProject(Long maProject);
    boolean existsByTenProject(String tenProject);
    List<Project> findAllByOrderByNgayTaoDesc(); // Mới nhất trước
    @Query("SELECT p FROM Project p WHERE LOWER(CAST(p.tenProject AS string)) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(CAST(p.moTa AS string)) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Project> findByTenProjectContainingIgnoreCaseOrMoTaContainingIgnoreCase(@Param("keyword") String keyword);
}