package br.edu.biblioteca.ui;

import br.edu.biblioteca.model.*;
import br.edu.biblioteca.service.BibliotecaService;
import br.edu.biblioteca.service.IdService;

import javax.swing.*;
import java.awt.*;

public class ItemExemplarFormDialog extends JDialog {
    private boolean confirmado = false;

    private final JTextField tfCodigo = new JTextField(20);
    private final JComboBox<String> cbTipo = new JComboBox<>(new String[]{"Livro", "Revista", "MidiaDigital"});
    private final JTextField tfTitulo = new JTextField(30);
    private final JSpinner spAno = new JSpinner(new SpinnerNumberModel(2024, 1400, 2100, 1));

    // Campos específicos
    private final JTextField tfAutor = new JTextField(25);
    private final JTextField tfIsbn = new JTextField(18);
    private final JTextField tfEdicao = new JTextField(18);
    private final JTextField tfFormato = new JTextField(10);

    private final CardLayout cards = new CardLayout();
    private final JPanel painelEspecifico = new JPanel(cards);

    private final BibliotecaService service;

    public ItemExemplarFormDialog(Window owner, BibliotecaService service) {
        super(owner, "Adicionar item + exemplar", ModalityType.APPLICATION_MODAL);
        this.service = service;

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6,6,6,6);
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;

        int y=0;
        c.gridx=0; c.gridy=y; form.add(new JLabel("Código de barras:"), c);
        c.gridx=1; form.add(tfCodigo, c); y++;

        c.gridx=0; c.gridy=y; form.add(new JLabel("Tipo:"), c);
        c.gridx=1; form.add(cbTipo, c); y++;

        c.gridx=0; c.gridy=y; form.add(new JLabel("Título:"), c);
        c.gridx=1; form.add(tfTitulo, c); y++;

        c.gridx=0; c.gridy=y; form.add(new JLabel("Ano:"), c);
        c.gridx=1; form.add(spAno, c); y++;

        // Cards
        JPanel livro = new JPanel(new GridLayout(2,2,6,6));
        livro.add(new JLabel("Autor:")); livro.add(tfAutor);
        livro.add(new JLabel("ISBN:")); livro.add(tfIsbn);

        JPanel revista = new JPanel(new GridLayout(1,2,6,6));
        revista.add(new JLabel("Edição:")); revista.add(tfEdicao);

        JPanel midia = new JPanel(new GridLayout(1,2,6,6));
        midia.add(new JLabel("Formato:")); midia.add(tfFormato);

        painelEspecifico.add(livro, "Livro");
        painelEspecifico.add(revista, "Revista");
        painelEspecifico.add(midia, "MidiaDigital");

        c.gridx=0; c.gridy=y; c.gridwidth=2;
        form.add(painelEspecifico, c);
        y++;

        cbTipo.addActionListener(e -> cards.show(painelEspecifico, (String) cbTipo.getSelectedItem()));
        cards.show(painelEspecifico, "Livro");

        JButton ok = new JButton("Cadastrar");
        JButton cancel = new JButton("Cancelar");
        ok.addActionListener(e -> onOk());
        cancel.addActionListener(e -> dispose());

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttons.add(cancel);
        buttons.add(ok);

        setLayout(new BorderLayout());
        add(form, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(owner);
    }

    private void onOk() {
        try {
            String tipo = (String) cbTipo.getSelectedItem();
            String idItem = IdService.novoId();
            String titulo = tfTitulo.getText();
            int ano = (Integer) spAno.getValue();
            ItemAcervo item;

            if ("Livro".equals(tipo)) {
                item = new Livro(idItem, titulo, ano, tfAutor.getText(), tfIsbn.getText());
            } else if ("Revista".equals(tipo)) {
                item = new Revista(idItem, titulo, ano, tfEdicao.getText());
            } else {
                item = new MidiaDigital(idItem, titulo, ano, tfFormato.getText());
            }

            service.cadastrarItem(item);
            service.cadastrarExemplar(tfCodigo.getText(), item);

            confirmado = true;
            dispose();
        } catch (Exception ex) {
            UiUtil.erro(this, ex);
        }
    }

    public boolean isConfirmado() { return confirmado; }
}
