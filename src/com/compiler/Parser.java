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
            TokenType.OPENS_PARENTHESIS,
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

        while(!cursor.isEof()) {
            Token token = scanner.process(cursor);
            this.token = token;
            if(token != null){
                program(scanner, cursor);
            }
        }
    }

    private void program(Scanner scanner, Cursor cursor) {
        if(token.getClassification() != TokenType.RESERVED_INT) {
            // Erro
        }
        token = scanner.process(cursor);
        if(token.getClassification() != TokenType.RESERVED_MAIN) {
            // Erro
        }
        token = scanner.process(cursor);
        if(token.getClassification() != TokenType.OPENS_PARENTHESIS) {
            // Erro
        }
        token = scanner.process(cursor);
        if(token.getClassification() != TokenType.CLOSES_PARENTHESIS) {
            // Erro
        }
        token = scanner.process(cursor);
        block(scanner, cursor);
        if(!cursor.isEof()) {
            // Progama nao terminou apos o MAIN
        }
    }

    private void block(Scanner scanner, Cursor cursor) {
        if(token.getClassification() != TokenType.OPENS_CURLY_BRACKET) {
            // Erro
        }
        token = scanner.process(cursor);
        while(first(token, varTypes)) {
            varDecl(scanner, cursor);
        }
        while(first(token, firstOfCommand)) {
            command(scanner, cursor);
        }
        if(token.getClassification() != TokenType.CLOSE_CURLY_BRACKET) {
            // Erro
        }
        token = scanner.process(cursor);
    }

    private void varDecl(Scanner scanner, Cursor cursor) {
        token = scanner.process(cursor);
        if(token.getClassification() != TokenType.IDENTIFIER) {
            // Erro
        }
        token = scanner.process(cursor);
        while(token.getClassification() != TokenType.SEMICOLON) {
            if(token.getClassification() != TokenType.COMMA) {
                // Erro
            }
            token = scanner.process(cursor);
            if(token.getClassification() != TokenType.IDENTIFIER) {
                // Erro
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
                // Erro
            }
            token = scanner.process(cursor);
            relationalExpression(scanner, cursor);
            if(token.getClassification() != TokenType.CLOSES_PARENTHESIS) {
                // Erro
            }
            token = scanner.process(cursor);
            command(scanner, cursor);
            if(token.getClassification() != TokenType.RESERVED_ELSE) {
                token = scanner.process(cursor);
                command(scanner, cursor);
            }
        } else {
            // Erro
        }
    }

    private void basicCommand(Scanner scanner, Cursor cursor) {
        if(token.getClassification() == TokenType.IDENTIFIER) {
            token = scanner.process(cursor);
            assignment(scanner, cursor);

        } else if(token.getClassification() == TokenType.OPENS_CURLY_BRACKET) {
            block(scanner, cursor);
        } else {
            // ?
        }
    }

    private void assignment(Scanner scanner, Cursor cursor) {
        if(token.getClassification() != TokenType.ASSIGNMENT) {
            // Erro
        }
        token = scanner.process(cursor);
        arithmeticExpression(scanner, cursor);
        if(token.getClassification() != TokenType.SEMICOLON) {
            // Erro
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
                // Erro
            }
            token = scanner.process(cursor);
        } else {
            // Erro
        }
    }

    private void iteration(Scanner scanner, Cursor cursor) {
        if(token.getClassification() == TokenType.RESERVED_DO) {
            token = scanner.process(cursor);
            command(scanner, cursor);
            if(token.getClassification() != TokenType.RESERVED_WHILE) {
                // Erro
            }
            token = scanner.process(cursor);
            if(token.getClassification() != TokenType.OPENS_PARENTHESIS) {
                // Erro
            }
            token = scanner.process(cursor);
            relationalExpression(scanner, cursor);
            if(token.getClassification() != TokenType.CLOSES_PARENTHESIS) {
                // Erro
            }
            token = scanner.process(cursor);
            if(token.getClassification() != TokenType.SEMICOLON) {
                // Erro
            }
            token = scanner.process(cursor);
        } else if(token.getClassification() == TokenType.RESERVED_WHILE) {
            token = scanner.process(cursor);
            if(token.getClassification() != TokenType.OPENS_PARENTHESIS) {

            }
            token = scanner.process(cursor);
            relationalExpression(scanner, cursor);
            if(token.getClassification() != TokenType.CLOSES_PARENTHESIS) {
                // Erro
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
            // Erro
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
