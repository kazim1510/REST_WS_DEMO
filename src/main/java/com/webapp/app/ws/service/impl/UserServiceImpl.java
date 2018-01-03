package com.webapp.app.ws.service.impl;

import java.util.List;

import com.webapp.app.ws.exceptions.CouldNotDeleteRecordException;
import com.webapp.app.ws.exceptions.CouldNotUpdateRecordException;
import com.webapp.app.ws.exceptions.NoRecordFoundException;
import com.webapp.app.ws.exceptions.UserServiceException;
import com.webapp.app.ws.io.dao.DAO;
import com.webapp.app.ws.io.dao.impl.MYSQLDAO;
import com.webapp.app.ws.service.UserService;
import com.webapp.app.ws.shared.dto.UserDTO;
import com.webapp.app.ws.ui.model.response.ErrorMessages;
import com.webapp.app.ws.utils.UserProfileUtils;

public class UserServiceImpl implements UserService {

    private DAO database;

    public UserServiceImpl() {
        this.database = new MYSQLDAO();
    }

    UserProfileUtils userProfileUtils = new UserProfileUtils();

    @Override
    public UserDTO createUser(UserDTO user) {
        UserDTO userDTO = null;

        userProfileUtils.validateRequiredFields(user);

        UserDTO existingUser = this.getUserByUserName(user.getEmail());
        if (existingUser != null) {
            throw new UserServiceException(ErrorMessages.RECORD_ALREADY_EXIST.name());
        }

        String userId = userProfileUtils.generateUserId(30);
        user.setUserId(userId);

        // Generate salt
        String salt = userProfileUtils.getSalt(30);
        user.setSalt(salt);

        String encryptedPassword = userProfileUtils.generateSecurePassword(user.getPassword(), salt);
        user.setEncryptedPassword(encryptedPassword);

        userDTO = this.saveUser(user);

        return userDTO;
    }

    @Override
    public UserDTO getUser(String userId) {
        UserDTO userDTO = null;

        try {
            this.database.openConnection();
            userDTO = this.database.getUser(userId);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new NoRecordFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessages());
        } finally {
            this.database.closeConnection();
        }

        return userDTO;
    }

    @Override
    public UserDTO getUserByUserName(String userName) {
        UserDTO userDTO = null;

        if (userName == null || userName.isEmpty()) {
            return null;
        }

        try {
            this.database.openConnection();
            userDTO = this.database.getUserByUserName(userName);
        } finally {
            this.database.closeConnection();
        }

        return userDTO;

    }

    @Override
    public List<UserDTO> getUsers(int start, int limit) {
        List<UserDTO> returnValue = null;

        try {
            this.database.openConnection();
            returnValue = this.database.getUsers(start, limit);
        } finally {

            this.database.closeConnection();
        }

        return returnValue;
    }

    @Override
    public void updateUser(UserDTO user) {

        try {
            database.openConnection();
            database.updateUser(user);
        } catch (Exception ex) {
            throw new CouldNotUpdateRecordException(ErrorMessages.COULD_NOT_UPDATE.getErrorMessages());
        } finally {
            database.closeConnection();
        }
    }

    @Override
    public void deleteUser(UserDTO user) {

        try {
            this.database.openConnection();
            this.database.deleteUser(user);
        } catch (Exception ex) {
            throw new CouldNotUpdateRecordException(ErrorMessages.COULD_NOT_UPDATE.getErrorMessages());
        } finally {
            this.database.closeConnection();
        }

        try {
            user = getUser(user.getUserId());
        } catch (NoRecordFoundException ex) {
            user = null;
        }

        if (user != null) {
            throw new CouldNotDeleteRecordException(ErrorMessages.COULD_NOT_DELETE.getErrorMessages());
        }
    }

    private UserDTO saveUser(UserDTO user) {
        UserDTO returnValue = null;

        try {
            this.database.openConnection();
            returnValue = this.database.saveUser(user);
        } finally {

            this.database.closeConnection();
        }
        return returnValue;
    }
}
