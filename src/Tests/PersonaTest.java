package Tests;

import ClasesDeNegocio.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PersonaTest {

    @Test
    public void testEsIncompatibleCon() {
        Persona p1 = new Persona("Ana", Rol.LIDER_DE_PROYECTO, 5);
        Persona p2 = new Persona("Carlos", Rol.ARQUITECTO, 4);
        p1.agregarIncompatible(p2);
        assertTrue(p1.esIncompatibleCon(p2));
        assertFalse(p2.esIncompatibleCon(p1)); // no es simétrica sola
    }

    @Test
    public void testGetters() {
        Persona p = new Persona("Luis", Rol.TESTER, 3);
        assertEquals("Luis", p.getNombre());
        assertEquals(Rol.TESTER, p.getRol());
        assertEquals(3, p.getCalificacion());
    }
}