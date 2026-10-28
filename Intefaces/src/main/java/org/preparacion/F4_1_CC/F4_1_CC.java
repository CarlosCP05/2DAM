package org.preparacion.F4_1_CC;

import componente.QFecha;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicInteger;

public class F4_1_CC {

    public static void main(String[] args) {

        QFecha fechaComp =  new QFecha();

        JFrame ventana = new JFrame("Adivina la fecha");
        ventana.setSize(550, 300);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setLocationRelativeTo(null);

        fechaComp.establecerRangoFecha(
                LocalDate.of(2015, 1, 1),
                LocalDate.of(2025, 12, 31)
        );

        JPanel panel = new JPanel(new BorderLayout(10, 10));

        JLabel titulo = new JLabel("Adivina la fecha secreta", SwingConstants.CENTER);
        panel.add(titulo, BorderLayout.NORTH);

        JPanel panelDatos = new JPanel(new GridLayout(4, 3, 10, 10));
        panelDatos.setBorder(new EmptyBorder(10, 20, 10, 20));

        panel.add(fechaComp.get(), BorderLayout.CENTER);

        JPanel panelInferior = new JPanel(new FlowLayout());

        JButton boton = new JButton("Comprobar fecha");
        JLabel contador = new JLabel("Intentos: 0");

        panelInferior.add(boton);
        panelInferior.add(contador);

        panel.add(panelInferior, BorderLayout.SOUTH);

        AtomicInteger intentos = new AtomicInteger(0);

        boton.addActionListener(e -> {
            if(fechaComp.compararDatos(ventana)) {
                contador.setText("Intentos: " + intentos.addAndGet(1));
            }
        });

        ventana.add(panel);
        ventana.setVisible(true);
    }
}
