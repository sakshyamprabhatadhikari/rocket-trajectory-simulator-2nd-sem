package com.rockettrajectory.util;

import java.util.regex.Pattern;

/**
 * ValidationUtil
 * --------------
 * Small library of input-format checks used across servlets.  Each
 * method is null-safe so callers don't have to guard against blanks.
 */
public final class ValidationUtil {

    // Letters and spaces only, 2 – 60 chars
    private static final Pattern NAME_RE  =
            Pattern.compile("^[A-Za-z][A-Za-z ]{1,59}$");

    // Standard-ish email regex – not RFC-perfect but rejects obvious typos
    private static final Pattern EMAIL_RE =
            Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    // Exactly 10 digits (project requirement)
    private static final Pattern PHONE_RE =
            Pattern.compile("^[0-9]{10}$");

    private ValidationUtil() { }

    public static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    public static boolean isValidName(String s) {
        return s != null && NAME_RE.matcher(s.trim()).matches();
    }

    public static boolean isValidEmail(String s) {
        return s != null && EMAIL_RE.matcher(s.trim()).matches();
    }

    public static boolean isValidPhone(String s) {
        return s != null && PHONE_RE.matcher(s.trim()).matches();
    }

    /** At least 8 chars and contains both a letter and a digit. */
    public static boolean isValidPassword(String s) {
        if (s == null || s.length() < 8) return false;
        boolean letter = false, digit = false;
        for (char c : s.toCharArray()) {
            if (Character.isLetter(c)) letter = true;
            else if (Character.isDigit(c)) digit = true;
            if (letter && digit) return true;
        }
        return false;
    }

    public static boolean isPositiveNumber(String s) {
        if (isBlank(s)) return false;
        try {
            return Double.parseDouble(s.trim()) > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isPositiveInt(String s) {
        if (isBlank(s)) return false;
        try {
            return Integer.parseInt(s.trim()) > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
