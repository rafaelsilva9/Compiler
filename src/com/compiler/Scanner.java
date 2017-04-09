package com.compiler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * created by rafael on 12/03/2017.
 */
public class Scanner {

    private String lastToken;

    public Token process(Cursor cursor) throws IllegalArgumentException {
        boolean noToken = false;

        do {
            char character = cursor.getActualCharacter();
            StringBuffer token = new StringBuffer(String.valueOf(character));

            if (Character.isDigit(character)) {

                token = processDigit(token, cursor);
                character = cursor.getActualCharacter();

                if(character == '.') {
                    token.append(character);
                    token = processPoint(token, cursor);
                    if (token != null) {
                        lastToken = token.toString();
                        return new Token(TokenType.FLOAT, token.toString());
                    }
                } else {
                    lastToken = token.toString();
                    return new Token(TokenType.INT, token.toString());
                }
            } else if (character == '.') {
                token = processPoint(token, cursor);
                if (token != null) {
                    lastToken = token.toString();
                    return new Token(TokenType.FLOAT, token.toString());
                }
            } else if (character == '+') {
                cursor.getNext();
                lastToken = token.toString();
                return new Token(TokenType.SUM, token.toString());
            } else if (character == '-') {
                cursor.getNext();
                lastToken = token.toString();
                return new Token(TokenType.SUB, token.toString());
            } else if (character == '*') {
                cursor.getNext();
                lastToken = token.toString();
                return new Token(TokenType.MULT, token.toString());
            } else if (character == '<') {
                character = cursor.getNext();
                if (character == '=') {
                    token.append(character);
                    cursor.getNext();
                    lastToken = token.toString();
                    return new Token(TokenType.LESS_OR_EQUAL, token.toString());
                } else {
                    lastToken = token.toString();
                    return new Token(TokenType.LESS_THAN, token.toString());
                }
            } else if (character == '>') {
                character = cursor.getNext();
                if (character == '=') {
                    token.append(character);
                    cursor.getNext();
                    lastToken = token.toString();
                    return new Token(TokenType.GREATER_OR_EQUAL, token.toString());
                } else {
                    lastToken = token.toString();
                    return new Token(TokenType.GREATER_THAN, token.toString());
                }
            } else if (character == '=') {
                character = cursor.getNext();
                if (character == '=') {
                    token.append(character);
                    cursor.getNext();
                    lastToken = token.toString();
                    return new Token(TokenType.EQUALITY, token.toString());
                } else {
                    lastToken = token.toString();
                    return new Token(TokenType.ASSIGNMENT, token.toString());
                }
            } else if (character == '!') {
                character = cursor.getNext();
                if (character == '=') {
                    token.append(character);
                    cursor.getNext();
                    lastToken = token.toString();
                    return new Token(TokenType.DIFFERENT, token.toString());
                } else {
                    new ScannerException(ErrorType.WRONG_EXCLAMATION_USE, lastToken,
                            cursor.getLine(), cursor.getColumn() - token.length());
                }
            } else if(character == '(') {
                cursor.getNext();
                lastToken = token.toString();
                return new Token(TokenType.OPENS_PARENTHESIS, token.toString());
            } else if(character == ')') {
                cursor.getNext();
                lastToken = token.toString();
                return new Token(TokenType.CLOSES_PARENTHESIS, token.toString());
            } else if(character == '{') {
                cursor.getNext();
                lastToken = token.toString();
                return new Token(TokenType.OPENS_CURLY_BRACKET, token.toString());
            } else if(character == '}') {
                cursor.getNext();
                lastToken = token.toString();
                return new Token(TokenType.CLOSE_CURLY_BRACKET, token.toString());
            } else if(character == ',') {
                cursor.getNext();
                lastToken = token.toString();
                return new Token(TokenType.COMMA, token.toString());
            } else if(character == ';') {
                cursor.getNext();
                lastToken = token.toString();
                return new Token(TokenType.SEMICOLON, token.toString());
            } else if(character == '/') {
                character = cursor.getNext();
                if(character == '/') {
                    token.append(character);
                    processCommentLine(cursor);
                    if(!cursor.hasNext()) {
                        cursor.setEof(true);
                        noToken = false;
                    } else {
                        cursor.getNext();
                        noToken = true;
                    }
                } else if(character == '*') {
                    token.append(character);
                    processCommentBlock(cursor);
                    noToken = true;

                } else {
                    lastToken = token.toString();
                    return new Token(TokenType.DIV, token.toString());
                }
            } else if(Character.isLetter(character) || character == '_') {
                Pattern regex = Pattern.compile("[A-Za-z_]");
                Matcher matcher = regex.matcher(Character.toString(character));
                if (matcher.find()) {
                    token = processLetters(token, cursor);
                    TokenType tokenType = isReservedWord(token.toString());
                    if(tokenType == null) {
                        lastToken = token.toString();
                        return new Token(TokenType.IDENTIFIER, token.toString());
                    } else {
                        lastToken = token.toString();
                        return new Token(tokenType, token.toString());
                    }
                }
            } else if(character == '\'') {
                token = processChar(token, cursor);
                if(token != null) {
                    lastToken = token.toString();
                    return new Token(TokenType.CHAR, token.toString());
                }
            } else {
            /*[$&+,:;=?@#|]*/
                Pattern regex = Pattern.compile("['\"'$%¨&:?@#|]");
                Matcher matcher = regex.matcher(Character.toString(character));
                if (matcher.find()) {
                    new ScannerException(ErrorType.INVALID_CHARACTER, lastToken,
                            cursor.getLine(), cursor.getColumn() - token.length());
                }

                if(!cursor.hasNext()) {
                    cursor.setEof(true);
                    noToken = false;
                } else {
                    cursor.getNext();
                    noToken = true;
                }
            }

        } while(noToken);

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
            new ScannerException(ErrorType.FLOAT_MALFORMATION, lastToken,
                    cursor.getLine(), cursor.getColumn() - token.length());
        }

