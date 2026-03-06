package br.edu.biblioteca.service;

import br.edu.biblioteca.exception.*;
import br.edu.biblioteca.model.*;
import br.edu.biblioteca.model.enums.StatusExemplar;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BibliotecaService {
    private final Biblioteca biblioteca;

    /**
     * Utilitário genérico para demonstrar polimorfismo via interface (Identificavel).
     * Permite buscar entidades de diferentes tipos pelo mesmo contrato (getId()).
     */
    private static <T extends Identificavel> T exigirPorId(List<T> lista, String id, String msgErro) throws EntidadeNaoEncontradaException {
        return lista.stream()
                .filter(e -> e.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new EntidadeNaoEncontradaException(msgErro + id));
    }

    public BibliotecaService(Biblioteca biblioteca) {
        this.biblioteca = biblioteca;
    }

    public Biblioteca getBiblioteca() { return biblioteca; }

    // ==== Usuários ====
    public Usuario cadastrarUsuario(String nome, String email, String matricula, int limite) {
        Usuario u = new Usuario(IdService.novoId(), nome, email, matricula, limite);
        biblioteca.getUsuarios().add(u);
        return u;
    }

    public void atualizarUsuario(Usuario u, String nome, String email, String matricula, int limite, boolean bloqueado) {
        u.setNome(nome);
        u.setEmail(email);
        u.setMatricula(matricula);
        u.setLimiteEmprestimos(limite);
        u.setBloqueado(bloqueado);
    }

    public void removerUsuario(Usuario u) throws BibliotecaException {
        boolean temEmprestimoAtivo = biblioteca.getEmprestimos().stream()
                .anyMatch(e -> e.isAtivo() && e.getUsuario().getId().equals(u.getId()));
        if (temEmprestimoAtivo) {
            throw new BibliotecaException("Usuário possui empréstimos ativos.");
        }
        biblioteca.getUsuarios().remove(u);
    }

    public List<Usuario> listarUsuarios() { return biblioteca.getUsuarios(); }

    // ==== Acervo ====
    public ItemAcervo cadastrarItem(ItemAcervo item) {
        biblioteca.getItens().add(item);
        return item;
    }

    public Exemplar cadastrarExemplar(String codigoBarras, ItemAcervo item) {
        Exemplar ex = new Exemplar(IdService.novoId(), codigoBarras, item);
        biblioteca.getExemplares().add(ex);
        return ex;
    }

    public void removerExemplar(Exemplar ex) throws BibliotecaException {
        boolean ativo = biblioteca.getEmprestimos().stream().anyMatch(e -> e.isAtivo() && e.getExemplar().getId().equals(ex.getId()));
        if (ativo) throw new BibliotecaException("Exemplar possui empréstimo ativo.");
        biblioteca.getExemplares().remove(ex);
    }

    public List<Exemplar> listarExemplares() { return biblioteca.getExemplares(); }

    public List<Exemplar> pesquisarExemplares(String termo) {
        String t = termo == null ? "" : termo.trim().toLowerCase();
        return biblioteca.getExemplares().stream()
                .filter(ex -> ex.getCodigoBarras().toLowerCase().contains(t)
                        || ex.getItem().getTitulo().toLowerCase().contains(t)
                        || ex.getItem().getTipo().toLowerCase().contains(t))
                .collect(Collectors.toList());
    }

    // ==== Empréstimos ====
    public Emprestimo emprestar(String matriculaUsuario, String codigoBarrasExemplar) throws BibliotecaException {
        Usuario usuario = biblioteca.getUsuarios().stream()
                .filter(u -> u.getMatricula().equalsIgnoreCase(matriculaUsuario))
                .findFirst()
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Usuário não encontrado (matrícula: " + matriculaUsuario + ")."));

        if (usuario.isBloqueado()) throw new UsuarioBloqueadoException("Usuário está bloqueado.");

        Exemplar exemplar = biblioteca.getExemplares().stream()
                .filter(e -> e.getCodigoBarras().equalsIgnoreCase(codigoBarrasExemplar))
                .findFirst()
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Exemplar não encontrado (código: " + codigoBarrasExemplar + ")."));

        if (!exemplar.isDisponivel()) throw new ExemplarIndisponivelException("Exemplar indisponível (já emprestado).");

        long ativos = biblioteca.getEmprestimos().stream()
                .filter(Emprestimo::isAtivo)
                .filter(e -> e.getUsuario().getId().equals(usuario.getId()))
                .count();

        if (ativos >= usuario.getLimiteEmprestimos()) {
            throw new LimiteEmprestimosException("Usuário atingiu o limite de empréstimos (" + usuario.getLimiteEmprestimos() + ").");
        }

        exemplar.setStatus(StatusExemplar.EMPRESTADO);
        Emprestimo emp = new Emprestimo(IdService.novoId(), exemplar, usuario, LocalDate.now());
        biblioteca.getEmprestimos().add(emp);
        return emp;
    }

    public double devolver(String idEmprestimo) throws BibliotecaException {
        Emprestimo emp = exigirPorId(biblioteca.getEmprestimos(), idEmprestimo, "Empréstimo não encontrado: ");

        if (!emp.isAtivo()) throw new BibliotecaException("Empréstimo já foi devolvido.");

        emp.registrarDevolucao(LocalDate.now());
        emp.getExemplar().setStatus(StatusExemplar.DISPONIVEL);

        return emp.calcularMulta(LocalDate.now());
    }

    public List<Emprestimo> listarEmprestimos(boolean somenteAtivos) {
        if (!somenteAtivos) return biblioteca.getEmprestimos();
        return biblioteca.getEmprestimos().stream().filter(Emprestimo::isAtivo).collect(Collectors.toList());
    }
}
