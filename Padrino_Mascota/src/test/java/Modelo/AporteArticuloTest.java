package Modelo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

/**
 * Pruebas unitarias de la clase AporteArticulo.
 *
 * @author Equipo Delta
 */
public class AporteArticuloTest {

    private static final LocalDate ENTREGA = LocalDate.of(2026, 11, 10);
    private static final LocalDate VENCE = LocalDate.of(2027, 3, 1);

    private AporteArticulo crear(String categoria) {
        return new AporteArticulo(ENTREGA, "Entrega personal", 3, "COP",
                categoria, 2500, VENCE, true);
    }

    @Test
    void valorTotalEsCantidadPorValorUnitario() {
        assertEquals(7500.0, crear("alimento").calcularValorTotal(), 0.001);
    }

    @Test
    void polimorfismoElArticuloSeCalculaSegunSuTipo() {
        Aporte aporte = crear("medicamento");
        assertEquals(7500.0, aporte.calcularValorTotal(), 0.001);
    }

    @Test
    void categoriaValidaSeGuardaEnMinusculas() {
        assertEquals("producto aseo", crear("Producto Aseo").getCategoria());
    }

    @Test
    void categoriaInvalidaLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> crear("juguete"));
    }

    @Test
    void categoriaNulaLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> crear(null));
    }

    @Test
    void perecederoSePuedeCambiar() {
        AporteArticulo aporte = crear("alimento");
        assertTrue(aporte.isPerecedero());
        aporte.setPerecedero(false);
        assertFalse(aporte.isPerecedero());
    }

    @Test
    void toStringIncluyeCategoriaYVencimiento() {
        String texto = crear("alimento").toString();
        assertTrue(texto.contains("alimento"));
        assertTrue(texto.contains("01/03/2027"));
    }
}
