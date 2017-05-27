package com.compiler;

/**
 * Created by Rafael on 27/05/2017.
 */
public class SemanticAnalyzer {
    private SymbolTable table;

    public SemanticAnalyzer() {
        table = new SymbolTable();
    }

    public void checkVarDecl(String symbolName, TokenType symbolType, int stackIndex) {
        boolean alreadyExists = ( table.getSymbol(symbolName, stackIndex) != null );
        if(alreadyExists) {
            // Gerar erro semântico pois a varíavel já existe
            System.out.println(" Erro semântico pois a varíavel já existe");
        } else {
            table.put(symbolType, symbolName, stackIndex);
        }
    }
}
