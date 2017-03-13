package com.compiler;

/**
 * created by rafael on 12/03/2017.
 */
public class Scanner {

    public Token process(String line, Cursor cursor) {
        char character = line.charAt(cursor.getColumn());
        StringBuffer token = new StringBuffer(String.valueOf(character));

        if (Character.isDigit(character)) {

            token = processDigit(token, line, cursor);

            if (!cursor.eof()) {
                character = line.charAt(cursor.getColumn());
            } else {
                return new Token(TokenType.INT, token.toString());
            }
            if (character == '.') {
                token.append(character);
                token = processPoint(token, line, cursor);
                if (token != null) {
                    return new Token(TokenType.FLOAT, token.toString());
                } else {
                    //TODO malformation
                }
            } else {
                return new Token(TokenType.INT, token.toString());
            }
        } else if (character == '.') {
            token = processPoint(token, line, cursor);
            if (token != null) {
                return new Token(TokenType.FLOAT, token.toString());
            } else {
                //TODO malformation
            }
        } else if (character == '+') {
            cursor.move();
            return new Token(TokenType.SUM, token.toString());
        } else if (character == '-') {
            cursor.move();
            return new Token(TokenType.SUB, token.toString());
        } else if (character == '*') {
            cursor.move();
            return new Token(TokenType.MULT, token.toString());
        } else if (character == '<') {
            cursor.move();
            if (!cursor.eof()) {
                character = line.charAt(cursor.getColumn());
                if (character == '=') {
                    cursor.move();
                    token.append(character);
                    return new Token(TokenType.LESS_OR_EQUAL, token.toString());
                } else {
                    return new Token(TokenType.LESS_THAN, token.toString());
                }
            } else {
                return new Token(TokenType.LESS_THAN, token.toString());
            }
        } else if (character == '>') {
            cursor.move();
            if (!cursor.eof()) {
                character = line.charAt(cursor.getColumn());
                if (character == '=') {
                    cursor.move();
                    token.append(character);
                    return new Token(TokenType.GREATER_OR_EQUAL, token.toString());
                } else {
                    return new Token(TokenType.GREATER_THAN, token.toString());
                }
            } else {
                return new Token(TokenType.GREATER_THAN, token.toString());
            }
        } else if (character == '=') {
            cursor.move();
            if (!cursor.eof()) {
                character = line.charAt(cursor.getColumn());
                if (character == '=') {
                    cursor.move();
                    token.append(character);
                    return new Token(TokenType.ASSIGNMENT, token.toString());
                } else {
                    return new Token(TokenType.EQUALITY, token.toString());
                }
            } else {
                return new Token(TokenType.EQUALITY, token.toString());
            }
        } else if (character == '!') {
            cursor.move();
            if (!cursor.eof()) {
                character = line.charAt(cursor.getColumn());
                if (character == '=') {
                    cursor.move();
                    token.append(character);
                    return new Token(TokenType.DENIAL, token.toString());
                } else {
                    return new Token(TokenType.DIFFERENT, token.toString());
                }
            } else {
                return new Token(TokenType.DENIAL, token.toString());
            }
        }

        return null;
    }

    private StringBuffer processDigit(StringBuffer token, String line, Cursor cursor) {
        char character = line.charAt(cursor.getColumn());

        do {
            cursor.move();

            if (!cursor.eof()) {
                character = line.charAt(cursor.getColumn());

                if (Character.isDigit(character)) {
                    token.append(String.valueOf(character));
                }
            }
        } while (Character.isDigit(character) && !cursor.eof());

        return token;
    }

    private StringBuffer processPoint(StringBuffer token, String line, Cursor cursor) {
        char character = line.charAt(cursor.getColumn());

        cursor.move();

        if (!cursor.eof()) {
            character = line.charAt(cursor.getColumn());

            if (Character.isDigit(character)) {
                token.append(character);
                token = processDigit(token, line, cursor);
            } else {
                //TODO malformation
                return null;
            }
        } else {
            //TODO malformation
            return null;
        }

        return token;
    }
}

