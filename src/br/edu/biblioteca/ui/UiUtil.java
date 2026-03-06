package br.edu.biblioteca.ui;

import javax.swing.*;
import java.awt.*;

public final class UiUtil {
    private UiUtil() {}

    public static void erro(Component parent, Exception e) {
        JOptionPane.showMessageDialog(parent, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
    }

    public static void info(Component parent, String msg) {
        JOptionPane.showMessageDialog(parent, msg, "Info", JOptionPane.INFORMATION_MESSAGE);
    }

    public static String prompt(Component parent, String msg) {
        return JOptionPane.showInputDialog(parent, msg);
    }

    public static boolean confirmar(Component parent, String msg) {
        return JOptionPane.showConfirmDialog(parent, msg, "Confirmar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }
}
