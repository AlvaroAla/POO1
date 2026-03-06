package br.edu.biblioteca.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

/**
 * Composição: um Emprestimo "é composto" por um Exemplar (referência obrigatória) e dados de datas.
 */
public class Emprestimo implements Serializable, Identificavel {
    private static final long serialVersionUID = 1L;

    private final String id;
    private final Exemplar exemplar;
    private final Usuario usuario;

    private final LocalDate dataEmprestimo;
    private final LocalDate dataPrevista;
    private LocalDate dataDevolucao; // null enquanto ativo

    public Emprestimo(String id, Exemplar exemplar, Usuario usuario, LocalDate dataEmprestimo) {
        this.id = Objects.requireNonNull(id, "id");
        this.exemplar = Objects.requireNonNull(exemplar, "exemplar");
        this.usuario = Objects.requireNonNull(usuario, "usuario");
        this.dataEmprestimo = Objects.requireNonNull(dataEmprestimo, "dataEmprestimo");
        this.dataPrevista = dataEmprestimo.plusDays(exemplar.getItem().getPrazoEmprestimoDias());
    }

    public String getId() { return id; }
    public Exemplar getExemplar() { return exemplar; }
    public Usuario getUsuario() { return usuario; }
    public LocalDate getDataEmprestimo() { return dataEmprestimo; }
    public LocalDate getDataPrevista() { return dataPrevista; }
    public LocalDate getDataDevolucao() { return dataDevolucao; }

    public boolean isAtivo() { return dataDevolucao == null; }

    public void registrarDevolucao(LocalDate dataDevolucao) {
        if (dataDevolucao == null) throw new IllegalArgumentException("Data de devolução inválida");
        this.dataDevolucao = dataDevolucao;
    }

    public long diasAtraso(LocalDate hoje) {
        LocalDate base = isAtivo() ? hoje : dataDevolucao;
        if (base.isAfter(dataPrevista)) {
            return ChronoUnit.DAYS.between(dataPrevista, base);
        }
        return 0;
    }

    public double calcularMulta(LocalDate hoje) {
        long atraso = diasAtraso(hoje);
        return atraso * exemplar.getItem().getMultaDiaria();
    }

    @Override
    public String toString() {
        return "Emprestimo{" + id + ", usuario=" + usuario.getNome() + ", exemplar=" + exemplar.getCodigoBarras() + "}";
    }
}
