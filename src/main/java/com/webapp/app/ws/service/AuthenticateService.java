package com.webapp.app.ws.service;

import com.webapp.app.ws.exceptions.AuthenticationException;
import com.webapp.app.ws.shared.dto.UserDTO;

public interface AuthenticateService {

    UserDTO authenticate(String userName, String password) throws AuthenticationException;

    String issueAccessToken(UserDTO userDTO);

    void resetSecurityCredentials(String password, UserDTO userDTO);
}
