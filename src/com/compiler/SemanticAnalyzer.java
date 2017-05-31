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

    public Symbol findSymbol(String symbolName, int stackIndex) {
        return table.getSymbol(symbolName, stackIndex);
    }

    // The parameter 'symbolType' is the value type to be assigned
    public void checkAssignment(String symbolName, TokenType symbolType, int stackIndex, Cursor cursor) {
        Symbol variable = table.getSymbol(symbolName, stackIndex);

        if(variable == null) {
            new SemanticException("Existe uma atribuição a uma variável que não foi declarada.",
                    symbolName, cursor.getLine(), cursor.getColumn());
        }

        if(variable.getType() == TokenType.CHAR && symbolType != TokenType.CHAR) {
            new SemanticException("Existe uma atribuição de um valor diferente de um char a uma variável do tipo char. \n" +
                    "Uma variável do tipo char só em compativel com char",
                    symbolName, cursor.getLine(), cursor.getColumn());
        }

        else if(variable.getType() == TokenType.INT && symbolType != TokenType.INT) {
            new SemanticException("Existe uma atribuição de um valor diferente de um int a uma variável do tipo int.",
                    symbolName, cursor.getLine(), cursor.getColumn());
        }

        else if(variable.getType() == TokenType.FLOAT && symbolType == TokenType.CHAR) {
            new SemanticException("Uma variável do tipo float não é compatível com um char.",
                    symbolName, cursor.getLine(), cursor.getColumn());
        }
    }

    public void checkVarDecl(String symbolName, TokenType symbolType, int stackIndex, Cursor cursor) {
        boolean alreadyExists = checkVariable(symbolName, stackIndex);
        TokenType varType;
        if(symbolType == TokenType.RESERVED_CHAR) {
            varType = TokenType.CHAR;
        } else if(symbolType == TokenType.RESERVED_INT) {
            varType = TokenType.INT;
        } else {
            varType = TokenType.FLOAT;
        }

        if(alreadyExists) {
            new SemanticException("A variavel já foi declarada anteriormente.",
                    symbolName, cursor.getLine(), cursor.getColumn());
        } else {
            table.put(varType, symbolName, stackIndex);
        }
    }

    public TokenType checkArithmeticExpression(TokenType termTypeA, TokenType termTypeB, Cursor cursor) {
        if(termTypeA == TokenType.INT && termTypeB == TokenType.INT ) {
            return TokenType.INT;
        }

        if(termTypeA == TokenType.CHAR || termTypeB == TokenType.CHAR) {
            // If factor A or factor B is not a Char
            if(termTypeA != termTypeB) {
                new SemanticException("Existe uma operação de uma variável do tipo char com uma variável do tipo diferente.",
                        null, cursor.getLine(), cursor.getColumn());
            }

            return TokenType.CHAR;
        }

        // It Returns float type because we are sure that the operation is of a float with an int
        return TokenType.FLOAT;
    }

    public TokenType checkTerm(TokenType factorTypeA, TokenType factorTypeB, TokenType operationType, Cursor cursor) {
        if(factorTypeA == TokenType.INT && factorTypeB == TokenType.INT ) {
            if(operationType == TokenType.MULT)
                return TokenType.INT;
            else
                return TokenType.FLOAT;
        }

        if(factorTypeA == TokenType.CHAR || factorTypeB == TokenType.CHAR) {
            // If factor A or factor B is not a Char
            if(factorTypeA != factorTypeB) {
                new SemanticException("Existe uma operação de uma variável do tipo char com uma variável do tipo diferente.",
                        null, cursor.getLine(), cursor.getColumn());
            }
            return TokenType.CHAR;
        }

        // It Returns float type because we are sure that the operation is of a float with an int
        return TokenType.FLOAT;
    }

    public void checkRelationalExpression(TokenType termTypeA, TokenType termTypeB, Cursor cursor) {
        if(termTypeA == TokenType.CHAR || termTypeB == TokenType.CHAR) {
            // If factor A or factor B is not a Char
            if(termTypeA != termTypeB) {
                new SemanticException("Existe uma operação de uma variável do tipo char com uma variável do tipo diferente.",
                        null, cursor.getLine(), cursor.getColumn());
            }
        }
    }

    public void removeFromStack(int stackIndex) {
        table.removeFromStack(stackIndex);
    }
}
