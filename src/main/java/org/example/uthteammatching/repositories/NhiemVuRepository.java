package org.example.uthteammatching.repositories;

import org.example.uthteammatching.models.NhiemVu;
import org.example.uthteammatching.models.Project;
import org.example.uthteammatching.models.ThanhvienProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NhiemVuRepository extends JpaRepository<NhiemVu, Long> {
    // Trả về 1 nhiệm vụ nếu biết rõ project và người nhận (ThanhvienProject)
    List<NhiemVu> findByProjectMaSoAndThanhvienProject(Project projectMaSo, ThanhvienProject thanhvienProject);

    // Lấy danh sách nhiệm vụ theo mã project
    List<NhiemVu> findByProjectMaSo_MaProject(Long maProject);


    // Lấy danh sách nhiệm vụ theo người nhận
    List<NhiemVu> findByThanhvienProject(ThanhvienProject thanhvienProject);
}
