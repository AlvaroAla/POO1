package br.edu.biblioteca.app;

import br.edu.biblioteca.exception.PersistenciaException;
import br.edu.biblioteca.model.Biblioteca;
import br.edu.biblioteca.persistence.PersistenciaService;
import br.edu.biblioteca.service.BibliotecaService;
import br.edu.biblioteca.ui.MainFrame;
import br.edu.biblioteca.ui.UiUtil;

import javax.swing.*;
import java.io.File;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                File arquivo = new File("data/biblioteca.dat");
                PersistenciaService persistencia = new PersistenciaService(arquivo);
                Biblioteca biblioteca = persistencia.carregar();

                // Seed mínimo (para a primeira execução)
                BibliotecaService service = new BibliotecaService(biblioteca);
                if (biblioteca.getUsuarios().isEmpty()) {
                    service.cadastrarUsuario("Aluno Demo", "aluno@demo.com", "2024001", 3);
                }

                MainFrame frame = new MainFrame(biblioteca, persistencia);
                frame.setVisible(true);
            } catch (PersistenciaException e) {
                UiUtil.erro(null, e);
            }
        });
    }
}
