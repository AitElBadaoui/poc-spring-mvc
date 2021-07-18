package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.service.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.service.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.service.UserService;
import com.udacity.jwdnd.course1.cloudstorage.services.EncryptionService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/home")
public class HomeController {

    private final NoteService noteService;
    private final UserService userService;
    private final CredentialService credentialService;

    public HomeController(NoteService noteService, UserService userService, CredentialService credentialService, EncryptionService encryptionService ) {
        this.noteService = noteService;
        this.userService = userService;
        this.credentialService = credentialService;
    }

    @ModelAttribute("encryptionService")
    public EncryptionService getEncryptionService(){return new EncryptionService();}

    @GetMapping
    public String getHomePage(Authentication authentication, Model model){
        User user = userService.getUser(authentication.getName());
        List<Note> notes = this.noteService.getNotes(user.getUserid());
        List<Credential> credentials = this.credentialService.getCredentials(user.getUserid());
        model.addAttribute("notes",notes);
        model.addAttribute("credentials",credentials);
        return "home";
    }
}
