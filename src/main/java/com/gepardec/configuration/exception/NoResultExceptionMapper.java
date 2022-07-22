package com.gepardec.configuration.exception;

import com.gepardec.model.HwcwmResponse;

import javax.persistence.NoResultException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class NoResultExceptionMapper implements ExceptionMapper<NoResultException> {

    private static final String RESOURCE_NOT_FOUND = "Resource not found.";

    @Override
    public Response toResponse(NoResultException noResultException) {

        String message = RESOURCE_NOT_FOUND;
        if (!noResultException.getMessage().isEmpty()) {
            message = noResultException.getMessage();
        }
        HwcwmResponse response = new HwcwmResponse(Response.Status.NOT_FOUND.getStatusCode(), message);
        return Response.status(Response.Status.NOT_FOUND).entity(response).build();
    }

//    @Override
//    public Response toResponse(Exception exception) {
//        return null;
//    }
}
