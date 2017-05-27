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
            // TODO Gerar erro semântico pois a varíavel já existe
            System.out.println(" Erro semântico pois a varíavel já existe");
        } else {
            table.put(symbolType, symbolName, stackIndex);
        }
    }

    public TokenType checkTerm(TokenType factorTypeA, TokenType factorTypeB, TokenType operationType) {
        if(factorTypeA == TokenType.INT && factorTypeB == TokenType.INT ) {
            if(operationType == TokenType.MULT)
                return TokenType.INT;
            else
                return TokenType.FLOAT;
        }

        if(factorTypeA == TokenType.CHAR || factorTypeB == TokenType.CHAR) {
            // If factor A or factor B is not a Char
            if(factorTypeA != factorTypeB) {
                // TODO Gerar erro semântico pois existe uma operação de uma variável do tipo char com uma variável do tipo diferente
                System.out.println("Erro semântico pois existe uma operação de uma variável do tipo char com uma variável do tipo diferente");
            }
        }

        // It Returns float type because we are sure that the operation is of a float with an int
        return TokenType.FLOAT;
    }
}
