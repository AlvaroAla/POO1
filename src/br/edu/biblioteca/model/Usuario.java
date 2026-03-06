package br.edu.biblioteca.model;

public class Usuario extends Pessoa {
    private static final long serialVersionUID = 1L;

    private String matricula;
    private int limiteEmprestimos;
    private boolean bloqueado;

    public Usuario(String id, String nome, String email, String matricula, int limiteEmprestimos) {
        super(id, nome, email);
        setMatricula(matricula);
        setLimiteEmprestimos(limiteEmprestimos);
        this.bloqueado = false;
    }

    public String getMatricula() { return matricula; }
    public void setMatricula(String matricula) {
        if (matricula == null || matricula.trim().isEmpty()) throw new IllegalArgumentException("Matrícula inválida");
        this.matricula = matricula.trim();
    }

    public int getLimiteEmprestimos() { return limiteEmprestimos; }
    public void setLimiteEmprestimos(int limiteEmprestimos) {
        if (limiteEmprestimos < 1 || limiteEmprestimos > 10) throw new IllegalArgumentException("Limite deve ser entre 1 e 10");
        this.limiteEmprestimos = limiteEmprestimos;
    }

    public boolean isBloqueado() { return bloqueado; }
    public void setBloqueado(boolean bloqueado) { this.bloqueado = bloqueado; }
}
