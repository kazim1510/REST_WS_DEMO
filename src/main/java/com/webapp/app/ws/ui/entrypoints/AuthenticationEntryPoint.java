package com.webapp.app.ws.ui.entrypoints;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.webapp.app.ws.service.AuthenticateService;
import com.webapp.app.ws.service.impl.AuthenticateServiceImpl;
import com.webapp.app.ws.shared.dto.UserDTO;
import com.webapp.app.ws.ui.model.request.LoginCrendential;
import com.webapp.app.ws.ui.model.response.AuthenticationDetails;

@Path("/authentication")
public class AuthenticationEntryPoint {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public AuthenticationDetails userLogin(LoginCrendential loginCrendential) {
        AuthenticationDetails returnValue = new AuthenticationDetails();

        AuthenticateService authenticateService = new AuthenticateServiceImpl();
        UserDTO authenticatedUser = authenticateService.authenticate(loginCrendential.getUserName(), loginCrendential.getPassword());

        authenticateService.resetSecurityCredentials(loginCrendential.getPassword(), authenticatedUser);

        String accessToken = authenticateService.issueAccessToken(authenticatedUser);
        returnValue.setId(authenticatedUser.getUserId());
        returnValue.setToken(accessToken);
        return returnValue;
    }
}
