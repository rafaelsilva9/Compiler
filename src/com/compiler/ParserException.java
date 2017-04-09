package com.compiler;

/**
 * Created by Rafael on 09/04/2017.
 */
public class ParserException {
    public ParserException(String errorMessage, String token,  int line, int column) {
        String message = getMessage(errorMessage, token, line, column);
        throw new IllegalArgumentException(message);
    }

    private String getMessage(String errorMessage, String token, int line, int column) {
        String lastTokenMessage = token != null ? ", ultimo token lido: " + token : "";
        errorMessage = "ERRO na linha "+ line +", coluna "+ column + lastTokenMessage
                + "\n" + errorMessage;
        return errorMessage;
    }
}
