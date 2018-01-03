package com.webapp.app.ws.service;

import java.util.List;

import com.webapp.app.ws.shared.dto.UserDTO;

public interface UserService {

    UserDTO createUser(UserDTO user);

    UserDTO getUser(String userId);

    UserDTO getUserByUserName(String userName);

    List<UserDTO> getUsers(int start, int limit);

    void updateUser(UserDTO user);

    void deleteUser(UserDTO user);
}
