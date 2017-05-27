package com.compiler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rafael on 27/05/2017.
 */
public class SymbolTable {
    private List<Symbol> symbolList;

    public SymbolTable(){
        symbolList = new ArrayList<>();
    }

    public Symbol getSymbol(String symbolName, int stackIndex) {
        for(Symbol symbol: symbolList) {
            if(symbol.getName().equals(symbolName) && symbol.getStackIndex() == stackIndex)
                return symbol;
        }
        return null;
    }

    public void put(TokenType type, String name, int stackIndex) {
        if(getSymbol(name, stackIndex) == null) {
            Symbol newSymbol = new Symbol(name, type, stackIndex);
            symbolList.add(newSymbol);
        }
    }
}
