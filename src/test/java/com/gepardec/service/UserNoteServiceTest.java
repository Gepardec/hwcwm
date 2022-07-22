package com.gepardec.service;

import com.gepardec.model.User;
import com.gepardec.repository.NoteRepository;
import com.gepardec.repository.UserRepository;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@QuarkusTest
class UserNoteServiceTest {

    public static final String USER_NAME = "Christoph";

    @InjectMock
    UserRepository userRepository;

    @InjectMock
    NoteRepository noteRepository;

    @Inject
    UserNoteService userNoteService;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

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
    }

    @Test
    void createUser() {
    }

    @Test
    void createNote() {
    }

    @Test
    void updateContent() {
    }

    @Test
    void isEdited() {
    }

    private User createUserMock() {
        User user = new User();
        user.setName(USER_NAME);
        user.setCreationTimestamp(LocalDateTime.now());

        return user;
    }
}