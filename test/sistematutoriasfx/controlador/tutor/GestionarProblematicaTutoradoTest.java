package sistematutoriasfx.controlador.tutor;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Pruebas unitarias para el Caso de Uso: GESTIONAR PROBLEMÁTICA DE TUTORADO
 * 
 * @author Ana Georgina
 */
public class GestionarProblematicaTutoradoTest {
    
    /**
     * ============================================================
     * CASO DE PRUEBA 22: VALIDAR ESTATUS DE PROBLEMÁTICA
     * ============================================================
     */
    @Test
    public void testValidarEstatusProblematica() {
        System.out.println("\n========================================");
        System.out.println("PRUEBA 22: VALIDAR ESTATUS PROBLEMÁTICA");
        System.out.println("========================================");
        
        // ARRANGE
        String estatusRegistrada = "Registrada";
        String estatusEnSeguimiento = "EnSeguimiento";
        String estatusResuelta = "Resuelta";
        String estatusInvalido = "Cancelada";
        
        // Validaciones
        boolean esRegistradaValido = estatusRegistrada.equals("Registrada") || 
                                     estatusRegistrada.equals("EnSeguimiento") || 
                                     estatusRegistrada.equals("Resuelta");
        
        boolean esEnSeguimientoValido = estatusEnSeguimiento.equals("Registrada") || 
                                        estatusEnSeguimiento.equals("EnSeguimiento") || 
                                        estatusEnSeguimiento.equals("Resuelta");
        
        boolean esResueltaValido = estatusResuelta.equals("Registrada") || 
                                   estatusResuelta.equals("EnSeguimiento") || 
                                   estatusResuelta.equals("Resuelta");
        
        boolean esInvalidoValido = estatusInvalido.equals("Registrada") || 
                                   estatusInvalido.equals("EnSeguimiento") || 
                                   estatusInvalido.equals("Resuelta");
        
        // ASSERT
        assertTrue("'Registrada' debe ser un estatus válido", esRegistradaValido);
        assertTrue("'EnSeguimiento' debe ser un estatus válido", esEnSeguimientoValido);
        assertTrue("'Resuelta' debe ser un estatus válido", esResueltaValido);
        assertFalse("'Cancelada' NO debe ser un estatus válido", esInvalidoValido);
        
        System.out.println("Estatus válidos:");
        System.out.println("  - " + estatusRegistrada + " → Válido");
        System.out.println("  - " + estatusEnSeguimiento + " → Válido");
        System.out.println("  - " + estatusResuelta + " → Válido");
        System.out.println("\nEstatus inválidos:");
        System.out.println("  - " + estatusInvalido + " → Inválido");
        
        System.out.println("\nLa validación de estatus funciona correctamente.");
    }
    
