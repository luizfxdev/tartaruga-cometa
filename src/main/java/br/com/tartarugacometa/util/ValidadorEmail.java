package br.com.tartarugacometa.util;

import java.util.regex.Pattern;

public final class ValidadorEmail {
    private static final Pattern EMAIL_PATTERN =
        Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private ValidadorEmail() {}

    public static boolean valido(String email) {
        if (email == null || email.trim().isEmpty()) return false;
        return EMAIL_PATTERN.matcher(email).matches();
    }
}
