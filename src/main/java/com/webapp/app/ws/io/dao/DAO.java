package com.webapp.app.ws.io.dao;

import java.util.List;

import com.webapp.app.ws.shared.dto.UserDTO;

public interface DAO {

    void openConnection();

    void closeConnection();

    UserDTO getUserByUserName(String userName);

    UserDTO getUser(String userId);

    UserDTO saveUser(UserDTO userName);

    void updateUser(UserDTO userDTO);

    List<UserDTO> getUsers(int start, int limit);

    void deleteUser(UserDTO userDTO);
}
