package Modelo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 * Pruebas unitarias de la clase Mascota.
 *
 * @author Isabella Rodriguez Granja-16-11-2026
 */
public class MascotaTest {

    @Test
    void constructorAsignaTodosLosDatos() {
        Mascota m = new Mascota("MASC0001", "Luna", (byte) 4, "Blanca", "Tranquila");
        assertEquals("MASC0001", m.getCodigo());
        assertEquals("Luna", m.getNombre());
        assertEquals(4, m.getEdad());
        assertEquals("Blanca", m.getColor());
        assertEquals("Tranquila", m.getCaracter());
    }

    @Test
    void codigoDeMasDeOchoCaracteresLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> new Mascota("MASC00012", "Luna", (byte) 4, "Blanca", "Tranquila"));
    }

    @Test
    void codigoDeMenosDeOchoCaracteresLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> new Mascota("MASC001", "Luna", (byte) 4, "Blanca", "Tranquila"));
    }

    @Test
    void nombreEnBlancoLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> new Mascota("MASC0001", "   ", (byte) 4, "Blanca", "Tranquila"));
    }

    @Test
    void codigoNuloLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> new Mascota(null, "Luna", (byte) 4, "Blanca", "Tranquila"));
    }

    @Test
    void nombreDeMasDeVeinteCaracteresLanzaExcepcion() {
        String nombreLargo = "Nombre demasiado largo para la mascota";
        assertThrows(IllegalArgumentException.class,
                () -> new Mascota("MASC0001", nombreLargo, (byte) 4, "Blanca", "Tranquila"));
    }

    @Test
    void edadNegativaLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> new Mascota("MASC0001", "Luna", (byte) -1, "Blanca", "Tranquila"));
    }

    @Test
    void setNombreActualizaElNombre() {
        Mascota m = new Mascota("MASC0001", "Luna", (byte) 4, "Blanca", "Tranquila");
        m.setNombre("Nube");
        assertEquals("Nube", m.getNombre());
    }

    @Test
    void toStringIncluyeElCodigoYElNombre() {
        Mascota m = new Mascota("MASC0001", "Luna", (byte) 4, "Blanca", "Tranquila");
        assertTrue(m.toString().contains("MASC0001"));
        assertTrue(m.toString().contains("Luna"));
        assertTrue(m.toString().startsWith("Mascota{"));
    }
}
