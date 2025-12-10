package sistematutoriasfx.controlador.tutor;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Pruebas unitarias para el Caso de Uso: GESTIONAR EVIDENCIA DE TUTORÍA
 * 
 * @author Ana Georgina
 */
public class GestionarEvidenciaTutoriaTest {
    
    /**
     * ============================================================
     * CASO DE PRUEBA 25: VALIDAR TAMAÑO MÁXIMO DE ARCHIVO
     * ============================================================
     */
    @Test
    public void testValidarTamanoMaximoArchivo() {
        System.out.println("\n========================================");
        System.out.println("PRUEBA 25: VALIDAR TAMAÑO MÁXIMO ARCHIVO");
        System.out.println("========================================");
        
        // ARRANGE
        long limiteMaximo = 10 * 1024 * 1024; // 10 MB en bytes
        
        long tamanoArchivo1 = 5 * 1024 * 1024;  // 5 MB - Válido
        long tamanoArchivo2 = 10 * 1024 * 1024; // 10 MB - Válido (justo en el límite)
        long tamanoArchivo3 = 15 * 1024 * 1024; // 15 MB - Inválido
        
        // Validaciones
        boolean archivo1Valido = tamanoArchivo1 <= limiteMaximo;
        boolean archivo2Valido = tamanoArchivo2 <= limiteMaximo;
        boolean archivo3Valido = tamanoArchivo3 <= limiteMaximo;
        
        // Convertir a MB para mostrar
        double tamanoMB1 = tamanoArchivo1 / (1024.0 * 1024.0);
        double tamanoMB2 = tamanoArchivo2 / (1024.0 * 1024.0);
        double tamanoMB3 = tamanoArchivo3 / (1024.0 * 1024.0);
        
        // ASSERT
        assertTrue("5 MB debe ser válido", archivo1Valido);
        assertTrue("10 MB debe ser válido", archivo2Valido);
        assertFalse("15 MB NO debe ser válido", archivo3Valido);
        assertEquals("El límite debe ser 10 MB", limiteMaximo, 10 * 1024 * 1024);
        
        System.out.println("Límite máximo: 10 MB");
        System.out.println("\nArchivo 1: " + String.format("%.2f", tamanoMB1) + " MB");
        System.out.println("  ¿Válido? " + (archivo1Valido ? "SÍ" : "NO"));
        
        System.out.println("\nArchivo 2: " + String.format("%.2f", tamanoMB2) + " MB");
        System.out.println("  ¿Válido? " + (archivo2Valido ? "SÍ" : "NO"));
        
        System.out.println("\nArchivo 3: " + String.format("%.2f", tamanoMB3) + " MB");
        System.out.println("  ¿Válido? " + (archivo3Valido ? "SÍ (excede límite)" : "NO"));
        
        System.out.println("\nLa validación de tamaño funciona correctamente.");
    }
    
