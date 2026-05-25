package ClasesDeNegocio;

import java.util.HashSet;
import java.util.Set;

public class Persona {

    private String nombre;
    private Rol rol;
    private int calificacion;
    private Set<Persona> incompatibles;

    public Persona(String nombre, Rol rol, int calificacion) {
        this.nombre = nombre;
        this.rol = rol;
        this.calificacion = calificacion;
        this.incompatibles = new HashSet<>();
    }

    public void agregarIncompatible(Persona persona) {
        incompatibles.add(persona);
    }

    public boolean esIncompatibleCon(Persona persona) {
        return incompatibles.contains(persona);
    }

    // getters
    public String getNombre() { return nombre; }
    public Rol getRol() { return rol; }
    public int getCalificacion() { return calificacion; }
    public Set<Persona> getIncompatibles() { return incompatibles; }

    @Override
    public String toString() {
        return nombre + " (" + rol + ") - Calif: " + calificacion;
    }
}
