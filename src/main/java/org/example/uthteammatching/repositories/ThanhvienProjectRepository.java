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
    boolean existsById(ThanhvienProjectId id);
}
