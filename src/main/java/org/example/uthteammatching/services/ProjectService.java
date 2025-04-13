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

<<<<<<< HEAD
    public List<Project> searchProjects(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return projectRepository.findAll(); // Hoặc trả về danh sách rỗng
        }
        return projectRepository.findByTenProjectContainingIgnoreCaseOrMoTaContainingIgnoreCase(keyword);
    }

=======
    public boolean existsByTenProject(String nameProject) {
        return projectRepository.existsByTenProject(nameProject);
    }
>>>>>>> 597f62c63893779ae4208e50b7d418fd2d500b08
}