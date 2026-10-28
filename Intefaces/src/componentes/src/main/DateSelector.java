
import javax.swing.*;
import java.awt.*;
import java.time.YearMonth;
import java.time.LocalDate;
import java.time.Month;
import java.util.stream.IntStream;

public class DateSelector extends JPanel {

    private final JComboBox<Integer> dia;
    private final JComboBox<String> mes;
    private final JSpinner anio;

    public DateSelector() {
        setLayout(new FlowLayout(FlowLayout.LEFT, 8, 5));

        dia = new JComboBox<>(
                IntStream.rangeClosed(1, 31).boxed()
                        .toArray(Integer[]::new)
        );

        mes = new JComboBox<>(
                new String[] {
                        "Enero", "Febrero", "Marzo", "Abril",
                        "Mayo", "Junio", "Julio", "Agosto",
                        "Septiembre", "Octubre", "Noviembre", "Diciembre"
                }
        );

        anio = new JSpinner(
                new SpinnerNumberModel(2026, 1900, 2100, 1)
        );

        add(new JLabel("Día:"));
        add(dia);
        add(new JLabel("Mes:"));
        add(mes);
        add(new JLabel("Año:"));
        add(anio);

        dia.setSelectedItem(1);

        mes.addActionListener(e -> actualizarDias());
        anio.addChangeListener(e -> actualizarDias());
    }

    private void actualizarDias() {
        int mesSeleccionado = mes.getSelectedIndex() + 1;
        int anioSeleccionado = (int) anio.getValue();
        int diaSeleccionado = (int) dia.getSelectedItem();

        int maxDias = YearMonth.of(
                anioSeleccionado, mesSeleccionado
        ).lengthOfMonth();

        dia.removeAllItems();

        for (int i = 1; i <= maxDias; i++) {
            dia.addItem(i);
        }

        dia.setSelectedItem(Math.min(diaSeleccionado, maxDias));
    }

    public LocalDate getFecha() {
        return LocalDate.of(
                (int) anio.getValue(),
                mes.getSelectedIndex() + 1,
                (int) dia.getSelectedItem()
        );
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Selector de fecha");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            DateSelector selector = new DateSelector();
            frame.add(selector, BorderLayout.CENTER);

            JButton aceptar = new JButton("Obtener fecha");
            aceptar.addActionListener(e ->
                    JOptionPane.showMessageDialog(
                            frame, selector.getFecha().toString()
                    )
            );

            frame.add(aceptar, BorderLayout.SOUTH);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
