package com.compiler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * created by rafael on 12/03/2017.
 */
public class Scanner {

    public Token process(Cursor cursor) {
        char character = cursor.getActualCharacter();
        StringBuffer token = new StringBuffer(String.valueOf(character));

        //TODO Scanner sempre deve processar o arquivo até retornar um token
        if (Character.isDigit(character)) {

            token = processDigit(token, cursor);
            character = cursor.getActualCharacter();

            if(character == '.') {
                token.append(character);
                token = processPoint(token, cursor);
                if (token != null) {
                    return new Token(TokenType.FLOAT, token.toString());
                } else {
                    //TODO Float malformation
                }
            } else {
                return new Token(TokenType.INT, token.toString());
            }
        } else if (character == '.') {
            token = processPoint(token, cursor);
            if (token != null) {
                return new Token(TokenType.FLOAT, token.toString());
            } else {
                //TODO Float malformation
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
            //TODO Não aceitar '!'
        } else if (character == '!') {
            character = cursor.getNext();
            if (character == '=') {
                token.append(character);
                cursor.getNext();
                return new Token(TokenType.DIFFERENT, token.toString());
            } else {
//                return new Token(TokenType.DENIAL, token.toString());
                //TODO Denial malformation
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
        } else if(character == ';') {
            cursor.getNext();
            return new Token(TokenType.SEMICOLON, token.toString());
        } else if(character == '/') {
            character = cursor.getNext();
            if(character == '/') {
                token.append(character);
                processCommentLine(cursor);
                return null;
            } else if(character == '*') {
                token.append(character);
                processCommentBlock(cursor);
                return null;

            } else {
                return new Token(TokenType.DIV, token.toString());
            }
        } else if(Character.isLetter(character) || character == '_') {
            Pattern regex = Pattern.compile("[A-Za-z_]");
            Matcher matcher = regex.matcher(Character.toString(character));
            if (matcher.find()) {
                token = processLetters(token, cursor);
                TokenType tokenType = isReservedWord(token.toString());
                if(tokenType == null) {
                    return new Token(TokenType.IDETIFIER, token.toString());
                } else {
                    return new Token(tokenType, token.toString());
                }
            }
        } else if(character == '\'') {
            token = processChar(token, cursor);
            if(token != null) {
                return new Token(TokenType.CHAR, token.toString());
            } else {
                //TODO Char malformation
            }
        } else {
//            [$&+,:;=?@#|]
            Pattern regex = Pattern.compile("['\"'$%¨&:?@#|]");
            Matcher matcher = regex.matcher(Character.toString(character));
            if (matcher.find()) {
                System.out.println("Erro: " + character);
                //TODO character malformation
            }

            if(!cursor.hasNext()) {
                cursor.setLastCharacterProcessed(true);
            } else {
                cursor.getNext();
            }
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
            //TODO Float malformation
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

        if(!endOfBlock) {
            cursor.getNext();
            //TODO comments malformation
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
            return null;
        }

        character = cursor.getNext();
        token.append(character);

        if(character != '\'') {
            isMalformation = true;
            return null;
        }

        if(isMalformation) {
            //TODO char malformation
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
}

