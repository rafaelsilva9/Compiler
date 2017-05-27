package com.compiler;

/**
 * Created by Rafael on 27/05/2017.
 */
public class Symbol {

    private String name;
    private TokenType type;
    private int stackIndex;

    public Symbol(String name, TokenType type, int stackIndex) {

        this.name = name;
        this.type = type;
        this.stackIndex = stackIndex;
    }

    public String getName() {
        return name;
    }

    public TokenType getType() {
        return type;
    }

    public int getStackIndex() {
        return stackIndex;
    }
}
