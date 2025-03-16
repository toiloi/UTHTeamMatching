package org.example.uthteammatching.controllers;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class accountController {
    @GetMapping("/account")
    public String account(){
        return "account";
    }
}