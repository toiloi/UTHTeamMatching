package org.example.uthteammatching.controllers;

import org.example.uthteammatching.models.Project;
import org.example.uthteammatching.models.UthUser;
import org.example.uthteammatching.services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.example.uthteammatching.models.BaiViet;
import org.example.uthteammatching.services.PostService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class homeController {

    @Autowired
    private PostService postService;

    @Autowired
    private ProjectService projectService;

    @GetMapping("/")
    public String home(Model model){
        List<BaiViet> baiViets = postService.getAllPost();
        model.addAttribute("baiViets", baiViets);
        return "home";
    }

    @GetMapping("/profile/{id}")
    public String showProfile(@PathVariable("id") Long id, Model model) {
        List<BaiViet> baiViets = postService.getAllPost();

        UthUser user = null;
        for (BaiViet bv : baiViets) {
            if (bv.getUserMaSo().getMaSo().equals(id)) {
                user = bv.getUserMaSo();
                break;
            }
        }

        if (user != null) {
            model.addAttribute("user", user);
            return "profile";
        } else {
            model.addAttribute("errorMessage", "User not found");
            return "404";
        }
    }

    @GetMapping("/project/{id}")
    public String showProject(@PathVariable("id") String id, Model model) {
        Project project = projectService.getProject(id);
        model.addAttribute("project", project);
        return "project";
    }

}
