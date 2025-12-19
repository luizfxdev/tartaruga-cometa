package com.tartarugacometasystem.util;

import java.util.regex.Pattern;

public class Validator {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private static final Pattern CEP_PATTERN = Pattern.compile("^\\d{5}-?\\d{3}$");

    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }

    public static boolean isValidPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return false;
        }
        String cleanedPhone = phone.replaceAll("[^\\d]", "");
        if (cleanedPhone.length() < 10 || cleanedPhone.length() > 11) {
            return false;
        }
        if (cleanedPhone.charAt(0) == '0') {
            return false;
        }
        return true;
    }

    public static boolean isValidCPF(String cpf) {
        if (cpf == null) {
            return false;
        }
        cpf = cpf.replaceAll("[^\\d]", "");
        if (cpf.length() != 11) {
            return false;
        }
        if (cpf.matches("(\\d)\\1{10}")) {
            return false;
        }
        char firstDigit = calculateCPFDigit(cpf.substring(0, 9), 10);
        char secondDigit = calculateCPFDigit(cpf.substring(0, 9) + firstDigit, 11);
        return cpf.charAt(9) == firstDigit && cpf.charAt(10) == secondDigit;
    }

    public static boolean isValidCNPJ(String cnpj) {
        if (cnpj == null) {
            return false;
        }
        cnpj = cnpj.replaceAll("[^\\d]", "");
        if (cnpj.length() != 14) {
            return false;
        }
        if (cnpj.matches("(\\d)\\1{13}")) {
            return false;
        }
        int[] pesosDV1 = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        int[] pesosDV2 = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

        int soma = 0;
        for (int i = 0; i < 12; i++) {
            soma += (cnpj.charAt(i) - '0') * pesosDV1[i];
        }
        int resultado = soma % 11;
        int dv1 = (resultado < 2) ? 0 : (11 - resultado);
        if (dv1 != (cnpj.charAt(12) - '0')) {
            return false;
        }

        soma = 0;
        for (int i = 0; i < 13; i++) {
            soma += (cnpj.charAt(i) - '0') * pesosDV2[i];
        }
        resultado = soma % 11;
        int dv2 = (resultado < 2) ? 0 : (11 - resultado);
        return dv2 == (cnpj.charAt(13) - '0');
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
}