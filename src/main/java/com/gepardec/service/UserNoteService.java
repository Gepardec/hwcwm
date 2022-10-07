package com.gepardec.service;

import com.gepardec.model.Note;
import com.gepardec.model.User;
import com.gepardec.repository.NoteRepository;
import com.gepardec.repository.UserRepository;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.List;

@RequestScoped
public class UserNoteService {

    @Inject
    UserRepository userRepository;

    @Inject
    NoteRepository noteRepository;

    public User getUserByUsername(String username) {
        return userRepository.getUser(username);
    }

    public List<Note> getAllNotesForUser(String username) {
        User user = userRepository.getUser(username);
        return noteRepository.findAllNotesForUser(user);
    }

    @Transactional
    public User createUser(User user) {
        userRepository.persist(user);
        return userRepository.find("username", user.getName()).firstResult();
    }

    @Transactional
    public Note createNote(String username, String content) {
        User user = getUserByUsername(username);

        if (user == null) {
            throw new IllegalArgumentException("User must be set.");
        }

        Note note = new Note();
        note.setOwner(user);

        LocalDateTime date = LocalDateTime.now();
        note.setCreationTimestamp(date);
        note.setLastEditTimestamp(date);
        note.setContent(content);

        noteRepository.persist(note);
        return note;
    }

    @Transactional
    public Note updateContent(Long id, String content) {
        Note note = noteRepository.findById(id);

        if (note == null) {
            return null;
        }

        note.setContent(content);
        note.setLastEditTimestamp(LocalDateTime.now());

        return note;
    }

    public boolean isEdited(Long id) {
        Note note = noteRepository.findById(id);

        if (note == null) {
            return false;
        }

        LocalDateTime creationTs = note.getCreationTimestamp().truncatedTo(ChronoUnit.SECONDS);
        LocalDateTime updateTs = note.getLastEditTimestamp().truncatedTo(ChronoUnit.SECONDS);

        return updateTs.isAfter(creationTs);
    }
}
