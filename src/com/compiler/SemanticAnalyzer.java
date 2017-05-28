package com.compiler;

/**
 * Created by Rafael on 27/05/2017.
 */
public class SemanticAnalyzer {
    private SymbolTable table;

    public SemanticAnalyzer() {
        table = new SymbolTable();
    }

    // Checks whether the variable exists
    private boolean checkVariable(String symbolName, int stackIndex) {
        return ( table.getSymbol(symbolName, stackIndex) != null );
    }

    // The parameter 'symbolType' is the value type to be assigned
    public void checkAssignment(String symbolName, TokenType symbolType, int stackIndex) {
        Symbol variable = table.getSymbol(symbolName, stackIndex);

        if(variable == null) {
            // TODO Existe uma atribuição a uma variável que não foi declarada
            System.out.println("Existe uma atribuição a uma variável que não foi declarada");
        }

        if(variable.getType() == TokenType.RESERVED_CHAR && symbolType != TokenType.CHAR) {
            // TODO Erro semântico pois existe uma atribuição de um valor diferente de um char a uma variável do tipo char
            System.out.println("Erro semântico pois existe uma atribuição de um valor diferente de um char a uma variável do tipo char");
        }

        else if(variable.getType() == TokenType.RESERVED_INT && symbolType != TokenType.INT) {
            // TODO Erro semântico pois existe uma atribuição de um valor diferente de um int a uma variável do tipo int
            System.out.println("Erro semântico pois existe uma atribuição de um valor diferente de um int a uma variável do tipo int");
        }

        else if(variable.getType() == TokenType.RESERVED_FLOAT && symbolType == TokenType.CHAR) {
            // TODO Erro semântico pois não se pode atribuir uma char a um float
            System.out.println("Erro semântico pois não se pode atribuir uma char a um float");
        }
    }

    public void checkVarDecl(String symbolName, TokenType symbolType, int stackIndex) {
        boolean alreadyExists = checkVariable(symbolName, stackIndex);
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

    public void removeFromStack(int stackIndex) {
        table.removeFromStack(stackIndex);
    }
}
