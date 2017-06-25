package com.petrovma92.tests.exceptions;


public class TestFileNotFoundException extends RuntimeException {
    public TestFileNotFoundException(String message) {
        super(message);
    }
}
