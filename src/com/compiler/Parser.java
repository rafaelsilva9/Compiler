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

    public void procces(Cursor cursor) {
        Scanner scanner = new Scanner();
        token = scanner.process(cursor);
//        if(token != null){
            program(scanner, cursor);
//        }
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
    }

    private void varDecl(Scanner scanner, Cursor cursor) {
        token = scanner.process(cursor);
        if(token.getClassification() != TokenType.IDENTIFIER) {
            new ParserException("Declaração não possui um identificador válido", token.getLexeme(), cursor.getLine(),
                    cursor.getColumn() - token.getLexeme().length());
        }
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
            token = scanner.process(cursor);
            if(token.getClassification() != TokenType.OPENS_PARENTHESIS) {
                new ParserException("if não contém o token \"(\"", token.getLexeme(), cursor.getLine(),
                        cursor.getColumn() - token.getLexeme().length());
            }
            token = scanner.process(cursor);
            relationalExpression(scanner, cursor);
            if(token.getClassification() != TokenType.CLOSES_PARENTHESIS) {
                new ParserException("if não contém o token \")\"", token.getLexeme(), cursor.getLine(),
                        cursor.getColumn() - token.getLexeme().length());
            }
            token = scanner.process(cursor);
            command(scanner, cursor);
            if(token.getClassification() == TokenType.RESERVED_ELSE) {
                token = scanner.process(cursor);
                command(scanner, cursor);
            }
        } else {
            new ParserException("Comando inválido", token.getLexeme(), cursor.getLine(),
                    cursor.getColumn() - token.getLexeme().length());
        }
    }

    private void basicCommand(Scanner scanner, Cursor cursor) {
        if(token.getClassification() == TokenType.IDENTIFIER) {
            token = scanner.process(cursor);
            assignment(scanner, cursor);

        } else if(token.getClassification() == TokenType.OPENS_CURLY_BRACKET) {
            block(scanner, cursor);
        }
    }

    private void assignment(Scanner scanner, Cursor cursor) {
        if(token.getClassification() != TokenType.ASSIGNMENT) {
            new ParserException("Atribuição não contém o token \"=\"", token.getLexeme(), cursor.getLine(),
                    cursor.getColumn() - token.getLexeme().length());
        }
        token = scanner.process(cursor);
        arithmeticExpression(scanner, cursor);
        if(token.getClassification() != TokenType.SEMICOLON) {
            new ParserException("Token \";\" não foi encontrado no final da atribuição", token.getLexeme(),
                    cursor.getLine(), cursor.getColumn() - token.getLexeme().length());
        }
        token = scanner.process(cursor);
    }

    private void arithmeticExpression(Scanner scanner, Cursor cursor) {
        term(scanner, cursor);
        arithmeticExpProductions(scanner, cursor);
    }

    private void arithmeticExpProductions(Scanner scanner, Cursor cursor) {
        if(token.getClassification() == TokenType.SUM || token.getClassification() == TokenType.SUB) {
            token = scanner.process(cursor);
            term(scanner, cursor);
            arithmeticExpProductions(scanner, cursor);
        }
    }

    private void term(Scanner scanner, Cursor cursor) {
        factor(scanner, cursor);
        termProductions(scanner, cursor);
    }

    private void termProductions(Scanner scanner, Cursor cursor) {
        if(token.getClassification() == TokenType.MULT || token.getClassification() == TokenType.DIV) {
            token = scanner.process(cursor);
            factor(scanner, cursor);
            termProductions(scanner, cursor);
        }
    }

    private void factor(Scanner scanner, Cursor cursor) {
        if(token.getClassification() == TokenType.IDENTIFIER
                || token.getClassification() == TokenType.FLOAT
                || token.getClassification() == TokenType.INT
                || token.getClassification() == TokenType.CHAR) {
            token = scanner.process(cursor);
        } else if(token.getClassification() == TokenType.OPENS_PARENTHESIS) {
            token = scanner.process(cursor);
            arithmeticExpression(scanner, cursor);
            if(token.getClassification() != TokenType.CLOSES_PARENTHESIS) {
                new ParserException("Token \")\" é necessário e não foi encontrado na expressão",
                        token.getLexeme(), cursor.getLine(), cursor.getColumn() - token.getLexeme().length());
            }
            token = scanner.process(cursor);
        } else {
            new ParserException("Expressão não possui um fator conhecido", token.getLexeme(), cursor.getLine(),
                    cursor.getColumn() - token.getLexeme().length());
        }
    }

    private void iteration(Scanner scanner, Cursor cursor) {
        if(token.getClassification() == TokenType.RESERVED_DO) {
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
            relationalExpression(scanner, cursor);
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
            token = scanner.process(cursor);
            if(token.getClassification() != TokenType.OPENS_PARENTHESIS) {
                new ParserException("O token \"(\" não foi encontrado", token.getLexeme(), cursor.getLine(),
                        cursor.getColumn() - token.getLexeme().length());
            }
            token = scanner.process(cursor);
            relationalExpression(scanner, cursor);
            if(token.getClassification() != TokenType.CLOSES_PARENTHESIS) {
                new ParserException("O token \")\" não foi encontrado", token.getLexeme(), cursor.getLine(),
                        cursor.getColumn() - token.getLexeme().length());
            }
            token = scanner.process(cursor);
            command(scanner, cursor);
        }
    }

    private void relationalExpression(Scanner scanner, Cursor cursor) {
        arithmeticExpression(scanner, cursor);
        if(token.getClassification() != TokenType.LESS_OR_EQUAL
                && token.getClassification() != TokenType.LESS_THAN
                && token.getClassification() != TokenType.GREATER_OR_EQUAL
                && token.getClassification() != TokenType.GREATER_THAN
                && token.getClassification() != TokenType.EQUALITY
                && token.getClassification() != TokenType.DIFFERENT) {
            new ParserException("Operador relacional não foi encontrado na expressão", token.getLexeme(), cursor.getLine(),
                    cursor.getColumn() - token.getLexeme().length());
        }
        token = scanner.process(cursor);
        arithmeticExpression(scanner, cursor);
    }

    private boolean first(Token token, TokenType[] firstList) {
        for (int i = 0; i < firstList.length; i++) {
            if(token.getClassification() == firstList[i]) {
                return true;
            }
        }
        return false;
    }
}
