package Interfaz;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import ClasesDeNegocio.*;

public class PanelResultado extends JPanel {

    private GestorDePersonas gestor;
    private PanelRequerimientos panelRequerimientos;

    private JButton botonResolver;
    private JLabel labelCalificacion;
    private DefaultTableModel modeloTabla;
    private JLabel labelEstado;

    public PanelResultado(GestorDePersonas gestor, PanelRequerimientos panelRequerimientos) {
        this.gestor = gestor;
        this.panelRequerimientos = panelRequerimientos;
        setLayout(new BorderLayout());
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        // --- Botón arriba ---
        JPanel panelBoton = new JPanel(new FlowLayout());
        botonResolver = new JButton("Resolver");
        labelEstado = new JLabel("Presioná Resolver para encontrar el equipo ideal.");
        panelBoton.add(botonResolver);
        panelBoton.add(labelEstado);

        // --- Tabla en el centro ---
        String[] columnas = {"Nombre", "Rol", "Calificación"};
        modeloTabla = new DefaultTableModel(columnas, 0);
        JTable tabla = new JTable(modeloTabla);
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createTitledBorder("Equipo resultante"));

        // --- Calificación total abajo ---
        JPanel panelInfo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        labelCalificacion = new JLabel("Calificación total: -");
        panelInfo.add(labelCalificacion);

        // --- Acción del botón ---
        botonResolver.addActionListener(e -> resolver());

        add(panelBoton, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(panelInfo, BorderLayout.SOUTH);
    }

    private void resolver() {
        if (gestor.getPersonas().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay personas cargadas.");
            return;
        }

        botonResolver.setEnabled(false);
        labelEstado.setText("Calculando...");
        modeloTabla.setRowCount(0);

        Requerimiento requerimiento = panelRequerimientos.getRequerimiento();

        // SwingWorker para correr en thread separado
        SwingWorker<Equipo, Void> worker = new SwingWorker<>() {
            @Override
            protected Equipo doInBackground() {
                Backtracking bt = new Backtracking(gestor, requerimiento);
                return bt.resolver();
            }

            @Override
            protected void done() {
                try {
                    Equipo resultado = get();
                    if (resultado == null) {
                        labelEstado.setText("No se encontró equipo posible.");
                    } else {
                        labelEstado.setText("Equipo encontrado.");
                        for (Persona p : resultado.getIntegrantes()) {
                            modeloTabla.addRow(new Object[]{
                                p.getNombre(), p.getRol(), p.getCalificacion()
                            });
                        }
                        labelCalificacion.setText("Calificación total: " + resultado.getCalificacionTotal());
                    }
                } catch (Exception ex) {
                    labelEstado.setText("Error al resolver.");
                }
                botonResolver.setEnabled(true);
            }
        };

        worker.execute();
    }
}