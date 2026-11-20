/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package Vista;
/*
 * Author Daniel Stiven Libreros Marulanda 15-11-2026
 */
import Modelo.Aporte;
import Modelo.AporteArticulo;
import Modelo.AporteEfectivo;
import Modelo.Apadrinamiento;
import Modelo.Mascota;
import Modelo.Padrino;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class UsaPadrino extends JFrame {

    private static ArrayList<Padrino> losPadrinos = new ArrayList<>();
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final String ARCHIVO = "padrinos.dat";

    // Componentes de la ventana
    private static UsaPadrino ventana;
    private static JTextField txtCodigo, txtCedula, txtCorreo, txtTelefono, txtCiudad, txtConsulta;
    private static JTextArea areaResultados;

    // ------------------------------------------------------------------
    // MAIN y MENÚ
    // ------------------------------------------------------------------
    public static void main(String[] args) {
        ejecutarMenu();
    }

    public static void ejecutarMenu() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // Si no se puede, se usa el aspecto por defecto
        }
        SwingUtilities.invokeLater(() -> new UsaPadrino().setVisible(true));
    }

    // ------------------------------------------------------------------
    // CONSTRUCCIÓN DE LA INTERFAZ
    // ------------------------------------------------------------------
    public UsaPadrino() {
        super("GESTIÓN DE PADRINOS");
        ventana = this;
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel raiz = new JPanel(new BorderLayout(10, 10));
        raiz.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel superior = new JPanel(new BorderLayout(10, 10));
        superior.add(crearPanelRegistro(), BorderLayout.CENTER);
        superior.add(crearPanelDerecho(), BorderLayout.EAST);

        raiz.add(superior, BorderLayout.NORTH);
        raiz.add(crearPanelConsultas(), BorderLayout.CENTER);

        setContentPane(raiz);
        setSize(920, 720);
        setLocationRelativeTo(null);
        actualizarCodigo();
    }

    /** Panel izquierdo: "Registro padrino". */
    private JPanel crearPanelRegistro() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBorder(BorderFactory.createTitledBorder("Registro padrino"));

        txtCodigo = new JTextField(10);
        txtCodigo.setEditable(false);
        txtCedula = new JTextField(15);
        txtCorreo = new JTextField(25);
        txtTelefono = new JTextField(15);
        txtCiudad = new JTextField(15);

        agregarFila(p, 0, "Código", txtCodigo);
        agregarFila(p, 1, "Cédula (6 a 11 caracteres)", txtCedula);
        agregarFila(p, 2, "Correo (15 a 50 caracteres)", txtCorreo);
        agregarFila(p, 3, "Teléfono (7 a 12 caracteres)", txtTelefono);
        agregarFila(p, 4, "Ciudad (3 a 15 caracteres)", txtCiudad);

        JButton btnAgregar = new JButton("Agregar");
        JButton btnApadrinar = new JButton("Apadrinar mascota");
        btnAgregar.addActionListener(e -> losPadrinos = registrarPadrino(losPadrinos));
        btnApadrinar.addActionListener(e -> losPadrinos = apadrinarMascota(losPadrinos));

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botones.add(btnApadrinar);
        botones.add(btnAgregar);
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = 0;
        gc.gridy = 5;
        gc.gridwidth = 2;
        gc.anchor = GridBagConstraints.EAST;
        gc.insets = new Insets(14, 8, 8, 8);
        p.add(botones, gc);
        return p;
    }

    private void agregarFila(JPanel p, int fila, String etiqueta, JTextField campo) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = fila;
        c.anchor = GridBagConstraints.WEST;
        c.insets = new Insets(6, 8, 6, 12);
        p.add(new JLabel(etiqueta), c);

        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = fila;
        c.weightx = 1.0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(6, 0, 6, 8);
        p.add(campo, c);
    }

    /** Panel derecho: "Archivo objetos" y "Ordenamiento". */
    private JPanel crearPanelDerecho() {
        JPanel derecho = new JPanel();
        derecho.setLayout(new javax.swing.BoxLayout(derecho, javax.swing.BoxLayout.Y_AXIS));
        derecho.setPreferredSize(new Dimension(270, 300));

        JPanel archivo = new JPanel(new GridLayout(2, 1, 6, 6));
        archivo.setBorder(BorderFactory.createTitledBorder("Archivo objetos"));
        JButton btnGenerar = new JButton("Generar");
        JButton btnLeer = new JButton("Leer");
        archivo.add(btnGenerar);
        archivo.add(btnLeer);

        JPanel orden = new JPanel(new GridLayout(4, 1, 6, 6));
        orden.setBorder(BorderFactory.createTitledBorder("Ordenamiento"));
        JButton btnCedulaAsc = new JButton("Cédula ASC");
        JButton btnAportesAsc = new JButton("Aportes ASC");
        JButton btnCedulaDesc = new JButton("Cédula DESC");
        JButton btnAportesDesc = new JButton("Aportes DESC");
        orden.add(btnCedulaAsc);
        orden.add(btnAportesAsc);
        orden.add(btnCedulaDesc);
        orden.add(btnAportesDesc);

        derecho.add(archivo);
        derecho.add(javax.swing.Box.createVerticalStrut(8));
        derecho.add(orden);

        btnGenerar.addActionListener(e -> guardarPadrinos(losPadrinos));
        btnLeer.addActionListener(e -> losPadrinos = cargarPadrinos(losPadrinos));
        btnCedulaAsc.addActionListener(e -> mostrar(ordenarPadrinos(losPadrinos, true, true)));
        btnAportesAsc.addActionListener(e -> mostrar(ordenarPadrinos(losPadrinos, false, true)));
        btnCedulaDesc.addActionListener(e -> mostrar(ordenarPadrinos(losPadrinos, true, false)));
        btnAportesDesc.addActionListener(e -> mostrar(ordenarPadrinos(losPadrinos, false, false)));
        return derecho;
    }

    /** Panel inferior: "Consultas" con botones, campo de parámetro y área de resultados. */
    private JPanel crearPanelConsultas() {
        JPanel p = new JPanel(new BorderLayout(6, 6));
        p.setBorder(BorderFactory.createTitledBorder("Consultas"));

        JButton btnTodo = new JButton("Todo");
        JButton btnUltimo = new JButton("Último");
        txtConsulta = new JTextField(14);
        JButton btnCedula = new JButton("Cédula");
        JButton btnMascota = new JButton("Mascota");
        JButton btnFechas = new JButton("Rango de fechas");

        JPanel barra = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        barra.add(btnTodo);
        barra.add(btnUltimo);
        barra.add(txtConsulta);
        barra.add(btnCedula);
        barra.add(btnMascota);
        barra.add(btnFechas);

        areaResultados = new JTextArea(18, 60);
        areaResultados.setEditable(false);
        areaResultados.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));

        p.add(barra, BorderLayout.NORTH);
        p.add(new JScrollPane(areaResultados), BorderLayout.CENTER);

        btnTodo.addActionListener(e -> mostrar(consultarTodosLosPadrinos(losPadrinos)));
        btnUltimo.addActionListener(e -> mostrar(consultarUltimoPadrinoRegistrado(losPadrinos)));
        btnCedula.addActionListener(e -> mostrar(consultarPadrinoDadaCedula(losPadrinos)));
        btnMascota.addActionListener(e ->
                mostrar(consultarApadrinamientosDadaMascota(losPadrinos, txtConsulta.getText().trim())));
        btnFechas.addActionListener(e ->
                mostrar(consultarMascotasApadrinadasDadoRangoFechas(losPadrinos)));
        return p;
    }

    // ------------------------------------------------------------------
    // UTILIDADES DE LA INTERFAZ
    // ------------------------------------------------------------------
    private static void mostrar(String texto) {
        areaResultados.setText(texto);
        areaResultados.setCaretPosition(0);
    }

    private static void error(String mensaje) {
        JOptionPane.showMessageDialog(ventana, mensaje, "Dato inválido", JOptionPane.ERROR_MESSAGE);
    }

    private static void actualizarCodigo() {
        txtCodigo.setText(String.valueOf(generarCodigoPadrino(losPadrinos)));
    }

    private static void limpiarRegistro() {
        txtCedula.setText("");
        txtCorreo.setText("");
        txtTelefono.setText("");
        txtCiudad.setText("");
        actualizarCodigo();
        txtCedula.requestFocus();
    }

    // ------------------------------------------------------------------
    // REGISTRAR PADRINO (lee el formulario "Registro padrino")
    // ------------------------------------------------------------------
    public static ArrayList<Padrino> registrarPadrino(ArrayList<Padrino> losPadrinos) {
        String cedula = txtCedula.getText().trim();
        String correo = txtCorreo.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String ciudad = txtCiudad.getText().trim();

        String problema = validarPadrino(cedula, correo, telefono, ciudad);
        if (problema != null) {
            error(problema);
            return losPadrinos;
        }
        if (buscarPadrinoDadaCedula(losPadrinos, cedula) != null) {
            error("Ya existe un padrino registrado con esa cédula.");
            return losPadrinos;
        }

        int codigo = generarCodigoPadrino(losPadrinos);
        Padrino nuevo = new Padrino(codigo, cedula, correo, telefono, ciudad, LocalDateTime.now());
        losPadrinos.add(nuevo);

        mostrar("Padrino registrado con éxito. Código asignado: " + codigo + "\n" + nuevo);
        limpiarRegistro();
        return losPadrinos;
    }

    /** Restricciones de los datos de entrada del padrino. Devuelve null si todo es válido. */
    private static String validarPadrino(String cedula, String correo, String telefono, String ciudad) {
        if (cedula.length() < 6 || cedula.length() > 11 || !cedula.matches("\\d+")) {
            return "La cédula debe tener entre 6 y 11 dígitos.";
        }
        if (correo.length() < 15 || correo.length() > 50 || !correo.contains("@")) {
            return "El correo debe tener entre 15 y 50 caracteres e incluir '@'.";
        }
        if (telefono.length() < 7 || telefono.length() > 12 || !telefono.matches("\\d+")) {
            return "El teléfono debe tener entre 7 y 12 dígitos.";
        }
        if (ciudad.length() < 3 || ciudad.length() > 15) {
            return "La ciudad debe tener entre 3 y 15 caracteres.";
        }
        return null;
    }

    // ------------------------------------------------------------------
    // APADRINAR MASCOTA (ventanas de diálogo)
    // ------------------------------------------------------------------
    public static ArrayList<Padrino> apadrinarMascota(ArrayList<Padrino> losPadrinos) {
        if (losPadrinos.isEmpty()) {
            error("Aún no hay padrinos registrados. Registre uno primero.");
            return losPadrinos;
        }

        JTextField fCedula = new JTextField(txtCedula.getText().trim());
        JTextField fCodigo = new JTextField();
        JTextField fNombre = new JTextField();
        JTextField fEdad = new JTextField();
        JTextField fColor = new JTextField();
        JTextField fCaracter = new JTextField();

        JPanel panel = new JPanel(new GridLayout(0, 2, 6, 6));
        panel.add(new JLabel("Cédula del padrino"));
        panel.add(fCedula);
        panel.add(new JLabel("Código mascota (8 caracteres)"));
        panel.add(fCodigo);
        panel.add(new JLabel("Nombre (máx. 20 caracteres)"));
        panel.add(fNombre);
        panel.add(new JLabel("Edad (años)"));
        panel.add(fEdad);
        panel.add(new JLabel("Color"));
        panel.add(fColor);
        panel.add(new JLabel("Carácter"));
        panel.add(fCaracter);

        Padrino padrino;
        Mascota mascota;
        while (true) {
            int r = JOptionPane.showConfirmDialog(ventana, panel, "Apadrinar mascota",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (r != JOptionPane.OK_OPTION) {
                return losPadrinos;
            }

            padrino = buscarPadrinoDadaCedula(losPadrinos, fCedula.getText().trim());
            String codMascota = fCodigo.getText().trim();
            String nombre = fNombre.getText().trim();
            String color = fColor.getText().trim();
            String caracter = fCaracter.getText().trim();

            if (padrino == null) {
                error("No existe un padrino con esa cédula. Regístrelo primero.");
            } else if (padrino.getSusApadrinamientos().size() >= 5) {
                error("El padrino ya tiene el máximo de 5 apadrinamientos.");
                return losPadrinos;
            } else if (codMascota.length() != 8) {
                error("El código de la mascota debe tener exactamente 8 caracteres.");
            } else if (nombre.length() < 1 || nombre.length() > 20) {
                error("El nombre debe tener entre 1 y 20 caracteres.");
            } else if (color.isEmpty() || caracter.isEmpty()) {
                error("El color y el carácter de la mascota son obligatorios.");
            } else {
                try {
                    int edad = Integer.parseInt(fEdad.getText().trim());
                    if (edad < 0 || edad > 100) {
                        error("La edad debe estar entre 0 y 100 años.");
                    } else {
                        mascota = new Mascota(codMascota, nombre, (byte) edad, color, caracter);
                        break;
                    }
                } catch (NumberFormatException ex) {
                    error("La edad debe ser un número entero.");
                }
            }
        }

        // Mínimo un aporte
        Aporte primerAporte = pedirAporte();
        if (primerAporte == null) {
            error("Un apadrinamiento requiere al menos un aporte. No se registró.");
            return losPadrinos;
        }

        int numero = generarNumeroApadrinamiento(losPadrinos);
        Apadrinamiento apadrinamiento =
                new Apadrinamiento(numero, LocalDateTime.now(), mascota, primerAporte);

        // Aportes adicionales
        while (JOptionPane.showConfirmDialog(ventana, "¿Desea agregar otro aporte?",
                "Aportes", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            Aporte otro = pedirAporte();
            if (otro != null) {
                apadrinamiento.agregarAporte(otro);
            }
        }

        if (padrino.agregarApadrinamiento(apadrinamiento)) {
            mostrar("Apadrinamiento N.º " + numero + " registrado con éxito.\n"
                    + apadrinamiento + "\n"
                    + String.format("Valor total de aportes: %.2f", apadrinamiento.calcularValorTotalAportes()));
        } else {
            error("No fue posible registrar el apadrinamiento.");
        }
        return losPadrinos;
    }

    /**
     * Registro de objetos jerarquizados: pide los datos y crea un AporteArticulo
     * o un AporteEfectivo. Devuelve null si el usuario cancela.
     */
    private static Aporte pedirAporte() {
        JComboBox<String> cmbTipo = new JComboBox<>(new String[]{"Artículo", "Efectivo"});
        JTextField fFecha = new JTextField();
        JTextField fModo = new JTextField();
        JTextField fCantidad = new JTextField();
        JTextField fMoneda = new JTextField("COP");
        JComboBox<String> cmbCategoria = new JComboBox<>(
                new String[]{"alimento", "medicamento", "producto aseo", "otro"});
        JTextField fValor = new JTextField();
        JTextField fVence = new JTextField();
        JCheckBox chkPerecedero = new JCheckBox("Perecedero");
        JTextField fTasa = new JTextField();

        Runnable ajustar = () -> {
            boolean articulo = cmbTipo.getSelectedIndex() == 0;
            cmbCategoria.setEnabled(articulo);
            fValor.setEnabled(articulo);
            fVence.setEnabled(articulo);
            chkPerecedero.setEnabled(articulo);
            fTasa.setEnabled(!articulo);
        };
        cmbTipo.addActionListener(e -> ajustar.run());
        ajustar.run();

        JPanel panel = new JPanel(new GridLayout(0, 2, 6, 6));
        panel.add(new JLabel("Tipo de aporte"));
        panel.add(cmbTipo);
        panel.add(new JLabel("Fecha de entrega (dd/mm/aaaa)"));
        panel.add(fFecha);
        panel.add(new JLabel("Modo de entrega (máx. 50)"));
        panel.add(fModo);
        panel.add(new JLabel("Cantidad"));
        panel.add(fCantidad);
        panel.add(new JLabel("Moneda (ej. COP, USD)"));
        panel.add(fMoneda);
        panel.add(new JLabel("Categoría (artículo)"));
        panel.add(cmbCategoria);
        panel.add(new JLabel("Valor unitario COP (artículo)"));
        panel.add(fValor);
        panel.add(new JLabel("Vencimiento dd/mm/aaaa (artículo)"));
        panel.add(fVence);
        panel.add(new JLabel(""));
        panel.add(chkPerecedero);
        panel.add(new JLabel("Tasa de cambio COP (efectivo)"));
        panel.add(fTasa);

        while (true) {
            int r = JOptionPane.showConfirmDialog(ventana, panel, "Registrar aporte",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (r != JOptionPane.OK_OPTION) {
                return null;
            }
            try {
                LocalDate fechaEntrega = LocalDate.parse(fFecha.getText().trim(), FORMATO_FECHA);
                String modo = fModo.getText().trim();
                if (modo.length() < 1 || modo.length() > 50) {
                    throw new IllegalArgumentException("El modo de entrega debe tener entre 1 y 50 caracteres.");
                }
                double cantidad = Double.parseDouble(fCantidad.getText().trim().replace(',', '.'));
                if (cantidad <= 0) {
                    throw new IllegalArgumentException("La cantidad debe ser mayor que 0.");
                }
                cantidad = Math.round(cantidad * 100.0) / 100.0; // 2 cifras decimales
                String moneda = fMoneda.getText().trim().toUpperCase();
                if (moneda.length() != 3) {
                    throw new IllegalArgumentException("La moneda debe tener 3 letras (ej. COP, USD).");
                }

                if (cmbTipo.getSelectedIndex() == 0) {
                    int valor = Integer.parseInt(fValor.getText().trim());
                    if (valor < 0) {
                        throw new IllegalArgumentException("El valor unitario no puede ser negativo.");
                    }
                    LocalDate vence = LocalDate.parse(fVence.getText().trim(), FORMATO_FECHA);
                    if (vence.isBefore(fechaEntrega)) {
                        throw new IllegalArgumentException(
                                "El vencimiento no puede ser anterior a la fecha de entrega.");
                    }
                    return new AporteArticulo(fechaEntrega, modo, cantidad, moneda,
                            (String) cmbCategoria.getSelectedItem(), valor, vence,
                            chkPerecedero.isSelected());
                } else {
                    int tasa = Integer.parseInt(fTasa.getText().trim());
                    if (tasa < 1) {
                        throw new IllegalArgumentException("La tasa de cambio debe ser mayor que 0.");
                    }
                    return new AporteEfectivo(fechaEntrega, modo, cantidad, moneda, tasa);
                }
            } catch (DateTimeParseException ex) {
                error("Fecha inválida. Use el formato dd/mm/aaaa.");
            } catch (NumberFormatException ex) {
                error("Revise los campos numéricos: deben contener números válidos.");
            } catch (IllegalArgumentException ex) {
                error(ex.getMessage());
            }
        }
    }

    // ------------------------------------------------------------------
    // GENERADORES
    // ------------------------------------------------------------------
    public static int generarCodigoPadrino(ArrayList<Padrino> losPadrinos) {
        int max = 0;
        for (Padrino p : losPadrinos) {
            if (p.getCodigo() > max) {
                max = p.getCodigo();
            }
        }
        return max + 1;
    }

    public static int generarNumeroApadrinamiento(ArrayList<Padrino> losPadrinos) {
        int max = 0;
        for (Padrino p : losPadrinos) {
            for (Apadrinamiento a : p.getSusApadrinamientos()) {
                if (a.getNumero() > max) {
                    max = a.getNumero();
                }
            }
        }
        return max + 1;
    }

    // ------------------------------------------------------------------
    // BÚSQUEDA Y CONSULTAS
    // ------------------------------------------------------------------
    public static Padrino buscarPadrinoDadaCedula(ArrayList<Padrino> losPadrinos, String cedula) {
        for (Padrino p : losPadrinos) {
            if (p.getCedula().equals(cedula)) {
                return p;
            }
        }
        return null;
    }

    /** Botón "Todo": consulta de toda la colección.
     * @param losPadrinos
     * @return  */
    public static String consultarTodosLosPadrinos(ArrayList<Padrino> losPadrinos) {
        if (losPadrinos.isEmpty()) {
            return "Aún no hay padrinos registrados.";
        }
        StringBuilder sb = new StringBuilder("Padrinos registrados (" + losPadrinos.size() + "):\n\n");
        for (Padrino p : losPadrinos) {
            sb.append(p.toString())
              .append(String.format("%nValor total de aportes: %.2f%n%n", p.calcularValorTotalAportes()));
        }
        return sb.toString();
    }

    /** Botón "Cédula": usa el valor escrito en el campo de consulta.
     * @param losPadrinos
     * @return  */
    public static String consultarPadrinoDadaCedula(ArrayList<Padrino> losPadrinos) {
        if (losPadrinos.isEmpty()) {
            return "Aún no hay padrinos registrados.";
        }
        String cedula = txtConsulta.getText().trim();
        if (cedula.isEmpty()) {
            return "Escriba la cédula en el campo de consulta.";
        }
        Padrino p = buscarPadrinoDadaCedula(losPadrinos, cedula);
        if (p == null) {
            return "No existe un padrino con la cédula " + cedula + ".";
        }
        return p.toString()
                + String.format("%nValor total de aportes: %.2f", p.calcularValorTotalAportes());
    }

    /** Botón "Último".
     * @param losPadrinos
     * @return  */
    public static String consultarUltimoPadrinoRegistrado(ArrayList<Padrino> losPadrinos) {
        if (losPadrinos.isEmpty()) {
            return "Aún no hay padrinos registrados.";
        }
        Padrino ultimo = losPadrinos.get(losPadrinos.size() - 1);
        StringBuilder sb = new StringBuilder("Último padrino registrado:\n");
        sb.append(ultimo.toString()).append("\n");
        if (ultimo.getSusApadrinamientos().isEmpty()) {
            sb.append("Aún no tiene apadrinamientos.\n");
        } else {
            for (Apadrinamiento a : ultimo.getSusApadrinamientos()) {
                sb.append(describirApadrinamiento(a));
            }
        }
        sb.append(String.format("Valor total de aportes del padrino: %.2f", ultimo.calcularValorTotalAportes()));
        return sb.toString();
    }

    /** Botón "Rango de fechas": pide las dos fechas en ventanas de diálogo.
     * @param losPadrinos
     * @return  */
    public static String consultarMascotasApadrinadasDadoRangoFechas(ArrayList<Padrino> losPadrinos) {
        if (losPadrinos.isEmpty()) {
            return "Aún no hay padrinos registrados.";
        }
        String txtInicio = JOptionPane.showInputDialog(ventana, "Fecha inicial (dd/mm/aaaa):");
        if (txtInicio == null) {
            return "Consulta cancelada.";
        }
        String txtFin = JOptionPane.showInputDialog(ventana, "Fecha final (dd/mm/aaaa):");
        if (txtFin == null) {
            return "Consulta cancelada.";
        }

        LocalDate inicio;
        LocalDate fin;
        try {
            inicio = LocalDate.parse(txtInicio.trim(), FORMATO_FECHA);
            fin = LocalDate.parse(txtFin.trim(), FORMATO_FECHA);
        } catch (DateTimeParseException e) {
            return "Fecha inválida. Use el formato dd/mm/aaaa.";
        }
        if (fin.isBefore(inicio)) {
            return "La fecha final no puede ser anterior a la fecha inicial.";
        }

        StringBuilder sb = new StringBuilder();
        for (Padrino p : losPadrinos) {
            for (Apadrinamiento a : p.getSusApadrinamientos()) {
                if (a.estaEnRangoFechas(inicio, fin)) {
                    sb.append("Padrino (cédula ").append(p.getCedula()).append(") -> ")
                      .append(a.getSuMascota().toString()).append("\n");
                }
            }
        }
        if (sb.length() == 0) {
            return "No hay mascotas apadrinadas entre " + inicio.format(FORMATO_FECHA)
                    + " y " + fin.format(FORMATO_FECHA) + ".";
        }
        return "Mascotas apadrinadas entre " + inicio.format(FORMATO_FECHA)
                + " y " + fin.format(FORMATO_FECHA) + ":\n" + sb;
    }

    /** Arma el texto de un apadrinamiento: sus datos, la mascota, el detalle de cada aporte y el valor total.
     * @param a apadrinamiento a describir
     * @return  */
    public static String describirApadrinamiento(Apadrinamiento a) {
        StringBuilder sb = new StringBuilder(a.toString()).append("\n");
        sb.append("   Aportes (").append(a.getSusAportes().size()).append("):\n");
        for (Aporte aporte : a.getSusAportes()) {
            sb.append("     - ").append(aporte.toString())
              .append(String.format(" -> Valor: %.2f%n", aporte.calcularValorTotal()));
        }
        sb.append(String.format("   Valor total de aportes: %.2f%n", a.calcularValorTotalAportes()));
        return sb.toString();
    }

    /** Botón "Mascota": el código viene del campo de consulta.
     * @param losPadrinos
     * @param codigoMascota
     * @return  */
    public static String consultarApadrinamientosDadaMascota(ArrayList<Padrino> losPadrinos,
                                                             String codigoMascota) {
        if (losPadrinos.isEmpty()) {
            return "Aún no hay padrinos registrados.";
        }
        if (codigoMascota.length() != 8) {
            return "Escriba en el campo de consulta el código de la mascota (8 caracteres).";
        }
        StringBuilder sb = new StringBuilder();
        for (Padrino p : losPadrinos) {
            for (Apadrinamiento a : p.getSusApadrinamientos()) {
                if (a.getSuMascota().getCodigo().equals(codigoMascota)) {
                    sb.append("Padrino (cédula ").append(p.getCedula()).append(")\n")
                      .append(describirApadrinamiento(a))
                      .append("-----\n");
                }
            }
        }
        if (sb.length() == 0) {
            return "No hay apadrinamientos para la mascota con código " + codigoMascota + ".";
        }
        return "Apadrinamientos de la mascota " + codigoMascota + ":\n" + sb;
    }

    // ------------------------------------------------------------------
    // ORDENAMIENTO (se ordena una copia para no alterar el orden de registro)
    // ------------------------------------------------------------------
    public static String ordenarPadrinos(ArrayList<Padrino> losPadrinos,
                                         boolean porCedula, boolean ascendente) {
        if (losPadrinos.isEmpty()) {
            return "Aún no hay padrinos registrados.";
        }
        ArrayList<Padrino> copia = new ArrayList<>(losPadrinos);
        Comparator<Padrino> criterio;
        if (porCedula) {
            criterio = Comparator.comparing(Padrino::getCedula);
        } else {
            criterio = Comparator.comparingDouble(Padrino::calcularValorTotalAportes);
        }
        if (!ascendente) {
            criterio = criterio.reversed();
        }
        copia.sort(criterio);

        StringBuilder sb = new StringBuilder("Padrinos ordenados por "
                + (porCedula ? "cédula" : "valor total de aportes")
                + (ascendente ? " (ascendente)" : " (descendente)") + ":\n\n");
        for (Padrino p : copia) {
            sb.append(p.toString())
              .append(String.format("%nValor total de aportes: %.2f%n%n", p.calcularValorTotalAportes()));
        }
        return sb.toString();
    }

    // ------------------------------------------------------------------
    // ARCHIVO DE OBJETOS (con sus excepciones)
    // ------------------------------------------------------------------
    /** Botón "Generar": escribe la colección en el archivo de objetos.
     * @param losPadrinos */
    public static void guardarPadrinos(ArrayList<Padrino> losPadrinos) {
        if (losPadrinos.isEmpty()) {
            error("No hay padrinos para guardar en el archivo.");
            return;
        }
        try (ObjectOutputStream salida = new ObjectOutputStream(new FileOutputStream(ARCHIVO))) {
            salida.writeObject(losPadrinos);
            mostrar("Archivo '" + ARCHIVO + "' generado con " + losPadrinos.size() + " padrino(s).");
        } catch (IOException e) {
            error("Error al escribir el archivo: " + e.getMessage());
        }
    }

    /** Botón "Leer": carga la colección desde el archivo de objetos.
     * @param losPadrinos
     * @return  */
    @SuppressWarnings("unchecked")
    public static ArrayList<Padrino> cargarPadrinos(ArrayList<Padrino> losPadrinos) {
        try (ObjectInputStream entrada = new ObjectInputStream(new FileInputStream(ARCHIVO))) {
            ArrayList<Padrino> leidos = (ArrayList<Padrino>) entrada.readObject();
            txtCodigo.setText(String.valueOf(generarCodigoPadrino(leidos)));
            mostrar("Archivo leído: " + leidos.size() + " padrino(s) cargado(s).\n\n"
                    + consultarTodosLosPadrinos(leidos));
            losPadrinos = leidos;
        } catch (FileNotFoundException e) {
            error("No existe el archivo '" + ARCHIVO + "'. Use primero el botón Generar.");
        } catch (EOFException e) {
            error("El archivo está vacío.");
        } catch (IOException e) {
            error("Error al leer el archivo: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            error("El archivo contiene una clase desconocida: " + e.getMessage());
        }
        return losPadrinos;
    }
}