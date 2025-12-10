package sistematutoriasfx.controlador.administrador;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Pruebas unitarias para el Caso de Uso: GESTIONAR USUARIO
 * 
 * @author JOANA XCARET
 */
public class GestionarUsuarioTest {
    
    /**
     * ============================================================
     * CASO DE PRUEBA 4: VALIDAR QUE USUARIO TIENE ROL ASIGNADO
     * ============================================================
     */
    @Test
    public void testValidarUsuarioTieneRol() {
        System.out.println("\n========================================");
        System.out.println("PRUEBA 4: VALIDAR USUARIO TIENE ROL");
        System.out.println("========================================");
        
        // ARRANGE 
        String nombreUsuario = "María González";
        String rolAsignado = "Tutor";
        int idRol = 2;
        
        // Validaciones
        boolean tieneRol = rolAsignado != null && !rolAsignado.isEmpty();
        boolean idRolValido = idRol > 0;
        boolean rolEsValido = rolAsignado.equals("Administrador") || 
                             rolAsignado.equals("Tutor") || 
                             rolAsignado.equals("Coordinador");
        
        // ASSERT
        assertTrue("El usuario debe tener un rol asignado", tieneRol);
        assertTrue("El ID del rol debe ser mayor a 0", idRolValido);
        assertTrue("El rol debe ser válido en el sistema", rolEsValido);
        assertNotNull("El rol no puede ser null", rolAsignado);
        
        System.out.println("Usuario: " + nombreUsuario);
        System.out.println("Rol asignado: " + rolAsignado);
        System.out.println("ID Rol: " + idRol);
        System.out.println("¿Rol válido? " + (rolEsValido ? "SÍ" : "NO"));
        System.out.println("El usuario tiene un rol válido asignado.");
    }
    
    /**
     * ============================================================
     * CASO DE PRUEBA 5: VALIDAR DAR DE BAJA USUARIO
     * ============================================================
     */
    @Test
    public void testValidarDarBajaUsuario() {
        System.out.println("\n========================================");
        System.out.println("PRUEBA 5: VALIDAR DAR DE BAJA USUARIO");
        System.out.println("========================================");
        
        // ARRANGE
        int idUsuarioActual = 1;  // Admin que está dando de baja
        int idUsuarioADarBaja = 2; // Usuario a dar de baja
        String motivoBaja = "Finalización de contrato";
        
        // Validaciones
        boolean sonDiferentes = idUsuarioActual != idUsuarioADarBaja;
        boolean motivoValido = motivoBaja.length() >= 10;
        
        // ASSERT
        assertTrue("No puede darse de baja a sí mismo", sonDiferentes);
        assertTrue("El motivo debe tener mínimo 10 caracteres", motivoValido);
        assertNotEquals("Los IDs deben ser diferentes", idUsuarioActual, idUsuarioADarBaja);
        
        System.out.println("ID Usuario Actual: " + idUsuarioActual);
        System.out.println("ID Usuario a Dar Baja: " + idUsuarioADarBaja);
        System.out.println("¿Son diferentes? " + sonDiferentes);
        System.out.println("Motivo: '" + motivoBaja + "' (" + motivoBaja.length() + " chars) Válido");
        System.out.println("La baja puede proceder.");
    }
    
    /**
     * ============================================================
     * CASO DE PRUEBA 6: VALIDAR LISTA DE USUARIOS
     * ============================================================
     */
    @Test
    public void testValidarListaUsuarios() {
        System.out.println("\n========================================");
        System.out.println("PRUEBA 6: VALIDAR LISTA DE USUARIOS");
        System.out.println("========================================");
        
        // ARRANGE 
        String[] nombresUsuarios = {"María González", "Juan Pérez"};
        int totalUsuarios = nombresUsuarios.length;
        
        // Validaciones
        boolean listaNoVacia = totalUsuarios > 0;
        boolean cantidadCorrecta = totalUsuarios == 2;
        
        // ASSERT
        assertTrue("La lista no debe estar vacía", listaNoVacia);
        assertEquals("Debe haber 2 usuarios", 2, totalUsuarios);
        
        System.out.println("Total de usuarios: " + totalUsuarios);
        for (int i = 0; i < nombresUsuarios.length; i++) {
            System.out.println("  " + (i+1) + ". " + nombresUsuarios[i]);
        }
        System.out.println("La lista de usuarios está correcta.");
    }
}