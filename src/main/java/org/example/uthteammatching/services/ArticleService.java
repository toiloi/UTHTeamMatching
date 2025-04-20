package org.example.uthteammatching.services;

import org.example.uthteammatching.models.BaiViet;
import org.example.uthteammatching.models.UthUser;
import org.example.uthteammatching.models.Project;
import org.example.uthteammatching.repositories.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    public List<BaiViet> getAllArticles() {
        return articleRepository.findAll();
    }

    public List<BaiViet> searchArticlesByProjectName(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return List.of(); // Trả về danh sách rỗng nếu từ khóa rỗng
        }
        return articleRepository.findByProjectMaSo_TenProjectContainingIgnoreCase(keyword);
    }

    public BaiViet createArticle(String noiDung, UthUser user, Project project) {
        BaiViet baiViet = new BaiViet();
        baiViet.setNoiDung(noiDung);
        baiViet.setNgayDang(Instant.now());
        baiViet.setUserMaSo(user);
        baiViet.setProjectMaSo(project);
        return articleRepository.save(baiViet);
    }

    public void deleteArticle(Long articleId) {
        articleRepository.deleteById(articleId);
    }
}