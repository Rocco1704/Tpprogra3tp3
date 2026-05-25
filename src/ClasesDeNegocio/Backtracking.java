package ClasesDeNegocio;

import java.util.List;

public class Backtracking {

    private GestorDePersonas gestor;
    private Requerimiento requerimiento;
    private Equipo mejorEquipo;
    private int cantidadLlamadas;

    public Backtracking(GestorDePersonas gestor, Requerimiento requerimiento) {
        this.gestor = gestor;
        this.requerimiento = requerimiento;
        this.mejorEquipo = null;
        this.cantidadLlamadas = 0;
    }

    public Equipo resolver() {
        Equipo equipoActual = new Equipo();
        List<Persona> personas = gestor.getPersonas();
        backtrack(personas, equipoActual, 0);
        return mejorEquipo;
    }

    private void backtrack(List<Persona> personas, Equipo equipoActual, int indice) {
        cantidadLlamadas++;

        // Caso base: llegamos al final de la lista
        if (indice == personas.size()) {
            if (esEquipoCompleto(equipoActual)) {
                if (mejorEquipo == null || 
                    equipoActual.getCalificacionTotal() > mejorEquipo.getCalificacionTotal()) {
                    mejorEquipo = copiarEquipo(equipoActual);
                }
            }
            return;
        }

        Persona candidata = personas.get(indice);

        // Rama 1: agrego a la persona si es válido
        if (puedeAgregar(candidata, equipoActual)) {
            equipoActual.agregarPersona(candidata);
            backtrack(personas, equipoActual, indice + 1);
            equipoActual.quitarPersona(candidata); // backtrack
        }

        // Rama 2: no agrego a la persona
        backtrack(personas, equipoActual, indice + 1);
    }

    private boolean puedeAgregar(Persona candidata, Equipo equipo) {
        // ¿Ya tenemos suficientes de ese rol?
        if (equipo.cantidadPorRol(candidata.getRol()) >= 
            requerimiento.getCantidadPorRol(candidata.getRol())) {
            return false;
        }

        // ¿Tiene incompatibilidad con alguien del equipo?
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
    public Equipo getMejorEquipo() { return mejorEquipo; }
}