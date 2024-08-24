package com.example.project2.util;

/**
 * This is the Password Handler, this class functions to abstract as
 * much of the hashing processes undertaken during registration and
 * login as possible out of MainActivity, it is a bridge between
 * MainActivity, and PasswordHasher
 */

import java.util.Arrays;

public class PasswordHandler {

    PasswordHasher passwordHasher = new PasswordHasher();

    /**
     * Calls methods to verify the user's password during login.
     *
     * @param password     The user's input password.
     * @param storedHash   The stored password hash.
     * @param storedSalt   The stored salt value.
     * return True if the password is a match, false otherwise.
     */

    public boolean onUserLogin(String password, String storedHash, String storedSalt) {
        char[] passwordCharArray = password.toCharArray();
        byte[] hashByteArray = passwordHasher.fromBase64(storedHash);
        byte[] saltByteArray = passwordHasher.fromBase64(storedSalt);

        try {
            return passwordHasher.verifyPassword(passwordCharArray, hashByteArray, saltByteArray);
        } finally {
            Arrays.fill(passwordCharArray, '0');
            Arrays.fill(hashByteArray, (byte) 0);
            Arrays.fill(saltByteArray, (byte) 0);
        }
    }

    /**
     * Generates a salt value for user registration.
     * return the salt value as a base64-encoded string.
     */
    public String getRegistrationSalt() {
        byte[] salt = passwordHasher.generateSalt();
        return passwordHasher.toBase64(salt);
    }

    /**
     * Calls methods to hash the user's password during registration.
     *
     * @param newPassword The user's input password.
     * @param newSalt     The salt value for the new user.
     * return the hashed password as a base64-encoded string.
     */
    public String onUserRegistration (String newPassword, String newSalt) {
        char[] passwordCharArray = newPassword.toCharArray();
        byte[] saltByteArray = passwordHasher.fromBase64(newSalt);
        byte[] hashedPassword = passwordHasher.hashPassword(passwordCharArray,saltByteArray);

        try {
            return passwordHasher.toBase64(hashedPassword);
        } finally {
            Arrays.fill(passwordCharArray, '0');
            Arrays.fill(saltByteArray, (byte) 0);
            Arrays.fill(hashedPassword, (byte) 0);
        }
    }
}
