package com.compiler;

/**
 * Created by Rafael on 08/04/2017.
 */
public class Parser {

    public static TokenType[] varTypes = {
            TokenType.RESERVED_INT,
            TokenType.RESERVED_FLOAT,
            TokenType.RESERVED_CHAR
    };

    public static TokenType[] firstOfCommand = {
            TokenType.OPENS_CURLY_BRACKET,
            TokenType.IDENTIFIER,
            TokenType.RESERVED_WHILE,
            TokenType.RESERVED_DO,
            TokenType.RESERVED_IF
    };

    public static TokenType[] firstOfbasicCmd = {
            TokenType.IDENTIFIER,
            TokenType.OPENS_CURLY_BRACKET
    };

    public static TokenType[] firstIteration = {
            TokenType.RESERVED_DO,
            TokenType.RESERVED_WHILE
    };

    private Token token;
    private SemanticAnalyzer semanticAnalyzer;
    private int stackIndex = 0;
    private int labelNumber = 0;

    public void procces(Cursor cursor) {
        Scanner scanner = new Scanner();
        token = scanner.process(cursor);
        semanticAnalyzer = new SemanticAnalyzer();
        program(scanner, cursor);
    }

    private void program(Scanner scanner, Cursor cursor) {
        if(token.getClassification() != TokenType.RESERVED_INT) {
            new ParserException("Programa não contém o token \"int\"", token.getLexeme(), cursor.getLine(),
                    cursor.getColumn() - token.getLexeme().length());
        }
        token = scanner.process(cursor);
        if(token.getClassification() != TokenType.RESERVED_MAIN) {
            new ParserException("Programa não contém o token \"main\"", token.getLexeme(), cursor.getLine(),
                    cursor.getColumn() - token.getLexeme().length());
        }
        token = scanner.process(cursor);
        if(token.getClassification() != TokenType.OPENS_PARENTHESIS) {
            new ParserException("Programa não contém o token \"(\"", token.getLexeme(), cursor.getLine(),
                    cursor.getColumn() - token.getLexeme().length());
        }
        token = scanner.process(cursor);
        if(token.getClassification() != TokenType.CLOSES_PARENTHESIS) {
            new ParserException("Programa não contém o token \")\"", token.getLexeme(), cursor.getLine(),
                    cursor.getColumn() - token.getLexeme().length());
        }
        token = scanner.process(cursor);
        block(scanner, cursor);
        if(!cursor.isEof()) {
            new ParserException("Programa não terminou após o main", token.getLexeme(), cursor.getLine(),
                    cursor.getColumn() - token.getLexeme().length());
        }
    }

    private void block(Scanner scanner, Cursor cursor) {
        stackIndex++;

        if(token.getClassification() != TokenType.OPENS_CURLY_BRACKET) {
            new ParserException("Bloco não contém o \"{\"", token.getLexeme(), cursor.getLine(),
                    cursor.getColumn() - token.getLexeme().length());
        }
        token = scanner.process(cursor);
        while(first(token, varTypes)) {
            varDecl(scanner, cursor);
        }
        while(first(token, firstOfCommand)) {
            command(scanner, cursor);
        }
        if(token.getClassification() != TokenType.CLOSE_CURLY_BRACKET) {
            new ParserException("Bloco não contém o \"}\"", token.getLexeme(), cursor.getLine(),
                    cursor.getColumn() - token.getLexeme().length());
        }
        token = scanner.process(cursor);

        semanticAnalyzer.removeFromStack(stackIndex--);
    }

    private void varDecl(Scanner scanner, Cursor cursor) {
        String symbolName;
        TokenType symbolType = token.getClassification();

        token = scanner.process(cursor);
        if(token.getClassification() != TokenType.IDENTIFIER) {
            new ParserException("Declaração não possui um identificador válido", token.getLexeme(), cursor.getLine(),
                    cursor.getColumn() - token.getLexeme().length());
        }

        // Get the name of symbol
        symbolName = token.getLexeme();

        // Checks if the variable already exists
        semanticAnalyzer.checkVarDecl(symbolName, symbolType, stackIndex, cursor);

        token = scanner.process(cursor);
        while(token.getClassification() != TokenType.SEMICOLON) {

            if(token.getClassification() != TokenType.COMMA) {
                new ParserException("Declaração não possui uma vírgula para separar identificadores", token.getLexeme(),
                        cursor.getLine(), cursor.getColumn() - token.getLexeme().length());
            }

            token = scanner.process(cursor);
            if(token.getClassification() != TokenType.IDENTIFIER) {
                new ParserException("Declaração não possui um identificador válido", token.getLexeme(), cursor.getLine(),
                        cursor.getColumn() - token.getLexeme().length());
            }

            // Get the name of the next symbol
            symbolName = token.getLexeme();

            // Checks if the variable already exists
            semanticAnalyzer.checkVarDecl(symbolName, symbolType, stackIndex, cursor);

            token = scanner.process(cursor);
        }
        token = scanner.process(cursor);
    }

