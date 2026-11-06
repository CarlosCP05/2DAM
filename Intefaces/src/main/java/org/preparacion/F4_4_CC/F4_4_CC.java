package org.preparacion.F4_4_CC;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class F4_4_CC {
    private static final Font fuenteBase = new Font("Arial", Font.BOLD, 24);

    static void main() {
        JFrame ventana = new JFrame("Calculadora");
        JPanel fondo = new JPanel();
        JPanel panel = new JPanel();
        JPanel encabezado = new JPanel();
        JPanel botonesPanel = new JPanel();

        JTextField operacion = new JTextField();
        String[] botonesTxt = {"=", "C", "()", "%", "⌫", "7", "8", "9", "/", "4", "5", "6", "X", "1", "2", "3", "-", "±", "0", ",", "+"};
        List<JButton> botones = new ArrayList<>();
        for(String b: botonesTxt) botones.add(new JButton(b));

        fondo.setLayout(new GridBagLayout());
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        encabezado.setLayout(new GridBagLayout());
        botonesPanel.setLayout(new GridBagLayout());

        fondo.setBackground(Color.DARK_GRAY);
        panel.setBackground(Color.GRAY);
        panel.setMaximumSize(new Dimension(400, 500));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        encabezado.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        encabezado.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        operacion.setPreferredSize(new Dimension(120, 40));
        operacion.setMinimumSize(new Dimension(120, 40));
        operacion.setMaximumSize(new Dimension(120, 40));
        operacion.setFont(fuenteBase);
        operacion.setEditable(false);

        for (JButton btn : botones) {
            btn.setFont(fuenteBase);
            btn.setPreferredSize(new Dimension(40, 40));
            btn.setMinimumSize(new Dimension(40, 40));

            if (btn.getText().equals("C") || btn.getText().equals("()") || btn.getText().equals("%") || btn.getText().equals("±") || btn.getText().equals(",")) {
                btn.setBackground(Color.yellow);
            }

            if (btn.getText().equals("⌫")) {
                // Problema de Arial con el Unicode
                btn.setFont(new Font("Segoe UI Symbol", Font.BOLD, 20));
                btn.setBackground(Color.red);
            }

            if (btn.getText().equals("-") || btn.getText().equals("+") || btn.getText().equals("X") || btn.getText().equals("/")) {
                btn.setBackground(Color.blue);
            }

            btn.addActionListener(e -> {
                manejarBoton(operacion, btn.getText());
            });

            btn.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            btn.setBorder(BorderFactory.createLineBorder(Color.black));
        }

        operacion.setHorizontalAlignment(JTextField.RIGHT);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(2, 2, 2, 2);
        gbc.weighty = 1.0;

        gbc.gridx = 0;
        gbc.weightx = 1;
        gbc.gridwidth = 3;
        encabezado.add(operacion, gbc);

        gbc.gridx = 3;
        gbc.weightx = 0;
        gbc.gridwidth = 1;
        encabezado.add(botones.getFirst(), gbc);

        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(2, 2, 2, 2);
        gbc.weighty = 1;
        gbc.weightx = 1;
        for(int i = 0; i < botones.size()-1; i++){
            gbc.gridx = i%4 ;
            gbc.gridy = i/4;
            botonesPanel.add(botones.get(i+1), gbc);
        }

        panel.add(encabezado);
        panel.add(botonesPanel);

        fondo.add(panel);

        ventana.setSize(400, 600);
        ventana.add(fondo);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setVisible(true);
    }

    private static void manejarBoton(JTextField operacion, String boton) {
        String texto = operacion.getText();

        switch (boton) {
            case "C":
                operacion.setText("");
                break;

            case "⌫":
                if (!texto.isEmpty()) {
                    operacion.setText(texto.substring(0, texto.length() - 1));
                }
                break;

            case "=":
                if (!texto.isEmpty()) {
                    try {
                        double resultado = calcular(texto);

                        if (!Double.isFinite(resultado)) throw new ArithmeticException();

                        operacion.setText(formatearResultado(resultado));

                    } catch (Exception e) {
                        operacion.setText("Error");
                    }
                }
                break;

            case "±":
                operacion.setText(cambiarSigno(texto));
                break;

            case "()":
                operacion.setText(cambiarParentesis(texto));
                break;

            default:
                if (texto.equals("Error")) texto = "";
                operacion.setText(texto + boton);
                break;
        }
    }

    private static String cambiarParentesis(String texto) {
        int abiertos = 0;
        int cerrados = 0;

        for (int i = 0; i < texto.length(); i++) {
            if (texto.charAt(i) == '(') abiertos++;
            if (texto.charAt(i) == ')') cerrados++;
        }

        if (abiertos > cerrados) return texto + ")";
        else return texto + "(";
    }

    private static double calcular(String texto) {
        Calculador calculador = new Calculador(texto);
        return calculador.analizar();
    }

    private static String formatearResultado(double resultado) {
        return BigDecimal.valueOf(resultado)
                .stripTrailingZeros()
                .toPlainString()
                .replace('.', ',');
    }

    private static String cambiarSigno(String texto) {
        int inicio = texto.length();

        while (inicio > 0) {
            char c = texto.charAt(inicio - 1);

            if (Character.isDigit(c) || c == ',' || c == '.') inicio--;
            else break;
        }

        if (inicio == texto.length()) return texto;

        if (inicio > 0 && texto.charAt(inicio - 1) == '-') {
            if (inicio == 1) return texto.substring(1);

            char anterior = texto.charAt(inicio - 2);

            if (anterior == '+' || anterior == '-' || anterior == 'X' || anterior == '/' || anterior == '(') {
                return texto.substring(0, inicio - 1) + texto.substring(inicio);
            }
        }

        return texto.substring(0, inicio) + "-" + texto.substring(inicio);
    }

    private static class Calculador {
        private final String texto;
        private int posicion = 0;

        Calculador(String texto) {
            this.texto = texto.replace(',', '.').replace('X', '*');
        }

        double analizar() {
            double resultado = expresion();

            if (posicion < texto.length()) throw new IllegalArgumentException();

            return resultado;
        }

        private double expresion() {
            double resultado = termino();

            while (posicion < texto.length()) {
                char operacion = texto.charAt(posicion);

                if (operacion == '+') {
                    posicion++;
                    resultado += termino();

                } else if (operacion == '-') {
                    posicion++;
                    resultado -= termino();

                } else break;
            }

            return resultado;
        }

        private double termino() {
            double resultado = factor();

            while (posicion < texto.length()) {
                char operacion = texto.charAt(posicion);

                if (operacion == '*') {
                    posicion++;
                    resultado *= factor();

                } else if (operacion == '/') {
                    posicion++;
                    double divisor = factor();

                    if (divisor == 0) throw new ArithmeticException();

                    resultado /= divisor;
                } else break;
            }

            return resultado;
        }

        private double factor() {

            if (posicion >= texto.length()) throw new IllegalArgumentException();

            char c = texto.charAt(posicion);

            if (c == '-') {
                posicion++;
                return -factor();
            }

            if (c == '+') {
                posicion++;
                return factor();
            }

            double resultado;

            if (c == '(') {
                posicion++;
                resultado = expresion();

                if (posicion >= texto.length() || texto.charAt(posicion) != ')') {
                    throw new IllegalArgumentException();
                }

                posicion++;

            } else resultado = numero();

            while (posicion < texto.length() && texto.charAt(posicion) == '%') {
                resultado /= 100;
                posicion++;
            }

            return resultado;
        }

        private double numero() {
            int inicio = posicion;

            while (posicion < texto.length()) {
                char c = texto.charAt(posicion);

                if (Character.isDigit(c) || c == '.') posicion++;
                else break;
            }

            if (inicio == posicion) throw new IllegalArgumentException();

            return Double.parseDouble(texto.substring(inicio, posicion));
        }
    }
}
