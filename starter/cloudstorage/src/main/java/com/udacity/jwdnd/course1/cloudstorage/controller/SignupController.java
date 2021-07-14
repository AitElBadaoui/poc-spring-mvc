package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/signup")
public class SignupController {

    public final UserService userService;

    public SignupController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String getSignupPage(Model model){
        return "signup";
    }

    @PostMapping
    public String signupUser(User user, Model model){
        String signupError = null;
        if(!userService.isUsernameAvailable(user.getUsername())){
            signupError = "Username already exists !";
        }

        if(signupError ==  null){
            int newRow = userService.createUser(user);
            if(newRow < 0){
                signupError = "There was an error signing you up. Please try again.";
            }
        }

        if(signupError ==  null){
            model.addAttribute("signupSuccess", true);
        }
        else {
            model.addAttribute("signupError", signupError);
        }

        return "signup";
    }
}