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
    private JLabel labelTiempo;
    private JLabel labelLlamadas;
    private JLabel labelCasoBase;
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

        // --- Info abajo ---
        JPanel panelInfo = new JPanel(new GridLayout(4, 1, 2, 2));
        labelCalificacion = new JLabel("Calificación total: -");
        labelTiempo = new JLabel("Tiempo: -");
        labelLlamadas = new JLabel("Llamadas recursivas: -");
        labelCasoBase = new JLabel("Casos base evaluados: -");

        panelInfo.add(labelCalificacion);
        panelInfo.add(labelTiempo);
        panelInfo.add(labelLlamadas);
        panelInfo.add(labelCasoBase);

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

        final Backtracking[] btRef = new Backtracking[1];

        SwingWorker<Equipo, Void> worker = new SwingWorker<>() {
            @Override
            protected Equipo doInBackground() {
                btRef[0] = new Backtracking(gestor, requerimiento);
                return btRef[0].resolver();
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

                    // Estadísticas siempre se muestran
                    labelTiempo.setText("Tiempo: " + btRef[0].getTiempoTotal() + " ms");
                    labelLlamadas.setText("Llamadas recursivas: " + btRef[0].getCantidadLlamadas());
                    labelCasoBase.setText("Casos base evaluados: " + btRef[0].getCantidadCasoBase());

                } catch (Exception ex) {
                    labelEstado.setText("Error al resolver.");
                }
                botonResolver.setEnabled(true);
            }
        };

        worker.execute();
    }
}