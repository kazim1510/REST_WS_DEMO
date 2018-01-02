package com.webapp.app.ws.exceptions;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.webapp.app.ws.ui.model.response.ErrorMessage;
import com.webapp.app.ws.ui.model.response.ErrorMessages;

@Provider
public class MissingRequiredFieldExceptionMapper implements ExceptionMapper<MissingRequiredFieldsException> {

    public Response toResponse(MissingRequiredFieldsException exception) {
        ErrorMessage errorMessage = new ErrorMessage(exception.getMessage(),
                ErrorMessages.MISSING_REQUIRED_FIELD.name(), "http://kazimreshamwala.com");
        return Response.status(Response.Status.BAD_REQUEST).entity(errorMessage).build();
    }
}
