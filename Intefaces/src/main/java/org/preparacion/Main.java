package org.preparacion;


import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        JFrame ventana = new JFrame("Mi primera ventana");
        ventana.setSize(600, 400);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        JButton boton = new JButton("Boton");
        JLabel saludo = new JLabel("¡Hola, mundo!");
        JTextField saludoText = new JTextField(10);

        panel.add(saludo);
        panel.add(saludoText);
        panel.add(boton);

        ventana.add(panel);

        ventana.setVisible(true);
        boton.addActionListener(e -> {
            String texto = saludoText.getText().trim();
            if (texto.isEmpty()) {
                JOptionPane.showMessageDialog(ventana, "Ingresa un texto", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            saludo.setText(texto);
            saludoText.setText("");
        });
    }
}
