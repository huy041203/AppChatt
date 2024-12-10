package com.midterm.appchatt.utils;

public class RetrivingDataException extends Exception {
    public RetrivingDataException(String message) {
        super(message);
    }

    public RetrivingDataException() {
        super("Error at retrieving data from database!!");
    }

    public RetrivingDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
