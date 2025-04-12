package org.example.uthteammatching.services;

import org.example.uthteammatching.models.Project;
import org.example.uthteammatching.repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    public List<Project> getAllProject() {
        return projectRepository.findAll();
    }

    public Project getProject(Long maProject) {
        return projectRepository.findByMaProject(maProject);
    }
}