package org.example.uthteammatching.services;

import org.example.uthteammatching.models.BaiViet;
import org.example.uthteammatching.repositories.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleService {
    @Autowired
    private ArticleRepository articleRepository;

    public List<BaiViet> getAllArticles() {
        return articleRepository.findAll();
    }

}
