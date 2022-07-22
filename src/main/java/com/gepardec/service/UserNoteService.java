package com.gepardec.service;

import com.gepardec.model.Note;
import com.gepardec.model.User;
import com.gepardec.repository.NoteRepository;
import com.gepardec.repository.UserRepository;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
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

    public User createUser(User user) {
        userRepository.persist(user);
        return userRepository.find("username", user.getName()).firstResult();
    }

    public Note createNote(Note note, String username) {
        User user = getUserByUsername(username);

        if (user == null) {
            throw new IllegalArgumentException("User must be set.");
        }
        note.setOwner(user);

        noteRepository.persist(note);
        return noteRepository.findById(note.getId());
    }
}
