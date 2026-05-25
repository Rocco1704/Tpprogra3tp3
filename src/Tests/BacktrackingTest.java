package Tests;

import ClasesDeNegocio.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BacktrackingTest {

    private GestorDePersonas gestor;
    private Requerimiento requerimiento;

    @BeforeEach
    public void setUp() {
        gestor = new GestorDePersonas();

        gestor.agregarPersona(new Persona("Ana", Rol.LIDER_DE_PROYECTO, 5));
        gestor.agregarPersona(new Persona("Carlos", Rol.ARQUITECTO, 4));
        gestor.agregarPersona(new Persona("Maria", Rol.PROGRAMADOR, 3));
        gestor.agregarPersona(new Persona("Luis", Rol.TESTER, 4));
        gestor.agregarPersona(new Persona("Pedro", Rol.TESTER, 2));

        requerimiento = new Requerimiento(1, 1, 1, 1);
    }

    @Test
    public void testEncuentraEquipoValido() {
        Backtracking bt = new Backtracking(gestor, requerimiento);
        Equipo resultado = bt.resolver();

        assertNotNull(resultado, "Debe encontrar un equipo");
        assertEquals(4, resultado.size());
    }

    @Test
    public void testEquipoTieneRolesCorrectos() {
        Backtracking bt = new Backtracking(gestor, requerimiento);
        Equipo resultado = bt.resolver();

        assertEquals(1, resultado.cantidadPorRol(Rol.LIDER_DE_PROYECTO));
        assertEquals(1, resultado.cantidadPorRol(Rol.ARQUITECTO));
        assertEquals(1, resultado.cantidadPorRol(Rol.PROGRAMADOR));
        assertEquals(1, resultado.cantidadPorRol(Rol.TESTER));
    }

    @Test
    public void testEligeMejorCalificacion() {
        Backtracking bt = new Backtracking(gestor, requerimiento);
        Equipo resultado = bt.resolver();

        // Luis (4) debe ser elegido sobre Pedro (2) como tester
        boolean tieneALuis = resultado.getIntegrantes().stream()
                .anyMatch(p -> p.getNombre().equals("Luis"));
        assertTrue(tieneALuis, "Debe elegir al tester mejor calificado");
    }

    @Test
    public void testRespetaIncompatibilidades() {
        // Ana y Carlos son incompatibles
        Persona ana = gestor.getPersonas().get(0);
        Persona carlos = gestor.getPersonas().get(1);
        gestor.agregarIncompatibilidad(ana, carlos);

        Backtracking bt = new Backtracking(gestor, requerimiento);
        Equipo resultado = bt.resolver();

        assertFalse(resultado.tieneIncompatibilidades(), 
                    "El equipo no debe tener incompatibilidades");
    }

    @Test
    public void testRetornaNullSiNoHaySolucion() {
        // Solo hay lideres, imposible armar equipo completo
        GestorDePersonas gestorImposible = new GestorDePersonas();
        gestorImposible.agregarPersona(new Persona("A", Rol.LIDER_DE_PROYECTO, 5));
        gestorImposible.agregarPersona(new Persona("B", Rol.LIDER_DE_PROYECTO, 3));

        Backtracking bt = new Backtracking(gestorImposible, requerimiento);
        Equipo resultado = bt.resolver();

        assertNull(resultado, "Debe retornar null si no hay solución posible");
    }

    @Test
    public void testCuentaLlamadas() {
        Backtracking bt = new Backtracking(gestor, requerimiento);
        bt.resolver();

        assertTrue(bt.getCantidadLlamadas() > 0, 
                   "Debe registrar la cantidad de llamadas recursivas");
    }
}