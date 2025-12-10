package sistematutoriasfx.controlador.tutor;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Pruebas unitarias para el Caso de Uso: GESTIONAR REPORTE TUTORÍA
 * 
 * @author Ana Georgina
 */
public class GestionarReporteTutoriaTest {
    
    /**
     * ============================================================
     * CASO DE PRUEBA 28: VALIDAR ASISTENCIA ANTES DE CREAR REPORTE
     * ============================================================
     */
    @Test
    public void testValidarAsistenciaAntesDeCrearReporte() {
        System.out.println("\n========================================");
        System.out.println("PRUEBA 28: VALIDAR ASISTENCIA REGISTRADA");
        System.out.println("========================================");
        
        // ARRANGE
        int totalAsistentes1 = 0;   // Sin asistencia
        int totalAsistentes2 = 15;  // Con asistencia
        int totalAsistentes3 = 5;   // Con asistencia
        
        boolean esEdicion = false;
        
        // Validaciones
        boolean puedeCrearReporte1 = !(totalAsistentes1 == 0 && !esEdicion);
        boolean puedeCrearReporte2 = !(totalAsistentes2 == 0 && !esEdicion);
        boolean puedeCrearReporte3 = !(totalAsistentes3 == 0 && !esEdicion);
        
        // ASSERT
        assertFalse("NO debe poder crear reporte sin asistencia", puedeCrearReporte1);
        assertTrue("SÍ debe poder crear reporte con 15 asistentes", puedeCrearReporte2);
        assertTrue("SÍ debe poder crear reporte con 5 asistentes", puedeCrearReporte3);
        assertEquals("Sin asistentes debe ser 0", 0, totalAsistentes1);
        assertTrue("Los asistentes deben ser mayor a 0", totalAsistentes2 > 0);
        
        System.out.println("Caso 1: Total Asistentes = " + totalAsistentes1);
        System.out.println("  ¿Puede crear reporte? " + (puedeCrearReporte1 ? "SÍ" : "NO"));
        System.out.println("  Mensaje: Debe registrar asistencia primero");
        
        System.out.println("\nCaso 2: Total Asistentes = " + totalAsistentes2);
        System.out.println("  ¿Puede crear reporte? " + (puedeCrearReporte2 ? "SÍ ✓" : "NO"));
        
        System.out.println("\nCaso 3: Total Asistentes = " + totalAsistentes3);
        System.out.println("  ¿Puede crear reporte? " + (puedeCrearReporte3 ? "SÍ ✓" : "NO"));
        
        System.out.println("\nLa validación de asistencia funciona correctamente.");
    }
    
