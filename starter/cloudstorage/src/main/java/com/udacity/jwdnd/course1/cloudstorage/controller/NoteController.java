package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.service.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/note")
public class NoteController {
    private final UserService userService;
    private final NoteService noteService;

    public NoteController(UserService userService, NoteService noteService) {
        this.userService = userService;
        this.noteService = noteService;
    }

    @PostMapping
    public String addNote(Authentication authentication, Note note, RedirectAttributes redirectAttributes){
        User user = userService.getUser(authentication.getName());
        note.setUserId(user.getUserid());
        int rowAffected = -1;
        String successMessage = null;

        if(note.getNoteId() == null){
            rowAffected = noteService.addNote(note);
            successMessage= "The note was successfully added !";
        }
        else if(note.getNoteId()!= null){
            rowAffected = noteService.editNote(note);
            successMessage= "The note was successfully edited !";
        }
        if(rowAffected < 0){
            redirectAttributes.addFlashAttribute("errorMessage", "There was an error. Please try again.");
        }
        else{
            redirectAttributes.addFlashAttribute("successMessage", successMessage);
        }

        return "redirect:/home";
    }

    @GetMapping("delete/{noteId}")
    public String deleteNote(@PathVariable("noteId") int noteId, RedirectAttributes redirectAttributes){
        int rowAffected = noteService.deleteNote(noteId);

        if(rowAffected <=0){
            redirectAttributes.addFlashAttribute("errorMessage", "There was an error. Please try again.");
        }
        else{
            redirectAttributes.addFlashAttribute("successMessage", "The note was successfully deleted !");
        }

        return "redirect:/home";
    }
}
