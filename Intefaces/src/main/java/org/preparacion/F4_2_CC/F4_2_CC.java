package org.preparacion.F4_2_CC;

import componente.QFecha;

import javax.swing.*;
import java.awt.*;
import java.time.ZoneId;
import java.util.concurrent.atomic.AtomicInteger;

public class F4_2_CC {

    public static void main(String[] args) {

        QFecha fechaInicio = new QFecha();
        QFecha fechaFin = new QFecha();
        QFecha fechaComp = new QFecha();

        JFrame ventana = new JFrame("Adivina la fecha");
        ventana.setSize(550, 500);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setLocationRelativeTo(null);

        JPanel panelInicio = new JPanel(new BorderLayout());
        panelInicio.add(new JLabel("Fecha de inicio:"), BorderLayout.NORTH);
        panelInicio.add(fechaInicio._panelDatos(), BorderLayout.CENTER);

        JPanel panelFin = new JPanel(new BorderLayout());
        panelFin.add(new JLabel("Fecha de fin:"), BorderLayout.NORTH);
        panelFin.add(fechaFin._panelDatos(), BorderLayout.CENTER);

        JPanel panel = new JPanel(new BorderLayout(10, 10));

        JLabel titulo = new JLabel("Adivina la fecha secreta", SwingConstants.CENTER);
        panel.add(titulo, BorderLayout.NORTH);
        panel.add(fechaComp.get(), BorderLayout.CENTER);

        JPanel panelInferior = new JPanel(new FlowLayout());
        JButton boton = new JButton("Comprobar fecha");
        JLabel contador = new JLabel("Intentos: 0");
        panelInferior.add(boton);
        panelInferior.add(contador);
        panel.add(panelInferior, BorderLayout.SOUTH);

        AtomicInteger intentos = new AtomicInteger(0);

        boton.addActionListener(e -> {
            fechaComp.establecerRangoFecha(
                    fechaInicio.getFechaAsDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                    fechaFin.getFechaAsDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
            );
            if (fechaComp.compararDatos(ventana)) {
                contador.setText("Intentos: " + intentos.addAndGet(1));
            }
        });

        ventana.getContentPane().setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 1;
        ventana.add(panelInicio, gbc);

        gbc.gridy = 1;
        gbc.weighty = 1;
        ventana.add(panelFin, gbc);

        gbc.gridy = 2;
        gbc.weighty = 2;
        ventana.add(panel, gbc);
        ventana.setVisible(true);
    }
}