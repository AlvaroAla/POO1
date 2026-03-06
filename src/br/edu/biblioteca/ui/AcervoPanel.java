package br.edu.biblioteca.ui;

import br.edu.biblioteca.exception.BibliotecaException;
import br.edu.biblioteca.model.Exemplar;
import br.edu.biblioteca.service.BibliotecaService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AcervoPanel extends JPanel {
    private final BibliotecaService service;
    private final DefaultTableModel model;
    private final JTextField tfBusca = new JTextField(30);

    public AcervoPanel(BibliotecaService service) {
        this.service = service;
        setLayout(new BorderLayout());

        model = new DefaultTableModel(new Object[]{"ID", "Código", "Tipo", "Título", "Ano", "Status"}, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        JTable table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnAdd = new JButton("Adicionar item+exemplar");
        JButton btnDel = new JButton("Remover exemplar");
        JButton btnBuscar = new JButton("Buscar");
        JButton btnLimpar = new JButton("Limpar");

        top.add(btnAdd);
        top.add(btnDel);
        top.add(new JLabel("Filtro:"));
        top.add(tfBusca);
        top.add(btnBuscar);
        top.add(btnLimpar);

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        btnAdd.addActionListener(e -> {
            ItemExemplarFormDialog dlg = new ItemExemplarFormDialog(SwingUtilities.getWindowAncestor(this), service);
            dlg.setVisible(true);
            if (dlg.isConfirmado()) recarregar(service.listarExemplares());
        });

        btnDel.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { UiUtil.info(this, "Selecione um exemplar."); return; }
            String id = (String) model.getValueAt(row, 0);
            Exemplar ex = service.listarExemplares().stream().filter(x -> x.getId().equals(id)).findFirst().orElse(null);
            if (ex == null) { UiUtil.info(this, "Exemplar não encontrado."); return; }

            if (UiUtil.confirmar(this, "Remover exemplar '" + ex.getCodigoBarras() + "'?")) {
                try {
                    service.removerExemplar(ex);
                    recarregar(service.listarExemplares());
                } catch (BibliotecaException ex1) {
                    UiUtil.erro(this, ex1);
                }
            }
        });

        btnBuscar.addActionListener(e -> recarregar(service.pesquisarExemplares(tfBusca.getText())));
        btnLimpar.addActionListener(e -> { tfBusca.setText(""); recarregar(service.listarExemplares()); });

        recarregar(service.listarExemplares());
    }

    private void recarregar(List<Exemplar> exemplares) {
        model.setRowCount(0);
        for (Exemplar ex : exemplares) {
            model.addRow(new Object[]{
                    ex.getId(),
                    ex.getCodigoBarras(),
                    ex.getItem().getTipo(),
                    ex.getItem().getTitulo(),
                    ex.getItem().getAnoPublicacao(),
                    ex.getStatus().name()
            });
        }
    }
}
