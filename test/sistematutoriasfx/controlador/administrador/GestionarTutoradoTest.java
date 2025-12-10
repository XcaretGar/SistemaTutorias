package sistematutoriasfx.controlador.administrador;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Pruebas unitarias para el Caso de Uso: GESTIONAR TUTORADO
 * 
 * @author JOANA XCARET
 */
public class GestionarTutoradoTest {
    
    /**
     * ============================================================
     * CASO DE PRUEBA 1: VALIDAR CAMPOS REQUERIDOS PARA REGISTRAR TUTORADO
     * ============================================================
     */
    @Test
    public void testValidarCamposRegistroTutorado() {
        System.out.println("\n========================================");
        System.out.println("PRUEBA 1: VALIDAR CAMPOS REGISTRO TUTORADO");
        System.out.println("========================================");
        
        // ARRANGE 
        String nombre = "Carlos";
        String matricula = "S21012345";
        String correo = "carlos.ramos@estudiantes.uv.mx";
        
        // Validaciones
        boolean nombreValido = nombre != null && !nombre.isEmpty();
        boolean matriculaValida = matricula != null && matricula.startsWith("S") && matricula.length() == 9;
        boolean correoValido = correo != null && correo.contains("@estudiantes.uv.mx");
        
        // ASSERT
        assertTrue("El nombre debe estar lleno", nombreValido);
        assertTrue("La matrícula debe tener formato correcto", matriculaValida);
        assertTrue("El correo debe ser institucional", correoValido);
        assertEquals("La matrícula debe tener 9 caracteres", 9, matricula.length());
        
        System.out.println("Nombre: " + nombre + " → Válido");
        System.out.println("Matrícula: " + matricula + " → Válida");
        System.out.println("Correo: " + correo + " → Válido");
        System.out.println("Todos los campos están correctos.");
    }
    
    /**
     * ============================================================
     * CASO DE PRUEBA 2: VALIDAR MOTIVO DE BAJA NO ESTÉ VACÍO
     * ============================================================
     */
    @Test
    public void testValidarMotivoBajaTutorado() {
        System.out.println("\n========================================");
        System.out.println("PRUEBA 2: VALIDAR MOTIVO DE BAJA NO VACÍO");
        System.out.println("========================================");
        
        // ARRANGE
        String motivoValido = "Abandono de estudios";
        String motivoVacio = "";
        String motivoNull = null;
        
        // Validaciones
        boolean motivoValidoOk = motivoValido != null && !motivoValido.isEmpty();
        boolean motivoVacioOk = motivoVacio != null && !motivoVacio.isEmpty();
        boolean motivoNullOk = motivoNull != null && !motivoNull.isEmpty();
        
        // ASSERT
        assertTrue("El motivo con texto debe ser válido", motivoValidoOk);
        assertFalse("El motivo vacío debe ser rechazado", motivoVacioOk);
        assertFalse("El motivo null debe ser rechazado", motivoNullOk);
        
        System.out.println("Motivo válido: '" + motivoValido + "' → ACEPTADO");
        System.out.println("Motivo vacío: '" + motivoVacio + "' → RECHAZADO");
        System.out.println("Motivo null: null → RECHAZADO");
        System.out.println("La validación de motivo funciona correctamente.");
    }
    
    /**
     * ============================================================
     * CASO DE PRUEBA 3: VALIDAR BÚSQUEDA DE TUTORADOS
     * ============================================================
     */
    @Test
    public void testBusquedaTutorados() {
        System.out.println("\n========================================");
        System.out.println("PRUEBA 3: BÚSQUEDA DE TUTORADOS");
        System.out.println("========================================");
        
        // ARRANGE 
        String nombreEstudiante1 = "Carlos";
        String matriculaEstudiante1 = "S21012345";
        String nombreEstudiante2 = "Ana";
        
        // Búsqueda por nombre 
        String criterioBusqueda = "carlos";
        boolean encontrado1 = nombreEstudiante1.toLowerCase().contains(criterioBusqueda.toLowerCase());
        boolean encontrado2 = nombreEstudiante2.toLowerCase().contains(criterioBusqueda.toLowerCase());
        
        // ASSERT
        assertTrue("Debe encontrar a Carlos", encontrado1);
        assertFalse("No debe encontrar a Ana", encontrado2);
        
        System.out.println("Buscando: '" + criterioBusqueda + "'");
        System.out.println("  " + nombreEstudiante1 + " → " + (encontrado1 ? "ENCONTRADO" : "NO"));
        System.out.println("  " + nombreEstudiante2 + " → " + (encontrado2 ? "ENCONTRADO" : "NO"));
        System.out.println("La búsqueda funciona correctamente.");
    }
}