package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.apache.ibatis.annotations.*;

import java.util.ArrayList;

@Mapper
public interface FileMapper {
    @Select("SELECT * FROM FILES WHERE userId = #{userid}")
    ArrayList<File> getUserFiles(int userid);

    @Select("SELECT * FROM FILES WHERE fileid = #{fileId}")
    File getFile(int fileId);

    @Insert("INSERT INTO FILES (filename, contenttype, filesize, userid,filedata) VALUES(#{fileName}, #{contentType}, #{fileSize}, #{userId}, #{fileData})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    int addFile(File file);

    @Delete("DELETE FROM FILES WHERE fileid = #{fileId}")
    int deleteFile(int fileId);

    @Select("SELECT * FROM FILES WHERE filename = '${fileName}' AND userid = ${userId}")
    File getUserFileByName(int userId, String fileName);
}
