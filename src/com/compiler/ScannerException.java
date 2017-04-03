package com.compiler;

/**
 * Created by Rafael on 26/03/2017.
 */
public class ScannerException {
    public ScannerException (ErrorType errorType, String token,  int line, int column) {
        String message = getMessage(errorType, token, line, column);
        throw new IllegalArgumentException(message);
    }

    private String getMessage(ErrorType errorType, String token, int line, int column) {
        String message = "Unknown Error";
        String lastTokenMessage = token != null ? ", ultimo token lido: " + token : "";
        switch (errorType) {
            case CHAR_MALFORMATION:
                message = "ERRO na linha "+ line +", coluna "+ column + lastTokenMessage
                        + "\nMalformed char found";
                break;
            case FLOAT_MALFORMATION:
                message = "ERRO na linha "+ line +", coluna "+ column + lastTokenMessage
                        + "\nMalformed float found";
                break;
            case WRONG_EXCLAMATION_USE:
                message = "ERRO na linha "+ line +", coluna "+ column + lastTokenMessage
                        + "\nWrong exclamation use found";
                break;
            case INVALID_CHARACTER:
                message = "ERRO na linha "+ line +", coluna "+ column + lastTokenMessage
                        + "\nInvalid character found";
                break;
            case WRONG_END_COMMENT:
                message = "ERRO na linha "+ line +", coluna "+ column + lastTokenMessage
                        + "\nEnd of comment not found";
                break;
        }

        return message;
    }
}
