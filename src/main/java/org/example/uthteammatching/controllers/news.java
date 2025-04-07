package org.example.uthteammatching.controllers;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class news {
    @GetMapping("/news")
    public String news(){
        return "news";
    }
}
