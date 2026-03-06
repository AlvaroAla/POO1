package br.edu.biblioteca.ui;

import br.edu.biblioteca.model.Usuario;

import javax.swing.*;
import java.awt.*;

public class UsuarioFormDialog extends JDialog {
    private boolean confirmado = false;

    private final JTextField tfNome = new JTextField(30);
    private final JTextField tfEmail = new JTextField(30);
    private final JTextField tfMatricula = new JTextField(15);
    private final JSpinner spLimite = new JSpinner(new SpinnerNumberModel(3, 1, 10, 1));
    private final JCheckBox cbBloqueado = new JCheckBox("Bloqueado");

    public UsuarioFormDialog(Window owner, Usuario u) {
        super(owner, "Usuário", ModalityType.APPLICATION_MODAL);

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6,6,6,6);
        c.anchor = GridBagConstraints.WEST;

        int y=0;
        c.gridx=0; c.gridy=y; form.add(new JLabel("Nome:"), c);
        c.gridx=1; form.add(tfNome, c); y++;

        c.gridx=0; c.gridy=y; form.add(new JLabel("Email:"), c);
        c.gridx=1; form.add(tfEmail, c); y++;

        c.gridx=0; c.gridy=y; form.add(new JLabel("Matrícula:"), c);
        c.gridx=1; form.add(tfMatricula, c); y++;

        c.gridx=0; c.gridy=y; form.add(new JLabel("Limite de empréstimos:"), c);
        c.gridx=1; form.add(spLimite, c); y++;

        c.gridx=1; c.gridy=y; form.add(cbBloqueado, c); y++;

        if (u != null) {
            tfNome.setText(u.getNome());
            tfEmail.setText(u.getEmail());
            tfMatricula.setText(u.getMatricula());
            spLimite.setValue(u.getLimiteEmprestimos());
            cbBloqueado.setSelected(u.isBloqueado());
        } else {
            cbBloqueado.setSelected(false);
            cbBloqueado.setEnabled(false); // só faz sentido em edição
        }

        JButton ok = new JButton("Salvar");
        JButton cancel = new JButton("Cancelar");
        ok.addActionListener(e -> { confirmado = true; dispose(); });
        cancel.addActionListener(e -> { dispose(); });

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttons.add(cancel);
        buttons.add(ok);

        setLayout(new BorderLayout());
        add(form, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(owner);
    }

    public boolean isConfirmado() { return confirmado; }
    public String getNome() { return tfNome.getText(); }
    public String getEmail() { return tfEmail.getText(); }
    public String getMatricula() { return tfMatricula.getText(); }
    public int getLimite() { return (Integer) spLimite.getValue(); }
    public boolean isBloqueado() { return cbBloqueado.isSelected(); }
}
