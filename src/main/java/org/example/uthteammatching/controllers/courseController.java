package org.example.uthteammatching.controllers;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class courseController {
    @GetMapping("/courses")
    public String courses(){
        return "courses";
    }
}
