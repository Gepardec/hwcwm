package com.gepardec.api;

import com.gepardec.model.Note;
import com.gepardec.model.User;
import com.gepardec.service.UserNoteService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/user")
@RequestScoped
public class UserNoteResource {

    @Inject
    UserNoteService userNoteService;

    @GET
    @Path("{user}/notes")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Note> getNotes(@PathParam("user") String user) {
        return userNoteService.getAllNotesForUser(user);
    }

    @GET
    @Path("{user}")
    @Produces(MediaType.APPLICATION_JSON)
    public User getUser(@PathParam("user") String user) {
        return userNoteService.getUserByUsername(user);
    }

    @POST
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public User createUser(User user){
        return userNoteService.createUser(user);
    }

    @POST
    @Path("{user}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Note createNote(@PathParam("user") String user, Note note){
        return userNoteService.createNote(note);
    }
}
