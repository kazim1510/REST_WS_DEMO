package com.webapp.app.ws.exceptions;


import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import com.webapp.app.ws.ui.model.response.ErrorMessage;
import com.webapp.app.ws.ui.model.response.ErrorMessages;

public class AuthenticateExceptionMapper implements ExceptionMapper<AuthenticationException> {
    @Override
    public Response toResponse(AuthenticationException exception) {
        ErrorMessage errorMessage = new ErrorMessage(exception.getMessage(),
                ErrorMessages.AUTHENTICATION_FAILED.name(), "http://kazimreshamwala.com");
        return Response.status(Response.Status.UNAUTHORIZED).entity(errorMessage).build();
    }
}
