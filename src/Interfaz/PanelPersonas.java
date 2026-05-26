package Interfaz;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import ClasesDeNegocio.*;

public class PanelPersonas extends JPanel {

    private GestorDePersonas gestor;

    private JTextField campoNombre;
    private JComboBox<Rol> comboRol;
    private JSpinner spinnerCalificacion;
    private JTable tablaPersonas;
    private DefaultTableModel modeloTabla;

    public PanelPersonas(GestorDePersonas gestor) {
        this.gestor = gestor;
        setLayout(new BorderLayout());
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        // --- Panel de carga arriba ---
        JPanel panelCarga = new JPanel(new GridLayout(4, 2, 5, 5));
        panelCarga.setBorder(BorderFactory.createTitledBorder("Nueva Persona"));

        campoNombre = new JTextField();
        comboRol = new JComboBox<>(Rol.values());
        spinnerCalificacion = new JSpinner(new SpinnerNumberModel(3, 1, 5, 1));
        JButton botonAgregar = new JButton("Agregar Persona");

        panelCarga.add(new JLabel("Nombre:"));
        panelCarga.add(campoNombre);
        panelCarga.add(new JLabel("Rol:"));
        panelCarga.add(comboRol);
        panelCarga.add(new JLabel("Calificación (1-5):"));
        panelCarga.add(spinnerCalificacion);
        panelCarga.add(new JLabel(""));
        panelCarga.add(botonAgregar);

        // --- Tabla abajo ---
        String[] columnas = {"Nombre", "Rol", "Calificación"};
        modeloTabla = new DefaultTableModel(columnas, 0);
        tablaPersonas = new JTable(modeloTabla);
        JScrollPane scroll = new JScrollPane(tablaPersonas);
        scroll.setBorder(BorderFactory.createTitledBorder("Personas cargadas"));

        // --- Acción del botón ---
        botonAgregar.addActionListener(e -> agregarPersona());

        add(panelCarga, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
    }

    private void agregarPersona() {
        String nombre = campoNombre.getText().trim();
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingresá un nombre.");
            return;
        }

        Rol rol = (Rol) comboRol.getSelectedItem();
        int calificacion = (int) spinnerCalificacion.getValue();

        Persona nueva = new Persona(nombre, rol, calificacion);
        gestor.agregarPersona(nueva);

        modeloTabla.addRow(new Object[]{nombre, rol, calificacion});
        campoNombre.setText("");
    }

    public void actualizarTabla() {
        modeloTabla.setRowCount(0);
        for (Persona p : gestor.getPersonas()) {
            modeloTabla.addRow(new Object[]{p.getNombre(), p.getRol(), p.getCalificacion()});
        }
    }
}
