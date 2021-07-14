package com.udacity.finalreview.demo.controller;

import com.udacity.finalreview.demo.model.ChatForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
public class LoginController {

    @GetMapping
    public String getLoginPage(ChatForm chatForm, Model model) {
        return "login";
    }
}
