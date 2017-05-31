package com.compiler;

/**
 * Created by Rafael on 31/05/2017.
 */
public class SemanticException {
    public SemanticException(String errorMessage, String token,  int line, int column) {
        String message = getMessage(errorMessage, token, line, column);
        throw new IllegalArgumentException(message);
    }

    private String getMessage(String errorMessage, String token, int line, int column) {
        String lastTokenMessage = token != null ? ", token: " + token : "";
        errorMessage = "ERRO na linha "+ line +", coluna "+ column + lastTokenMessage
                + "\n" + errorMessage;
        return errorMessage;
    }
}
