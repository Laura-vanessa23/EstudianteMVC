package com.miapp.vista;

import com.miapp.controlador.EstudianteController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

/**
 * Vista: JFrame principal del módulo Estudiante.
 * Contiene un campo de búsqueda y una tabla de resultados.
 *
 * IMPORTANTE (MVC): esta clase NO conoce ni importa el Modelo (Estudiante).
 * Solo trabaja con tipos genéricos (Object[], List<Object[]>) que el
 * Controlador le entrega ya preparados. Así la Vista queda desacoplada
 * del Modelo y toda la comunicación pasa por el Controlador.
 */
public class EstudianteView extends JFrame {

    // ── Componentes UI ────────────────────────────────────────────────────────
    private JTextField txtNombre;         // Caja para buscar por nombre
    private JTextField txtNombreAgregar;  // Caja para el nombre del nuevo estudiante
    private JTextField txtCarrera;        // Caja para la carrera del nuevo estudiante
    private JTextField txtPromedio;       // Caja para el promedio del nuevo estudiante
    private JButton btnBuscar;            // Botón de buscar
    private JButton btnBuscar2;           // Botón de agregar
    private JComboBox<String> cmbCriterio; // Selector de criterio para ordenar
    private JButton btnOrdenar;           // Botón de ordenar
    private JTable tblResultados;
    private DefaultTableModel modeloTabla;
    private JLabel lblEstado;
    private JButton btnMostrarTodos;

    // ── Controlador ───────────────────────────────────────────────────────────
    private EstudianteController controlador;

    // ── Constructor ───────────────────────────────────────────────────────────
    public EstudianteView() {
        initComponentes();
        initEventos();
    }

