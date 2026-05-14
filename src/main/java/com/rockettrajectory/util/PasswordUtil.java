package com.rockettrajectory.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * PasswordUtil
 * ------------
 * Tiny helper around SHA-256 hashing.  Plain-text passwords never
 * leave the controllers; everything below this layer (services, DB)
 * deals exclusively with hashed values.
 *
 * NOTE: SHA-256 is used because it is part of the standard JDK and
 * keeps the project dependency-free; for production work a slow
 * adaptive hash (bcrypt / argon2) would be preferable.
 */
public final class PasswordUtil {

    private PasswordUtil() { }

    /**
     * Compute the lowercase hex SHA-256 digest of the supplied string.
     */
    public static String hash(String plain) {
        if (plain == null) plain = "";
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(plain.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder(bytes.length * 2);
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            // SHA-256 is part of every standard JDK, so this is unreachable
            throw new RuntimeException("SHA-256 not available on this JVM", e);
        }
    }

    /**
     * Constant-time-ish comparison between a plain password and a
     * stored hash.  Returns false if either input is null.
     */
    public static boolean matches(String plain, String storedHash) {
        if (plain == null || storedHash == null) return false;
        return hash(plain).equalsIgnoreCase(storedHash);
    }
}
