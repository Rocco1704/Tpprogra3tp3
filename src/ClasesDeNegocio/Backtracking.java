package ClasesDeNegocio;

import java.util.List;

public class Backtracking {

    private GestorDePersonas gestor;
    private Requerimiento requerimiento;
    private Equipo mejorEquipo;
    private int cantidadLlamadas;
    private int cantidadCasoBase;
    private long tiempoTotal;

    public Backtracking(GestorDePersonas gestor, Requerimiento requerimiento) {
        this.gestor = gestor;
        this.requerimiento = requerimiento;
        this.mejorEquipo = null;
        this.cantidadLlamadas = 0;
        this.cantidadCasoBase = 0;
        this.tiempoTotal = 0;
    }

    public Equipo resolver() {
        cantidadLlamadas = 0;
        cantidadCasoBase = 0;
        Equipo equipoActual = new Equipo();
        List<Persona> personas = gestor.getPersonas();

        long inicio = System.currentTimeMillis();
        backtrack(personas, equipoActual, 0);
        tiempoTotal = System.currentTimeMillis() - inicio;

        return mejorEquipo;
    }

    private void backtrack(List<Persona> personas, Equipo equipoActual, int indice) {
        cantidadLlamadas++;

        if (indice == personas.size()) {
            cantidadCasoBase++;
            if (esEquipoCompleto(equipoActual)) {
                if (mejorEquipo == null ||
                    equipoActual.getCalificacionTotal() > mejorEquipo.getCalificacionTotal()) {
                    mejorEquipo = copiarEquipo(equipoActual);
                }
            }
            return;
        }

        Persona candidata = personas.get(indice);

        if (puedeAgregar(candidata, equipoActual)) {
            equipoActual.agregarPersona(candidata);
            backtrack(personas, equipoActual, indice + 1);
            equipoActual.quitarPersona(candidata);
        }

        backtrack(personas, equipoActual, indice + 1);
    }

    private boolean puedeAgregar(Persona candidata, Equipo equipo) {
        if (equipo.cantidadPorRol(candidata.getRol()) >=
            requerimiento.getCantidadPorRol(candidata.getRol())) {
            return false;
        }

        for (Persona integrante : equipo.getIntegrantes()) {
            if (candidata.esIncompatibleCon(integrante)) {
                return false;
            }
        }

        return true;
    }

    private boolean esEquipoCompleto(Equipo equipo) {
        for (Rol rol : Rol.values()) {
            if (equipo.cantidadPorRol(rol) != requerimiento.getCantidadPorRol(rol)) {
                return false;
            }
        }
        return true;
    }

    private Equipo copiarEquipo(Equipo original) {
        Equipo copia = new Equipo();
        for (Persona p : original.getIntegrantes()) {
            copia.agregarPersona(p);
        }
        return copia;
    }

    public int getCantidadLlamadas() { return cantidadLlamadas; }
    public int getCantidadCasoBase() { return cantidadCasoBase; }
    public long getTiempoTotal() { return tiempoTotal; }
    public Equipo getMejorEquipo() { return mejorEquipo; }
}
