package Tests;

import ClasesDeNegocio.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RequerimientoTest {

    @Test
    public void testGetCantidadPorRol() {
        Requerimiento r = new Requerimiento(1, 2, 3, 4);
        assertEquals(1, r.getCantidadPorRol(Rol.LIDER_DE_PROYECTO));
        assertEquals(2, r.getCantidadPorRol(Rol.ARQUITECTO));
        assertEquals(3, r.getCantidadPorRol(Rol.PROGRAMADOR));
        assertEquals(4, r.getCantidadPorRol(Rol.TESTER));
    }

    @Test
    public void testGetTotalPersonas() {
        Requerimiento r = new Requerimiento(1, 2, 3, 4);
        assertEquals(10, r.getTotalPersonas());
    }
}