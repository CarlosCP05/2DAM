package org.preparacion.F3_CC;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class F3_CC {

    public static void main(String[] args) {

        LocalDate inicio = LocalDate.of(2015, 1, 1);
        LocalDate fin = LocalDate.of(2025, 1, 1);

        long dias = ChronoUnit.DAYS.between(inicio, fin);
        Random random = new Random();

        LocalDate fechaSecreta = inicio.plusDays(random.nextInt((int) dias));

        int diaCorrecto = fechaSecreta.getDayOfMonth();
        int mesCorrecto = fechaSecreta.getMonthValue();
        int anioCorrecto = fechaSecreta.getYear();

        JFrame ventana = new JFrame("Adivina la fecha");
        ventana.setSize(550, 300);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout(10, 10));

        JLabel titulo = new JLabel("Adivina la fecha secreta", SwingConstants.CENTER);
        panel.add(titulo, BorderLayout.NORTH);

        JPanel panelDatos = new JPanel(new GridLayout(4, 3, 10, 10));
        panelDatos.setBorder(new EmptyBorder(10, 20, 10, 20));

        panelDatos.add(new JLabel("Campo"));
        panelDatos.add(new JLabel("Tu respuesta"));
        panelDatos.add(new JLabel("Resultado"));

        panelDatos.add(new JLabel("Día:"));
        JTextField campoDia = new JTextField(5);
        JLabel resultadoDia = new JLabel("-", SwingConstants.CENTER);
        panelDatos.add(campoDia);
        panelDatos.add(resultadoDia);

        panelDatos.add(new JLabel("Mes:"));
        JTextField campoMes = new JTextField(5);
        JLabel resultadoMes = new JLabel("-", SwingConstants.CENTER);
        panelDatos.add(campoMes);
        panelDatos.add(resultadoMes);

        panelDatos.add(new JLabel("Anio:"));
        JTextField campoAnio = new JTextField(5);
        JLabel resultadoAnio = new JLabel("-", SwingConstants.CENTER);
        panelDatos.add(campoAnio);
        panelDatos.add(resultadoAnio);

        panel.add(panelDatos, BorderLayout.CENTER);

        JPanel panelInferior = new JPanel(new FlowLayout());

        JButton boton = new JButton("Comprobar fecha");
        JLabel contador = new JLabel("Intentos: 0");

        panelInferior.add(boton);
        panelInferior.add(contador);

        panel.add(panelInferior, BorderLayout.SOUTH);

        AtomicInteger intentos = new AtomicInteger(0);

        boton.addActionListener(e -> {

            try {
                int dia = Integer.parseInt(campoDia.getText().trim());
                int mes = Integer.parseInt(campoMes.getText().trim());
                int anio = Integer.parseInt(campoAnio.getText().trim());

                LocalDate.of(anio, mes, dia);

                contador.setText("Intentos: " + intentos.addAndGet(1));

                if (dia == diaCorrecto) {
                    resultadoDia.setText("Correcto");
                    resultadoDia.setForeground(Color.GREEN);
                } else if (dia > diaCorrecto) {
                    resultadoDia.setText("Demasiado alto");
                    resultadoDia.setForeground(Color.RED);
                } else {
                    resultadoDia.setText("Demasiado bajo");
                    resultadoDia.setForeground(Color.BLUE);
                }

                if (mes == mesCorrecto) {
                    resultadoMes.setText("Correcto");
                    resultadoMes.setForeground(Color.GREEN);
                } else if (mes > mesCorrecto) {
                    resultadoMes.setText("Demasiado alto");
                    resultadoMes.setForeground(Color.RED);
                } else {
                    resultadoMes.setText("Demasiado bajo");
                    resultadoMes.setForeground(Color.BLUE);
                }

                if (anio == anioCorrecto) {
                    resultadoAnio.setText("Correcto");
                    resultadoAnio.setForeground(Color.GREEN);
                } else if (anio > anioCorrecto) {
                    resultadoAnio.setText("Demasiado alto");
                    resultadoAnio.setForeground(Color.RED);
                } else {
                    resultadoAnio.setText("Demasiado bajo");
                    resultadoAnio.setForeground(Color.BLUE);
                }

                if (dia == diaCorrecto
                        && mes == mesCorrecto
                        && anio == anioCorrecto) {

                    JOptionPane.showMessageDialog(ventana,"¡Enhorabuena! Has acertado la fecha.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(ventana,"Introduce números en los tres campos.");
            } catch (java.time.DateTimeException ex) {
                JOptionPane.showMessageDialog(ventana,"La fecha introducida no es válida.");
            }
        });

        ventana.add(panel);
        ventana.setVisible(true);
    }
}
