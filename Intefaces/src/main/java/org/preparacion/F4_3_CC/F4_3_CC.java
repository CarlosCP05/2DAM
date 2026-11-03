package org.preparacion.F4_3_CC;


import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class F4_3_CC {
    static void main() {

        JFrame ventana = new JFrame("Calculadora");
        JPanel panel = new JPanel();
        JPanel encabezado = new JPanel();
        JPanel botonesPanel = new JPanel();

        JTextField operacion = new JTextField();
        String[] botonesTxt = {"=", "()", "C", "%", "⌫", "7", "8", "9", "%", "4", "5", "6", "X", "1", "2", "3", "-", "±", "0", ",", "+"};
        List<JButton> botones = new ArrayList<>();
        for(String b: botonesTxt) botones.add(new JButton(b));

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        encabezado.setLayout(new GridBagLayout());
        botonesPanel.setLayout(new GridLayout(5, 4));

        operacion.setHorizontalAlignment(JTextField.RIGHT);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;

        gbc.gridx = 0;
        gbc.gridwidth = 3;
        gbc.weightx = 4.0;
        encabezado.add(operacion, gbc);

        gbc.gridx = 3;
        gbc.gridwidth = 1;
        gbc.weightx = 1.0;
        encabezado.add(botones.getFirst(), gbc);
        for(int i = 1; i < botones.toArray().length; i++) botonesPanel.add(botones.get(i));


        panel.add(encabezado);
        panel.add(botonesPanel);

        ventana.setSize(400, 600);
        ventana.add(panel);
        ventana.setVisible(true);
    }
}
