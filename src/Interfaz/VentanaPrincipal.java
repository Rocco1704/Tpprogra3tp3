package Interfaz;

import javax.swing.*;
import ClasesDeNegocio.*;

public class VentanaPrincipal extends JFrame {

    private GestorDePersonas gestor;
    private Requerimiento requerimiento;

    private PanelPersonas panelPersonas;
    private PanelIncompatibilidades panelIncompatibilidades;
    private PanelRequerimientos panelRequerimientos;
    private PanelResultado panelResultado;

    public VentanaPrincipal() {
        gestor = new GestorDePersonas();
        requerimiento = new Requerimiento(0, 0, 0, 0);

        setTitle("Equipo Ideal");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // centra la ventana

        inicializarPaneles();
    }

    private void inicializarPaneles() {
        panelPersonas = new PanelPersonas(gestor);
        panelIncompatibilidades = new PanelIncompatibilidades(gestor);
        panelRequerimientos = new PanelRequerimientos();
        panelResultado = new PanelResultado(gestor, panelRequerimientos);

        JTabbedPane pestañas = new JTabbedPane();
        pestañas.addTab("Personas", panelPersonas);
        pestañas.addTab("Incompatibilidades", panelIncompatibilidades);
        pestañas.addTab("Requerimientos", panelRequerimientos);
        pestañas.addTab("Resultado", panelResultado);
        pestañas.addChangeListener(e -> {
            panelIncompatibilidades.actualizarCombos();
        });
        add(pestañas);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new VentanaPrincipal().setVisible(true);
            
        });
    }
}