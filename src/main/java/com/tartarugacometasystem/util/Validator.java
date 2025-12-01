package com.tartarugacometasystem.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\|$?\\d{2}\$|?\\s?\\d{4,5}-?\\d{4}$");
    private static final Pattern CEP_PATTERN = Pattern.compile("^\\d{5}-?\\d{3}$");

    // Método para validar e-mail
    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }

    // Método para validar telefone
    public static boolean isValidPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return false;
        }
        return PHONE_PATTERN.matcher(phone).matches();
    }

    // Método para validar CPF
    public static boolean isValidCPF(String cpf) {
        if (cpf == null || !cpf.matches("\\d{11}")) {
            return false;
        }

        // Verifica se todos os dígitos são iguais (ex: 111.111.111-11)
        if (cpf.matches("(\\d)\\1{10}")) {
            return false;
        }

        char firstDigit = calculateCPFDigit(cpf.substring(0, 9), 10);
        char secondDigit = calculateCPFDigit(cpf.substring(0, 9) + firstDigit, 11);

        return cpf.charAt(9) == firstDigit && cpf.charAt(10) == secondDigit;
    }

    // Método para validar CNPJ
    public static boolean isValidCNPJ(String cnpj) {
        if (cnpj == null || !cnpj.matches("\\d{14}")) {
            return false;
        }

        // Verifica se todos os dígitos são iguais (ex: 11.111.111/1111-11)
        if (cnpj.matches("(\\d)\\1{13}")) {
            return false;
        }

        char firstDigit = calculateCNPJDigit(cnpj.substring(0, 12), 5);
        char secondDigit = calculateCNPJDigit(cnpj.substring(0, 12) + firstDigit, 6);

        return cnpj.charAt(12) == firstDigit && cnpj.charAt(13) == secondDigit;
    }

    public static boolean isValidZipCode(String zipCode) {
        if (zipCode == null || zipCode.trim().isEmpty()) {
            return false;
        }
        return CEP_PATTERN.matcher(zipCode).matches();
    }

    public static boolean isValidState(String state) {
        if (state == null || state.length() != 2) {
            return false;
        }
        return state.matches("[A-Z]{2}");
    }

    private static char calculateCPFDigit(String sequence, int weight) {
        int sum = 0;
        for (int i = 0; i < sequence.length(); i++) {
            sum += Character.getNumericValue(sequence.charAt(i)) * (weight - i);
        }
        int remainder = sum % 11;
        return remainder < 2 ? '0' : (char) ('0' + (11 - remainder));
    }

    private static char calculateCNPJDigit(String sequence, int weight) {
        int sum = 0;
        for (int i = 0; i < sequence.length(); i++) {
            sum += Character.getNumericValue(sequence.charAt(i)) * (weight - (i % 6));
        }
        int remainder = sum % 11;
        return remainder < 2 ? '0' : (char) ('0' + (11 - remainder));
    }
}
