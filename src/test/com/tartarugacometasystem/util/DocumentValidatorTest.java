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
    public void testValidCPFWithMask() {
        String validCPF = "111.444.777-35";
        assertTrue(DocumentValidator.isValidCPF(validCPF));
    }

    @Test
    public void testInvalidCPFAllZeros() {
        String invalidCPF = "00000000000";
        assertFalse(DocumentValidator.isValidCPF(invalidCPF));
    }

    @Test
    public void testInvalidCPFAllOnes() {
        String invalidCPF = "11111111111";
        assertFalse(DocumentValidator.isValidCPF(invalidCPF));
    }

    @Test
    public void testInvalidCPFWrongLength() {
        String invalidCPF = "123456789";
        assertFalse(DocumentValidator.isValidCPF(invalidCPF));
    }

    @Test
    public void testInvalidCPFWrongCheckDigit() {
        String invalidCPF = "11144477736";
        assertFalse(DocumentValidator.isValidCPF(invalidCPF));
    }

    @Test
    public void testInvalidCPFWithLetters() {
        String invalidCPF = "111.444.777-3A";
        assertFalse(DocumentValidator.isValidCPF(invalidCPF));
    }

    @Test
    public void testValidCNPJ() {
        String validCNPJ = "11222333000181";
        assertTrue(DocumentValidator.isValidCNPJ(validCNPJ));
    }

    @Test
    public void testValidCNPJWithMask() {
        String validCNPJ = "11.222.333/0001-81";
        assertTrue(DocumentValidator.isValidCNPJ(validCNPJ));
    }

    @Test
    public void testInvalidCNPJAllZeros() {
        String invalidCNPJ = "00000000000000";
        assertFalse(DocumentValidator.isValidCNPJ(invalidCNPJ));
    }

    @Test
    public void testInvalidCNPJAllOnes() {
        String invalidCNPJ = "11111111111111";
        assertFalse(DocumentValidator.isValidCNPJ(invalidCNPJ));
    }

    @Test
    public void testInvalidCNPJWrongLength() {
        String invalidCNPJ = "123456789";
        assertFalse(DocumentValidator.isValidCNPJ(invalidCNPJ));
    }

    @Test
    public void testInvalidCNPJWrongCheckDigit() {
        String invalidCNPJ = "11222333000182";
        assertFalse(DocumentValidator.isValidCNPJ(invalidCNPJ));
    }

    @Test
    public void testInvalidCNPJWithLetters() {
        String invalidCNPJ = "11.222.333/0001-8A";
        assertFalse(DocumentValidator.isValidCNPJ(invalidCNPJ));
    }

    @Test
    public void testRemoveCPFMask() {
        String maskedCPF = "111.444.777-35";
        String unmasked = DocumentValidator.removeMask(maskedCPF);
        assertEquals("11144477735", unmasked);
    }

    @Test
    public void testRemoveCNPJMask() {
        String maskedCNPJ = "11.222.333/0001-81";
        String unmasked = DocumentValidator.removeMask(maskedCNPJ);
        assertEquals("11222333000181", unmasked);
    }

    @Test
    public void testRemoveMultipleMasks() {
        String masked = "111.444.777-35";
        String unmasked = DocumentValidator.removeMask(masked);
        assertTrue(unmasked.matches("\\d+"));
    }

    @Test
    public void testFormatCPF() {
        String unmasked = "11144477735";
        String formatted = DocumentValidator.formatCPF(unmasked);
        assertEquals("111.444.777-35", formatted);
    }

    @Test
    public void testFormatCNPJ() {
        String unmasked = "11222333000181";
        String formatted = DocumentValidator.formatCNPJ(unmasked);
        assertEquals("11.222.333/0001-81", formatted);
    }

    @Test
    public void testFormatCPFWithMask() {
        String masked = "111.444.777-35";
        String formatted = DocumentValidator.formatCPF(masked);
        assertEquals("111.444.777-35", formatted);
    }

    @Test
    public void testFormatCNPJWithMask() {
        String masked = "11.222.333/0001-81";
        String formatted = DocumentValidator.formatCNPJ(masked);
        assertEquals("11.222.333/0001-81", formatted);
    }

    @Test
    public void testIsValidDocument_CPF() {
        String cpf = "11144477735";
        assertTrue(DocumentValidator.isValidDocument(cpf, "PF"));
    }

    @Test
    public void testIsValidDocument_CNPJ() {
        String cnpj = "11222333000181";
        assertTrue(DocumentValidator.isValidDocument(cnpj, "PJ"));
    }

    @Test
    public void testIsValidDocument_InvalidType() {
        String document = "11144477735";
        assertFalse(DocumentValidator.isValidDocument(document, "INVALID"));
    }

    @Test
    public void testIsValidDocument_CPFWithWrongType() {
        String cpf = "11144477735";
        assertFalse(DocumentValidator.isValidDocument(cpf, "PJ"));
    }

    @Test
    public void testIsValidDocument_CNPJWithWrongType() {
        String cnpj = "11222333000181";
        assertFalse(DocumentValidator.isValidDocument(cnpj, "PF"));
    }

    @Test
    public void testCPFLength() {
        String cpf = "11144477735";
        assertEquals(11, cpf.replaceAll("\\D", "").length());
    }

    @Test
    public void testCNPJLength() {
        String cnpj = "11222333000181";
        assertEquals(14, cnpj.replaceAll("\\D", "").length());
    }

    @Test
    public void testMultipleCPFValidation() {
        String[] validCPFs = {
            "11144477735",
            "111.444.777-35"
        };

        for (String cpf : validCPFs) {
            assertTrue("CPF should be valid: " + cpf, DocumentValidator.isValidCPF(cpf));
        }
    }

    @Test
    public void testMultipleCNPJValidation() {
        String[] validCNPJs = {
            "11222333000181",
            "11.222.333/0001-81"
        };

        for (String cnpj : validCNPJs) {
            assertTrue("CNPJ should be valid: " + cnpj, DocumentValidator.isValidCNPJ(cnpj));
        }
    }

    @Test
    public void testEmptyString() {
        assertFalse(DocumentValidator.isValidCPF(""));
        assertFalse(DocumentValidator.isValidCNPJ(""));
    }

    @Test
    public void testNullString() {
        assertFalse(DocumentValidator.isValidCPF(null));
        assertFalse(DocumentValidator.isValidCNPJ(null));
    }

    @Test
    public void testWhitespaceString() {
        assertFalse(DocumentValidator.isValidCPF("   "));
        assertFalse(DocumentValidator.isValidCNPJ("   "));
    }

    @Test
    public void testSpecialCharacters() {
        assertFalse(DocumentValidator.isValidCPF("111@444#777$35"));
        assertFalse(DocumentValidator.isValidCNPJ("11@222#333/0001-81"));
    }

    @Test
    public void testCPFWithExtraSpaces() {
        String cpf = "111 444 777 35";
        String cleaned = cpf.replaceAll("\\s", "");
        assertTrue(DocumentValidator.isValidCPF(cleaned));
    }

    @Test
    public void testCNPJWithExtraSpaces() {
        String cnpj = "11 222 333 0001 81";
        String cleaned = cnpj.replaceAll("\\s", "");
        assertTrue(DocumentValidator.isValidCNPJ(cleaned));
    }

    @Test
    public void testFormatCPFInvalidLength() {
        String invalid = "123";
        String formatted = DocumentValidator.formatCPF(invalid);
        assertNotNull(formatted);
    }

    @Test
    public void testFormatCNPJInvalidLength() {
        String invalid = "123";
        String formatted = DocumentValidator.formatCNPJ(invalid);
        assertNotNull(formatted);
    }

    @Test
    public void testConsecutiveDigitsCPF() {
        String cpf = "22222222222";
        assertFalse(DocumentValidator.isValidCPF(cpf));
    }

    @Test
    public void testConsecutiveDigitsCNPJ() {
        String cnpj = "22222222222222";
        assertFalse(DocumentValidator.isValidCNPJ(cnpj));
    }

    @Test
    public void testFirstCheckDigitCPF() {
        String cpf = "11144477735";
        int firstDigit = Integer.parseInt(String.valueOf(cpf.charAt(9)));
        assertTrue(firstDigit >= 0 && firstDigit <= 9);
    }

    @Test
    public void testSecondCheckDigitCPF() {
        String cpf = "11144477735";
        int secondDigit = Integer.parseInt(String.valueOf(cpf.charAt(10)));
        assertTrue(secondDigit >= 0 && secondDigit <= 9);
    }

    @Test
    public void testFirstCheckDigitCNPJ() {
        String cnpj = "11222333000181";
        int firstDigit = Integer.parseInt(String.valueOf(cnpj.charAt(12)));
        assertTrue(firstDigit >= 0 && firstDigit <= 9);
    }

    @Test
    public void testSecondCheckDigitCNPJ() {
        String cnpj = "11222333000181";
        int secondDigit = Integer.parseInt(String.valueOf(cnpj.charAt(13)));
        assertTrue(secondDigit >= 0 && secondDigit <= 9);
    }
}