    private void command(Scanner scanner, Cursor cursor) {
        if(first(token, firstOfbasicCmd)) {
            basicCommand(scanner, cursor);
        } else if(first(token, firstIteration)) {
            iteration(scanner, cursor);
        } else if(token.getClassification() == TokenType.RESERVED_IF) {
            String labelElse = newLabel();
            String labelEndIf = newLabel();

            token = scanner.process(cursor);
            if(token.getClassification() != TokenType.OPENS_PARENTHESIS) {
                new ParserException("if não contém o token \"(\"", token.getLexeme(), cursor.getLine(),
                        cursor.getColumn() - token.getLexeme().length());
            }

            token = scanner.process(cursor);
            Symbol expr = relationalExpression(scanner, cursor);

            if(token.getClassification() != TokenType.CLOSES_PARENTHESIS) {
                new ParserException("if não contém o token \")\"", token.getLexeme(), cursor.getLine(),
                        cursor.getColumn() - token.getLexeme().length());
            }

            CodeGenerator.ifCode(expr.getName(), labelElse, new Token(TokenType.EQUALITY, "=="));

            token = scanner.process(cursor);
            command(scanner, cursor);

            if(token.getClassification() == TokenType.RESERVED_ELSE) {

                CodeGenerator.gotoCode(labelEndIf);
                CodeGenerator.labelCode(labelElse);

                token = scanner.process(cursor);
                command(scanner, cursor);
            }

            CodeGenerator.labelCode(labelEndIf);

        } else {
            new ParserException("Comando inválido", token.getLexeme(), cursor.getLine(),
                    cursor.getColumn() - token.getLexeme().length());
        }
    }

    private void basicCommand(Scanner scanner, Cursor cursor) {
        if(token.getClassification() == TokenType.IDENTIFIER) {
            assignment(scanner, cursor);

        } else if(token.getClassification() == TokenType.OPENS_CURLY_BRACKET) {
            block(scanner, cursor);
        }
    }

    private void assignment(Scanner scanner, Cursor cursor) {
        // name of variable
        String nameOfVariable = token.getLexeme();
        token = scanner.process(cursor);

        if(token.getClassification() != TokenType.ASSIGNMENT) {
            new ParserException("Atribuição não contém o token \"=\"", token.getLexeme(), cursor.getLine(),
                    cursor.getColumn() - token.getLexeme().length());
        }

        token = scanner.process(cursor);
        Symbol expr = arithmeticExpression(scanner, cursor);

        semanticAnalyzer.checkAssignment(nameOfVariable, expr.getType(), stackIndex, cursor);

        CodeGenerator.assignmentCode(nameOfVariable, expr.getName());

        if(token.getClassification() != TokenType.SEMICOLON) {
            new ParserException("Token \";\" não foi encontrado no final da atribuição", token.getLexeme(),
                    cursor.getLine(), cursor.getColumn() - token.getLexeme().length());
        }
        token = scanner.process(cursor);
    }

    // Returns the type of the expression result
    private Symbol arithmeticExpression(Scanner scanner, Cursor cursor) {
        Symbol termA = term(scanner, cursor);
        Symbol termB = arithmeticExpProductions(scanner, cursor);

        Symbol exprResult = termA;
        if(termB != null) {
            exprResult = semanticAnalyzer.checkArithmeticExpression(termA, termB, cursor);
            CodeGenerator.assignmentCode(exprResult, termA, termB, termB.getOperation());
        }

        return exprResult;
    }

    private Symbol arithmeticExpProductions(Scanner scanner, Cursor cursor) {
        Symbol exprResult = null;
        Token operationType = token;

        if(operationType.getClassification() == TokenType.SUM || operationType.getClassification() == TokenType.SUB) {
            token = scanner.process(cursor);
            Symbol termA = term(scanner, cursor);

            Symbol termB = arithmeticExpProductions(scanner, cursor);

            if(termB != null) {
                // Check operation between terms and returns the resulting type
                exprResult = semanticAnalyzer.checkArithmeticExpression(termA, termB, cursor);

                CodeGenerator.assignmentCode(exprResult, termA, termB, operationType);

                exprResult.setOperation(operationType);
                return exprResult;
            } else {
                termA.setOperation(operationType);
                return termA;
            }
        }

        return exprResult;
    }

    private Symbol term(Scanner scanner, Cursor cursor) {
        Symbol factorA = factor(scanner, cursor);

        while(token.getClassification() == TokenType.MULT || token.getClassification() == TokenType.DIV) {
            Token operationType = token;
            token = scanner.process(cursor);

            Symbol factorB = factor(scanner, cursor);

            // Check operation between terms and returns the resulting type
            Symbol termResult = semanticAnalyzer.checkTerm(factorA, factorB, operationType.getClassification(), cursor);

            CodeGenerator.assignmentCode(termResult, factorA, factorB, operationType);

            factorA = termResult;
        }

        return factorA;
    }

