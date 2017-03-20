package com.compiler;

/**
 * created by rafael on 12/03/2017.
 */
public class Scanner {

    public Token process(Cursor cursor) {
        char character = cursor.getNext();
        StringBuffer token = new StringBuffer(String.valueOf(character));

        if (Character.isDigit(character)) {

            token = processDigit(token, cursor);
            character = cursor.getActualCharacter();

            if(character == '.'){
                token.append(character);
                token = processPoint(token, cursor);
                if (token != null) {
                    return new Token(TokenType.FLOAT, token.toString());
                } else {
                    //TODO float malformation
                }
            } else {
                return new Token(TokenType.INT, token.toString());
            }

        } else if (character == '.') {
            token = processPoint(token, cursor);
            if (token != null) {
                return new Token(TokenType.FLOAT, token.toString());
            } else {
                //TODO malformation
            }
        } else if (character == '+') {
            return new Token(TokenType.SUM, token.toString());
        } else if (character == '-') {
            return new Token(TokenType.SUB, token.toString());
        } else if (character == '*') {
            return new Token(TokenType.MULT, token.toString());
        } //else if (character == '<') {
//            if (!cursor.eof()) {
//                character = line.charAt(cursor.getColumn());
//                if (character == '=') {
//                    token.append(character);
//                    return new Token(TokenType.LESS_OR_EQUAL, token.toString());
//                } else {
//                    return new Token(TokenType.LESS_THAN, token.toString());
//                }
//            } else {
//                return new Token(TokenType.LESS_THAN, token.toString());
//            }
//        } else if (character == '>') {
//            if (!cursor.eof()) {
//                character = line.charAt(cursor.getColumn());
//                if (character == '=') {
//                    token.append(character);
//                    return new Token(TokenType.GREATER_OR_EQUAL, token.toString());
//                } else {
//                    return new Token(TokenType.GREATER_THAN, token.toString());
//                }
//            } else {
//                return new Token(TokenType.GREATER_THAN, token.toString());
//            }
//        } else if (character == '=') {
//            if (!cursor.eof()) {
//                character = line.charAt(cursor.getColumn());
//                if (character == '=') {
//                    token.append(character);
//                    return new Token(TokenType.ASSIGNMENT, token.toString());
//                } else {
//                    return new Token(TokenType.EQUALITY, token.toString());
//                }
//            } else {
//                return new Token(TokenType.EQUALITY, token.toString());
//            }
//        } else if (character == '!') {
//            if (!cursor.eof()) {
//                character = line.charAt(cursor.getColumn());
//                if (character == '=') {
//                    token.append(character);
//                    return new Token(TokenType.DENIAL, token.toString());
//                } else {
//                    return new Token(TokenType.DIFFERENT, token.toString());
//                }
//            } else {
//                return new Token(TokenType.DENIAL, token.toString());
//            }
//        }

        return null;
    }

    private StringBuffer processDigit(StringBuffer token, Cursor cursor) {
        char character;

        do {
            character = cursor.getNext();

            if (Character.isDigit(character)) {
                token.append(character);
            }

        } while (Character.isDigit(character));

        return token;
    }

    private StringBuffer processPoint(StringBuffer token, Cursor cursor) {
        char character;

        if (!cursor.eof()) {
            character = cursor.getNext();

            if (Character.isDigit(character)) {
                token.append(character);
                token = processDigit(token, cursor);
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

