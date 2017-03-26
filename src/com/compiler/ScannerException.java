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
        switch (errorType) {
            case CHAR_MALFORMATION:
                message = "ERRO na linha "+ line +", coluna "+ column + ", ultimo token lido: " + token
                        + " Malformed char found";
                break;
            case FLOAT_MALFORMATION:
                message = "ERRO na linha "+ line +", coluna "+ column + ", ultimo token lido: " + token
                        + " Malformed float found";
                break;
            case WRONG_EXCLAMATION_USE:
                message = "ERRO na linha "+ line +", coluna "+ column + ", ultimo token lido: " + token
                        + " Wrong exclamation use found";
                break;
            case INVALID_CHARACTER:
                message = "ERRO na linha "+ line +", coluna "+ column + ", ultimo token lido: " + token
                        + " Invalid character found";
                break;
            case WRONG_END_COMMENT:
                message = "ERRO na linha "+ line +", coluna "+ column + ", ultimo token lido: " + token
                        + " End of comment not found";
                break;
        }

        return message;
    }
}
