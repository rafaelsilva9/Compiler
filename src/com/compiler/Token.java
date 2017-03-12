package com.compiler;

/**
 * Created by Rafael on 12/03/2017.
 */
public class Token {
    private TokenType classification;
    private String lexeme;

    public Token(TokenType classification, String lexeme){
        this.classification = classification;
        this.lexeme = lexeme;
    }

    public TokenType getClassification() {
        return classification;
    }

    public String getLexeme() {
        return lexeme;
    }
}
