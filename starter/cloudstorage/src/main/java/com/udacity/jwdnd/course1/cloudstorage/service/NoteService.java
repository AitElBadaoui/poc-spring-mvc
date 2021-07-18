package com.udacity.jwdnd.course1.cloudstorage.service;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {
    private final NoteMapper noteMapper;


    public NoteService(NoteMapper noteMapper) {
        this.noteMapper = noteMapper;
    }

    public int addNote(Note note){
        return noteMapper.addNote(note);
    }
    public List<Note> getNotes(int userId){
        return this.noteMapper.getUserNotes(userId);
    }
    public int editNote(Note note){
        return this.noteMapper.editNote(note);
    }

    public int deleteNote(int noteId){
        return this.noteMapper.deleteNote(noteId);
    }
}
