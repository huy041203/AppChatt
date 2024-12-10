package com.midterm.appchatt.utils;

public class UpdatingEmailException extends Exception {
    public UpdatingEmailException(String message) {
        super(message);
    }

    public UpdatingEmailException() {
        super("Error at updating the email!!");
    }

    public UpdatingEmailException(String message, Throwable cause) {
        super(message, cause);
    }

}
