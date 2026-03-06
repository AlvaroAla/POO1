package br.edu.biblioteca.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Classe abstrata: usada para polimorfismo no acervo.
 */
public abstract class ItemAcervo implements Serializable, Identificavel {
    private static final long serialVersionUID = 1L;

    private final String id;
    private String titulo;
    private int anoPublicacao;

    protected ItemAcervo(String id, String titulo, int anoPublicacao) {
        this.id = Objects.requireNonNull(id, "id");
        setTitulo(titulo);
        setAnoPublicacao(anoPublicacao);
    }

    public String getId() { return id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) {
        if (titulo == null || titulo.trim().isEmpty()) throw new IllegalArgumentException("Título inválido");
        this.titulo = titulo.trim();
    }

    public int getAnoPublicacao() { return anoPublicacao; }
    public void setAnoPublicacao(int anoPublicacao) {
        if (anoPublicacao < 1400 || anoPublicacao > 2100) throw new IllegalArgumentException("Ano inválido");
        this.anoPublicacao = anoPublicacao;
    }

    // Polimorfismo: cada tipo define prazo e multa.
    public abstract int getPrazoEmprestimoDias();
    public abstract double getMultaDiaria();

    public String getTipo() { return getClass().getSimpleName(); }

    @Override
    public String toString() { return getTipo() + ": " + titulo + " (" + anoPublicacao + ")"; }
}
