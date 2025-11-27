package com.tartarugacometasystem.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class DocumentValidatorTest {

    @Test
    public void testValidCPF() {
        String validCPF = "11144477735";
        assertTrue(DocumentValidator.isValidCPF(validCPF));
    }

    @Test
    public void testInvalidCPF() {
        String invalidCPF = "12345678901";
        assertFalse(DocumentValidator.isValidCPF(invalidCPF));
    }

    @Test
    public void testCPFTooShort() {
        String shortCPF = "123456789";
        assertFalse(DocumentValidator.isValidCPF(shortCPF));
    }

    @Test
    public void testCPFTooLong() {
        String longCPF = "123456789012";
        assertFalse(DocumentValidator.isValidCPF(longCPF));
    }

    @Test
    public void testValidCNPJ() {
        String validCNPJ = "11222333000181";
        assertTrue(DocumentValidator.isValidCNPJ(validCNPJ));
    }

    @Test
    public void testInvalidCNPJ() {
        String invalidCNPJ = "12345678901234";
        assertFalse(DocumentValidator.isValidCNPJ(invalidCNPJ));
    }

    @Test
    public void testCNPJTooShort() {
        String shortCNPJ = "123456789012";
        assertFalse(DocumentValidator.isValidCNPJ(shortCNPJ));
    }

    @Test
    public void testCNPJTooLong() {
        String longCNPJ = "123456789012345";
        assertFalse(DocumentValidator.isValidCNPJ(longCNPJ));
    }

    @Test
    public void testEmptyDocument() {
        assertFalse(DocumentValidator.isValidCPF(""));
        assertFalse(DocumentValidator.isValidCNPJ(""));
    }

    @Test
    public void testNullDocument() {
        assertFalse(DocumentValidator.isValidCPF(null));
        assertFalse(DocumentValidator.isValidCNPJ(null));
    }
}
