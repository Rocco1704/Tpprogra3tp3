package Interfaz;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import ClasesDeNegocio.*;

public class PanelResultado extends JPanel {

    private GestorDePersonas gestor;
    private PanelRequerimientos panelRequerimientos;

    private JButton botonResolver;
    private JLabel labelEstado;

    // --- Backtracking ---
    private DefaultTableModel modeloBT;
    private JLabel labelCalBT;
    private JLabel labelTiempoBT;
    private JLabel labelLlamadasBT;
    private JLabel labelCasoBaseBT;

    // --- Heurística ---
    private DefaultTableModel modeloH;
    private JLabel labelCalH;
    private JLabel labelTiempoH;
    private JLabel labelIterH;

    // --- Comparación ---
    private JLabel labelComparacion;

    public PanelResultado(GestorDePersonas gestor, PanelRequerimientos panelRequerimientos) {
        this.gestor = gestor;
        this.panelRequerimientos = panelRequerimientos;
        setLayout(new BorderLayout(5, 5));
        inicializarComponentes();
    }

    private void inicializarComponentes() {

        // ── Botón superior ──────────────────────────────────────────────────
        JPanel panelBoton = new JPanel(new FlowLayout());
        botonResolver = new JButton("Resolver");
        labelEstado = new JLabel("Presioná Resolver para encontrar el equipo ideal.");
        panelBoton.add(botonResolver);
        panelBoton.add(labelEstado);

        // ── Panel Backtracking ───────────────────────────────────────────────
        String[] columnas = {"Nombre", "Rol", "Calificación"};

        modeloBT = new DefaultTableModel(columnas, 0);
        JTable tablaBT = new JTable(modeloBT);
        JScrollPane scrollBT = new JScrollPane(tablaBT);
        scrollBT.setBorder(BorderFactory.createTitledBorder("Backtracking (solución óptima)"));

        labelCalBT      = new JLabel("Calificación total: -");
        labelTiempoBT   = new JLabel("Tiempo: -");
        labelLlamadasBT = new JLabel("Llamadas recursivas: -");
        labelCasoBaseBT = new JLabel("Casos base evaluados: -");

        JPanel infoBT = new JPanel(new GridLayout(4, 1, 2, 2));
        infoBT.add(labelCalBT);
        infoBT.add(labelTiempoBT);
        infoBT.add(labelLlamadasBT);
        infoBT.add(labelCasoBaseBT);

        JPanel panelBT = new JPanel(new BorderLayout(3, 3));
        panelBT.add(scrollBT, BorderLayout.CENTER);
        panelBT.add(infoBT,   BorderLayout.SOUTH);

        // ── Panel Heurística ─────────────────────────────────────────────────
        modeloH = new DefaultTableModel(columnas, 0);
        JTable tablaH = new JTable(modeloH);
        JScrollPane scrollH = new JScrollPane(tablaH);
        scrollH.setBorder(BorderFactory.createTitledBorder("Heurística greedy (solución aproximada)"));

        labelCalH    = new JLabel("Calificación total: -");
        labelTiempoH = new JLabel("Tiempo: -");
        labelIterH   = new JLabel("Iteraciones: -");

        JPanel infoH = new JPanel(new GridLayout(3, 1, 2, 2));
        infoH.add(labelCalH);
        infoH.add(labelTiempoH);
        infoH.add(labelIterH);

        JPanel panelH = new JPanel(new BorderLayout(3, 3));
        panelH.add(scrollH, BorderLayout.CENTER);
        panelH.add(infoH,   BorderLayout.SOUTH);

        // ── Comparación ──────────────────────────────────────────────────────
        labelComparacion = new JLabel("  Comparación: -", SwingConstants.CENTER);
        labelComparacion.setFont(labelComparacion.getFont().deriveFont(Font.BOLD, 12f));
        labelComparacion.setBorder(BorderFactory.createEmptyBorder(4, 0, 4, 0));

        // ── Centro: dos paneles lado a lado ──────────────────────────────────
        JPanel panelCentro = new JPanel(new GridLayout(1, 2, 8, 0));
        panelCentro.add(panelBT);
        panelCentro.add(panelH);

        // ── Acción ───────────────────────────────────────────────────────────
        botonResolver.addActionListener(e -> resolver());

        add(panelBoton,      BorderLayout.NORTH);
        add(panelCentro,     BorderLayout.CENTER);
        add(labelComparacion, BorderLayout.SOUTH);
    }

