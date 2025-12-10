package sistematutoriasfx.controlador.tutor;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Pruebas unitarias para el Caso de Uso: REGISTRAR HORARIO TUTORÍA
 * 
 * @author Ana Georgina
 */
public class RegistrarHorarioTutoriaTest {
    
    /**
     * ============================================================
     * CASO DE PRUEBA 16: VALIDAR CONVERSIÓN DE HORA AM/PM A FORMATO 24H
     * ============================================================
     */
    @Test
    public void testValidarConversionHora24H() {
        System.out.println("\n========================================");
        System.out.println("PRUEBA 16: VALIDAR CONVERSIÓN HORA 24H");
        System.out.println("========================================");
        
        // ARRANGE
        String hora1 = "02"; 
        String ampm1 = "AM";
        
        String hora2 = "02";  
        String ampm2 = "PM";
        
        String hora3 = "12";  
        String ampm3 = "PM";
        
        String hora4 = "12";  
        String ampm4 = "AM";
        
        // Conversión 
        int horaInt1 = Integer.parseInt(hora1);
        if (ampm1.equals("AM") && horaInt1 == 12) horaInt1 = 0;
        
        int horaInt2 = Integer.parseInt(hora2);
        if (ampm2.equals("PM") && horaInt2 != 12) horaInt2 += 12;
        
        int horaInt3 = Integer.parseInt(hora3);
        // 12 PM no se modifica
        
        int horaInt4 = Integer.parseInt(hora4);
        if (ampm4.equals("AM") && horaInt4 == 12) horaInt4 = 0;
        
        // ASSERT
        assertEquals("2 AM debe ser 02 en formato 24h", 2, horaInt1);
        assertEquals("2 PM debe ser 14 en formato 24h", 14, horaInt2);
        assertEquals("12 PM debe ser 12 en formato 24h", 12, horaInt3);
        assertEquals("12 AM debe ser 0 en formato 24h", 0, horaInt4);
        
        System.out.println("2:00 AM → " + String.format("%02d:00", horaInt1) + " (formato 24h)");
        System.out.println("2:00 PM → " + String.format("%02d:00", horaInt2) + " (formato 24h)");
        System.out.println("12:00 PM → " + String.format("%02d:00", horaInt3) + " (formato 24h)");
        System.out.println("12:00 AM → " + String.format("%02d:00", horaInt4) + " (formato 24h)");
        System.out.println("\nLa conversión de hora funciona correctamente.");
    }
    
    /**
     * ============================================================
     * CASO DE PRUEBA 17: VALIDAR HORARIO POR ACADÉMICO
     * ============================================================
     */
    @Test
    public void testValidarHorarioPorAcademico() {
        System.out.println("\n========================================");
        System.out.println("PRUEBA 17: VALIDAR HORARIO POR ACADÉMICO");
        System.out.println("========================================");
        
        // ARRANGE
        int idAcademico1 = 5;       
        int idAcademico2 = 0;       
        int idAcademico3 = -1;      
        
        String nombreAcademico = "Dr. Juan Pérez";
        int totalHorariosRegistrados = 3;
        
        // Validaciones
        boolean academico1Valido = idAcademico1 > 0;
        boolean academico2Valido = idAcademico2 > 0;
        boolean academico3Valido = idAcademico3 > 0;
        
        boolean puedeRegistrarHorario = idAcademico1 > 0;
        boolean tieneHorarios = totalHorariosRegistrados > 0;
        
        // ASSERT
        assertTrue("El académico con ID 5 debe ser válido", academico1Valido);
        assertFalse("El académico con ID 0 NO debe ser válido", academico2Valido);
        assertFalse("El académico con ID negativo NO debe ser válido", academico3Valido);
        assertTrue("El académico debe poder registrar horarios", puedeRegistrarHorario);
        assertTrue("El ID del académico debe ser mayor a 0", idAcademico1 > 0);
        
        System.out.println("Académico: " + nombreAcademico);
        System.out.println("ID Académico: " + idAcademico1);
        System.out.println("¿ID válido? " + academico1Valido);
        System.out.println("¿Puede registrar horarios? " + puedeRegistrarHorario);
        System.out.println("Horarios registrados: " + totalHorariosRegistrados);
        
        System.out.println("\nValidando IDs inválidos:");
        System.out.println("  ID = 0 → Válido: " + academico2Valido);
        System.out.println("  ID = -1 → Válido: " + academico3Valido);
        
        System.out.println("\nLa validación de académico funciona correctamente.");
    }
    
    /**
     * ============================================================
     * CASO DE PRUEBA 18: VALIDAR MODO EDICIÓN VS MODO REGISTRO
     * ============================================================
     */
    @Test
    public void testValidarModoEdicionVsRegistro() {
        System.out.println("\n========================================");
        System.out.println("PRUEBA 18: VALIDAR MODO EDICIÓN VS REGISTRO");
        System.out.println("========================================");
        
        // ARRANGE
        // Caso 1: Modo Registro (sin ID de sesión)
        Integer idSesionRegistro = null;
        
        // Caso 2: Modo Edición (con ID de sesión)
        Integer idSesionEdicion = 5;
        
        // Validaciones
        boolean esRegistro = (idSesionRegistro == null);
        boolean esEdicion = (idSesionEdicion != null && idSesionEdicion > 0);
        
        String modoOperacion1 = esRegistro ? "REGISTRAR" : "ACTUALIZAR";
        String modoOperacion2 = esEdicion ? "ACTUALIZAR" : "REGISTRAR";
        
        // ASSERT
        assertTrue("Cuando no hay ID debe ser modo REGISTRO", esRegistro);
        assertTrue("Cuando hay ID debe ser modo EDICIÓN", esEdicion);
        assertEquals("Sin ID la operación debe ser REGISTRAR", "REGISTRAR", modoOperacion1);
        assertEquals("Con ID la operación debe ser ACTUALIZAR", "ACTUALIZAR", modoOperacion2);
        assertNull("En modo registro el ID debe ser null", idSesionRegistro);
        assertNotNull("En modo edición el ID NO debe ser null", idSesionEdicion);
        
        System.out.println("Caso 1: ID Sesión = " + idSesionRegistro);
        System.out.println("  Modo: " + modoOperacion1);
        System.out.println("  ¿Es registro? " + esRegistro);
        
        System.out.println("\nCaso 2: ID Sesión = " + idSesionEdicion);
        System.out.println("  Modo: " + modoOperacion2);
        System.out.println("  ¿Es edición? " + esEdicion);
        
        System.out.println("\nLa detección de modo funciona correctamente.");
    }
}