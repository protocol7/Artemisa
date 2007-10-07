package com.protocol7.artemisa.casters;

public class InvalidDateException extends RuntimeException {

    private static final long serialVersionUID = -5756352137216092076L;

    private int startIndex;
    
    /**
     * @param message
     * @param cause
     */
    public InvalidDateException(String message, int startIndex, Throwable cause) {
        super(message, cause);
        this.startIndex = startIndex;
    }

    public InvalidDateException(String message, int startIndex) {
        super(message);
        this.startIndex = startIndex;
    }

    public int getStartIndex() {
        return startIndex;
    }

}
