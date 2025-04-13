package org.example.uthteammatching.services;

import org.example.uthteammatching.models.*;
import org.example.uthteammatching.repositories.ThanhvienProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ThanhvienProjectService {
    private final ThanhvienProjectRepository thanhvienProjectRepository;

    public ThanhvienProjectService(ThanhvienProjectRepository thanhvienProjectRepository) {
        this.thanhvienProjectRepository = thanhvienProjectRepository;
    }

    public boolean isUserInProject(Long userMaSo, Long projectMaSo) {
        ThanhvienProjectId id = new ThanhvienProjectId(userMaSo, projectMaSo);
        return thanhvienProjectRepository.existsById(id);
    }

    public void addUserToProject(UthUser user, Project project, String vaiTro) {
        ThanhvienProjectId id = new ThanhvienProjectId(user.getMaSo(), project.getMaProject());

        ThanhvienProject tvp = new ThanhvienProject();
        tvp.setId(id);
        tvp.setUserMaSo(user);
        tvp.setProjectMaSo(project);
        tvp.setVaiTro(vaiTro);

        thanhvienProjectRepository.save(tvp);
    }

    public void addJoinRequest(UthUser user, Project project) {
        ThanhvienProject thanhvienProject = new ThanhvienProject();
        thanhvienProject.setUserMaSo(user);
        thanhvienProject.setProjectMaSo(project);
        thanhvienProject.setVaiTro("Thành viên");

        thanhvienProjectRepository.save(thanhvienProject);
    }


}