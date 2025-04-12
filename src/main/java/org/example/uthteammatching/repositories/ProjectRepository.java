package org.example.uthteammatching.repositories;


import org.example.uthteammatching.models.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findAll();

    Project findByMaProject(String maProject);
    boolean existsByTenProject(String tenProject);
    List<Project> findAllByOrderByNgayTaoDesc(); // Mới nhất trước
}