    private Symbol factor(Scanner scanner, Cursor cursor) {
        Symbol symbol = new Symbol(token.getLexeme(), token.getClassification());

        if(symbol.getType() == TokenType.IDENTIFIER
                || token.getClassification() == TokenType.FLOAT
                || token.getClassification() == TokenType.INT
                || token.getClassification() == TokenType.CHAR) {

            if(symbol.getType() == TokenType.IDENTIFIER) {
                symbol = semanticAnalyzer.checkFactor(token.getLexeme(), stackIndex, cursor);
            }

            token = scanner.process(cursor);
        } else if(token.getClassification() == TokenType.OPENS_PARENTHESIS) {
            token = scanner.process(cursor);
            symbol = arithmeticExpression(scanner, cursor);

            if(token.getClassification() != TokenType.CLOSES_PARENTHESIS) {
                new ParserException("Token \")\" é necessário e não foi encontrado na expressão",
                        token.getLexeme(), cursor.getLine(), cursor.getColumn() - token.getLexeme().length());
            }
            token = scanner.process(cursor);
        } else {
            new ParserException("Expressão não possui um fator conhecido", token.getLexeme(), cursor.getLine(),
                    cursor.getColumn() - token.getLexeme().length());
        }

        return symbol;
    }

    private void iteration(Scanner scanner, Cursor cursor) {
        if(token.getClassification() == TokenType.RESERVED_DO) {
            String labelDo = newLabel();
            CodeGenerator.labelCode(labelDo);

            token = scanner.process(cursor);
            command(scanner, cursor);

            if(token.getClassification() != TokenType.RESERVED_WHILE) {
                new ParserException("O token \"while\" não foi encontrado", token.getLexeme(), cursor.getLine(),
                        cursor.getColumn() - token.getLexeme().length());
            }
            token = scanner.process(cursor);
            if(token.getClassification() != TokenType.OPENS_PARENTHESIS) {
                new ParserException("O token \"(\" não foi encontrado", token.getLexeme(), cursor.getLine(),
                        cursor.getColumn() - token.getLexeme().length());
            }

            token = scanner.process(cursor);

            Symbol expr = relationalExpression(scanner, cursor);
            CodeGenerator.ifCode(expr.getName(), labelDo, new Token(TokenType.DIFFERENT, "!="));

            if(token.getClassification() != TokenType.CLOSES_PARENTHESIS) {
                new ParserException("O token \")\" não foi encontrado", token.getLexeme(), cursor.getLine(),
                        cursor.getColumn() - token.getLexeme().length());
            }
            token = scanner.process(cursor);
            if(token.getClassification() != TokenType.SEMICOLON) {
                new ParserException("O token \";\" não foi encontrado", token.getLexeme(), cursor.getLine(),
                        cursor.getColumn() - token.getLexeme().length());
            }
            token = scanner.process(cursor);
        } else if(token.getClassification() == TokenType.RESERVED_WHILE) {
            String labelWhile = newLabel();
            String labelEndWhile = newLabel();

            token = scanner.process(cursor);

            if(token.getClassification() != TokenType.OPENS_PARENTHESIS) {
                new ParserException("O token \"(\" não foi encontrado", token.getLexeme(), cursor.getLine(),
                        cursor.getColumn() - token.getLexeme().length());
            }

            CodeGenerator.labelCode(labelWhile);
            token = scanner.process(cursor);

            Symbol expr = relationalExpression(scanner, cursor);
            CodeGenerator.ifCode(expr.getName(), labelEndWhile, new Token(TokenType.EQUALITY, "=="));

            if(token.getClassification() != TokenType.CLOSES_PARENTHESIS) {
                new ParserException("O token \")\" não foi encontrado", token.getLexeme(), cursor.getLine(),
                        cursor.getColumn() - token.getLexeme().length());
            }

            CodeGenerator.gotoCode(labelWhile);
            CodeGenerator.labelCode(labelEndWhile);

            token = scanner.process(cursor);
            command(scanner, cursor);
        }
    }

    private Symbol relationalExpression(Scanner scanner, Cursor cursor) {
        Symbol exprA = arithmeticExpression(scanner, cursor);
        Token operation = token;

        if(operation.getClassification() != TokenType.LESS_OR_EQUAL
                && operation.getClassification() != TokenType.LESS_THAN
                && operation.getClassification() != TokenType.GREATER_OR_EQUAL
                && operation.getClassification() != TokenType.GREATER_THAN
                && operation.getClassification() != TokenType.EQUALITY
                && operation.getClassification() != TokenType.DIFFERENT) {
            new ParserException("Operador relacional não foi encontrado na expressão", token.getLexeme(), cursor.getLine(),
                    cursor.getColumn() - token.getLexeme().length());
        }

        token = scanner.process(cursor);
        Symbol exprB = arithmeticExpression(scanner, cursor);

        Symbol exprResult = semanticAnalyzer.checkRelationalExpression(exprA.getType(), exprB.getType(), cursor);
        CodeGenerator.assignmentCode(exprResult, exprA, exprB, operation);

        return exprResult;
    }

    private boolean first(Token token, TokenType[] firstList) {
        for (int i = 0; i < firstList.length; i++) {
            if(token.getClassification() == firstList[i]) {
                return true;
            }
        }
        return false;
    }

    private String newLabel() {
        return "L" + labelNumber++;
    }
}
