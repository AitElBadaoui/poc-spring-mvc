package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.service.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.service.UserService;
import com.udacity.jwdnd.course1.cloudstorage.services.EncryptionService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.SecureRandom;
import java.util.Base64;

@Controller
@RequestMapping("/credential")
public class CredentialController {
    private final UserService userService;
    private final CredentialService credentialService;
    private final EncryptionService encryptionService;

    public CredentialController(UserService userService, CredentialService credentialService, EncryptionService encryptionService) {
        this.userService = userService;
        this.credentialService = credentialService;
        this.encryptionService = encryptionService;
    }

    @PostMapping
    public String addCredential(Authentication authentication, Credential credential, RedirectAttributes redirectAttributes){
        int rowAffected = -1;
        String successMessage = null;

        User user = userService.getUser(authentication.getName());
        credential.setUserId(user.getUserid());

        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);
        credential.setKey(encodedKey);
        credential.setPassword(encryptionService.encryptValue(credential.getPassword(), encodedKey));

        if(credential.getCredentialId() == null){
            rowAffected = credentialService.addCredential(credential);
            successMessage= "The credential was successfully added !";
        }
        else if(credential.getCredentialId() != null){
            rowAffected = credentialService.editCredential(credential);
            successMessage= "The credential was successfully edited !";
        }

        if(rowAffected < 0){
            redirectAttributes.addFlashAttribute("errorMessage", "There was an error. Please try again.");
        }
        else{
            redirectAttributes.addFlashAttribute("successMessage", successMessage);
        }

        return "redirect:/home";
    }

    @GetMapping("delete/{credentialId}")
    public String deleteCredential(@PathVariable("credentialId") int credentialId, RedirectAttributes redirectAttributes){
        int rowAffected = credentialService.deleteCredential(credentialId);

        if(rowAffected <=0){
            redirectAttributes.addFlashAttribute("errorMessage", "There was an error. Please try again.");
        }
        else{
            redirectAttributes.addFlashAttribute("successMessage", "The credential was successfully deleted !");
        }

        return "redirect:/home";
    }
}
