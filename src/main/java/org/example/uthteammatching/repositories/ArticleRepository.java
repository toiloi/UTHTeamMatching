package org.example.uthteammatching.repositories;

import org.example.uthteammatching.models.BaiViet;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<BaiViet, Long> {
    @EntityGraph(attributePaths = {"userMaSo", "projectMaSo"})
    List<BaiViet> findAll();

    // Phương thức tìm kiếm bài viết theo tên dự án
    @EntityGraph(attributePaths = {"userMaSo", "projectMaSo"})
    List<BaiViet> findByProjectMaSo_TenProjectContainingIgnoreCase(String tenProject);
}