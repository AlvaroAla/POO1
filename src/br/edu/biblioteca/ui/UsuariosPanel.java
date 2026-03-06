package br.edu.biblioteca.ui;

import br.edu.biblioteca.exception.BibliotecaException;
import br.edu.biblioteca.model.Usuario;
import br.edu.biblioteca.service.BibliotecaService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class UsuariosPanel extends JPanel {
    private final BibliotecaService service;
    private final DefaultTableModel model;

    public UsuariosPanel(BibliotecaService service) {
        this.service = service;
        setLayout(new BorderLayout());

        model = new DefaultTableModel(new Object[]{"ID", "Nome", "Email", "Matrícula", "Limite", "Bloqueado"}, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };

        JTable table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnAdd = new JButton("Cadastrar");
        JButton btnEdit = new JButton("Editar");
        JButton btnDel = new JButton("Remover");

        actions.add(btnAdd);
        actions.add(btnEdit);
        actions.add(btnDel);
        add(actions, BorderLayout.NORTH);

        btnAdd.addActionListener(e -> {
            UsuarioFormDialog dlg = new UsuarioFormDialog(SwingUtilities.getWindowAncestor(this), null);
            dlg.setVisible(true);
            if (dlg.isConfirmado()) {
                try {
                    service.cadastrarUsuario(dlg.getNome(), dlg.getEmail(), dlg.getMatricula(), dlg.getLimite());
                    recarregar();
                } catch (Exception ex) {
                    UiUtil.erro(this, ex);
                }
            }
        });

        btnEdit.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { UiUtil.info(this, "Selecione um usuário."); return; }
            Usuario u = service.listarUsuarios().stream().filter(x -> x.getId().equals(model.getValueAt(row, 0))).findFirst().orElse(null);
            if (u == null) { UiUtil.info(this, "Usuário não encontrado."); return; }

            UsuarioFormDialog dlg = new UsuarioFormDialog(SwingUtilities.getWindowAncestor(this), u);
            dlg.setVisible(true);
            if (dlg.isConfirmado()) {
                try {
                    service.atualizarUsuario(u, dlg.getNome(), dlg.getEmail(), dlg.getMatricula(), dlg.getLimite(), dlg.isBloqueado());
                    recarregar();
                } catch (Exception ex) {
                    UiUtil.erro(this, ex);
                }
            }
        });

        btnDel.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { UiUtil.info(this, "Selecione um usuário."); return; }
            String id = (String) model.getValueAt(row, 0);
            Usuario u = service.listarUsuarios().stream().filter(x -> x.getId().equals(id)).findFirst().orElse(null);
            if (u == null) { UiUtil.info(this, "Usuário não encontrado."); return; }

            if (UiUtil.confirmar(this, "Remover usuário '" + u.getNome() + "'?")) {
                try {
                    service.removerUsuario(u);
                    recarregar();
                } catch (BibliotecaException ex) {
                    UiUtil.erro(this, ex);
                }
            }
        });

        recarregar();
    }

    private void recarregar() {
        model.setRowCount(0);
        List<Usuario> usuarios = service.listarUsuarios();
        for (Usuario u : usuarios) {
            model.addRow(new Object[]{u.getId(), u.getNome(), u.getEmail(), u.getMatricula(), u.getLimiteEmprestimos(), u.isBloqueado()});
        }
    }
}
