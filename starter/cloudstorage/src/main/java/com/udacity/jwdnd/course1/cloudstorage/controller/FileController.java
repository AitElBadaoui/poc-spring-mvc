package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.service.FileService;
import com.udacity.jwdnd.course1.cloudstorage.service.UserService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@RequestMapping("/file")
public class FileController {
    private final UserService userService;
    private final FileService fileService;

    public FileController(UserService userService, FileService fileService) {
        this.userService = userService;
        this.fileService = fileService;
    }

    @PostMapping
    public String addFile(@RequestParam("fileUpload") MultipartFile uploadedFile, Authentication authentication, RedirectAttributes redirectAttributes){
        String errorMessage = null;
        User user = userService.getUser(authentication.getName());
        //Check if filename exist:
        if(!fileService.isFileNameAvailable(user.getUserid(),uploadedFile.getOriginalFilename())){
            errorMessage = "File name already exists !";
        }
        File file = new File();
        file.setFileName(uploadedFile.getOriginalFilename());
        file.setContentType(uploadedFile.getContentType());
        file.setFileSize(uploadedFile.getSize());
        file.setUserId(user.getUserid());
        try {
            file.setFileData(uploadedFile.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
            errorMessage = "There was an error while uploading. Please try again.";
        }
        if(errorMessage ==  null){
            int newRow = fileService.addFile(file);
            if(newRow < 0){
                errorMessage = "There was an error while inserting. Please try again.";
            }
        }
        if(errorMessage ==  null){
            redirectAttributes.addFlashAttribute("successMessage", "Upload success !");
        }
        else {
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
        }
        return "redirect:/home";
    }

    @GetMapping("delete/{fileId}")
    public String deleteFile(@PathVariable("fileId") int fileId, RedirectAttributes redirectAttributes){
        int rowAffected = fileService.deleteFile(fileId);

        if(rowAffected <=0){
            redirectAttributes.addFlashAttribute("errorMessage", "There was an error. Please try again.");
        }
        else{
            redirectAttributes.addFlashAttribute("successMessage", "The file was successfully deleted !");
        }
        return "redirect:/home";
    }

    @GetMapping("download/{fieldId}")
    public ResponseEntity downloadFile(@PathVariable("fieldId") int fileId, RedirectAttributes redirectAttributes){
        File file = fileService.getFilesById(fileId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFileName() + "\"")
                .body(new ByteArrayResource(file.getFileData()));
    }

}
