package com.allocinit.bukkit;

public class ErrorException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ErrorException(String string) {
        super(string);
    }
}
