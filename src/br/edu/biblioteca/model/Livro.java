package br.edu.biblioteca.model;

public class Livro extends ItemAcervo {
    private static final long serialVersionUID = 1L;

    private String autor;
    private String isbn;

    public Livro(String id, String titulo, int anoPublicacao, String autor, String isbn) {
        super(id, titulo, anoPublicacao);
        setAutor(autor);
        setIsbn(isbn);
    }

    public String getAutor() { return autor; }
    public void setAutor(String autor) {
        if (autor == null || autor.trim().isEmpty()) throw new IllegalArgumentException("Autor inválido");
        this.autor = autor.trim();
    }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) {
        if (isbn == null || isbn.trim().isEmpty()) throw new IllegalArgumentException("ISBN inválido");
        this.isbn = isbn.trim();
    }

    @Override public int getPrazoEmprestimoDias() { return 14; }
    @Override public double getMultaDiaria() { return 1.50; }
}
