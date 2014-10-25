package com.norconex.jefmon;

public class JEFMonException extends RuntimeException {

    private static final long serialVersionUID = 2527095187272024448L;

    public JEFMonException() {
        super();
    }
    public JEFMonException(
            String message,
            Throwable cause,
            boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public JEFMonException(String message, Throwable cause) {
        super(message, cause);
    }

    public JEFMonException(String message) {
        super(message);
    }

    public JEFMonException(Throwable cause) {
        super(cause);
    }


}
