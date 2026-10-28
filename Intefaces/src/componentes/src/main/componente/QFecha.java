package componente;

import javax.swing.*;
import java.awt.*;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Objects;
import java.util.Random;
import java.util.stream.IntStream;

public class QFecha extends JPanel {
    private LocalDate fecha;
    private JComboBox<Integer> diaDat;
    private JComboBox<Integer> mesDat;
    private JTextField anioDat;
    private JLabel diaRes;
    private JLabel mesRes;
    private JLabel anioRes;
    private LocalDate inicio;
    private LocalDate fin;

    public QFecha() {
        fecha = LocalDate.of(1900, 1, 1);
    }

    public void establecerRangoFecha(LocalDate inicio, LocalDate fin) {
        long dias = ChronoUnit.DAYS.between(inicio, fin);
        Random random = new Random();
        fecha = inicio.plusDays(random.nextInt((int) dias));
        this.inicio = inicio;
        this.fin = fin;
    }

    public long diferenciaFecha(LocalDate inicio, LocalDate fin) { return ChronoUnit.DAYS.between(inicio, fin); }

    public boolean compararFecha(LocalDate fecha1,  LocalDate fecha2) { return fecha1.equals(fecha2); }

    public boolean fechaValida(int anio,  int mes, int dia) {
        try {
            LocalDate.of(anio, mes, dia);
        } catch (DateTimeException e) {
            return false;
        }
        return true;
    }

    public boolean fechaCorrecta(LocalDate fechaIntroducida){ return fecha.equals(fechaIntroducida); }

    public JPanel _panelIndice(){
        JLabel diaInd = new JLabel("Día", JLabel.CENTER);
        JLabel mesInd = new JLabel("Mes", JLabel.CENTER);
        JLabel anioInd = new JLabel("Año", JLabel.CENTER);

        JPanel panelInd = new JPanel(new GridLayout(1, 3));
        panelInd.add(diaInd);
        panelInd.add(mesInd);
        panelInd.add(anioInd);
        return panelInd;
    }

    public JPanel _panelDatos(){
        DefaultListCellRenderer renderizador = new DefaultListCellRenderer();
        diaDat = new JComboBox<>(IntStream.rangeClosed(1, 31).boxed().toArray(Integer[]::new));
        mesDat = new JComboBox<>(IntStream.rangeClosed(1, 12).boxed().toArray(Integer[]::new));
        anioDat = new JTextField(4);

        renderizador.setHorizontalAlignment(SwingConstants.CENTER);
        diaDat.setRenderer(renderizador);
        mesDat.setRenderer(renderizador);
        anioDat.setHorizontalAlignment(JTextField.CENTER);



        JPanel panelDat = new JPanel(new GridLayout(1, 3));
        panelDat.add(diaDat);
        panelDat.add(mesDat);
        panelDat.add(anioDat);
        return panelDat;
    }

    public JPanel _panelRespuesta(){
        diaRes = new JLabel("-", SwingConstants.CENTER);
        mesRes = new JLabel("-", SwingConstants.CENTER);
        anioRes = new JLabel("-", SwingConstants.CENTER);
        JPanel panelRes = new JPanel(new GridLayout(1, 3));

        panelRes.add(diaRes);
        panelRes.add(mesRes);
        panelRes.add(anioRes);
        return panelRes;
    }

    public QFecha get(){
        setLayout(new GridLayout(3, 1, 5, 5));
        add(_panelIndice());
        add(_panelDatos());
        add(_panelRespuesta());
        return this;
    }

    public boolean compararDatos(JFrame ventana){
        try {
            int dia = Integer.parseInt(Objects.requireNonNull(diaDat.getSelectedItem()).toString());
            int mes = Integer.parseInt(Objects.requireNonNull(mesDat.getSelectedItem()).toString());
            int anio = Integer.parseInt(anioDat.getText().trim());
            int diaCorrecto = fecha.getDayOfMonth();
            int mesCorrecto = fecha.getMonthValue();
            int anioCorrecto = fecha.getYear();

            LocalDate.of(anio, mes, dia);

            if (inicio.getYear() > anio || anio > fin.getYear()) {
                JOptionPane.showMessageDialog(ventana,
                        "Introduce un rango del " + inicio.getYear() + " al " + fin.getYear());
                return false;
            }

            if (dia == diaCorrecto) {
                diaRes.setText("Correcto");
                diaRes.setForeground(Color.GREEN);
            } else if (dia > diaCorrecto) {
                diaRes.setText("Demasiado alto");
                diaRes.setForeground(Color.RED);
            } else {
                diaRes.setText("Demasiado bajo");
                diaRes.setForeground(Color.BLUE);
            }

            if (mes == mesCorrecto) {
                mesRes.setText("Correcto");
                mesRes.setForeground(Color.GREEN);
            } else if (mes > mesCorrecto) {
                mesRes.setText("Demasiado alto");
                mesRes.setForeground(Color.RED);
            } else {
                mesRes.setText("Demasiado bajo");
                mesRes.setForeground(Color.BLUE);
            }

            if (anio == anioCorrecto) {
                anioRes.setText("Correcto");
                anioRes.setForeground(Color.GREEN);
            } else if (anio > anioCorrecto) {
                anioRes.setText("Demasiado alto");
                anioRes.setForeground(Color.RED);
            } else {
                anioRes.setText("Demasiado bajo");
                anioRes.setForeground(Color.BLUE);
            }

            if (dia == diaCorrecto
                    && mes == mesCorrecto
                    && anio == anioCorrecto) {

                JOptionPane.showMessageDialog(ventana,"¡Enhorabuena! Has acertado la fecha.");
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(ventana,"Introduce un número en el campo año.");
            return false;
        } catch (java.time.DateTimeException ex) {
            JOptionPane.showMessageDialog(ventana,"La fecha introducida no es válida.");
            return false;
        }
        return true;
    }

    public Date getFechaAsDate() throws QFechaException {
        try {
            int dia = Integer.parseInt(Objects.requireNonNull(diaDat.getSelectedItem()).toString());
            int mes = Integer.parseInt(Objects.requireNonNull(mesDat.getSelectedItem()).toString());
            int anio = Integer.parseInt(anioDat.getText().trim());

            LocalDate fecha = LocalDate.of(anio, mes, dia);

            return java.util.Date.from(fecha.atStartOfDay(java.time.ZoneId.systemDefault()).toInstant());

        } catch (NumberFormatException | java.time.DateTimeException ex) {
            throw new QFechaException("La fecha introducida está incompleta o no es válida.");
        }
    }

    public LocalDate getFecha() { return fecha; }


    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    public void setFecha(int anio, int mes, int dia) {
        try{
            this.fecha = LocalDate.of(anio, mes, dia);
        } catch (java.time.DateTimeException ex) {
            throw new QFechaException("La fecha introducida no es válida.");
        }
    }

}
