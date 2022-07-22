package com.gepardec.service;

import com.gepardec.model.Note;
import com.gepardec.model.User;
import com.gepardec.repository.NoteRepository;
import com.gepardec.repository.UserRepository;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@QuarkusTest
class UserNoteServiceTest {

    public static final String USER_NAME = "Christoph";

    private static final String NOTE_CONTENT = "Neue Notiz";

    private static final String UPDATED_NOTE_CONTENT = "Aktualisierte Notiz";

    @InjectMock
    UserRepository userRepository;

    @InjectMock
    NoteRepository noteRepository;

    @Inject
    UserNoteService userNoteService;

    @Test
    void getUserByUsername() {
        // given
        when(userRepository.getUser(anyString())).thenReturn(createUserMock());

        // when
        User user = userNoteService.getUserByUsername(USER_NAME);

        // then
        assertThat(user.getName()).isEqualTo(USER_NAME);
    }

    @Test
    void getAllNotesForUser() {
        // given
        when(userRepository.getUser(anyString())).thenReturn(createUserMock());
        when(noteRepository.findAllNotesForUser(any(User.class))).thenReturn(List.of(createNoteMock()));

        // when
        List<Note> notes = userNoteService.getAllNotesForUser(USER_NAME);

        // then
        assertThat(notes).hasSize(1);
    }

    @Test
    void createUser() {
        // given
        doNothing().when(userRepository).persist(any(User.class));
        when(userRepository.find(eq("username"), anyString()).firstResult()).thenReturn(createUserMock());

        // when
        User user = userNoteService.createUser(createUserMock());

        // then
        assertThat(user.getName()).isEqualTo(USER_NAME);
    }

    @Test
    void createNote() {
        // given
        doNothing().when(noteRepository).persist(any(Note.class));
        User user = createUserMock();
        when(userRepository.getUser(USER_NAME)).thenReturn(user);
        when(userRepository.findById(1L)).thenReturn(user);

        // when
        Note note = userNoteService.createNote(USER_NAME, NOTE_CONTENT);

        // then
        assertThat(note.getContent()).isEqualTo(NOTE_CONTENT);
        assertThat(note.getOwner().getName()).isEqualTo(USER_NAME);
    }

    @Test
    void updateContent() {
        // given
        when(noteRepository.findById(anyLong())).thenReturn(createNoteMock());
        doNothing().when(noteRepository).persist(any(Note.class));

        // when
        Note note = userNoteService.updateContent(1L, UPDATED_NOTE_CONTENT);

        // then
        assertThat(note.getContent()).isEqualTo(UPDATED_NOTE_CONTENT);
        assertThat(note.getLastEditTimestamp().truncatedTo(ChronoUnit.SECONDS)).isAfter(note.getCreationTimestamp().truncatedTo(ChronoUnit.SECONDS));
    }

    @Test
    void isEdited() {
        checkIsNotEditedBeforeUpdate();
        checkIsEditedAfterUpdate();
    }

    private void checkIsNotEditedBeforeUpdate() {
        // given
        when(noteRepository.findById(any(Long.class))).thenReturn(createNoteMock());

        // when
        boolean edited = userNoteService.isEdited(1L);

        // then
        assertThat(edited).isFalse();
    }

    private void checkIsEditedAfterUpdate() {
        // given
        doNothing().when(noteRepository).persist(any(Note.class));

        // when
        Note note = userNoteService.updateContent(1L, UPDATED_NOTE_CONTENT);
        when(noteRepository.findById(any(Long.class))).thenReturn(note);
        boolean edited = userNoteService.isEdited(1L);

        // then
        assertThat(edited).isTrue();
    }

    private User createUserMock() {
        User user = new User();
        user.setName(USER_NAME);
        user.setCreationTimestamp(LocalDateTime.now());

        return user;
    }

    private Note createNoteMock() {
        LocalDateTime now = LocalDateTime.now();
        Note note = new Note();
        note.setContent("Hallo Test");
        note.setCreationTimestamp(now);
        note.setLastEditTimestamp(now);
        note.setOwner(createUserMock());

        return note;
    }
}