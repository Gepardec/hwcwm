package com.gepardec.repository;


import com.gepardec.model.Note;
import com.gepardec.model.User;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class NoteRepository implements PanacheRepository<Note> {

    public List<Note> findAllNotesForUser(User user) {
        return list("owner", user);
    }
}
