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

    boolean existsByUserMaSoAndProjectMaSo(UthUser userMaSo, Project projectMaSo);

    List<ThanhvienProject> findByUserMaSoAndVaiTro(UthUser userMaSo, String vaiTro);

    List<ThanhvienProject> findByProjectMaSoInAndVaiTro(List<Project> projects, String vaiTro);

    ThanhvienProject findByUserMaSo_MaSoAndProjectMaSo_MaProject(Long userMaSo, Long projectMaSo);

    ThanhvienProject findByUserMaSoAndProjectMaSo(UthUser userMaSo, Project projectMaSo);

    List<ThanhvienProject> findByProjectMaSo(Project projectMaSo);

    // Thêm phương thức mới để lấy danh sách thành viên theo dự án
    List<ThanhvienProject> findByProjectMaSo_MaProject(Long projectMaSo);
}