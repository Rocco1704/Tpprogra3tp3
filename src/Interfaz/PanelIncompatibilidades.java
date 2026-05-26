package Interfaz;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import ClasesDeNegocio.*;

public class PanelIncompatibilidades extends JPanel {

    private GestorDePersonas gestor;

    private JComboBox<Persona> comboPersona1;
    private JComboBox<Persona> comboPersona2;
    private DefaultTableModel modeloTabla;

    public PanelIncompatibilidades(GestorDePersonas gestor) {
        this.gestor = gestor;
        setLayout(new BorderLayout());
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        // --- Panel de carga arriba ---
        JPanel panelCarga = new JPanel(new GridLayout(3, 2, 5, 5));
        panelCarga.setBorder(BorderFactory.createTitledBorder("Nueva Incompatibilidad"));

        comboPersona1 = new JComboBox<>();
        comboPersona2 = new JComboBox<>();
        JButton botonAgregar = new JButton("Agregar Incompatibilidad");

        panelCarga.add(new JLabel("Persona 1:"));
        panelCarga.add(comboPersona1);
        panelCarga.add(new JLabel("Persona 2:"));
        panelCarga.add(comboPersona2);
        panelCarga.add(new JLabel(""));
        panelCarga.add(botonAgregar);

        // --- Tabla abajo ---
        String[] columnas = {"Persona 1", "Persona 2"};
        modeloTabla = new DefaultTableModel(columnas, 0);
        JTable tabla = new JTable(modeloTabla);
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createTitledBorder("Incompatibilidades cargadas"));

        // --- Acción del botón ---
        botonAgregar.addActionListener(e -> agregarIncompatibilidad());

        add(panelCarga, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
    }

    private void agregarIncompatibilidad() {
        Persona p1 = (Persona) comboPersona1.getSelectedItem();
        Persona p2 = (Persona) comboPersona2.getSelectedItem();

        if (p1 == null || p2 == null) {
            JOptionPane.showMessageDialog(this, "No hay personas cargadas.");
            return;
        }
        if (p1 == p2) {
            JOptionPane.showMessageDialog(this, "Seleccioná dos personas distintas.");
            return;
        }

        gestor.agregarIncompatibilidad(p1, p2);
        modeloTabla.addRow(new Object[]{p1.getNombre(), p2.getNombre()});
    }

    public void actualizarCombos() {
        comboPersona1.removeAllItems();
        comboPersona2.removeAllItems();
        for (Persona p : gestor.getPersonas()) {
            comboPersona1.addItem(p);
            comboPersona2.addItem(p);
        }
    }
}
