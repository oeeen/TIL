package dev.smjeon.til.generic;

public class JdbcTemplateException extends RuntimeException {
    public JdbcTemplateException(Exception cause) {
        super(cause);
    }
}
