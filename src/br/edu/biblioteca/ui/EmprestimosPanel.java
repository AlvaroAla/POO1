package br.edu.biblioteca.ui;

import br.edu.biblioteca.exception.BibliotecaException;
import br.edu.biblioteca.model.Emprestimo;
import br.edu.biblioteca.service.BibliotecaService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class EmprestimosPanel extends JPanel {
    private final BibliotecaService service;
    private final DefaultTableModel model;

    private final JTextField tfMatricula = new JTextField(12);
    private final JTextField tfCodigo = new JTextField(12);

    public EmprestimosPanel(BibliotecaService service) {
        this.service = service;
        setLayout(new BorderLayout());

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(new JLabel("Matrícula do usuário:"));
        top.add(tfMatricula);
        top.add(new JLabel("Código do exemplar:"));
        top.add(tfCodigo);

        JButton btnEmprestar = new JButton("Emprestar");
        JButton btnDevolver = new JButton("Devolver selecionado");
        JButton btnAtualizar = new JButton("Atualizar lista");
        JCheckBox cbSomenteAtivos = new JCheckBox("Somente ativos", true);

        top.add(btnEmprestar);
        top.add(btnDevolver);
        top.add(cbSomenteAtivos);
        top.add(btnAtualizar);

        add(top, BorderLayout.NORTH);

        model = new DefaultTableModel(new Object[]{"ID", "Usuário", "Matrícula", "Exemplar", "Título", "Prevista", "Devolução", "Multa (hoje)"}, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };

        JTable table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(table), BorderLayout.CENTER);

        Runnable refresh = () -> recarregar(service.listarEmprestimos(cbSomenteAtivos.isSelected()));

        btnEmprestar.addActionListener(e -> {
            try {
                service.emprestar(tfMatricula.getText(), tfCodigo.getText());
                UiUtil.info(this, "Empréstimo realizado.");
                refresh.run();
            } catch (BibliotecaException ex) {
                UiUtil.erro(this, ex);
            }
        });

        btnDevolver.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { UiUtil.info(this, "Selecione um empréstimo."); return; }
            String id = (String) model.getValueAt(row, 0);
            try {
                double multa = service.devolver(id);
                UiUtil.info(this, multa > 0 ? ("Devolvido com multa: R$ " + String.format("%.2f", multa)) : "Devolvido sem multa.");
                refresh.run();
            } catch (BibliotecaException ex) {
                UiUtil.erro(this, ex);
            }
        });

        btnAtualizar.addActionListener(e -> refresh.run());
        cbSomenteAtivos.addActionListener(e -> refresh.run());

        refresh.run();
    }

    private void recarregar(List<Emprestimo> emprestimos) {
        model.setRowCount(0);
        LocalDate hoje = LocalDate.now();
        for (Emprestimo e : emprestimos) {
            model.addRow(new Object[]{
                    e.getId(),
                    e.getUsuario().getNome(),
                    e.getUsuario().getMatricula(),
                    e.getExemplar().getCodigoBarras(),
                    e.getExemplar().getItem().getTitulo(),
                    e.getDataPrevista(),
                    e.getDataDevolucao(),
                    String.format("R$ %.2f", e.calcularMulta(hoje))
            });
        }
    }
}
