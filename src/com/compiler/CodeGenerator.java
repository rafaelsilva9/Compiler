package com.compiler;

/**
 * Created by Rafael on 04/06/2017.
 */
public class CodeGenerator {
    private static String TAB = "    ";

    static String assignmentCode(String symbolA, String symbolB) {

        StringBuilder code = new StringBuilder();

        code.append(TAB);
        code.append(symbolA);
        code.append(' ');
        code.append("=");
        code.append(' ');
        code.append(symbolB);

        System.out.println(code);

        return code.toString();
    }

    static void assignmentCode(Symbol result, Symbol symbolA, Symbol symbolB, Token op) {
        String operation = op.getLexeme();
        StringBuilder code = new StringBuilder();

        code.append(TAB);
        code.append(result.getName());
        code.append(" = ");
        code.append(symbolA.getName());
        code.append(' ');
        code.append(operation);
        code.append(' ');
        code.append(symbolB.getName());

        System.out.println(code.toString());
    }

    static void ifCode(String expression, String label, Token operation) {
        StringBuilder code = new StringBuilder();

        code.append(TAB);
        code.append("if ");
        code.append(expression);
        code.append(' ');
        code.append(operation.getLexeme());
        code.append(" 0 goto ");
        code.append(label);
        code.append(':');

        System.out.println(code.toString());
    }

    static void labelCode(String label) {
        StringBuilder code = new StringBuilder();
        code.append(label);
        code.append(':');

        System.out.println(code.toString());
    }

    static void gotoCode(String label) {
        StringBuilder code = new StringBuilder();
        code.append(TAB);
        code.append("goto ");
        code.append(label);
        code.append(':');

        System.out.println(code.toString());
    }
}
