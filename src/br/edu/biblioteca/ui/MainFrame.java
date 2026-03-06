package br.edu.biblioteca.ui;

import br.edu.biblioteca.exception.PersistenciaException;
import br.edu.biblioteca.model.Biblioteca;
import br.edu.biblioteca.persistence.PersistenciaService;
import br.edu.biblioteca.service.BibliotecaService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainFrame extends JFrame {
    private final BibliotecaService service;
    private final PersistenciaService persistencia;

    public MainFrame(Biblioteca biblioteca, PersistenciaService persistencia) {
        super("SysBiblioteca - Projeto Final POO");
        this.service = new BibliotecaService(biblioteca);
        this.persistencia = persistencia;

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setSize(1100, 650);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Usuários", new UsuariosPanel(service));
        tabs.addTab("Acervo", new AcervoPanel(service));
        tabs.addTab("Empréstimos / Devoluções", new EmprestimosPanel(service));

        setLayout(new BorderLayout());
        add(tabs, BorderLayout.CENTER);

        addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) {
                if (UiUtil.confirmar(MainFrame.this, "Salvar dados e sair?")) {
                    try {
                        persistencia.salvar(service.getBiblioteca());
                        dispose();
                        System.exit(0);
                    } catch (PersistenciaException ex) {
                        UiUtil.erro(MainFrame.this, ex);
                    }
                }
            }
        });
    }
}
