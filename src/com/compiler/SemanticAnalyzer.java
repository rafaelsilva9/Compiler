package com.compiler;

import com.sun.org.apache.bcel.internal.classfile.Code;

/**
 * Created by Rafael on 27/05/2017.
 */
public class SemanticAnalyzer {
    private SymbolTable table;
    private int termNumber = 0;

    public SemanticAnalyzer() {
        table = new SymbolTable();
    }

    // Checks whether the variable exists on the actual scope
    private boolean checkVariable(String symbolName, int stackIndex) {
        return ( table.getSymbol(symbolName, stackIndex) != null );
    }

    // Checks whether the variable exists in the current or previous scope
    public Symbol findSymbol(String symbolName, int stackIndex) {
        int a = stackIndex;
        while(a > 0) {
            Symbol symbol =  table.getSymbol(symbolName, a);
            if(symbol != null) {
                return symbol;
            }
            a --;
        }
        return null;
    }

    // The parameter 'symbolType' is the value type to be assigned
    public void checkAssignment(String symbolName, TokenType symbolType, int stackIndex, Cursor cursor) {
        Symbol variable = findSymbol(symbolName, stackIndex);

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

    public Symbol checkArithmeticExpression(Symbol symbolA, Symbol symbolB, Cursor cursor) {
        Symbol result;

        if(symbolA.getType() == TokenType.INT && symbolB.getType() == TokenType.INT ) {
            return new Symbol(newTerm(), TokenType.INT);
        }

        if(symbolA.getType() == TokenType.CHAR || symbolB.getType() == TokenType.CHAR) {
            // If symbol A or symbol B is not a Char
            if(symbolA.getType() != symbolB.getType()) {
                new SemanticException("Existe uma operação de uma variável do tipo char com uma variável do tipo diferente.",
                        null, cursor.getLine(), cursor.getColumn());
            }

            return new Symbol(newTerm(), TokenType.CHAR);
        }

        if(symbolA.getType() == TokenType.INT) {
            symbolA.setName("(Float)" + symbolA.getName());
        } else if(symbolB.getType() == TokenType.INT) {
            symbolB.setName("(Float)" + symbolB.getName());
        }

        // It Returns float symbol because we are sure that the operation is of a float with an int
        return new Symbol(newTerm(), TokenType.FLOAT);
    }

    public Symbol checkTerm(Symbol symbolA, Symbol symbolB, TokenType operationType, Cursor cursor) {

        if(symbolA.getType() == TokenType.INT && symbolB.getType() == TokenType.INT ) {
            if(operationType == TokenType.MULT)
                return new Symbol(newTerm(), TokenType.INT);
            else
                return new Symbol(newTerm(), TokenType.FLOAT);
        }

        if(symbolA.getType() == TokenType.CHAR || symbolB.getType() == TokenType.CHAR) {
            // If symbol A or symbol B is not a Char
            if(symbolA.getType() != symbolB.getType()) {
                new SemanticException("Existe uma operação de uma variável do tipo char com uma variável do tipo diferente.",
                        null, cursor.getLine(), cursor.getColumn());
            }
            return new Symbol(newTerm(), TokenType.CHAR);
        }

        if(symbolA.getType() == TokenType.INT) {
            symbolA.setName("(Float)" + symbolA.getName());
        } else if(symbolB.getType() == TokenType.INT){
            symbolB.setName("(Float)" + symbolB.getName());
        }

        // It Returns float symbol because we are sure that the operation is of a float with an int
        return new Symbol(newTerm(), TokenType.FLOAT);
    }

    public Symbol checkFactor(String symbolName, int stackIndex, Cursor cursor) {
        Symbol variable = findSymbol(symbolName, stackIndex);

        if(variable == null) {
            new SemanticException("A variavel utilizada não foi declarada anteriormente.",
                    symbolName, cursor.getLine(), cursor.getColumn());
        }

        return variable;
    }

    public Symbol checkRelationalExpression(Symbol exprA, Symbol exprB, Token operation, Cursor cursor) {
        if(exprA.getType() == TokenType.CHAR || exprB.getType() == TokenType.CHAR) {
            // If factor A or factor B is not a Char
            if(exprA.getType() != exprB.getType()) {
                new SemanticException("Existe uma operação de uma variável do tipo char com uma variável do tipo diferente.",
                        null, cursor.getLine(), cursor.getColumn());
            }
            Symbol resultExpr = new Symbol(newTerm(), null);
            CodeGenerator.assignmentCode(resultExpr, exprA, exprB, operation);
            return resultExpr;
        }

        if(exprA.getType() == TokenType.INT &&exprB.getType() == TokenType.INT ) {
            Symbol resultExpr = new Symbol(newTerm(), null);
            CodeGenerator.assignmentCode(resultExpr, exprA, exprB, operation);
            return resultExpr;
        }

        Symbol castResult = new Symbol(newTerm(), TokenType.FLOAT);
        Symbol resultExpr = new Symbol(newTerm(), null);

        if(exprA.getType() == TokenType.INT) {
            // Do Cast
            CodeGenerator.assignmentCode(castResult.getName(), "(Float)" + exprA.getName());
            // Print expression result
            CodeGenerator.assignmentCode(resultExpr, castResult, exprB, operation);
            return resultExpr;
        } else if(exprB.getType() == TokenType.INT){
            // Do Cast
            CodeGenerator.assignmentCode(castResult.getName(), "(Float)" + exprB.getName());
            // Print expression result
            CodeGenerator.assignmentCode(resultExpr, exprA, castResult, operation);
            return resultExpr;
        }

        // Print expression result
        CodeGenerator.assignmentCode(resultExpr, exprA, exprB, operation);
        return resultExpr;
    }

    public void removeFromStack(int stackIndex) {
        table.removeFromStack(stackIndex);
    }

    private String newTerm() {
        return "T" + termNumber++;
    }
}
