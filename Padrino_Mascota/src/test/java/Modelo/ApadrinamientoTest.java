package Modelo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Pruebas unitarias de la clase Apadrinamiento.
 *
 * @author Equipo Delta
 */
public class ApadrinamientoTest {

    private Mascota mascota;
    private Aporte primerAporte;
    private Apadrinamiento apadrinamiento;

    @BeforeEach
    void crearApadrinamiento() {
        mascota = new Mascota("MASC0001", "Luna", (byte) 2, "Blanca", "Tranquila");
        primerAporte = new AporteEfectivo(LocalDate.of(2026, 11, 10), "Efectivo", 10000, "COP", 1);
        apadrinamiento = new Apadrinamiento(7, LocalDateTime.of(2026, 11, 10, 23, 59),
                mascota, primerAporte);
    }

    @Test
    void constructorGuardaLosDatosYElPrimerAporte() {
        assertEquals(7, apadrinamiento.getNumero());
        assertSame(mascota, apadrinamiento.getSuMascota());
        assertEquals(1, apadrinamiento.getSusAportes().size());
        assertSame(primerAporte, apadrinamiento.getSusAportes().get(0));
    }

    @Test
    void agregarAporteIncrementaLaLista() {
        Aporte otro = new AporteEfectivo(LocalDate.of(2026, 11, 11), "Efectivo", 500, "COP", 1);
        apadrinamiento.agregarAporte(otro);
        assertEquals(2, apadrinamiento.getSusAportes().size());
    }

    @Test
    void calcularValorTotalAportesSumaEfectivoYArticulos() {
        Aporte articulo = new AporteArticulo(LocalDate.of(2026, 11, 11), "Entrega personal",
                2, "COP", "alimento", 1500, LocalDate.of(2027, 1, 1), true);
        apadrinamiento.agregarAporte(articulo);
        // 10000 (efectivo en COP) + 2 x 1500 (artículo)
        assertEquals(13000.0, apadrinamiento.calcularValorTotalAportes(), 0.001);
    }

    @Test
    void estaEnRangoFechasIncluyeLosExtremos() {
        LocalDate dia = LocalDate.of(2026, 11, 10);
        assertTrue(apadrinamiento.estaEnRangoFechas(dia, dia));
        assertTrue(apadrinamiento.estaEnRangoFechas(dia.minusDays(1), dia.plusDays(1)));
    }

    @Test
    void estaEnRangoFechasRechazaFechasFueraDelRango() {
        LocalDate dia = LocalDate.of(2026, 11, 10);
        assertFalse(apadrinamiento.estaEnRangoFechas(dia.plusDays(1), dia.plusDays(5)));
        assertFalse(apadrinamiento.estaEnRangoFechas(dia.minusDays(5), dia.minusDays(1)));
    }

    @Test
    void toStringIncluyeNumeroFechaYMascota() {
        String texto = apadrinamiento.toString();
        assertTrue(texto.startsWith("Apadrinamiento{"));
        assertTrue(texto.contains("10/11/2026 23:59"));
        assertTrue(texto.contains("MASC0001"));
    }
}
