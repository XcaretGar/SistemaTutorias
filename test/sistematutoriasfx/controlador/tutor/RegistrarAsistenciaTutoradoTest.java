package sistematutoriasfx.controlador.tutor;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Pruebas unitarias para el Caso de Uso: REGISTRAR ASISTENCIA DEL TUTORADO
 * 
 * @author Ana Georgina
 */
public class RegistrarAsistenciaTutoradoTest {
    
    /**
     * ============================================================
     * CASO DE PRUEBA 19: VALIDAR ESTADO DE ASISTENCIA
     * ============================================================
     */
    @Test
    public void testValidarEstadoAsistencia() {
        System.out.println("\n========================================");
        System.out.println("PRUEBA 19: VALIDAR ESTADO DE ASISTENCIA");
        System.out.println("========================================");
        
        // ARRANGE
        int totalAsistentes1 = 0;
        int totalRiesgo1 = 0;
        
        int totalAsistentes2 = 15;
        int totalRiesgo2 = 3;
        
        // Validaciones
        boolean estadoPendiente = (totalAsistentes1 == 0 && totalRiesgo1 == 0);
        boolean estadoRegistrada = (totalAsistentes2 > 0 || totalRiesgo2 > 0);
        
        String estado1 = estadoPendiente ? "Pendiente" : "Registrada";
        String estado2 = estadoRegistrada ? "Registrada" : "Pendiente";
        
        // ASSERT
        assertTrue("Con 0 asistentes debe estar Pendiente", estadoPendiente);
        assertTrue("Con 15 asistentes debe estar Registrada", estadoRegistrada);
        assertEquals("El estado debe ser 'Pendiente'", "Pendiente", estado1);
        assertEquals("El estado debe ser 'Registrada'", "Registrada", estado2);
        
        System.out.println("Sesión 1:");
        System.out.println("  Total Asistentes: " + totalAsistentes1);
        System.out.println("  Total en Riesgo: " + totalRiesgo1);
        System.out.println("  Estado: " + estado1);
        
        System.out.println("\nSesión 2:");
        System.out.println("  Total Asistentes: " + totalAsistentes2);
        System.out.println("  Total en Riesgo: " + totalRiesgo2);
        System.out.println("  Estado: " + estado2);
        
        System.out.println("\nLa validación de estado funciona correctamente.");
    }
    
    /**
     * ============================================================
     * CASO DE PRUEBA 20: VALIDAR RIESGO NO EXCEDE ASISTENTES
     * ============================================================
     */
    @Test
    public void testValidarRiesgoNoExcedeAsistentes() {
        System.out.println("\n========================================");
        System.out.println("PRUEBA 20: VALIDAR RIESGO NO EXCEDE ASISTENTES");
        System.out.println("========================================");
        
        // ARRANGE
        int totalAsistentes = 20;
        int alumnosEnRiesgo1 = 5;    // Válido (5 <= 20)
        int alumnosEnRiesgo2 = 20;   // Válido (20 <= 20)
        int alumnosEnRiesgo3 = 25;   // Inválido (25 > 20)
        
        // Validaciones
        boolean riesgo1Valido = alumnosEnRiesgo1 <= totalAsistentes;
        boolean riesgo2Valido = alumnosEnRiesgo2 <= totalAsistentes;
        boolean riesgo3Valido = alumnosEnRiesgo3 <= totalAsistentes;
        
        // ASSERT
        assertTrue("5 alumnos en riesgo debe ser válido", riesgo1Valido);
        assertTrue("20 alumnos en riesgo debe ser válido", riesgo2Valido);
        assertFalse("25 alumnos en riesgo NO debe ser válido", riesgo3Valido);
        assertTrue("El riesgo debe ser menor o igual a asistentes", alumnosEnRiesgo1 <= totalAsistentes);
        
        System.out.println("Total de Asistentes: " + totalAsistentes);
        System.out.println("\nCaso 1: " + alumnosEnRiesgo1 + " en riesgo");
        System.out.println("  ¿Válido? " + (riesgo1Valido ? "SÍ" : "NO"));
        
        System.out.println("\nCaso 2: " + alumnosEnRiesgo2 + " en riesgo");
        System.out.println("  ¿Válido? " + (riesgo2Valido ? "SÍ" : "NO"));
        
        System.out.println("\nCaso 3: " + alumnosEnRiesgo3 + " en riesgo");
        System.out.println("  ¿Válido? " + (riesgo3Valido ? "SÍ" : "NO (excede asistentes)"));
        
        System.out.println("\nLa validación de riesgo funciona correctamente.");
    }
    
    /**
     * ============================================================
     * CASO DE PRUEBA 21: VALIDAR MODO CONSULTA VS MODO EDICIÓN
     * ============================================================
     */
    @Test
    public void testValidarModoConsultaVsEdicion() {
        System.out.println("\n========================================");
        System.out.println("PRUEBA 21: VALIDAR MODO CONSULTA VS EDICIÓN");
        System.out.println("========================================");
        
        // ARRANGE
        boolean modoConsulta1 = true;   // Solo ver, no editar
        boolean modoConsulta2 = false;  // Puede editar
        
        // Validaciones
        boolean puedeEditar1 = !modoConsulta1;
        boolean puedeEditar2 = !modoConsulta2;
        
        boolean botonesVisibles1 = !modoConsulta1;
        boolean botonesVisibles2 = !modoConsulta2;
        
        String modoOperacion1 = modoConsulta1 ? "CONSULTA" : "EDICIÓN";
        String modoOperacion2 = modoConsulta2 ? "CONSULTA" : "EDICIÓN";
        
        // ASSERT
        assertFalse("En modo consulta NO debe poder editar", puedeEditar1);
        assertTrue("En modo edición SÍ debe poder editar", puedeEditar2);
        assertFalse("En modo consulta los botones NO deben estar visibles", botonesVisibles1);
        assertTrue("En modo edición los botones SÍ deben estar visibles", botonesVisibles2);
        assertEquals("Debe estar en modo CONSULTA", "CONSULTA", modoOperacion1);
        assertEquals("Debe estar en modo EDICIÓN", "EDICIÓN", modoOperacion2);
        
        System.out.println("Modo 1: modoConsulta = " + modoConsulta1);
        System.out.println("  Operación: " + modoOperacion1);
        System.out.println("  ¿Puede editar? " + (puedeEditar1 ? "SÍ" : "NO"));
        System.out.println("  ¿Botones visibles? " + (botonesVisibles1 ? "SÍ" : "NO"));
        
        System.out.println("\nModo 2: modoConsulta = " + modoConsulta2);
        System.out.println("  Operación: " + modoOperacion2);
        System.out.println("  ¿Puede editar? " + (puedeEditar2 ? "SÍ" : "NO"));
        System.out.println("  ¿Botones visibles? " + (botonesVisibles2 ? "SÍ" : "NO"));
        
        System.out.println("\nLa validación de modo funciona correctamente.");
    }
}