package com.webapp.app.ws.service.impl;

import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

import com.webapp.app.ws.exceptions.AuthenticationException;
import com.webapp.app.ws.io.dao.DAO;
import com.webapp.app.ws.io.dao.impl.MYSQLDAO;
import com.webapp.app.ws.service.AuthenticateService;
import com.webapp.app.ws.service.UserService;
import com.webapp.app.ws.shared.dto.UserDTO;
import com.webapp.app.ws.ui.model.response.ErrorMessages;
import com.webapp.app.ws.utils.UserProfileUtils;

public class AuthenticateServiceImpl implements AuthenticateService {

    private DAO database;

    @Override
    public UserDTO authenticate(String userName, String password) throws AuthenticationException {
        UserService userService = new UserServiceImpl();

        UserDTO storedUser = userService.getUserByUserName(userName);

        if (storedUser == null) {
            throw new AuthenticationException(ErrorMessages.AUTHENTICATION_FAILED.getErrorMessages());
        }

        boolean authenticated = false;
        String encryptedPassword = null;
        encryptedPassword = new UserProfileUtils().generateSecurePassword(password, storedUser.getSalt());

        if (encryptedPassword != null && encryptedPassword.equalsIgnoreCase(storedUser.getEncryptedPassword())) {
            if (userName != null && userName.equalsIgnoreCase(storedUser.getEmail())) {
                authenticated = true;
            }
        }

        if (!authenticated) {
            throw new AuthenticationException(ErrorMessages.AUTHENTICATION_FAILED.getErrorMessages());
        }
        return storedUser;
    }

    @Override
    public String issueAccessToken(UserDTO userDTO) {
        String returnValue = null;

        String newSaltAsPostFix = userDTO.getSalt();
        String accessTokenMaterial = userDTO.getUserId() + newSaltAsPostFix;

        byte[] encryptedAccessToken = null;

        try {
            encryptedAccessToken = new UserProfileUtils().encrypt(userDTO.getEncryptedPassword(), accessTokenMaterial);
        } catch (InvalidKeySpecException ex) {
            throw new AuthenticationException(ErrorMessages.AUTHENTICATION_FAILED.getErrorMessages());
        }

        String encryptedAccessTokenBase64Encoded = Base64.getEncoder().encodeToString(encryptedAccessToken);
        int tokenLength = encryptedAccessTokenBase64Encoded.length();

        String tokenToSaveDatabase = encryptedAccessTokenBase64Encoded.substring(0, tokenLength / 2);
        returnValue = encryptedAccessTokenBase64Encoded.substring(tokenLength / 2, tokenLength);

        userDTO.setToken(tokenToSaveDatabase);
        updateUser(userDTO);

        return returnValue;
    }

    @Override
    public void resetSecurityCredentials(String password, UserDTO userDTO) {
        UserProfileUtils userProfileUtils = new UserProfileUtils();

        String salt = userProfileUtils.getSalt(30);

        String securePassword = userProfileUtils.generateSecurePassword(password, salt);

        userDTO.setSalt(salt);
        userDTO.setEncryptedPassword(securePassword);
        updateUser(userDTO);
    }

    private void updateUser(UserDTO userDTO) {
        this.database = new MYSQLDAO();

        try {
            database.openConnection();
            database.updateUser(userDTO);
        } finally {
            database.closeConnection();
        }
    }
}
