package ClasesDeNegocio;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Heuristica {

    private GestorDePersonas gestor;
    private Requerimiento requerimiento;
    private Equipo equipoResultado;
    private int cantidadIteraciones;
    private long tiempoTotal;

    public Heuristica(GestorDePersonas gestor, Requerimiento requerimiento) {
        this.gestor = gestor;
        this.requerimiento = requerimiento;
        this.equipoResultado = null;
        this.cantidadIteraciones = 0;
        this.tiempoTotal = 0;
    }

    public Equipo resolver() {
        cantidadIteraciones = 0;
        Equipo equipo = new Equipo();

        long inicio = System.currentTimeMillis();

        for (Rol rol : Rol.values()) {
            int cantidad = requerimiento.getCantidadPorRol(rol);

            // Candidatos del rol, ordenados de mayor a menor calificación
            List<Persona> candidatos = new ArrayList<>(gestor.getPersonasPorRol(rol));
            candidatos.sort(Comparator.comparingInt(Persona::getCalificacion).reversed());

            int asignados = 0;
            for (Persona candidato : candidatos) {
                cantidadIteraciones++;

                if (asignados == cantidad) break;

                if (esCompatibleConEquipo(candidato, equipo)) {
                    equipo.agregarPersona(candidato);
                    asignados++;
                }
            }

            // Si no se pudo cubrir el rol, no hay solución
            if (asignados < cantidad) {
                tiempoTotal = System.currentTimeMillis() - inicio;
                equipoResultado = null;
                return null;
            }
        }

        tiempoTotal = System.currentTimeMillis() - inicio;
        equipoResultado = equipo;
        return equipoResultado;
    }

    private boolean esCompatibleConEquipo(Persona candidato, Equipo equipo) {
        for (Persona integrante : equipo.getIntegrantes()) {
            if (candidato.esIncompatibleCon(integrante)) {
                return false;
            }
        }
        return true;
    }

    public int getCantidadIteraciones() { return cantidadIteraciones; }
    public long getTiempoTotal()        { return tiempoTotal;          }
    public Equipo getEquipoResultado()  { return equipoResultado;      }
}
