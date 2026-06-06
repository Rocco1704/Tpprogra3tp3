package Tests;

import ClasesDeNegocio.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

public class GestorDePersonasTest {

    @Test
    public void testAgregarYContarPersonas() {
        GestorDePersonas gestor = new GestorDePersonas();
        gestor.agregarPersona(new Persona("Ana", Rol.LIDER_DE_PROYECTO, 5));
        assertEquals(1, gestor.cantidadPersonas());
    }

    @Test
    public void testEliminarPersona() {
        GestorDePersonas gestor = new GestorDePersonas();
        Persona p = new Persona("Ana", Rol.LIDER_DE_PROYECTO, 5);
        gestor.agregarPersona(p);
        gestor.eliminarPersona(p);
        assertEquals(0, gestor.cantidadPersonas());
    }

    @Test
    public void testGetPersonasPorRol() {
        GestorDePersonas gestor = new GestorDePersonas();
        gestor.agregarPersona(new Persona("Ana", Rol.PROGRAMADOR, 4));
        gestor.agregarPersona(new Persona("Luis", Rol.TESTER, 3));
        List<Persona> programadores = gestor.getPersonasPorRol(Rol.PROGRAMADOR);
        assertEquals(1, programadores.size());
        assertEquals("Ana", programadores.get(0).getNombre());
    }

    @Test
    public void testIncompatibilidadEsSimetrica() {
        GestorDePersonas gestor = new GestorDePersonas();
        Persona p1 = new Persona("Ana", Rol.LIDER_DE_PROYECTO, 5);
        Persona p2 = new Persona("Carlos", Rol.ARQUITECTO, 4);
        gestor.agregarIncompatibilidad(p1, p2);
        assertTrue(p1.esIncompatibleCon(p2));
        assertTrue(p2.esIncompatibleCon(p1)); // debe ser simétrica
    }
}