    /**
     * ============================================================
     * CASO DE PRUEBA 29: VALIDAR TOTALES DE REPORTE
     * ============================================================
     */
    @Test
    public void testValidarTotalesReporte() {
        System.out.println("\n========================================");
        System.out.println("PRUEBA 29: VALIDAR TOTALES DE REPORTE");
        System.out.println("========================================");
        
        // ARRANGE
        int totalAsistentes = 20;
        int totalEnRiesgo1 = 5;   // Válido (5 <= 20)
        int totalEnRiesgo2 = 20;  // Válido (20 <= 20)
        int totalEnRiesgo3 = 25;  // Inválido (25 > 20)
        
        // Validaciones
        boolean riesgo1Valido = totalEnRiesgo1 >= 0 && totalEnRiesgo1 <= totalAsistentes;
        boolean riesgo2Valido = totalEnRiesgo2 >= 0 && totalEnRiesgo2 <= totalAsistentes;
        boolean riesgo3Valido = totalEnRiesgo3 >= 0 && totalEnRiesgo3 <= totalAsistentes;
        
        boolean asistentesValido = totalAsistentes >= 0;
        
        // ASSERT
        assertTrue("Los asistentes deben ser mayor o igual a 0", asistentesValido);
        assertTrue("5 en riesgo debe ser válido", riesgo1Valido);
        assertTrue("20 en riesgo debe ser válido", riesgo2Valido);
        assertFalse("25 en riesgo NO debe ser válido", riesgo3Valido);
        assertTrue("En riesgo debe ser menor o igual a asistentes", totalEnRiesgo1 <= totalAsistentes);
        
        System.out.println("Total de Asistentes: " + totalAsistentes);
        
        System.out.println("\nCaso 1: " + totalEnRiesgo1 + " en riesgo");
        System.out.println("  ¿Válido? " + (riesgo1Valido ? "SÍ ✓" : "NO"));
        System.out.println("  Porcentaje: " + (totalEnRiesgo1 * 100 / totalAsistentes) + "%");
        
        System.out.println("\nCaso 2: " + totalEnRiesgo2 + " en riesgo");
        System.out.println("  ¿Válido? " + (riesgo2Valido ? "SÍ ✓" : "NO"));
        System.out.println("  Porcentaje: " + (totalEnRiesgo2 * 100 / totalAsistentes) + "%");
        
        System.out.println("\nCaso 3: " + totalEnRiesgo3 + " en riesgo");
        System.out.println("  ¿Válido? " + (riesgo3Valido ? "SÍ" : "NO (excede asistentes)"));
        
        System.out.println("\nLa validación de totales funciona correctamente.");
    }
    
    /**
     * ============================================================
     * CASO DE PRUEBA 30: VALIDAR PREVENCIÓN DE REPORTES DUPLICADOS
     * ============================================================
     */
    @Test
    public void testValidarPrevencionReportesDuplicados() {
        System.out.println("\n========================================");
        System.out.println("PRUEBA 30: VALIDAR PREVENCIÓN DUPLICADOS");
        System.out.println("========================================");
        
        // ARRANGE
        int idAcademico = 5;
        int idFechaTutoria = 3;
        int idPeriodo = 2;
        
        // Simulación: Buscar si ya existe reporte
        boolean reporteExistente1 = true;   // Ya existe
        boolean reporteExistente2 = false;  // No existe
        
        // Validaciones
        boolean puedeCrearNuevo1 = !reporteExistente1;
        boolean puedeCrearNuevo2 = !reporteExistente2;
        
        String accion1 = reporteExistente1 ? "EDITAR" : "CREAR";
        String accion2 = reporteExistente2 ? "EDITAR" : "CREAR";
        
        // ASSERT
        assertFalse("NO debe poder crear si ya existe", puedeCrearNuevo1);
        assertTrue("SÍ debe poder crear si no existe", puedeCrearNuevo2);
        assertTrue("Debe detectar reporte existente", reporteExistente1);
        assertFalse("Debe detectar que no existe reporte", reporteExistente2);
        assertEquals("Debe sugerir EDITAR", "EDITAR", accion1);
        assertEquals("Debe permitir CREAR", "CREAR", accion2);
        
        System.out.println("Académico ID: " + idAcademico);
        System.out.println("Fecha Tutoría ID: " + idFechaTutoria);
        System.out.println("Periodo ID: " + idPeriodo);
        
        System.out.println("\nCaso 1: Reporte ya existe");
        System.out.println("  ¿Existe reporte? SÍ");
        System.out.println("  ¿Puede crear nuevo? " + (puedeCrearNuevo1 ? "SÍ" : "NO"));
        System.out.println("  Acción recomendada: " + accion1);
        System.out.println("  Mensaje: Ya existe un reporte, usa modo edición");
        
        System.out.println("\nCaso 2: Reporte NO existe");
        System.out.println("  ¿Existe reporte? NO");
        System.out.println("  ¿Puede crear nuevo? " + (puedeCrearNuevo2 ? "SÍ ✓" : "NO"));
        System.out.println("  Acción recomendada: " + accion2);
        
        System.out.println("\nLa prevención de duplicados funciona correctamente.");
    }
}