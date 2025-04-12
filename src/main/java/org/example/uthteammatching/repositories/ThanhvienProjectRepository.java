package org.example.uthteammatching.repositories;

import org.example.uthteammatching.models.Project;
import org.example.uthteammatching.models.ThanhvienProject;
import org.example.uthteammatching.models.ThanhvienProjectId;
import org.example.uthteammatching.models.UthUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThanhvienProjectRepository extends JpaRepository<ThanhvienProject, ThanhvienProjectId> {
    List<ThanhvienProject> findByProjectMaSo_MaProject(Long projectId);
    boolean existsById(ThanhvienProjectId id);

    boolean existsByUserMaSoAndProjectMaSo(UthUser currentUser, Project project);
}