    // ── Inicialización de componentes ─────────────────────────────────────────
    private void initComponentes() {
        setTitle("Búsqueda de Estudiantes — MVC NetBeans");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(750, 550);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // 1. Panel superior — Barra de búsqueda
        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelBusqueda.setBorder(BorderFactory.createTitledBorder("Buscar estudiante"));
        
        JLabel lblNombre = new JLabel("Nombre:");
        txtNombre = new JTextField(20);
        btnBuscar = new JButton("Buscar");
        btnBuscar.setBackground(new Color(59, 139, 212));
        btnBuscar.setForeground(Color.WHITE);
        btnBuscar.setFocusPainted(false);

        panelBusqueda.add(lblNombre);
        panelBusqueda.add(txtNombre);
        panelBusqueda.add(btnBuscar);
        
        // Crea el botón "Mostrar todos"
        btnMostrarTodos = new JButton("Mostrar todos");
        btnMostrarTodos.setBackground(new Color(153, 102, 204)); 
        btnMostrarTodos.setForeground(Color.WHITE);
        btnMostrarTodos.setFocusPainted(false);
        btnMostrarTodos.setFont(new Font("Arial", Font.BOLD, 12));

        panelBusqueda.add(lblNombre);
        panelBusqueda.add(txtNombre);
        panelBusqueda.add(btnBuscar);
        panelBusqueda.add(btnMostrarTodos); // Lo añadE al panel superior

        // 2. Panel superior 2 — Formulario para Añadir Estudiante
        JPanel panelBusqueda2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        panelBusqueda2.setBorder(BorderFactory.createTitledBorder("Agregar estudiante"));
        
        JLabel lblNom = new JLabel("Nombre:");
        txtNombreAgregar = new JTextField(10);
        
        JLabel lblCar = new JLabel("Carrera:");
        txtCarrera = new JTextField(10);
        
        JLabel lblProm = new JLabel("Promedio:");
        txtPromedio = new JTextField(5);
        
        btnBuscar2 = new JButton("Agregar");
        btnBuscar2.setBackground(new Color(111, 66, 193)); 
        btnBuscar2.setForeground(Color.WHITE);             
        btnBuscar2.setFocusPainted(false);                 
        btnBuscar2.setFont(new Font("Arial", Font.BOLD, 12));

        panelBusqueda2.add(lblNom);
        panelBusqueda2.add(txtNombreAgregar);
        panelBusqueda2.add(lblCar);
        panelBusqueda2.add(txtCarrera);
        panelBusqueda2.add(lblProm);
        panelBusqueda2.add(txtPromedio);
        panelBusqueda2.add(btnBuscar2);

        // 3. Panel superior 3 — Ordenamiento de Resultados (Enunciado 2)
        JPanel panelOrden = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        panelOrden.setBorder(BorderFactory.createTitledBorder("Ordenar resultados"));

        cmbCriterio = new JComboBox<>(new String[]{"Nombre", "Promedio"});
        btnOrdenar = new JButton("Ordenar");
        btnOrdenar.setBackground(new Color(214, 51, 132)); 
        btnOrdenar.setForeground(Color.WHITE);
        btnOrdenar.setFocusPainted(false);
        btnOrdenar.setFont(new Font("Arial", Font.BOLD, 12));

        panelOrden.add(new JLabel("Ordenar por:"));
        panelOrden.add(cmbCriterio);
        panelOrden.add(btnOrdenar);

        // Contenedor para poner los tres paneles arriba (uno sobre otro)
        JPanel panelNorte = new JPanel();
        panelNorte.setLayout(new BoxLayout(panelNorte, BoxLayout.Y_AXIS));
        panelNorte.add(panelBusqueda);
        panelNorte.add(panelBusqueda2);
        panelNorte.add(panelOrden);

        // 4. Panel central — Tabla de resultados
        String[] columnas = {"ID", "Nombre", "Carrera", "Promedio"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tblResultados = new JTable(modeloTabla);
        tblResultados.setRowHeight(24);
        tblResultados.getTableHeader().setReorderingAllowed(false);
        tblResultados.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scroll = new JScrollPane(tblResultados);
        scroll.setBorder(BorderFactory.createTitledBorder("Resultados"));

        // 5. Panel inferior — Estado
        lblEstado = new JLabel("Ingrese un nombre, agregue o filtre los datos.");
        lblEstado.setBorder(BorderFactory.createEmptyBorder(4, 10, 4, 10));
        lblEstado.setForeground(Color.GRAY);

        // Añadimos las secciones principales a la ventana
        add(panelNorte, BorderLayout.NORTH);
        add(scroll,     BorderLayout.CENTER);
        add(lblEstado,  BorderLayout.SOUTH);
    }

    // ── Eventos ───────────────────────────────────────────────────────────────
    private void initEventos() {
        // Evento para el botón de Buscar
        btnBuscar.addActionListener((ActionEvent e) -> {
            if (controlador != null) {
                controlador.buscarEstudiante(txtNombre.getText().trim());
            }
        });

        // Buscar al presionar Enter en el campo de texto de búsqueda
        txtNombre.addActionListener((ActionEvent e) -> btnBuscar.doClick());

        // Evento para el botón de Agregar (Enunciado 1)
        btnBuscar2.addActionListener((ActionEvent e) -> {
            if (controlador != null) {
                try {
                    String nombre = txtNombreAgregar.getText().trim();
                    String carrera = txtCarrera.getText().trim();
                    double promedio = Double.parseDouble(txtPromedio.getText().trim());

                    controlador.agregarEstudiante(nombre, carrera, promedio);

                    txtNombreAgregar.setText("");
                    txtCarrera.setText("");
                    txtPromedio.setText("");

                } catch (NumberFormatException ex) {
                    mostrarError("El promedio debe ser un número válido (ej. 4.5).");
                }
            }
        });

        // Evento para el botón de Ordenar (Enunciado 2)
        btnOrdenar.addActionListener((ActionEvent e) -> {
            if (controlador != null) {
                String criterioSeleccionado = (String) cmbCriterio.getSelectedItem();
                controlador.ordenarPor(criterioSeleccionado);
            }
        });
        
        // Evento para el botón "Mostrar todos" (Enunciado 3)
        btnMostrarTodos.addActionListener((ActionEvent e) -> {
            if (controlador != null) {
                controlador.mostrarTodos();
            }
        });
    }

    // ── Métodos públicos que llama el Controlador ─────────────────────────────
    public void mostrarEstudiante(Object[] fila) {
        limpiarTabla();
        agregarFila(fila);
        setEstado("Se encontró 1 estudiante.");
    }

    public void mostrarEstudiantes(List<Object[]> filas) {
        limpiarTabla();
        if (filas == null || filas.isEmpty()) {
            setEstado("No se encontraron registros.");
            return;
        }
        for (Object[] fila : filas) {
            agregarFila(fila);
        }
        setEstado("Se mostraron " + filas.size() + " estudiante(s).");
    }

    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
        setEstado("Error: " + mensaje);
    }

    public void mostrarConfirmacion(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    public String getNombreBuscado() {
        return txtNombre.getText().trim();
    }

    // ── Setter del controlador ────────────────────────────────────────────────
    public void setControlador(EstudianteController controlador) {
        this.controlador = controlador;
    }

    // ── Helpers privados ──────────────────────────────────────────────────────
    private void agregarFila(Object[] fila) {
        modeloTabla.addRow(fila);
    }

    private void limpiarTabla() {
        modeloTabla.setRowCount(0);
    }

    private void setEstado(String texto) {
        lblEstado.setText(texto);
    }
}