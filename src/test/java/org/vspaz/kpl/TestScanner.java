package org.vspaz.kpl;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestScanner {
    @Test
    public void testIsDigitOk() {
        assertTrue(Scanner.isDigit('8'));
    }

    @Test
    public void testIsDigitFail() {
        assertFalse(Scanner.isDigit('a'));
    }
}
