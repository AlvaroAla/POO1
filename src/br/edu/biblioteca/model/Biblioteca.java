package br.edu.biblioteca.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Raiz do agregado: contém o estado do sistema (persistência).
 */
public class Biblioteca implements Serializable {
    private static final long serialVersionUID = 1L;

    private final List<Usuario> usuarios = new ArrayList<>();
    private final List<Bibliotecario> bibliotecarios = new ArrayList<>();
    private final List<ItemAcervo> itens = new ArrayList<>();
    private final List<Exemplar> exemplares = new ArrayList<>();
    private final List<Emprestimo> emprestimos = new ArrayList<>();

    public List<Usuario> getUsuarios() { return usuarios; }
    public List<Bibliotecario> getBibliotecarios() { return bibliotecarios; }
    public List<ItemAcervo> getItens() { return itens; }
    public List<Exemplar> getExemplares() { return exemplares; }
    public List<Emprestimo> getEmprestimos() { return emprestimos; }
}
