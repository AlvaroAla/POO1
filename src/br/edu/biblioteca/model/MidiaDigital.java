package br.edu.biblioteca.model;

public class MidiaDigital extends ItemAcervo {
    private static final long serialVersionUID = 1L;

    private String formato; // PDF, EPUB, MP3 etc.

    public MidiaDigital(String id, String titulo, int anoPublicacao, String formato) {
        super(id, titulo, anoPublicacao);
        setFormato(formato);
    }

    public String getFormato() { return formato; }
    public void setFormato(String formato) {
        if (formato == null || formato.trim().isEmpty()) throw new IllegalArgumentException("Formato inválido");
        this.formato = formato.trim().toUpperCase();
    }

    @Override public int getPrazoEmprestimoDias() { return 5; }
    @Override public double getMultaDiaria() { return 0.50; }
}