        return token;
    }

    private void processCommentLine(Cursor cursor) {
        char character;

        do {
            character = cursor.getNext();

        } while(character != '\n' && cursor.hasNext());
    }

    private void processCommentBlock(Cursor cursor) {
        char character;
        boolean endOfBlock = false;
        int initialColumn = cursor.getColumn() - 1;
        int initialLine = cursor.getLine();
        boolean lastIsAsterisk = false;

        do {
            character = cursor.getNext();
            if(character == '*') {
                lastIsAsterisk = true;
            } else if(character == '/' && lastIsAsterisk) {
                cursor.getNext();
                endOfBlock = true;
            } else {
                lastIsAsterisk = false;
            }
        } while (!endOfBlock && cursor.hasNext());

        if(!endOfBlock) {
            cursor.getNext();
            new ScannerException(ErrorType.WRONG_END_COMMENT, lastToken,
                    initialLine, initialColumn);
        }
    }

    private StringBuffer processLetters(StringBuffer token, Cursor cursor) {
        char character;
        Pattern regex = Pattern.compile("[A-Za-z0-9_]");
        Matcher matcher;
        boolean isValid = false;

        do {

            character = cursor.getNext();
            matcher = regex.matcher(Character.toString(character));

            if (matcher.find()) {
                token.append(character);
                isValid = true;
            } else {
                isValid = false;
            }

        } while (isValid);

        return token;
    }

    private StringBuffer processChar(StringBuffer token, Cursor cursor) {
        char character;
        boolean isMalformation = false;
        Pattern regex = Pattern.compile("[A-Za-z0-9]");
        Matcher matcher;

        character = cursor.getNext();
        matcher = regex.matcher(Character.toString(character));
        token.append(character);

        if(!matcher.find()){
            isMalformation = true;
        }

        character = cursor.getNext();
        token.append(character);

        if(character != '\'') {
            isMalformation = true;
        }

        if(isMalformation) {
            new ScannerException(ErrorType.CHAR_MALFORMATION, lastToken,
                    cursor.getLine(), cursor.getColumn() - token.length());
        } else{
            cursor.getNext();
        }


        return token;
    }

    private TokenType isReservedWord(String token) {
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

    public String getLastToken() {
        return lastToken;
    }
}

