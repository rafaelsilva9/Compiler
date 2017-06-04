package com.compiler;

/**
 * Created by Rafael on 27/05/2017.
 */
public class Symbol {

    private String name;
    private TokenType type;
    private Token operation;

    public Symbol(String name, TokenType type) {

        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TokenType getType() {
        return type;
    }

    public Token getOperation() {
        return operation;
    }

    public void setOperation(Token operation) {
        this.operation = operation;
    }
}
