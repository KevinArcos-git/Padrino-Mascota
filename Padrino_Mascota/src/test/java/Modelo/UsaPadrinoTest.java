package Vista;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import Modelo.Apadrinamiento;
import Modelo.AporteArticulo;
import Modelo.AporteEfectivo;
import Modelo.Mascota;
import Modelo.Padrino;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Pruebas unitarias de los métodos de consulta, búsqueda y ordenamiento de la Vista.
 * No abren la ventana: solo usan los métodos estáticos que reciben la lista de padrinos.
 *
 * @author Equipo Delta
 */
public class UsaPadrinoTest {

    private ArrayList<Padrino> lista;
    private Padrino ana;
    private Padrino luis;

    @BeforeEach
    void crearDatos() {
        lista = new ArrayList<>();
        ana = new Padrino(1, "300000001", "ana.correo@ejemplo.com", "3001111111", "Cali",
                LocalDateTime.of(2026, 11, 10, 9, 0));
        luis = new Padrino(2, "100000002", "luis.correo@ejemplo.com", "3002222222", "Popayán",
                LocalDateTime.of(2026, 11, 12, 10, 30));
        // Ana: 1 apadrinamiento con aporte en efectivo de 10000 COP
        Mascota max = new Mascota("MASC0001", "Max", (byte) 1, "Negro", "Tranquilo");
        Apadrinamiento a1 = new Apadrinamiento(1, LocalDateTime.of(2026, 11, 10, 9, 5), max,
                new AporteEfectivo(LocalDate.of(2026, 11, 10), "Efectivo", 10000, "COP", 1));
        ana.agregarApadrinamiento(a1);
        // Luis: 1 apadrinamiento con 2 aportes (efectivo 5000 + artículo 2 x 1500 = 8000)
        Mascota luna = new Mascota("MASC0002", "Luna", (byte) 2, "Blanca", "Juguetona");
        Apadrinamiento a2 = new Apadrinamiento(2, LocalDateTime.of(2026, 11, 12, 10, 35), luna,
                new AporteEfectivo(LocalDate.of(2026, 11, 12), "Transferencia", 5000, "COP", 1));
        a2.agregarAporte(new AporteArticulo(LocalDate.of(2026, 11, 12), "Entrega personal", 2, "COP",
                "alimento", 1500, LocalDate.of(2027, 1, 1), true));
        luis.agregarApadrinamiento(a2);
        lista.add(ana);
        lista.add(luis);
    }

    @Test
    void generarCodigoPadrinoConListaVaciaEsUno() {
        assertEquals(1, UsaPadrino.generarCodigoPadrino(new ArrayList<>()));
    }

    @Test
    void generarCodigoPadrinoEsElSiguienteConsecutivo() {
        assertEquals(3, UsaPadrino.generarCodigoPadrino(lista));
    }

    @Test
    void generarNumeroApadrinamientoEsElSiguienteConsecutivo() {
        assertEquals(1, UsaPadrino.generarNumeroApadrinamiento(new ArrayList<>()));
        assertEquals(3, UsaPadrino.generarNumeroApadrinamiento(lista));
    }

    @Test
    void buscarPadrinoDadaCedulaEncuentraAlPadrino() {
        assertSame(luis, UsaPadrino.buscarPadrinoDadaCedula(lista, "100000002"));
    }

    @Test
    void buscarPadrinoDadaCedulaRetornaNullSiNoExiste() {
        assertNull(UsaPadrino.buscarPadrinoDadaCedula(lista, "999999999"));
    }

    @Test
    void consultasConListaVaciaInformanQueNoHayPadrinos() {
        ArrayList<Padrino> vacia = new ArrayList<>();
        String mensaje = "Aún no hay padrinos registrados.";
        assertEquals(mensaje, UsaPadrino.consultarTodosLosPadrinos(vacia));
        assertEquals(mensaje, UsaPadrino.consultarUltimoPadrinoRegistrado(vacia));
        assertEquals(mensaje, UsaPadrino.consultarPadrinoDadaCedula(vacia));
        assertEquals(mensaje, UsaPadrino.consultarApadrinamientosDadaMascota(vacia, "MASC0001"));
        assertEquals(mensaje, UsaPadrino.ordenarPadrinos(vacia, true, true));
    }

    @Test
    void consultarTodosListaATodosConSuValorTotal() {
        String texto = UsaPadrino.consultarTodosLosPadrinos(lista);
        assertTrue(texto.contains("Padrinos registrados (2)"));
        assertTrue(texto.contains("300000001"));
        assertTrue(texto.contains("100000002"));
    }

    @Test
    void consultarUltimoIncluyeApadrinamientosYTotalDeAportes() {
        String texto = UsaPadrino.consultarUltimoPadrinoRegistrado(lista);
        assertTrue(texto.contains("100000002"));
        assertTrue(texto.contains("MASC0002"));
        assertTrue(texto.contains("Aportes (2)"));
        assertTrue(texto.contains("Valor total de aportes del padrino: 8000"));
    }

    @Test
    void consultarApadrinamientosDeUnaMascotaMuestraElDetalleDeSusAportes() {
        String texto = UsaPadrino.consultarApadrinamientosDadaMascota(lista, "MASC0002");
        assertTrue(texto.contains("100000002"));
        assertTrue(texto.contains("Aportes (2)"));
        assertTrue(texto.contains("Entrega personal"));
        assertTrue(texto.contains("Valor total de aportes: 8000"));
    }

    @Test
    void consultarApadrinamientosDeMascotaSinApadrinamientosInformaQueNoHay() {
        String texto = UsaPadrino.consultarApadrinamientosDadaMascota(lista, "MASC9999");
        assertTrue(texto.contains("No hay apadrinamientos"));
    }

    @Test
    void ordenarPorCedulaAscendenteDejaPrimeroLaCedulaMenor() {
        String texto = UsaPadrino.ordenarPadrinos(lista, true, true);
        assertTrue(texto.indexOf("100000002") < texto.indexOf("300000001"));
    }

    @Test
    void ordenarPorCedulaDescendenteDejaPrimeroLaCedulaMayor() {
        String texto = UsaPadrino.ordenarPadrinos(lista, true, false);
        assertTrue(texto.indexOf("300000001") < texto.indexOf("100000002"));
    }

    @Test
    void ordenarPorAportesDescendenteDejaPrimeroAlQueMasAporto() {
        // Ana aportó 10000 y Luis 8000
        String texto = UsaPadrino.ordenarPadrinos(lista, false, false);
        assertTrue(texto.indexOf("300000001") < texto.indexOf("100000002"));
    }

    @Test
    void ordenarNoAlteraElOrdenDeRegistro() {
        UsaPadrino.ordenarPadrinos(lista, true, true);
        assertSame(ana, lista.get(0));
        assertSame(luis, lista.get(1));
    }
}
