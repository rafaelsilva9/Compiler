package com.compiler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Rafael on 27/05/2017.
 */
public class SymbolTable {
    private Map<Integer, List<Symbol>> symbolMap;

    public SymbolTable(){
        symbolMap = new HashMap<>();
    }

    public Symbol getSymbol(String symbolName, int stackIndex) {
        List<Symbol> symbolList = symbolMap.get(stackIndex);

        if(symbolList != null) {
            for(Symbol symbol: symbolList) {
                if(symbol.getName().equals(symbolName))
                    return symbol;
            }
        }

        return null;
    }

    public void put(TokenType type, String name, int stackIndex) {
        List<Symbol> symbolList = symbolMap.get(stackIndex);
        
        if(symbolList == null) {
            symbolList = new ArrayList<>();
            symbolMap.put(stackIndex, symbolList);
        }

        if(getSymbol(name, stackIndex) == null) {
            Symbol newSymbol = new Symbol(name, type);
            symbolList.add(newSymbol);
        }
    }

    public void removeFromStack(int stackIndex) {
        symbolMap.remove(stackIndex);
    }
}
