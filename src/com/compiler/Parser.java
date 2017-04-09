package com.compiler;

import javax.naming.NamingEnumeration;

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

    public void procces(Cursor cursor) {
        Scanner scanner = new Scanner();

        while(!cursor.isEof()) {
            Token token = scanner.process(cursor);
            if(token != null){
                program(scanner, cursor, token);
            }
        }
    }

    public void program(Scanner scanner, Cursor cursor, Token token) {
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
        block(scanner, cursor, token);
        if(!cursor.isEof()) {
            // Progama nao terminou apos o MAIN
        }
    }

    public void block(Scanner scanner, Cursor cursor, Token token) {
        if(token.getClassification() != TokenType.OPENS_CURLY_BRACKET) {
            // Erro
        }
        token = scanner.process(cursor);
        varDecl(scanner, cursor, token);
        token = scanner.process(cursor);
        while(first(token, firstOfCommand)) {
            command(scanner, cursor, token);
        }
        if(token.getClassification() != TokenType.CLOSE_CURLY_BRACKET) {
            // Erro
        }
    }

    public void varDecl(Scanner scanner, Cursor cursor, Token token) {
        if(first(token, varTypes)) {
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
            varDecl(scanner, cursor, token);
        }
    }

    public void command(Scanner scanner, Cursor cursor, Token token) {
        if(first(token, firstOfbasicCmd)) {
            basicCommand(scanner, cursor, token);
        } else if(first(token, firstIteration)) {
            iteration(scanner, cursor, token);
        }
    }

    public void basicCommand(Scanner scanner, Cursor cursor, Token token) {
        if(token.getClassification() == TokenType.IDENTIFIER) {
            token = scanner.process(cursor);
            assignment(scanner, cursor, token);

        } else if(token.getClassification() == TokenType.OPENS_CURLY_BRACKET) {
            block(scanner, cursor, token);
        } else {
            // ?
        }
    }

    public void assignment(Scanner scanner, Cursor cursor, Token token) {
        if(token.getClassification() != TokenType.ASSIGNMENT) {
            // Erro
        }
        token = scanner.process(cursor);
        arithmeticExpression(scanner, cursor, token);
        if(token.getClassification() != TokenType.SEMICOLON) {
            // Erro
        }
        token = scanner.process(cursor);
    }

    public void arithmeticExpression(Scanner scanner, Cursor cursor, Token token) {
        term(scanner, cursor, token);
        arithmeticExpProductions(scanner, cursor, token);
    }

    public void arithmeticExpProductions(Scanner scanner, Cursor cursor, Token token) {
        if(token.getClassification() == TokenType.SUM || token.getClassification() == TokenType.SUB) {
            token = scanner.process(cursor);
            term(scanner, cursor, token);
            arithmeticExpProductions(scanner, cursor, token);
        }
    }

    public void term(Scanner scanner, Cursor cursor, Token token) {
        factor(scanner, cursor, token);
        termProductions(scanner, cursor, token);
    }

    public void termProductions(Scanner scanner, Cursor cursor, Token token) {
        if(token.getClassification() == TokenType.MULT || token.getClassification() == TokenType.DIV) {
            token = scanner.process(cursor);
            factor(scanner, cursor, token);
            termProductions(scanner, cursor, token);
        }
    }

    public void factor(Scanner scanner, Cursor cursor, Token token) {
        Token next = token;
        if(next.getClassification() == TokenType.IDENTIFIER
                || next.getClassification() == TokenType.FLOAT
                || next.getClassification() == TokenType.INT
                || next.getClassification() == TokenType.CHAR) {
            next = scanner.process(cursor);
        } else if(next.getClassification() == TokenType.OPENS_PARENTHESIS) {
            next = scanner.process(cursor);
            arithmeticExpression(scanner, cursor, next);
            if(next.getClassification() != TokenType.CLOSES_PARENTHESIS) {
                // Erro
            }
            next = scanner.process(cursor);
        } else {
            // Erro
        }
    }

    public void iteration(Scanner scanner, Cursor cursor, Token token) {
        if(token.getClassification() == TokenType.RESERVED_DO) {
            token = scanner.process(cursor);
            command(scanner, cursor, token);
            if(token.getClassification() != TokenType.RESERVED_WHILE) {
                // Erro
            }
            token = scanner.process(cursor);
            if(token.getClassification() != TokenType.OPENS_PARENTHESIS) {
                // Erro
            }
            token = scanner.process(cursor);
            // expressão relacional
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
            // expressão relacional
            if(token.getClassification() != TokenType.CLOSES_PARENTHESIS) {
                // Erro
            }
            token = scanner.process(cursor);
            command(scanner, cursor, token);
        }
    }

    public boolean first(Token token, TokenType[] firstList) {
        for (int i = 0; i < firstList.length; i++) {
            if(token.getClassification() == firstList[i]) {
                return true;
            }
        }
        return false;
    }
}
