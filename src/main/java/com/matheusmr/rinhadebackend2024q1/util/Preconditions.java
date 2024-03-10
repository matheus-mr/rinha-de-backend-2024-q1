package com.matheusmr.rinhadebackend2024q1.util;

public class Preconditions {

    public static void checkArgument(boolean expression, String errorMessage) {
        if (!expression) {
            throw new IllegalArgumentException(errorMessage);
        }
    }
}
