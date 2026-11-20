package Modelo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

/**
 * Pruebas unitarias de la clase AporteEfectivo (y de las reglas heredadas de Aporte).
 *
 * @author Equipo Delta
 */
public class AporteEfectivoTest {

    private static final LocalDate FECHA = LocalDate.of(2026, 11, 10);

    @Test
    void valorTotalEnPesosEsLaCantidadSinConversion() {
        AporteEfectivo aporte = new AporteEfectivo(FECHA, "Transferencia", 50000, "COP", 4000);
        assertEquals(50000.0, aporte.calcularValorTotal(), 0.001);
    }

    @Test
    void valorTotalEnOtraMonedaSeMultiplicaPorLaTasaDeCambio() {
        AporteEfectivo aporte = new AporteEfectivo(FECHA, "Transferencia", 10, "USD", 4000);
        assertEquals(40000.0, aporte.calcularValorTotal(), 0.001);
    }

    @Test
    void polimorfismoElAporteSeCalculaSegunSuTipo() {
        Aporte aporte = new AporteEfectivo(FECHA, "Transferencia", 10, "USD", 4000);
        assertEquals(40000.0, aporte.calcularValorTotal(), 0.001);
    }

    @Test
    void cantidadSeRedondeaADosCifrasDecimales() {
        AporteEfectivo aporte = new AporteEfectivo(FECHA, "Efectivo", 10.456, "COP", 1);
        assertEquals(10.46, aporte.getCantidad(), 0.0001);
    }

    @Test
    void modoDeEntregaDeMasDeCincuentaCaracteresLanzaExcepcion() {
        String modoLargo = "x".repeat(51);
        assertThrows(IllegalArgumentException.class,
                () -> new AporteEfectivo(FECHA, modoLargo, 100, "COP", 1));
    }

    @Test
    void toStringIncluyeLaTasaDeCambio() {
        AporteEfectivo aporte = new AporteEfectivo(FECHA, "Efectivo", 100, "COP", 3900);
        assertTrue(aporte.toString().contains("3900"));
        assertTrue(aporte.toString().contains("10/11/2026"));
    }
}
