package com.webapp.app.ws.exceptions;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import com.webapp.app.ws.ui.model.response.ErrorMessage;
import com.webapp.app.ws.ui.model.response.ErrorMessages;

public class CouldNotDeleteRecordExceptionMapper implements ExceptionMapper {
    @Override
    public Response toResponse(Throwable exception) {
        ErrorMessage errorMessage = new ErrorMessage(exception.getMessage(),
                ErrorMessages.COULD_NOT_DELETE.name(), "http://kazimreshamwala.com");
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorMessage).build();
    }
}
