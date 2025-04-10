package org.example.uthteammatching.controllers;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Project {
    @GetMapping("/project")
    public String project(){
        return "project";
    }
}