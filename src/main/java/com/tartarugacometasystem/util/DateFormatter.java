package com.tartarugacometasystem.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateFormatter {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    /**
     * Formata um LocalDateTime para uma String no padrão "dd/MM/yyyy HH:mm:ss".
     *
     * @param dateTime O LocalDateTime a ser formatado.
     * @return A String formatada, ou null se o LocalDateTime for null.
     */
    public static String formatLocalDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.format(FORMATTER);
    }

    /**
     * Converte uma String no padrão "dd/MM/yyyy HH:mm:ss" para um LocalDateTime.
     *
     * @param dateTimeString A String a ser convertida.
     * @return O LocalDateTime resultante, ou null se a String for null ou vazia.
     * @throws java.time.format.DateTimeParseException Se a String não estiver no formato esperado.
     */
    public static LocalDateTime parseLocalDateTime(String dateTimeString) {
        if (dateTimeString == null || dateTimeString.trim().isEmpty()) {
            return null;
        }
        return LocalDateTime.parse(dateTimeString, FORMATTER);
    }
}
