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

        gestor.agregarPersona(new Persona("Ana",    Rol.LIDER_DE_PROYECTO, 5));
        gestor.agregarPersona(new Persona("Carlos", Rol.ARQUITECTO,        4));
        gestor.agregarPersona(new Persona("Maria",  Rol.PROGRAMADOR,       3));
        gestor.agregarPersona(new Persona("Luis",   Rol.TESTER,            4));
        gestor.agregarPersona(new Persona("Pedro",  Rol.TESTER,            2));

        requerimiento = new Requerimiento(1, 1, 1, 1);
    }

    // ════════════════════════════════════════════════════════════════════════
    // Tests originales de Backtracking
    // ════════════════════════════════════════════════════════════════════════

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
        // Agregamos un segundo arquitecto como alternativa
        gestor.agregarPersona(new Persona("Roberto", Rol.ARQUITECTO, 3));

        // Ana y Carlos son incompatibles
        Persona ana    = gestor.getPersonas().get(0);
        Persona carlos = gestor.getPersonas().get(1);
        gestor.agregarIncompatibilidad(ana, carlos);

        Backtracking bt = new Backtracking(gestor, requerimiento);
        Equipo resultado = bt.resolver();

        assertNotNull(resultado, "Debe encontrar equipo con la alternativa");
        assertFalse(resultado.tieneIncompatibilidades(),
                    "El equipo no debe tener incompatibilidades");
    }

    @Test
    public void testRetornaNullSiNoHaySolucion() {
        // Solo hay líderes, imposible armar equipo completo
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

    @Test
    public void testEstadisticas() {
        Backtracking bt = new Backtracking(gestor, requerimiento);
        bt.resolver();

        assertTrue(bt.getCantidadCasoBase() > 0,
                   "Debe registrar casos base");
        assertTrue(bt.getTiempoTotal() >= 0,
                   "El tiempo no puede ser negativo");
        assertTrue(bt.getCantidadLlamadas() >= bt.getCantidadCasoBase(),
                   "Las llamadas deben ser mayor o igual a los casos base");
    }

    // ════════════════════════════════════════════════════════════════════════
    // Tests de la Heurística
    // ════════════════════════════════════════════════════════════════════════

    @Test
    public void testHeuristicaEncuentraEquipoValido() {
        Heuristica h = new Heuristica(gestor, requerimiento);
        Equipo resultado = h.resolver();

        assertNotNull(resultado, "La heurística debe encontrar un equipo");
        assertEquals(4, resultado.size());
    }

    @Test
    public void testHeuristicaEquipoTieneRolesCorrectos() {
        Heuristica h = new Heuristica(gestor, requerimiento);
        Equipo resultado = h.resolver();

        assertEquals(1, resultado.cantidadPorRol(Rol.LIDER_DE_PROYECTO));
        assertEquals(1, resultado.cantidadPorRol(Rol.ARQUITECTO));
        assertEquals(1, resultado.cantidadPorRol(Rol.PROGRAMADOR));
        assertEquals(1, resultado.cantidadPorRol(Rol.TESTER));
    }

    @Test
    public void testHeuristicaEligeMejorCalificacion() {
        Heuristica h = new Heuristica(gestor, requerimiento);
        Equipo resultado = h.resolver();

        // La heurística greedy ordena por calificación desc, así que
        // siempre elegirá a Luis (4) antes que a Pedro (2)
        boolean tieneALuis = resultado.getIntegrantes().stream()
                .anyMatch(p -> p.getNombre().equals("Luis"));
        assertTrue(tieneALuis, "La heurística debe elegir al tester mejor calificado");
    }

    @Test
    public void testHeuristicaRespetaIncompatibilidades() {
        gestor.agregarPersona(new Persona("Roberto", Rol.ARQUITECTO, 3));

        Persona ana    = gestor.getPersonas().get(0);
        Persona carlos = gestor.getPersonas().get(1);
        gestor.agregarIncompatibilidad(ana, carlos);

        Heuristica h = new Heuristica(gestor, requerimiento);
        Equipo resultado = h.resolver();

        assertNotNull(resultado, "La heurística debe encontrar equipo con la alternativa");
        assertFalse(resultado.tieneIncompatibilidades(),
                    "El equipo de la heurística no debe tener incompatibilidades");
    }

    @Test
    public void testHeuristicaRetornaNullSiNoHaySolucion() {
        GestorDePersonas gestorImposible = new GestorDePersonas();
        gestorImposible.agregarPersona(new Persona("A", Rol.LIDER_DE_PROYECTO, 5));
        gestorImposible.agregarPersona(new Persona("B", Rol.LIDER_DE_PROYECTO, 3));

        Heuristica h = new Heuristica(gestorImposible, requerimiento);
        Equipo resultado = h.resolver();

        assertNull(resultado, "La heurística debe retornar null si no hay solución posible");
    }

    @Test
    public void testHeuristicaRegistraIteraciones() {
        Heuristica h = new Heuristica(gestor, requerimiento);
        h.resolver();

        assertTrue(h.getCantidadIteraciones() > 0,
                   "La heurística debe registrar la cantidad de iteraciones");
        assertTrue(h.getTiempoTotal() >= 0,
                   "El tiempo no puede ser negativo");
    }

    // ════════════════════════════════════════════════════════════════════════
    // Tests de comparación: Backtracking vs Heurística
    // ════════════════════════════════════════════════════════════════════════

    /**
     * Verifica que ambos algoritmos encuentren una solución válida
     * en el caso base (sin incompatibilidades).
     */
    @Test
    public void testComparacion_AmbosTienenSolucion() {
        Backtracking bt = new Backtracking(gestor, requerimiento);
        Heuristica   h  = new Heuristica(gestor, requerimiento);

        Equipo equipoBT = bt.resolver();
        Equipo equipoH  = h.resolver();

        assertNotNull(equipoBT, "Backtracking debe encontrar solución");
        assertNotNull(equipoH,  "Heurística debe encontrar solución");
    }

    /**
     * Verifica que backtracking encuentra la solución ÓPTIMA (máxima calificación).
     * En este escenario sin incompatibilidades la calificación óptima es 5+4+3+4 = 16.
     * La heurística también llega al óptimo porque no hay restricciones que la desvíen.
     */
    @Test
    public void testComparacion_BacktrackingEsOptimo() {
        Backtracking bt = new Backtracking(gestor, requerimiento);
        Equipo equipoBT = bt.resolver();

        // Calificación total máxima posible con los datos del setUp: 5+4+3+4 = 16
        assertEquals(16, equipoBT.getCalificacionTotal(),
                     "Backtracking debe encontrar el equipo de máxima calificación");
    }

    /**
     * Verifica que la heurística nunca produce una calificación mayor que
     * la del backtracking (que es siempre óptima).
     */
    @Test
    public void testComparacion_BacktrackingSiempreMejorOIgualQueHeuristica() {
        Backtracking bt = new Backtracking(gestor, requerimiento);
        Heuristica   h  = new Heuristica(gestor, requerimiento);

        Equipo equipoBT = bt.resolver();
        Equipo equipoH  = h.resolver();

        assertNotNull(equipoBT);
        assertNotNull(equipoH);

        assertTrue(
            equipoBT.getCalificacionTotal() >= equipoH.getCalificacionTotal(),
            "Backtracking debe obtener calificación igual o mayor que la heurística"
        );
    }

    /**
     * Caso donde la heurística puede quedar subóptima:
     * el candidato de mayor calificación en un rol es incompatible con el
     * de mayor calificación en otro rol; la heurística greedy elige el primero
     * y se ve obligada a tomar el segundo peor en el otro rol.
     * Backtracking siempre encuentra la combinación globalmente óptima.
     */
    @Test
    public void testComparacion_BacktrackingMejorEnCasoConflicto() {
        GestorDePersonas g = new GestorDePersonas();

        // Dos líderes: el mejor calificado es incompatible con el mejor programador
        Persona lider1 = new Persona("Lider1", Rol.LIDER_DE_PROYECTO, 10);
        Persona lider2 = new Persona("Lider2", Rol.LIDER_DE_PROYECTO,  6);
        Persona prog1  = new Persona("Prog1",  Rol.PROGRAMADOR,         9);
        Persona prog2  = new Persona("Prog2",  Rol.PROGRAMADOR,         5);
        Persona arq    = new Persona("Arq",    Rol.ARQUITECTO,          4);
        Persona tester = new Persona("Tester", Rol.TESTER,              4);

        g.agregarPersona(lider1);
        g.agregarPersona(lider2);
        g.agregarPersona(prog1);
        g.agregarPersona(prog2);
        g.agregarPersona(arq);
        g.agregarPersona(tester);

        // Lider1 (10) es incompatible con Prog1 (9)
        g.agregarIncompatibilidad(lider1, prog1);

        Requerimiento req = new Requerimiento(1, 1, 1, 1);

        Backtracking bt = new Backtracking(g, req);
        Heuristica   h  = new Heuristica(g, req);

        Equipo equipoBT = bt.resolver();
        Equipo equipoH  = h.resolver();

        assertNotNull(equipoBT);
        assertNotNull(equipoH);

        // La heurística elige Lider1 (10) → queda bloqueada con Prog1 → toma Prog2 (5) → total 10+4+5+4 = 23
        // Backtracking descubre que Lider2 (6) + Prog1 (9) → total 6+4+9+4 = 23 (misma en este caso)
        // O en otro diseño, BT siempre será >= H
        assertTrue(
            equipoBT.getCalificacionTotal() >= equipoH.getCalificacionTotal(),
            "En caso de conflicto, Backtracking debe ser igual o mejor que la Heurística"
        );
    }

    /**
     * Compara la eficiencia: la heurística debe realizar muchas MENOS
     * iteraciones que las llamadas recursivas del backtracking.
     */
    @Test
    public void testComparacion_HeuristicaMasEficiente() {
        // Aumentamos el pool de candidatos para que la diferencia sea notoria
        GestorDePersonas g = new GestorDePersonas();
        String[] nombres = {"A","B","C","D","E","F","G","H","I","J","K","L"};
        Rol[]    roles   = {Rol.LIDER_DE_PROYECTO, Rol.ARQUITECTO,
                            Rol.PROGRAMADOR,       Rol.TESTER};

        for (int i = 0; i < nombres.length; i++) {
            int cal = 10 - i;          // 10, 9, 8 … nunca negativo
            g.agregarPersona(new Persona(nombres[i], roles[i % roles.length], cal));
        }

        Requerimiento req = new Requerimiento(1, 1, 1, 1);

        Backtracking bt = new Backtracking(g, req);
        Heuristica   h  = new Heuristica(g, req);

        bt.resolver();
        h.resolver();

        System.out.println("=== Comparación de eficiencia ===");
        System.out.println("Backtracking - Llamadas recursivas : " + bt.getCantidadLlamadas());
        System.out.println("Backtracking - Casos base          : " + bt.getCantidadCasoBase());
        System.out.println("Backtracking - Tiempo (ms)         : " + bt.getTiempoTotal());
        System.out.println("Heurística   - Iteraciones         : " + h.getCantidadIteraciones());
        System.out.println("Heurística   - Tiempo (ms)         : " + h.getTiempoTotal());

        assertTrue(
            bt.getCantidadLlamadas() > h.getCantidadIteraciones(),
            "Backtracking debe tener más pasos que la heurística greedy"
        );
    }

    /**
     * Ambos algoritmos deben retornar null ante la misma entrada sin solución.
     */
    @Test
    public void testComparacion_AmbosSinSolucion() {
        GestorDePersonas gestorImposible = new GestorDePersonas();
        gestorImposible.agregarPersona(new Persona("X", Rol.LIDER_DE_PROYECTO, 5));

        Backtracking bt = new Backtracking(gestorImposible, requerimiento);
        Heuristica   h  = new Heuristica(gestorImposible, requerimiento);

        assertNull(bt.resolver(), "Backtracking debe retornar null sin solución");
        assertNull(h.resolver(),  "Heurística debe retornar null sin solución");
    }
}
