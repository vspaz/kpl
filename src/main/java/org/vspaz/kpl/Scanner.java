package org.vspaz.kpl;

import java.util.*;

import static org.vspaz.kpl.TokenType.*;

public class Scanner {
    private final String sourceCode;

    public List<Token> getTokens() {
        return tokens;
    }

    private final List<Token> tokens = new ArrayList<>();

    private int start = 0;
    private int current = 0;
    private int lineNo = 1;
    
    private static final Map<String, TokenType>  keywords = Map.ofEntries(
                    new AbstractMap.SimpleEntry<>("and", AND),
                    new AbstractMap.SimpleEntry<>("class", CLASS),
                    new AbstractMap.SimpleEntry<>("else", ELSE),
                    new AbstractMap.SimpleEntry<>("false", FALSE),
                    new AbstractMap.SimpleEntry<>("for", FOR),
                    new AbstractMap.SimpleEntry<>("fun", FUN),
                    new AbstractMap.SimpleEntry<>("if", IF),
                    new AbstractMap.SimpleEntry<>("nil", NIL),
                    new AbstractMap.SimpleEntry<>("or", OR),
                    new AbstractMap.SimpleEntry<>("print", PRINT),
                    new AbstractMap.SimpleEntry<>("return", RETURN),
                    new AbstractMap.SimpleEntry<>("super", SUPER),
                    new AbstractMap.SimpleEntry<>("this", THIS),
                    new AbstractMap.SimpleEntry<>("true", TRUE),
                    new AbstractMap.SimpleEntry<>("var", VAR),
                    new AbstractMap.SimpleEntry<>("while", WHILE)
    );

    public Scanner(String source) {
        this.sourceCode = source;
    }

    private boolean isAtEnd() {
        return current >= sourceCode.length();
    }

    List<Token> scanTokens() {
        while (!isAtEnd()) {
            start = current;
            scanToken();
        }
        tokens.add(new Token(EOF, "", null, lineNo));
        return tokens;
    }

    private void addToken(TokenType type) {
        addToken(type, null);
    }

    private void addToken(TokenType type, Object literal) {
        String text = sourceCode.substring(start, current);
        tokens.add(new Token(type, text, literal, lineNo));
    }

    private char advance() {
        return sourceCode.charAt((current++));
    }

    private boolean match(char expected) {
        if (isAtEnd()) return false;
        if (sourceCode.charAt(current) != expected) return false;
        current++;
        return true;
    }

    private char peek() {
        if (isAtEnd()) return '\0';
        return sourceCode.charAt(current);
    }

    private void string() {
        while (peek() != '"' && !isAtEnd()) {
            if (peek() == '\n') lineNo++;
            advance();
        }
        if (isAtEnd()) {
            Kpl.error(lineNo, "unterminated string", "");
        }

        advance();

        String value = sourceCode.substring(start + 1, current - 1);
        addToken(STRING, value);
    }

    static boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private char peekNext() {
        if (current + 1 >= sourceCode.length()) return '\0';
        return sourceCode.charAt(current + 1);
    }

    private void number() {
        while (isDigit(peek())) advance();
        if (peek() == '.' && isDigit(peekNext())) {
            advance();
            while (isDigit(peek())) advance();
        }
        addToken(NUMBER, Double.parseDouble(sourceCode.substring(start, current)));
    }

    private boolean isAlphaNumeric(char c) {
        return isAlpha(c) || isDigit(c);
    }

    private void identifier() {
        while (isAlphaNumeric(peek())) advance();

        String text = sourceCode.substring(start, current);
        TokenType type = keywords.get(text);
        if (Objects.isNull(type)) {
            type = IDENTIFIER;
        }
        addToken(type);
    }

    private boolean isAlpha(char c) {
        return (c >= 'a' && c <= 'z') ||
                (c >= 'A' && c <= 'Z') ||
                c == '_';
    }



    private void scanToken() {
        char c = advance();
        switch (c) {
            case  '(':
                addToken(LEFT_PAREN);
                break;
            case  ')':
                addToken(RIGHT_PAREN);
                break;
            case  '{':
                addToken(LEFT_BRACE);
                break;
            case  '}':
                addToken(RIGHT_BRACE);
                break;
            case  ',':
                addToken(COMMA);
                break;
            case  '.':
                addToken(DOT);
                break;
            case  '-':
                addToken(MINUS);
                break;
            case  '+':
                addToken(PLUS);
                break;
            case  ';':
                addToken(SEMICOLON);
                break;
            case  '*':
                addToken(STAR);
                break;
            case '!':
                addToken(match('=') ? BANG_EQUAL: BANG);
                break;
            case '=':
                addToken(match('=') ? EQUAL_EQUAL: EQUAL);
                break;
            case '<':
                addToken(match('<') ? LESS_EQUAL: LESS);
                break;
            case '>':
                addToken(match('=') ? GREATER_EQUAL: GREATER);
            case '/':
                if (match('/')) {
                    while (peek() != '\n' && !isAtEnd()) advance();
                } else {
                    addToken(SLASH);
                }
                break;
            case 'o':
                if (match('r')) {
                    addToken(OR);
                }
                break;
            case ' ':
            case '\r':
            case '\t':
                break;
            case '\n':
                lineNo++;
                break;
            case '"':
                string();
                break;
            default:
                if (isDigit(c)) {
                    number();
                } else if (isAlpha(c)) {
                    identifier();
                } else {
                    Kpl.error(lineNo, "unexpected character.", "");
                }
        }
    }
}
