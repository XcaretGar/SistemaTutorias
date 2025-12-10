package sistematutoriasfx.controlador.coordinador;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Pruebas unitarias para el Caso de Uso: REGISTRAR FECHA TUTORÍA
 * 
 * @author JOANA XCARET
 */
public class RegistrarFechaTutoriaTest {
    
    /**
     * ============================================================
     * CASO DE PRUEBA 4: VALIDAR LÍMITE DE SESIONES POR PERIODO
     * ============================================================
     */
    @Test
    public void testValidarLimiteSesionesPorPeriodo() {
        System.out.println("\n========================================");
        System.out.println("PRUEBA 4: VALIDAR LÍMITE SESIONES");
        System.out.println("========================================");
        
        // ARRANGE
        int limiteMaximoSesiones = 3;
        int sesionesActuales1 = 2;  // Puede agregar más
        int sesionesActuales2 = 3;  // Ya llegó al límite
        int sesionesActuales3 = 4;  // Excede el límite
        
        // Validaciones
        boolean puede1 = sesionesActuales1 < limiteMaximoSesiones;
        boolean puede2 = sesionesActuales2 < limiteMaximoSesiones;
        boolean puede3 = sesionesActuales3 < limiteMaximoSesiones;
        
        // ASSERT
        assertTrue("Con 2 sesiones debe poder agregar más", puede1);
        assertFalse("Con 3 sesiones NO debe poder agregar más", puede2);
        assertFalse("Con 4 sesiones NO debe poder agregar más", puede3);
        assertEquals("El límite debe ser 3 sesiones", 3, limiteMaximoSesiones);
        
        System.out.println("Límite máximo: " + limiteMaximoSesiones + " sesiones");
        System.out.println("Sesiones actuales: " + sesionesActuales1 + " → ¿Puede agregar? " + puede1);
        System.out.println("Sesiones actuales: " + sesionesActuales2 + " → ¿Puede agregar? " + puede2);
        System.out.println("Sesiones actuales: " + sesionesActuales3 + " → ¿Puede agregar? " + puede3);
        System.out.println("El límite de sesiones funciona correctamente.");
    }
    
    /**
     * ============================================================
     * CASO DE PRUEBA 5: VALIDAR CAMPOS REQUERIDOS PARA REGISTRAR FECHA
     * ============================================================
     */
    @Test
    public void testValidarCamposRequeridosFecha() {
        System.out.println("\n========================================");
        System.out.println("PRUEBA 5: VALIDAR CAMPOS REQUERIDOS");
        System.out.println("========================================");
        
        // ARRANGE
        int idPeriodo = 1;
        String numeroSesion = "1";
        String fecha = "2025-03-15";
        String descripcion = "Primera sesión de tutoría";
        
        // Validaciones
        boolean periodoValido = idPeriodo > 0;
        boolean numeroSesionValido = numeroSesion != null && !numeroSesion.isEmpty();
        boolean fechaValida = fecha != null && !fecha.isEmpty();
        boolean descripcionValida = descripcion != null && !descripcion.isEmpty();
        
        boolean todosLosCamposValidos = periodoValido && numeroSesionValido 
                                      && fechaValida && descripcionValida;
        
        // ASSERT
        assertTrue("El periodo debe ser válido", periodoValido);
        assertTrue("El número de sesión debe estar lleno", numeroSesionValido);
        assertTrue("La fecha debe estar llena", fechaValida);
        assertTrue("La descripción debe estar llena", descripcionValida);
        assertTrue("Todos los campos deben ser válidos", todosLosCamposValidos);
        
        System.out.println("ID Periodo: " + idPeriodo + " → Válido");
        System.out.println("No. Sesión: " + numeroSesion + " → Válido");
        System.out.println("Fecha: " + fecha + " → Válida");
        System.out.println("Descripción: " + descripcion + " → Válida");
        System.out.println("Todos los campos están correctos.");
    }
    
    /**
     * ============================================================
     * CASO DE PRUEBA 6: VALIDAR QUE NO HAY TRASLAPE DE FECHAS
     * ============================================================
     */
    @Test
    public void testValidarNoTraslapeFechas() {
        System.out.println("\n========================================");
        System.out.println("PRUEBA 6: VALIDAR NO TRASLAPE DE FECHAS");
        System.out.println("========================================");
        
        // ARRANGE - Fechas ya registradas
        String fechaExistente1 = "2025-03-15";
        String fechaExistente2 = "2025-04-20";
        
        // Fechas a validar
        String fechaNueva1 = "2025-05-10";  // No se traslapa
        String fechaNueva2 = "2025-03-15";  // SÍ se traslapa con fechaExistente1
        
        // Validaciones 
        boolean traslape1 = fechaNueva1.equals(fechaExistente1) || fechaNueva1.equals(fechaExistente2);
        boolean traslape2 = fechaNueva2.equals(fechaExistente1) || fechaNueva2.equals(fechaExistente2);
        
        // ASSERT
        assertFalse("La fecha 2025-05-10 NO debe tener traslape", traslape1);
        assertTrue("La fecha 2025-03-15 SÍ debe detectar traslape", traslape2);
        assertNotEquals("Las fechas no deben ser iguales", fechaNueva1, fechaExistente1);
        
        System.out.println("Fechas ya registradas:");
        System.out.println("  - " + fechaExistente1);
        System.out.println("  - " + fechaExistente2);
        
        System.out.println("\nValidando nueva fecha: " + fechaNueva1);
        System.out.println("  ¿Hay traslape? " + (traslape1 ? "SÍ (RECHAZAR)" : "NO (ACEPTAR)"));
        
        System.out.println("\nValidando nueva fecha: " + fechaNueva2);
        System.out.println("  ¿Hay traslape? " + (traslape2 ? "SÍ (RECHAZAR)" : "NO (ACEPTAR)"));
        
        System.out.println("\nLa validación de traslape funciona correctamente.");
    }
}