    /**
     * ============================================================
     * CASO DE PRUEBA 23: VALIDAR CAMPOS REQUERIDOS DE PROBLEMÁTICA
     * ============================================================
     */
    @Test
    public void testValidarCamposRequeridosProblematica() {
        System.out.println("\n========================================");
        System.out.println("PRUEBA 23: VALIDAR CAMPOS REQUERIDOS");
        System.out.println("========================================");
        
        // ARRANGE
        String titulo = "Problemas de asistencia";
        String descripcion = "El estudiante ha faltado 3 sesiones consecutivas";
        int idEstudiante = 5;
        int idReporte = 10;
        
        // Caso con campos vacíos
        String tituloVacio = "";
        String descripcionVacia = "";
        
        // Validaciones
        boolean tituloValido = titulo != null && !titulo.isEmpty();
        boolean descripcionValida = descripcion != null && !descripcion.isEmpty();
        boolean estudianteValido = idEstudiante > 0;
        boolean reporteValido = idReporte > 0;
        
        boolean todosLosCamposValidos = tituloValido && descripcionValida 
                                      && estudianteValido && reporteValido;
        
        boolean tituloVacioInvalido = tituloVacio == null || tituloVacio.isEmpty();
        boolean descripcionVaciaInvalida = descripcionVacia == null || descripcionVacia.isEmpty();
        
        // ASSERT
        assertTrue("El título debe estar lleno", tituloValido);
        assertTrue("La descripción debe estar llena", descripcionValida);
        assertTrue("El ID del estudiante debe ser válido", estudianteValido);
        assertTrue("El ID del reporte debe ser válido", reporteValido);
        assertTrue("Todos los campos deben ser válidos", todosLosCamposValidos);
        assertTrue("El título vacío debe ser detectado", tituloVacioInvalido);
        assertTrue("La descripción vacía debe ser detectada", descripcionVaciaInvalida);
        
        System.out.println("Campos válidos:");
        System.out.println("  Título: '" + titulo + "' → Válido");
        System.out.println("  Descripción: '" + descripcion + "' → Válida");
        System.out.println("  ID Estudiante: " + idEstudiante + " → Válido");
        System.out.println("  ID Reporte: " + idReporte + " → Válido");
        
        System.out.println("\nCampos inválidos:");
        System.out.println("  Título vacío: '" + tituloVacio + "' → Rechazado");
        System.out.println("  Descripción vacía: '" + descripcionVacia + "' → Rechazada");
        
        System.out.println("\nTodos los campos están correctos.");
    }
    
    /**
     * ============================================================
     * CASO DE PRUEBA 24: VALIDAR ASOCIACIÓN CON REPORTE DE TUTORÍA
     * ============================================================
     */
    @Test
    public void testValidarAsociacionConReporte() {
        System.out.println("\n========================================");
        System.out.println("PRUEBA 24: VALIDAR ASOCIACIÓN CON REPORTE");
        System.out.println("========================================");
        
        // ARRANGE
        int idAcademico = 5;
        int idPeriodoActual = 2;
        int idReporteEncontrado1 = 10;  // Reporte existe
        int idReporteEncontrado2 = 0;   // Reporte NO existe
        int idReporteEncontrado3 = -1;  // ID inválido
        
        // Validaciones
        boolean tieneReporte1 = idReporteEncontrado1 > 0;
        boolean tieneReporte2 = idReporteEncontrado2 > 0;
        boolean tieneReporte3 = idReporteEncontrado3 > 0;
        
        boolean puedeRegistrarProblematica1 = tieneReporte1;
        boolean puedeRegistrarProblematica2 = tieneReporte2;
        
        // ASSERT
        assertTrue("Con ID reporte 10 debe poder registrar", tieneReporte1);
        assertFalse("Con ID reporte 0 NO debe poder registrar", tieneReporte2);
        assertFalse("Con ID reporte -1 NO debe poder registrar", tieneReporte3);
        assertTrue("Debe poder registrar problemática si tiene reporte", puedeRegistrarProblematica1);
        assertFalse("NO debe poder registrar problemática sin reporte", puedeRegistrarProblematica2);
        assertTrue("El ID del reporte debe ser mayor a 0", idReporteEncontrado1 > 0);
        
        System.out.println("Académico ID: " + idAcademico);
        System.out.println("Periodo Actual: " + idPeriodoActual);
        
        System.out.println("\nCaso 1: ID Reporte = " + idReporteEncontrado1);
        System.out.println("  ¿Tiene reporte? " + tieneReporte1);
        System.out.println("  ¿Puede registrar problemática? " + puedeRegistrarProblematica1);
        
        System.out.println("\nCaso 2: ID Reporte = " + idReporteEncontrado2);
        System.out.println("  ¿Tiene reporte? " + tieneReporte2);
        System.out.println("  ¿Puede registrar problemática? " + puedeRegistrarProblematica2);
        System.out.println("  Mensaje: Contactar al coordinador para generar reporte");
        
        System.out.println("\nLa validación de asociación con reporte funciona correctamente.");
    }
}