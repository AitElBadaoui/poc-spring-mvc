package com.udacity.jwdnd.course1.cloudstorage.service;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileService {
    private final FileMapper fileMapper;

    public FileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    public int addFile(File file){
        return fileMapper.addFile(file);
    }

    public List<File> getFiles(int userId){
        return this.fileMapper.getUserFiles(userId);
    }

    public File getFilesById(int fileId){
        return this.fileMapper.getFile(fileId);
    }

    public int deleteFile(int fileId){
        return this.fileMapper.deleteFile(fileId);
    }

    public boolean isFileNameAvailable(int userId, String fileName){
        return this.fileMapper.getUserFileByName(userId,fileName) == null;
    }
}
