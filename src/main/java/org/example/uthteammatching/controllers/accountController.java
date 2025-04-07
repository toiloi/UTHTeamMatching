package org.example.uthteammatching.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class accountController {

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @GetMapping("/account")
    public String showAccountForm() {
        return "account";
    }

}