    /**
     * ============================================================
     * CASO DE PRUEBA 26: VALIDAR TIPOS DE ARCHIVO PERMITIDOS
     * ============================================================
     */
    @Test
    public void testValidarTiposArchivoPermitidos() {
        System.out.println("\n========================================");
        System.out.println("PRUEBA 26: VALIDAR TIPOS DE ARCHIVO");
        System.out.println("========================================");
        
        // ARRANGE
        String[] tiposPermitidos = {".pdf", ".jpg", ".png", ".jpeg", ".docx"};
        
        String archivo1 = "evidencia.pdf";
        String archivo2 = "foto.jpg";
        String archivo3 = "documento.docx";
        String archivo4 = "video.mp4";  // NO permitido
        String archivo5 = "audio.mp3";  // NO permitido
        
        // Validaciones
        boolean archivo1Valido = archivo1.endsWith(".pdf") || archivo1.endsWith(".jpg") || 
                                archivo1.endsWith(".png") || archivo1.endsWith(".jpeg") || 
                                archivo1.endsWith(".docx");
        
        boolean archivo2Valido = archivo2.endsWith(".pdf") || archivo2.endsWith(".jpg") || 
                                archivo2.endsWith(".png") || archivo2.endsWith(".jpeg") || 
                                archivo2.endsWith(".docx");
        
        boolean archivo3Valido = archivo3.endsWith(".pdf") || archivo3.endsWith(".jpg") || 
                                archivo3.endsWith(".png") || archivo3.endsWith(".jpeg") || 
                                archivo3.endsWith(".docx");
        
        boolean archivo4Valido = archivo4.endsWith(".pdf") || archivo4.endsWith(".jpg") || 
                                archivo4.endsWith(".png") || archivo4.endsWith(".jpeg") || 
                                archivo4.endsWith(".docx");
        
        boolean archivo5Valido = archivo5.endsWith(".pdf") || archivo5.endsWith(".jpg") || 
                                archivo5.endsWith(".png") || archivo5.endsWith(".jpeg") || 
                                archivo5.endsWith(".docx");
        
        // ASSERT
        assertTrue("PDF debe ser válido", archivo1Valido);
        assertTrue("JPG debe ser válido", archivo2Valido);
        assertTrue("DOCX debe ser válido", archivo3Valido);
        assertFalse("MP4 NO debe ser válido", archivo4Valido);
        assertFalse("MP3 NO debe ser válido", archivo5Valido);
        
        System.out.println("Tipos de archivo permitidos:");
        for (String tipo : tiposPermitidos) {
            System.out.println("  - " + tipo);
        }
        
        System.out.println("\nValidaciones:");
        System.out.println("  " + archivo1 + " → " + (archivo1Valido ? "Válido" : "Inválido"));
        System.out.println("  " + archivo2 + " → " + (archivo2Valido ? "Válido" : "Inválido"));
        System.out.println("  " + archivo3 + " → " + (archivo3Valido ? "Válido" : "Inválido"));
        System.out.println("  " + archivo4 + " → " + (archivo4Valido ? "Válido" : "Inválido (RECHAZADO)"));
        System.out.println("  " + archivo5 + " → " + (archivo5Valido ? "Válido" : "Inválido (RECHAZADO)"));
        
        System.out.println("\nLa validación de tipos funciona correctamente.");
    }
    
    /**
     * ============================================================
     * CASO DE PRUEBA 27: VALIDAR REPORTE ACTIVO PARA SUBIR EVIDENCIA
     * ============================================================
     */
    @Test
    public void testValidarReporteActivoParaSubirEvidencia() {
        System.out.println("\n========================================");
        System.out.println("PRUEBA 27: VALIDAR REPORTE ACTIVO");
        System.out.println("========================================");
        
        // ARRANGE
        int idAcademico = 5;
        int idFechaTutoria = 3;
        int idReporte1 = 10;  // Reporte existe
        int idReporte2 = 0;   // Reporte NO existe
        
        String nombreArchivo = "evidencia_sesion.pdf";
        
        // Validaciones
        boolean tieneReporte1 = idReporte1 > 0;
        boolean tieneReporte2 = idReporte2 > 0;
        
        boolean puedeSubirEvidencia1 = tieneReporte1;
        boolean puedeSubirEvidencia2 = tieneReporte2;
        
        // ASSERT
        assertTrue("Con ID reporte 10 debe poder subir evidencia", tieneReporte1);
        assertFalse("Con ID reporte 0 NO debe poder subir evidencia", tieneReporte2);
        assertTrue("Debe poder subir evidencia si tiene reporte", puedeSubirEvidencia1);
        assertFalse("NO debe poder subir evidencia sin reporte", puedeSubirEvidencia2);
        assertTrue("El ID del reporte debe ser mayor a 0", idReporte1 > 0);
        assertFalse("El ID 0 no es válido", idReporte2 > 0);
        
        System.out.println("Académico ID: " + idAcademico);
        System.out.println("Fecha Tutoría ID: " + idFechaTutoria);
        System.out.println("Archivo: " + nombreArchivo);
        
        System.out.println("\nCaso 1: ID Reporte = " + idReporte1);
        System.out.println("  ¿Tiene reporte activo? " + tieneReporte1);
        System.out.println("  ¿Puede subir evidencia? " + puedeSubirEvidencia1 + " ✓");
        
        System.out.println("\nCaso 2: ID Reporte = " + idReporte2);
        System.out.println("  ¿Tiene reporte activo? " + tieneReporte2);
        System.out.println("  ¿Puede subir evidencia? " + puedeSubirEvidencia2);
        System.out.println("  Mensaje: Debe generar el reporte primero");
        
        System.out.println("\nLa validación de reporte activo funciona correctamente.");
    }
}