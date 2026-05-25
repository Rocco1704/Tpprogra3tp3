package ClasesDeNegocio;

import java.util.ArrayList;
import java.util.List;

public class Equipo {

    private List<Persona> integrantes;

    public Equipo() {
        this.integrantes = new ArrayList<>();
    }

    public void agregarPersona(Persona persona) {
        integrantes.add(persona);
    }

    public void quitarPersona(Persona persona) {
        integrantes.remove(persona);
    }

    public int getCalificacionTotal() {
        int total = 0;
        for (Persona p : integrantes) {
            total += p.getCalificacion();
        }
        return total;
    }

    public double getCalificacionPromedio() {
        if (integrantes.isEmpty()) return 0;
        return (double) getCalificacionTotal() / integrantes.size();
    }

    public int cantidadPorRol(Rol rol) {
        int count = 0;
        for (Persona p : integrantes) {
            if (p.getRol() == rol) count++;
        }
        return count;
    }

    public boolean tieneIncompatibilidades() {
        for (Persona p : integrantes) {
            for (Persona otro : integrantes) {
                if (p != otro && p.esIncompatibleCon(otro)) {
                    return true;
                }
            }
        }
        return false;
    }

    public List<Persona> getIntegrantes() {
        return integrantes;
    }

    public int size() {
        return integrantes.size();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Persona p : integrantes) {
            sb.append(p.toString()).append("\n");
        }
        sb.append("Promedio: ").append(getCalificacionPromedio());
        return sb.toString();
    }
}
