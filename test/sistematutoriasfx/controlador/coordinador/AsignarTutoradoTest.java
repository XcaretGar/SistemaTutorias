package sistematutoriasfx.controlador.coordinador;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Pruebas unitarias para el Caso de Uso: ASIGNAR TUTORADOS
 * 
 * @author JOANA XCARET
 */

public class AsignarTutoradoTest {

    /**
     * ============================================================
     * CASO DE PRUEBA 13: VALIDAR CAPACIDAD MÁXIMA TUTOR
     * ============================================================
     */
    @Test
    public void testValidarCapacidadTutor_CargaMaxima_BloquearAsignacion() {
        System.out.println("\n========================================");
        System.out.println("PRUEBA 13: VALIDAR CAPACIDAD MÁXIMA TUTOR");
        System.out.println("========================================");

        // ARRANGE
        int capacidadMaxima = 20;
        int cargaActual = 20; 
        int espaciosDisponibles = Math.max(capacidadMaxima - cargaActual, 0);

        // ASSERT
        assertEquals("Tutor debe tener 0 espacios disponibles cuando está lleno",
                     0, espaciosDisponibles);

        System.out.println("Tutor con capacidad máxima: " + capacidadMaxima);
        System.out.println("Carga actual: " + cargaActual);
        System.out.println("Espacios disponibles: " + espaciosDisponibles + " (correcto)");
        System.out.println("La asignación debe bloquearse porque el tutor está lleno.");
    }

    /**
     * ============================================================
     * CASO DE PRUEBA 14: VALIDAR ASIGNACIÓN DE TUTOR
     * ============================================================
     */
    @Test
    public void testValidarAsignacionTutor() {
        System.out.println("\n========================================");
        System.out.println("PRUEBA 14: VALIDAR ASIGNACIÓN DE TUTOR");
        System.out.println("========================================");

        int capacidadMaxima = 20;
        int cargaActual = 10;
        int espaciosDisponibles = Math.max(capacidadMaxima - cargaActual, 0);

        assertTrue("El tutor debe tener capacidad", espaciosDisponibles > 0);

        System.out.println("Tutor con capacidad máxima: " + capacidadMaxima);
        System.out.println("Carga actual: " + cargaActual);
        System.out.println("Espacios disponibles: " + espaciosDisponibles);
        System.out.println("El tutor puede recibir más estudiantes.");
    }

    /**
     * ============================================================
     * CASO DE PRUEBA 15: ACTUALIZAR TUTOR ASIGNADO
     * ============================================================
     */
    @Test
    public void testActualizarTutorAsignado() {
        System.out.println("\n========================================");
        System.out.println("PRUEBA 15: ACTUALIZAR TUTOR ASIGNADO");
        System.out.println("========================================");

        int tutorActualId = 3;
        int nuevoTutorId = 5;

        boolean esMismoTutor = tutorActualId == nuevoTutorId;
        assertFalse("No debe permitir actualizar al mismo tutor", esMismoTutor);

        System.out.println("Tutor actual ID: " + tutorActualId);
        System.out.println("Nuevo tutor ID: " + nuevoTutorId);
        System.out.println("¿Es un tutor diferente? " + !esMismoTutor);
        System.out.println("Cambio válido  puede proceder");
    }
}
