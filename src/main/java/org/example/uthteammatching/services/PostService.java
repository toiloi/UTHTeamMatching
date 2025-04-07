package org.example.uthteammatching.services;

import org.example.uthteammatching.models.BaiViet;
import org.example.uthteammatching.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    public List<BaiViet> getAllPost() {
        return postRepository.findAll();
    }

}
