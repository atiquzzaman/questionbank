package com.questionbank.auth;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.Random;

public  class AuthUtils {
    public final static String UPPERCASE_ALPHANUMERIC_CHARS = "abcdefghijklmnoqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVXYZ0123456789";
    public final static int SALT_LENGTH = 16;
    public static String bytetoString(byte[] input) {
        return Base64.getEncoder().encodeToString(input);
    }

    public static String generateSalt() {
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < SALT_LENGTH) {
            int index = rnd.nextInt(UPPERCASE_ALPHANUMERIC_CHARS.length());
            salt.append(UPPERCASE_ALPHANUMERIC_CHARS.charAt(index));
        }

        return salt.toString();
    }

    public static String hashPass(String password, String salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeySpec keySpec = new PBEKeySpec(password.toCharArray(),   salt.getBytes(),65482,128);
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
        byte[] hash = secretKeyFactory.generateSecret(keySpec).getEncoded();
        return bytetoString(hash);
    }

    public static boolean matchPass(String storedPasswordHash, String inputPassword, String salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String inputPasswordHash = hashPass(inputPassword, salt);
        return inputPasswordHash.equals(storedPasswordHash);

    }

}
