package com.petrovma92.tests.exceptions;


public class TestMethodHasNoAnnotationException extends RuntimeException {
    public TestMethodHasNoAnnotationException(String message) {
        super(message);
    }
}
