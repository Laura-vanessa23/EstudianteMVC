package com.miapp.controlador;

import com.miapp.modelo.Estudiante;
import com.miapp.vista.EstudianteView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Controlador: gestiona la lógica entre la Vista y el Modelo.
 * Contiene el array de estudiantes y responde a las búsquedas.
 *
 * IMPORTANTE (MVC): el Controlador es el ÚNICO que conoce tanto la Vista
 * como el Modelo. Es el responsable de traducir objetos Estudiante
 * (Modelo) a Object[] / List<Object[]> (datos "neutros") antes de
 * entregárselos a la Vista. La Vista nunca recibe ni conoce la clase
 * Estudiante directamente.
 */
public class EstudianteController {

    // ── Vista ─────────────────────────────────────────────────────────────────
    private EstudianteView vista;

    // ── Array de estudiantes (fuente de datos) ────────────────────────────────
    private ArrayList<Estudiante> estudiantes;

    // ── Constructor ───────────────────────────────────────────────────────────

    public EstudianteController(EstudianteView vista) {
        this.vista = vista;
        this.vista.setControlador(this);
        cargarDatos();
    }

    // ── Carga de datos iniciales ──────────────────────────────────────────────

    /**
     * Inicializa el array de estudiantes con datos de ejemplo.
     * En un proyecto real este array vendría de una base de datos o servicio.
     */
    private void cargarDatos() {
        estudiantes = new ArrayList<Estudiante> (Arrays.asList(
            new Estudiante(1,  "Ana García",        "Ingeniería de Sistemas",  4.5),
            new Estudiante(2,  "Carlos López",      "Ingeniería Civil",        3.8),
            new Estudiante(3,  "María Rodríguez",   "Medicina",                4.9),
            new Estudiante(4,  "José Martínez",     "Derecho",                 3.5),
            new Estudiante(5,  "Laura Sánchez",     "Administración",          4.1),
            new Estudiante(6,  "Andrés Torres",     "Ingeniería de Sistemas",  3.9),
            new Estudiante(7,  "Valentina Gómez",   "Psicología",              4.3),
            new Estudiante(8,  "Luis Herrera",      "Economía",                3.7),
            new Estudiante(9,  "Sofía Díaz",        "Ingeniería Civil",        4.6),
            new Estudiante(10, "Juliana Morales",   "Medicina",                4.8),
            new Estudiante(11, "Ana Milena Ruiz",   "Derecho",                 4.0),
            new Estudiante(12, "Carlos Andrés Paz", "Administración",          3.6)
        ));
    }

    // ── Lógica de búsqueda ────────────────────────────────────────────────────

    /**
     * Busca estudiantes cuyo nombre contenga el criterio (sin distinción de mayúsculas).
     * Luego llama a vista.mostrarEstudiante(fila) para una coincidencia,
     * o a vista.mostrarEstudiantes(filas) cuando hay varias.
     *
     * @param criterio texto ingresado por el usuario en la Vista
     */
    public void buscarEstudiante(String criterio) {

        // Validación básica
        if (criterio == null || criterio.isEmpty()) {
            vista.mostrarError("Por favor ingrese un nombre para buscar.");
            return;
        }

        List<Estudiante> resultados = new ArrayList<>();
        String criterioBajo = criterio.toLowerCase();

        for (Estudiante e : estudiantes) {
            if (e.getNombre().toLowerCase().contains(criterioBajo)) {
                resultados.add(e);
            }
        }

        if (resultados.isEmpty()) {
            vista.mostrarEstudiantes(new ArrayList<>()); // mostrará mensaje vacío
        } else if (resultados.size() == 1) {
            // Un solo resultado: se convierte a fila y se usa vista.mostrarEstudiante(fila)
            vista.mostrarEstudiante(convertirAFila(resultados.get(0)));
        } else {
            // Varios resultados: se convierte toda la lista antes de enviarla a la Vista
            vista.mostrarEstudiantes(convertirAFilas(resultados));
        }
    }

    // ── Traducción Modelo → datos para la Vista ───────────────────────────────
    // Estos métodos son el "puente" que evita que la Vista dependa de Estudiante.

    /**
     * Convierte un Estudiante (Modelo) en un arreglo genérico que la Vista
     * puede pintar sin conocer la clase Estudiante.
     */
    private Object[] convertirAFila(Estudiante e) {
        return new Object[]{
            e.getId(),
            e.getNombre(),
            e.getCarrera(),
            String.format("%.2f", e.getPromedio())
        };
    }

    /**
     * Convierte una lista de Estudiante en una lista de filas genéricas.
     */
    private List<Object[]> convertirAFilas(List<Estudiante> lista) {
        List<Object[]> filas = new ArrayList<>();
        for (Estudiante e : lista) {
            filas.add(convertirAFila(e));
        }
        return filas;
    }

   
}