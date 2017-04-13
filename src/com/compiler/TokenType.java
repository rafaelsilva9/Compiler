package com.compiler;

/**
 * Created by Rafael on 12/03/2017.
 */
public enum TokenType {
    INT,
    FLOAT,
    SUM,
    SUB,
    MULT,
    DIV,
    LESS_THAN,
    LESS_OR_EQUAL,
    GREATER_THAN,
    GREATER_OR_EQUAL,
    EQUALITY,
    ASSIGNMENT,
    DIFFERENT,
    //DENIAL,
    OPENS_PARENTHESIS,
    CLOSES_PARENTHESIS,
    OPENS_CURLY_BRACKET,
    CLOSE_CURLY_BRACKET,
    COMMA,
    SEMICOLON,
    CHAR,
    IDENTIFIER,


    RESERVED_MAIN,
    RESERVED_IF,
    RESERVED_ELSE,
    RESERVED_WHILE,
    RESERVED_DO,
    RESERVED_FOR,
    RESERVED_INT,
    RESERVED_FLOAT,
    RESERVED_CHAR,

    END_OF_FILE
}
