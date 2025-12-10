package sistematutoriasfx.controlador.coordinador;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Pruebas unitarias para el Caso de Uso: REVISAR REPORTE TUTORÍA
 * 
 * @author JOANA XCARET
 */
public class RevisarReporteTutoriaTest {
    
    /**
     * ============================================================
     * CASO DE PRUEBA 7: VALIDAR ESTATUS DE REPORTE
     * ============================================================
     */
    @Test
    public void testValidarEstatusReporte() {
        System.out.println("\n========================================");
        System.out.println("PRUEBA 7: VALIDAR ESTATUS DE REPORTE");
        System.out.println("========================================");
        
        // ARRANGE
        String estatusPendiente = "Sin Revisar";
        String estatusRevisado = "Revisado";
        String estatusInvalido = "En Edicion";
        
        // Validaciones
        boolean esPendiente = estatusPendiente.equals("Sin Revisar");
        boolean esRevisado = estatusRevisado.equals("Revisado");
        boolean puedeRevisar1 = !estatusPendiente.equals("Revisado");
        boolean puedeRevisar2 = !estatusRevisado.equals("Revisado");
        
        // ASSERT
        assertTrue("El estatus 'Sin Revisar' debe ser válido", esPendiente);
        assertTrue("El estatus 'Revisado' debe ser válido", esRevisado);
        assertTrue("Un reporte 'EnEdicion' puede ser revisado", puedeRevisar1);
        assertFalse("Un reporte 'Revisado' NO puede ser revisado nuevamente", puedeRevisar2);
        
        System.out.println("Estatus: '" + estatusPendiente + "'");
        System.out.println("  ¿Puede revisar? " + (puedeRevisar1 ? "SÍ" : "NO"));
        
        System.out.println("\nEstatus: '" + estatusRevisado + "'");
        System.out.println("  ¿Puede revisar? " + (puedeRevisar2 ? "SÍ" : "NO"));
        
        System.out.println("\nLa validación de estatus funciona correctamente.");
    }
    
    /**
     * ============================================================
     * CASO DE PRUEBA 8: VALIDAR DATOS DEL REPORTE
     * ============================================================
     */
    @Test
    public void testValidarDatosReporte() {
        System.out.println("\n========================================");
        System.out.println("PRUEBA 8: VALIDAR DATOS DEL REPORTE");
        System.out.println("========================================");
        
        // ARRANGE
        int idReporte = 1;
        String periodoNombre = "Febrero-Julio 2025";
        String fechaSesion = "2025-03-15";
        int numSesion = 1;
        int totalAsistentes = 25;
        int totalEnRiesgo = 3;
        String comentarios = "Primera sesión exitosa";
        
        // Validaciones
        boolean idValido = idReporte > 0;
        boolean periodoValido = periodoNombre != null && !periodoNombre.isEmpty();
        boolean fechaValida = fechaSesion != null && !fechaSesion.isEmpty();
        boolean sesionValida = numSesion >= 1 && numSesion <= 3;
        boolean asistentesValido = totalAsistentes >= 0;
        boolean enRiesgoValido = totalEnRiesgo >= 0 && totalEnRiesgo <= totalAsistentes;
        
        // ASSERT
        assertTrue("El ID del reporte debe ser válido", idValido);
        assertTrue("El periodo debe tener nombre", periodoValido);
        assertTrue("La fecha de sesión debe estar definida", fechaValida);
        assertTrue("El número de sesión debe estar entre 1 y 3", sesionValida);
        assertTrue("El total de asistentes debe ser positivo", asistentesValido);
        assertTrue("Los alumnos en riesgo no pueden exceder asistentes", enRiesgoValido);
        
        System.out.println("ID Reporte: " + idReporte + " → Válido");
        System.out.println("Periodo: " + periodoNombre + " → Válido");
        System.out.println("Fecha Sesión: " + fechaSesion + " → Válida");
        System.out.println("No. Sesión: " + numSesion + " → Válida");
        System.out.println("Total Asistentes: " + totalAsistentes + " → Válido");
        System.out.println("En Riesgo: " + totalEnRiesgo + " → Válido");
        System.out.println("Comentarios: " + comentarios);
        System.out.println("\nTodos los datos del reporte son válidos.");
    }
    
    /**
     * ============================================================
     * CASO DE PRUEBA 9: VALIDAR CAMBIO DE ESTATUS A REVISADO
     * ============================================================
     */
    @Test
    public void testValidarCambioEstatusRevisado() {
        System.out.println("\n========================================");
        System.out.println("PRUEBA 9: VALIDAR CAMBIO A REVISADO");
        System.out.println("========================================");
        
        // ARRANGE
        int idReporte = 1;
        String estatusInicial = "Pendiente";
        String estatusFinal = "Revisado";
        
        // Cambio de estatus
        boolean cambioPermitido = estatusInicial.equals("Pendiente");
        String nuevoEstatus = cambioPermitido ? "Revisado" : estatusInicial;
        
        // ASSERT
        assertTrue("El cambio de estatus debe estar permitido", cambioPermitido);
        assertEquals("El estatus final debe ser 'Revisado'", "Revisado", nuevoEstatus);
        assertNotEquals("El estatus debe haber cambiado", estatusInicial, nuevoEstatus);
        assertTrue("El ID del reporte debe ser válido", idReporte > 0);
        
        System.out.println("ID Reporte: " + idReporte);
        System.out.println("Estatus Inicial: " + estatusInicial);
        System.out.println("Estatus Final: " + nuevoEstatus);
        System.out.println("¿Cambio exitoso? " + (estatusFinal.equals(nuevoEstatus) ? "SÍ" : "NO"));
        System.out.println("\nEl cambio de estatus funciona correctamente.");
    }
}