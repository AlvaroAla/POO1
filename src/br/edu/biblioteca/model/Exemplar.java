package br.edu.biblioteca.model;

import br.edu.biblioteca.model.enums.StatusExemplar;

import java.io.Serializable;
import java.util.Objects;

/**
 * Agregação: Exemplar referencia um ItemAcervo, que também pode existir "independente".
 */
public class Exemplar implements Serializable, Identificavel {
    private static final long serialVersionUID = 1L;

    private final String id;
    private String codigoBarras;
    private ItemAcervo item;
    private StatusExemplar status;

    public Exemplar(String id, String codigoBarras, ItemAcervo item) {
        this.id = Objects.requireNonNull(id, "id");
        setCodigoBarras(codigoBarras);
        setItem(item);
        this.status = StatusExemplar.DISPONIVEL;
    }

    public String getId() { return id; }

    public String getCodigoBarras() { return codigoBarras; }
    public void setCodigoBarras(String codigoBarras) {
        if (codigoBarras == null || codigoBarras.trim().isEmpty()) throw new IllegalArgumentException("Código de barras inválido");
        this.codigoBarras = codigoBarras.trim();
    }

    public ItemAcervo getItem() { return item; }
    public void setItem(ItemAcervo item) {
        this.item = Objects.requireNonNull(item, "item");
    }

    public StatusExemplar getStatus() { return status; }
    public void setStatus(StatusExemplar status) { this.status = Objects.requireNonNull(status, "status"); }

    public boolean isDisponivel() { return status == StatusExemplar.DISPONIVEL; }

    @Override
    public String toString() {
        return codigoBarras + " - " + item.toString() + " [" + status + "]";
    }
}
