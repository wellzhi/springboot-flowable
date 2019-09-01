package com.dapeng.flow.common.exception;

/**
 * @author Administrator
 */
public class UnexpectedException extends RuntimeException {
    private static final long serialVersionUID = -2104359090217311866L;

    public UnexpectedException(String message) {
        super("[500000999] " + message);
    }

    public UnexpectedException(String message, Throwable cause) {
        super("[500000999] " + message, cause);
    }

    @Override
    public String toString() {
        return "[500000999] " + this.getMessage();
    }
}
