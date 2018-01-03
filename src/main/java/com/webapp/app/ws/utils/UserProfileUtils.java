package com.webapp.app.ws.utils;


import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Random;
import java.util.UUID;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import com.webapp.app.ws.exceptions.MissingRequiredFieldsException;
import com.webapp.app.ws.shared.dto.UserDTO;
import com.webapp.app.ws.ui.model.response.ErrorMessages;

public class UserProfileUtils {

    private final Random RANDOM = new SecureRandom();
    private final String ALPHABET = "0123456789abcdefghijklmnopqrstuvwxyz";
    private final int ITERATION = 10000;
    private final int KEY_LENGTH = 256;

    public String generateUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public void validateRequiredFields(UserDTO user) {
        if (user.getFirstName() == null || user.getFirstName().isEmpty() || user.getLastName() == null) {
            throw new MissingRequiredFieldsException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessages());
        }
    }

    public String generateUserId(int length) {
        return generateRandomString(length);
    }

    private String generateRandomString(int length) {
        StringBuilder returnValue = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            returnValue.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }

        return new String(returnValue);
    }


    public String getSalt(int length) {
        return generateRandomString(length);
    }


    public String generateSecurePassword(String password, String salt) {
        String returnValue = null;

        byte[] securePassword = hash(password.toCharArray(), salt.getBytes());
        returnValue = Base64.getEncoder().encodeToString(securePassword);

        return returnValue;
    }


    public byte[] hash(char[] password, byte[] salt) {
        PBEKeySpec spec = new PBEKeySpec(password, salt, ITERATION, KEY_LENGTH);
        Arrays.fill(password, Character.MIN_VALUE);
        try {
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            return secretKeyFactory.generateSecret(spec).getEncoded();

        } catch (NoSuchAlgorithmException e) {
            throw new AssertionError("Error while hashing password" + e.getMessage(), e);
        } catch (InvalidKeySpecException e) {
            throw new AssertionError("Error while hashing password" + e.getMessage(), e);
        } finally {
            spec.clearPassword();
        }
    }

    public byte[] encrypt(String password, String accesTokenMaterial) throws InvalidKeySpecException{
        return hash(password.toCharArray(), accesTokenMaterial.getBytes());
    }


}