    private void resolver() {
        if (gestor.getPersonas().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay personas cargadas.");
            return;
        }

        botonResolver.setEnabled(false);
        labelEstado.setText("Calculando...");
        modeloBT.setRowCount(0);
        modeloH.setRowCount(0);

        Requerimiento requerimiento = panelRequerimientos.getRequerimiento();

        final Backtracking[] btRef = new Backtracking[1];
        final Heuristica[]   hRef  = new Heuristica[1];

        SwingWorker<Equipo[], Void> worker = new SwingWorker<>() {
            @Override
            protected Equipo[] doInBackground() {
                btRef[0] = new Backtracking(gestor, requerimiento);
                hRef[0]  = new Heuristica(gestor, requerimiento);
                Equipo equipoBT = btRef[0].resolver();
                Equipo equipoH  = hRef[0].resolver();
                return new Equipo[]{equipoBT, equipoH};
            }

            @Override
            protected void done() {
                try {
                    Equipo[] resultados = get();
                    Equipo equipoBT = resultados[0];
                    Equipo equipoH  = resultados[1];

                    // ── Mostrar resultado Backtracking ──
                    if (equipoBT == null) {
                        labelEstado.setText("No se encontró equipo posible.");
                        labelCalBT.setText("Calificación total: sin solución");
                    } else {
                        labelEstado.setText("Listo.");
                        for (Persona p : equipoBT.getIntegrantes()) {
                            modeloBT.addRow(new Object[]{
                                p.getNombre(), p.getRol(), p.getCalificacion()
                            });
                        }
                        labelCalBT.setText("Calificación total: " + equipoBT.getCalificacionTotal());
                    }
                    labelTiempoBT.setText("Tiempo: "              + btRef[0].getTiempoTotal()      + " ms");
                    labelLlamadasBT.setText("Llamadas recursivas: " + btRef[0].getCantidadLlamadas());
                    labelCasoBaseBT.setText("Casos base evaluados: " + btRef[0].getCantidadCasoBase());

                    // ── Mostrar resultado Heurística ──
                    if (equipoH == null) {
                        labelCalH.setText("Calificación total: sin solución");
                    } else {
                        for (Persona p : equipoH.getIntegrantes()) {
                            modeloH.addRow(new Object[]{
                                p.getNombre(), p.getRol(), p.getCalificacion()
                            });
                        }
                        labelCalH.setText("Calificación total: " + equipoH.getCalificacionTotal());
                    }
                    labelTiempoH.setText("Tiempo: "      + hRef[0].getTiempoTotal()         + " ms");
                    labelIterH.setText("Iteraciones: "   + hRef[0].getCantidadIteraciones());

                    // ── Comparación ──
                    mostrarComparacion(equipoBT, equipoH, btRef[0], hRef[0]);

                } catch (Exception ex) {
                    labelEstado.setText("Error al resolver.");
                }
                botonResolver.setEnabled(true);
            }
        };

        worker.execute();
    }

    private void mostrarComparacion(Equipo equipoBT, Equipo equipoH,
                                    Backtracking bt, Heuristica h) {
        if (equipoBT == null && equipoH == null) {
            labelComparacion.setText("  Ambos algoritmos: sin solución posible.");
            labelComparacion.setForeground(Color.RED);
            return;
        }
        if (equipoBT == null || equipoH == null) {
            labelComparacion.setText("  Un algoritmo encontró solución y el otro no.");
            labelComparacion.setForeground(Color.ORANGE);
            return;
        }

        int calBT = equipoBT.getCalificacionTotal();
        int calH  = equipoH.getCalificacionTotal();
        long diffTiempo = bt.getTiempoTotal() - h.getTiempoTotal();

        String calidadMsg;
        if (calBT == calH) {
            calidadMsg = "Ambos encontraron la misma calificación (" + calBT + ").";
            labelComparacion.setForeground(new Color(0, 128, 0));
        } else {
            calidadMsg = "Backtracking obtuvo " + calBT + " vs Heurística " + calH
                       + " (diferencia: " + (calBT - calH) + ").";
            labelComparacion.setForeground(new Color(180, 100, 0));
        }

        String eficienciaMsg = "BT usó " + bt.getCantidadLlamadas()
                             + " llamadas vs " + h.getCantidadIteraciones()
                             + " iteraciones de la heurística.";

        labelComparacion.setText("  " + calidadMsg + "  |  " + eficienciaMsg);
    }
}
