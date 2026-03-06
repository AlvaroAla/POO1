package br.edu.biblioteca.model;

public class Revista extends ItemAcervo {
    private static final long serialVersionUID = 1L;

    private String edicao;

    public Revista(String id, String titulo, int anoPublicacao, String edicao) {
        super(id, titulo, anoPublicacao);
        setEdicao(edicao);
    }

    public String getEdicao() { return edicao; }
    public void setEdicao(String edicao) {
        if (edicao == null || edicao.trim().isEmpty()) throw new IllegalArgumentException("Edição inválida");
        this.edicao = edicao.trim();
    }

    @Override public int getPrazoEmprestimoDias() { return 7; }
    @Override public double getMultaDiaria() { return 1.00; }
}
