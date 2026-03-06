package br.edu.biblioteca.exception;

public class BibliotecaException extends Exception {
    public BibliotecaException(String message) { super(message); }
    public BibliotecaException(String message, Throwable cause) { super(message, cause); }
}
