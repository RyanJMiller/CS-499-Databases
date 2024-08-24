package com.example.project2.util;

import java.security.SecureRandom;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Base64;

public class PasswordHasher {

    private static final int SALT_LENGTH = 16; // Salt length in bytes
    private static final int ITERATIONS = 1000; // Iterations for key generation
    private static final int KEY_LENGTH = 256; // Key length in bits

    /**
     * Generates a random salt.
     * return the generated salt as a byte array.
     */
    public byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        return salt;
    }

    /**
     * Hashes a password using PBKDF2 with HMAC-SHA256.
     *
     * @param password The password as a character array.
     * @param salt     The salt used for hashing.
     * return the hashed password as a byte array.
     */
    public byte[] hashPassword(char[] password, byte[] salt) {
        try {
            KeySpec spec = new PBEKeySpec(password, salt, ITERATIONS, KEY_LENGTH);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            return factory.generateSecret(spec).getEncoded();
        } catch (Exception e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    /**
     * Verifies a password by comparing a stored hash with a newly hashed password.
     *
     * @param enteredPassword The entered password as a character array.
     * @param storedHash      The stored password hash.
     * @param storedSalt      The stored salt value.
     * return True if the password is correct, false otherwise.
     */
    public boolean verifyPassword(char[] enteredPassword, byte[] storedHash, byte[] storedSalt) {
        byte[] hashedPassword = hashPassword(enteredPassword, storedSalt);
        return Arrays.equals(hashedPassword, storedHash);
    }

    // methods to convert to and from Base64 (String to Byte array and back)
    public String toBase64(byte[] array) {
        return Base64.getEncoder().encodeToString(array);
    }

    public byte[] fromBase64(String base64String) {
        return Base64.getDecoder().decode(base64String);
    }


}
