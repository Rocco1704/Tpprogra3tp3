package ClasesDeNegocio;

import java.util.ArrayList;
import java.util.List;

public class GestorDePersonas {

    private List<Persona> personas;

    public GestorDePersonas() {
        this.personas = new ArrayList<>();
    }

    public void agregarPersona(Persona persona) {
        personas.add(persona);
    }

    public void agregarIncompatibilidad(Persona persona1, Persona persona2) {
        persona1.agregarIncompatible(persona2);
        persona2.agregarIncompatible(persona1);
    }

    public void eliminarPersona(Persona persona) {
        personas.remove(persona);
    }

    public List<Persona> getPersonas() {
        return personas;
    }

    public List<Persona> getPersonasPorRol(Rol rol) {
        List<Persona> resultado = new ArrayList<>();
        for (Persona p : personas) {
            if (p.getRol() == rol) {
                resultado.add(p);
            }
        }
        return resultado;
    }

    public int cantidadPersonas() {
        return personas.size();
    }
}