package br.edu.biblioteca.service;

import java.util.UUID;

public final class IdService {
    private IdService() {}
    public static String novoId() {
        return UUID.randomUUID().toString();
    }
}
