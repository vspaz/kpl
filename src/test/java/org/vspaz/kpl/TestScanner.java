package org.vspaz.kpl;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestScanner {

    @Test
    void testScanTokensOk() {
        var scanner = new Scanner("var total = 10 * 5 / 4;\nvar text = \"foo\" + \"bar\"");
        scanner.scanTokens();
        var tokens = scanner.getTokens();
        assertEquals("VAR var null 1", tokens.get(0).toString());
        assertEquals("EOF  null 2", tokens.get(15).toString());
    }

    @Test
    void testIsDigitOk() {
        assertTrue(Scanner.isDigit('8'));
    }

    @Test
    void testIsDigitFail() {
        assertFalse(Scanner.isDigit('a'));
    }
}
