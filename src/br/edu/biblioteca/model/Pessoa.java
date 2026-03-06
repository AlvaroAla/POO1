package br.edu.biblioteca.model;

import java.io.Serializable;
import java.util.Objects;

public abstract class Pessoa implements Serializable, Identificavel {
    private static final long serialVersionUID = 1L;

    private final String id;
    private String nome;
    private String email;

    protected Pessoa(String id, String nome, String email) {
        this.id = Objects.requireNonNull(id, "id");
        setNome(nome);
        setEmail(email);
    }

    public String getId() { return id; }

    public String getNome() { return nome; }
    public void setNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) throw new IllegalArgumentException("Nome inválido");
        this.nome = nome.trim();
    }

    public String getEmail() { return email; }
    public void setEmail(String email) {
        if (email == null || !email.contains("@")) throw new IllegalArgumentException("Email inválido");
        this.email = email.trim();
    }

    @Override
    public String toString() { return nome + " (" + id + ")"; }
}
