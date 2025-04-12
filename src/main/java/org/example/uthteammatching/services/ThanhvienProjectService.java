package org.example.uthteammatching.services;

import org.example.uthteammatching.models.*;
import org.example.uthteammatching.repositories.ThanhvienProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ThanhvienProjectService {

    @Autowired
    private ThanhvienProjectRepository repository;

    public void themThanhVien(UthUser user, Project project, String vaiTro) {
        ThanhvienProjectId id = new ThanhvienProjectId();
        if (!repository.existsById(id)) {
            ThanhvienProject tvp = new ThanhvienProject();
            tvp.setId(id);
            tvp.setUserMaSo(user);
            tvp.setProjectMaSo(project);
            tvp.setVaiTro(vaiTro);

            repository.save(tvp);
        }
    }

    public List<ThanhvienProject> layThanhVienTheoProject(Long projectId) {
        return repository.findByProjectMaSo_MaProject(projectId);
    }
}