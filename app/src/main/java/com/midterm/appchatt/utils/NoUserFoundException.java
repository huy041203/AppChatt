package com.midterm.appchatt.utils;

public class NoUserFoundException extends Exception {
    public NoUserFoundException(String message) {
        super(message);
    }

    public NoUserFoundException() {
        super("No user found!!");
    }

    public NoUserFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
