package com.webapp.app.ws.filters;

import java.io.IOException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.ext.Provider;

import com.webapp.app.ws.annotations.Secured;
import com.webapp.app.ws.exceptions.AuthenticationException;
import com.webapp.app.ws.service.UserService;
import com.webapp.app.ws.service.impl.UserServiceImpl;
import com.webapp.app.ws.shared.dto.UserDTO;
import com.webapp.app.ws.ui.model.response.ErrorMessages;
import com.webapp.app.ws.utils.UserProfileUtils;

@Secured
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {


    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

        String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer")) {
            throw new AuthenticationException(ErrorMessages.AUTHENTICATION_FAILED.getErrorMessages());
        }

        String token = authorizationHeader.substring("Bearer".length()).trim();

        String userId = requestContext.getUriInfo().getPathParameters().getFirst("id");

        validateToken(token, userId);
    }

    private void validateToken(String token, String userId) {
        UserService userService = new UserServiceImpl();

        UserDTO userDTO = userService.getUser(userId);

        String completeToken = userDTO.getToken() + token;

        String accessTokenMaterial = userId + userDTO.getSalt();
        byte[] encryptedAccessToken = null;

        try {
            encryptedAccessToken = new UserProfileUtils().encrypt(userDTO.getEncryptedPassword(), accessTokenMaterial);
        } catch (InvalidKeySpecException ex) {
            throw new AuthenticationException(ErrorMessages.AUTHENTICATION_FAILED.getErrorMessages());
        }

        String encryptedAccessTokenBase64Encoded = Base64.getEncoder().encodeToString(encryptedAccessToken);

        if (!encryptedAccessTokenBase64Encoded.equals(completeToken)) {
            throw new AuthenticationException(ErrorMessages.AUTHENTICATION_FAILED.getErrorMessages());
        }
    }
}
