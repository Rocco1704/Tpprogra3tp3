package Interfaz;

import javax.swing.*;
import java.awt.*;
import ClasesDeNegocio.*;

public class PanelRequerimientos extends JPanel {

    private JSpinner spinnerLideres;
    private JSpinner spinnerArquitectos;
    private JSpinner spinnerProgramadores;
    private JSpinner spinnerTesters;

    public PanelRequerimientos() {
        setLayout(new BorderLayout());
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        JPanel panelSpinners = new JPanel(new GridLayout(4, 2, 10, 10));
        panelSpinners.setBorder(BorderFactory.createTitledBorder("Cantidad requerida por rol"));

        spinnerLideres = new JSpinner(new SpinnerNumberModel(1, 0, 20, 1));
        spinnerArquitectos = new JSpinner(new SpinnerNumberModel(1, 0, 20, 1));
        spinnerProgramadores = new JSpinner(new SpinnerNumberModel(1, 0, 20, 1));
        spinnerTesters = new JSpinner(new SpinnerNumberModel(1, 0, 20, 1));

        panelSpinners.add(new JLabel("Líderes de proyecto:"));
        panelSpinners.add(spinnerLideres);
        panelSpinners.add(new JLabel("Arquitectos:"));
        panelSpinners.add(spinnerArquitectos);
        panelSpinners.add(new JLabel("Programadores:"));
        panelSpinners.add(spinnerProgramadores);
        panelSpinners.add(new JLabel("Testers:"));
        panelSpinners.add(spinnerTesters);

        add(panelSpinners, BorderLayout.NORTH);
    }

    public Requerimiento getRequerimiento() {
        return new Requerimiento(
            (int) spinnerLideres.getValue(),
            (int) spinnerArquitectos.getValue(),
            (int) spinnerProgramadores.getValue(),
            (int) spinnerTesters.getValue()
        );
    }
}