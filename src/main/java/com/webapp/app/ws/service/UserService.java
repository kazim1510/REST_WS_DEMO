package com.webapp.app.ws.service;

import com.webapp.app.ws.shared.dto.UserDTO;

public interface UserService {

    public UserDTO createUser(UserDTO user);

    public UserDTO getUser(String userId);

    public UserDTO getUserByUserName(String userName);
}
