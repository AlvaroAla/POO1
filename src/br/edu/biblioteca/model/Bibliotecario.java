package br.edu.biblioteca.model;

public class Bibliotecario extends Pessoa {
    private static final long serialVersionUID = 1L;

    private String registro;

    public Bibliotecario(String id, String nome, String email, String registro) {
        super(id, nome, email);
        setRegistro(registro);
    }

    public String getRegistro() { return registro; }
    public void setRegistro(String registro) {
        if (registro == null || registro.trim().isEmpty()) throw new IllegalArgumentException("Registro inválido");
        this.registro = registro.trim();
    }
}
