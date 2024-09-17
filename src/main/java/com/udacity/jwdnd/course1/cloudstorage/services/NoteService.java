package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mappers.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.mappers.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.models.Note;
import com.udacity.jwdnd.course1.cloudstorage.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoteService {

    private final NoteMapper noteMapper;

    private final UserMapper userMapper;

    public List<Note> getNotesByUserName(String username) {
        User user = userMapper.findByUserName(username);
        return noteMapper.findByUserId(user.getUserId());
    }

    public int saveNote(Note note, String username) {
        User user = userMapper.findByUserName(username);
        int rowsEffected = 0;

        if (note.getNoteId() == null) {
            note.setUserId(user.getUserId());
            rowsEffected = noteMapper.insert(note);
        } else {
            // Check owner of note
            Note dbNote = noteMapper.findById(note.getNoteId());
            if (dbNote.getUserId().intValue() == user.getUserId().intValue()){
                rowsEffected = noteMapper.update(note);
            }
        }
        return rowsEffected;
    }

    public int removeNote(int noteId, String username) {
        User user = userMapper.findByUserName(username);
        Note note = noteMapper.findById(noteId);
        if (note.getUserId().intValue() == user.getUserId().intValue()) {
            return noteMapper.delete(noteId);
        }
        return 0;
    }
}
