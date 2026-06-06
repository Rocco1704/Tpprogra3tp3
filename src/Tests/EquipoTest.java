package Tests;

import ClasesDeNegocio.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EquipoTest {

    @Test
    public void testAgregarYQuitarPersona() {
        Equipo equipo = new Equipo();
        Persona p = new Persona("Ana", Rol.LIDER_DE_PROYECTO, 5);
        equipo.agregarPersona(p);
        assertEquals(1, equipo.size());
        equipo.quitarPersona(p);
        assertEquals(0, equipo.size());
    }

    @Test
    public void testCalificacionTotal() {
        Equipo equipo = new Equipo();
        equipo.agregarPersona(new Persona("Ana", Rol.LIDER_DE_PROYECTO, 5));
        equipo.agregarPersona(new Persona("Carlos", Rol.ARQUITECTO, 3));
        assertEquals(8, equipo.getCalificacionTotal());
    }

    @Test
    public void testCalificacionPromedio() {
        Equipo equipo = new Equipo();
        equipo.agregarPersona(new Persona("Ana", Rol.LIDER_DE_PROYECTO, 4));
        equipo.agregarPersona(new Persona("Carlos", Rol.ARQUITECTO, 2));
        assertEquals(3.0, equipo.getCalificacionPromedio(), 0.001);
    }

    @Test
    public void testCantidadPorRol() {
        Equipo equipo = new Equipo();
        equipo.agregarPersona(new Persona("Ana", Rol.PROGRAMADOR, 4));
        equipo.agregarPersona(new Persona("Luis", Rol.PROGRAMADOR, 3));
        assertEquals(2, equipo.cantidadPorRol(Rol.PROGRAMADOR));
        assertEquals(0, equipo.cantidadPorRol(Rol.TESTER));
    }

    @Test
    public void testTieneIncompatibilidades() {
        Equipo equipo = new Equipo();
        Persona p1 = new Persona("Ana", Rol.LIDER_DE_PROYECTO, 5);
        Persona p2 = new Persona("Carlos", Rol.ARQUITECTO, 4);
        p1.agregarIncompatible(p2);
        p2.agregarIncompatible(p1);
        equipo.agregarPersona(p1);
        equipo.agregarPersona(p2);
        assertTrue(equipo.tieneIncompatibilidades());
    }

    @Test
    public void testEquipoVacioPromedioEsCero() {
        Equipo equipo = new Equipo();
        assertEquals(0, equipo.getCalificacionPromedio(), 0.001);
    }
}