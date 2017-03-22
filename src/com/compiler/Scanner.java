package com.compiler;

import javax.print.DocFlavor;

/**
 * created by rafael on 12/03/2017.
 */
public class Scanner {

    public Token process(Cursor cursor) {
        char character = cursor.getActualCharacter();
        StringBuffer token = new StringBuffer(String.valueOf(character));

        if (Character.isDigit(character)) {

            token = processDigit(token, cursor);
            character = cursor.getActualCharacter();

            if(character == '.') {
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
            cursor.getNext();
            return new Token(TokenType.SUM, token.toString());
        } else if (character == '-') {
            cursor.getNext();
            return new Token(TokenType.SUB, token.toString());
        } else if (character == '*') {
            cursor.getNext();
            return new Token(TokenType.MULT, token.toString());
        } else if (character == '<') {
            character = cursor.getNext();
            if (character == '=') {
                token.append(character);
                cursor.getNext();
                return new Token(TokenType.LESS_OR_EQUAL, token.toString());
            } else {
                return new Token(TokenType.LESS_THAN, token.toString());
            }
        } else if (character == '>') {
            character = cursor.getNext();
            if (character == '=') {
                token.append(character);
                cursor.getNext();
                return new Token(TokenType.GREATER_OR_EQUAL, token.toString());
            } else {
                return new Token(TokenType.GREATER_THAN, token.toString());
            }
        } else if (character == '=') {
            character = cursor.getNext();
            if (character == '=') {
                token.append(character);
                cursor.getNext();
                return new Token(TokenType.EQUALITY, token.toString());
            } else {
                return new Token(TokenType.ASSIGNMENT, token.toString());
            }
        } else if (character == '!') {
            character = cursor.getNext();
            if (character == '=') {
                token.append(character);
                cursor.getNext();
                return new Token(TokenType.DIFFERENT, token.toString());
            } else {
                return new Token(TokenType.DENIAL, token.toString());
            }
        } else if(character == '(') {
            cursor.getNext();
            return new Token(TokenType.OPENS_PARENTHESIS, token.toString());
        } else if(character == ')') {
            cursor.getNext();
            return new Token(TokenType.CLOSES_PARENTHESIS, token.toString());
        } else if(character == '{') {
            cursor.getNext();
            return new Token(TokenType.OPENS_CURLY_BRACKET, token.toString());
        } else if(character == '}') {
            cursor.getNext();
            return new Token(TokenType.CLOSE_CURLY_BRACKET, token.toString());
        } else if(character == '.') {
            cursor.getNext();
            return new Token(TokenType.DOT, token.toString());
        } else if(character == ',') {
            cursor.getNext();
            return new Token(TokenType.COMMA, token.toString());
        } else if(character == '/') {
            character = cursor.getNext();
            if(character == '/') {
                token.append(character);
                processCommentLine(cursor);
                return null;
            } else if(character == '*'){
                token.append(character);
                processCommentBlock(cursor);
                return null;

            } else {
                return new Token(TokenType.DIV, token.toString());
            }
        } else if(Character.isLowerCase(character)){
            token = processLetters(token, cursor);
            TokenType tokenType = isReservedWord(token.toString());
            if(tokenType == null) {
                return new Token(TokenType.IDETIFIER, token.toString());
            } else {
                return new Token(tokenType, token.toString());
            }
        } else if(character == '\'') {
            processChar(cursor);
            return null;
        }

        else {
            cursor.getNext();
        }

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

        character = cursor.getNext();

        if (Character.isDigit(character)) {
            token.append(character);
            token = processDigit(token, cursor);
        } else {
            //TODO malformation
            return null;
        }

        return token;
    }

    private void processCommentLine(Cursor cursor) {
        char character;

        do {
            character = cursor.getNext();

        } while(character != '\n');
    }

    private void processCommentBlock(Cursor cursor) {
        char character;
        boolean endOfBlock = false;

        do {
            character = cursor.getNext();
            if(character == '*') {
                character = cursor.getNext();
                if(character == '/') {
                    cursor.getNext();
                    endOfBlock = true;
                }
            }
        } while (!endOfBlock && cursor.hasNext());
    }

    private StringBuffer processLetters(StringBuffer token, Cursor cursor) {
        char character;

        do {
            character = cursor.getNext();
            if(Character.isLetterOrDigit(character) || character == '_'){
                token.append(character);
            }
        } while(Character.isLetterOrDigit(character) || character == '_');

        return token;
    }

    private void processChar(Cursor cursor) {
        char character;

        do {
            character = cursor.getNext();
        } while(character != '\'');

        cursor.getNext();
    }

    private TokenType isReservedWord(String token){
        switch (token){
            case "main":
                return TokenType.RESERVED_MAIN;
            case "if":
                return TokenType.RESERVED_IF;
            case "else":
                return TokenType.RESERVED_ELSE;
            case "while":
                return TokenType.RESERVED_WHILE;
            case "do":
                return TokenType.RESERVED_DO;
            case "for":
                return TokenType.RESERVED_FOR;
            case "int":
                return TokenType.RESERVED_INT;
            case "float":
                return TokenType.RESERVED_FLOAT;
            case "char":
                return TokenType.RESERVED_CHAR;
            default:
                return null;
        }

    }
}

