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
    private JButton botonEliminar; // JButton, no JTextField

    public PanelPersonas(GestorDePersonas gestor) {
        this.gestor = gestor;
        setLayout(new BorderLayout());
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        // GridLayout de 5 filas porque ahora hay un botón más
        JPanel panelCarga = new JPanel(new GridLayout(5, 2, 5, 5));
        panelCarga.setBorder(BorderFactory.createTitledBorder("Nueva Persona"));

        campoNombre = new JTextField();
        comboRol = new JComboBox<>(Rol.values());
        spinnerCalificacion = new JSpinner(new SpinnerNumberModel(3, 1, 5, 1));
        JButton botonAgregar = new JButton("Agregar Persona");
        botonEliminar = new JButton("Eliminar Persona"); // inicializar acá

        panelCarga.add(new JLabel("Nombre:"));
        panelCarga.add(campoNombre);
        panelCarga.add(new JLabel("Rol:"));
        panelCarga.add(comboRol);
        panelCarga.add(new JLabel("Calificación (1-5):"));
        panelCarga.add(spinnerCalificacion);
        panelCarga.add(new JLabel(""));
        panelCarga.add(botonAgregar);
        panelCarga.add(new JLabel(""));
        panelCarga.add(botonEliminar);

        // --- Tabla abajo ---
        String[] columnas = {"Nombre", "Rol", "Calificación"};
        modeloTabla = new DefaultTableModel(columnas, 0);
        tablaPersonas = new JTable(modeloTabla);
        JScrollPane scroll = new JScrollPane(tablaPersonas);
        scroll.setBorder(BorderFactory.createTitledBorder("Personas cargadas"));

        // --- Acciones ---
        botonAgregar.addActionListener(e -> agregarPersona());
        botonEliminar.addActionListener(e -> eliminarPersona());

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

    private void eliminarPersona() {
        int filaSeleccionada = tablaPersonas.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccioná una persona para eliminar.");
            return;
        }
        Persona persona = gestor.getPersonas().get(filaSeleccionada);
        gestor.eliminarPersona(persona);
        modeloTabla.removeRow(filaSeleccionada);
    }

    public void actualizarTabla() {
        modeloTabla.setRowCount(0);
        for (Persona p : gestor.getPersonas()) {
            modeloTabla.addRow(new Object[]{p.getNombre(), p.getRol(), p.getCalificacion()});
        }
    }
}