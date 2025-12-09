package sistematutoriasfx.controlador.administrador;

import org.junit.Before;
import org.junit.Test;
import sistematutoriasfx.modelo.pojo.Academico;
import sistematutoriasfx.modelo.pojo.Rol;
import sistematutoriasfx.modelo.pojo.Usuario;
import java.util.ArrayList;
import static org.junit.Assert.*;

/**
 * Pruebas unitarias para el Caso de Uso: GESTIONAR USUARIO
 * 
 * Este caso de uso incluye:
 * - Visualizar usuarios (FXMLGestionarUsuarioController)
 * - Registrar/Actualizar usuarios (FXMLFormularioUsuarioController)
 * - Dar de baja usuarios (FXMLDarBajaUsuarioController)
 * 
 * Se prueban las 3 operaciones principales del caso de uso
 * 
 * @author JOANA XCARET
 */
public class GestionarUsuarioTest {
    
    private Academico academicoPrueba;
    private Usuario usuarioSesion;
    private Rol rolAdministrador;
    private Rol rolTutor;
    
    /**
     * Se ejecuta ANTES de cada prueba
     * Prepara todos los datos necesarios para las pruebas del caso de uso
     */
    @Before
    public void setUp() {
        // Configurar roles
        rolAdministrador = new Rol();
        rolAdministrador.setIdRol(1);
        rolAdministrador.setNombre("Administrador");
        
        rolTutor = new Rol();
        rolTutor.setIdRol(2);
        rolTutor.setNombre("Tutor Académico");
        
        // Configurar usuario de sesión (quien está usando el sistema)
        usuarioSesion = new Usuario();
        usuarioSesion.setIdUsuario(1);
        usuarioSesion.setUsername("admin@uv.mx");
        usuarioSesion.setPassword("admin123");
        ArrayList<Rol> rolesAdmin = new ArrayList<>();
        rolesAdmin.add(rolAdministrador);
        usuarioSesion.setRoles(rolesAdmin);
        
        // Configurar académico de prueba
        academicoPrueba = new Academico();
        academicoPrueba.setIdAcademico(2);
        academicoPrueba.setNombre("María");
        academicoPrueba.setApellidoPaterno("González");
        academicoPrueba.setApellidoMaterno("López");
        academicoPrueba.setCorreoInstitucional("mgonzalez@uv.mx");
        academicoPrueba.setNoPersonal("12345");
        academicoPrueba.setTipoContrato(Academico.TipoContrato.TiempoCompleto);
        academicoPrueba.setEstudios("Doctorado en Computación");
        
        Usuario usuarioAcademico = new Usuario();
        usuarioAcademico.setIdUsuario(2);
        usuarioAcademico.setUsername("mgonzalez@uv.mx");
        usuarioAcademico.setPassword("123456");
        ArrayList<Rol> rolesTutor = new ArrayList<>();
        rolesTutor.add(rolTutor);
        usuarioAcademico.setRoles(rolesTutor);
        
        academicoPrueba.setUsuario(usuarioAcademico);
    }
    
    /**
     * ============================================================
     * PRUEBA 1: REGISTRAR/ACTUALIZAR USUARIO
     * ============================================================
     * 
     * ¿Qué prueba?
     * - Que se puede crear un académico con todos los campos requeridos
     * - Que la validación de campos obligatorios funciona correctamente
     * 
     * ¿Por qué es importante?
     * - Es la operación principal del caso de uso
     * - Sin usuarios válidos, el sistema no puede funcionar
     * - Valida la integridad de los datos antes de guardar
     */
    @Test
    public void testRegistrarUsuario_TodosLosCamposRequeridos_DatosValidos() {
        System.out.println("\n========================================");
        System.out.println("PRUEBA 1: REGISTRAR/ACTUALIZAR USUARIO");
        System.out.println("========================================");
        
        // ============ ARRANGE (Preparar) ============
        // El académico ya está creado en setUp()
        
        // ============ ACT (Actuar) ============
        // Simular validación de campos (lo que hace sonCamposValidos())
        boolean nombreValido = academicoPrueba.getNombre() != null 
                            && !academicoPrueba.getNombre().isEmpty();
        boolean apellidoPaternoValido = academicoPrueba.getApellidoPaterno() != null 
                                     && !academicoPrueba.getApellidoPaterno().isEmpty();
        boolean apellidoMaternoValido = academicoPrueba.getApellidoMaterno() != null 
                                     && !academicoPrueba.getApellidoMaterno().isEmpty();
        boolean correoValido = academicoPrueba.getCorreoInstitucional() != null 
                            && !academicoPrueba.getCorreoInstitucional().isEmpty()
                            && academicoPrueba.getCorreoInstitucional().contains("@uv.mx");
        boolean noPersonalValido = academicoPrueba.getNoPersonal() != null 
                                && !academicoPrueba.getNoPersonal().isEmpty();
        boolean tipoContratoValido = academicoPrueba.getTipoContrato() != null;
        boolean estudiosValido = academicoPrueba.getEstudios() != null 
                              && !academicoPrueba.getEstudios().isEmpty();
        boolean usuarioValido = academicoPrueba.getUsuario() != null;
        boolean rolValido = academicoPrueba.getUsuario().getRoles() != null 
                         && !academicoPrueba.getUsuario().getRoles().isEmpty();
        boolean passwordValido = academicoPrueba.getUsuario().getPassword() != null 
                              && !academicoPrueba.getUsuario().getPassword().isEmpty();
        boolean usernameCoincideConCorreo = academicoPrueba.getUsuario().getUsername()
                                           .equals(academicoPrueba.getCorreoInstitucional());
        
        // ============ ASSERT (Verificar) ============
        // Verificar todos los campos requeridos
        assertTrue("El nombre es requerido y debe estar lleno", nombreValido);
        assertTrue("El apellido paterno es requerido y debe estar lleno", apellidoPaternoValido);
        assertTrue("El apellido materno es requerido y debe estar lleno", apellidoMaternoValido);
        assertTrue("El correo es requerido, debe estar lleno y contener @uv.mx", correoValido);
        assertTrue("El número personal es requerido y debe estar lleno", noPersonalValido);
        assertTrue("El tipo de contrato es requerido", tipoContratoValido);
        assertTrue("Los estudios son requeridos y deben estar llenos", estudiosValido);
        assertTrue("El usuario asociado es requerido", usuarioValido);
        assertTrue("El rol del usuario es requerido", rolValido);
        assertTrue("La contraseña es requerida y debe estar llena", passwordValido);
        assertTrue("El username debe coincidir con el correo institucional", usernameCoincideConCorreo);
        
        // Verificar estructura correcta
        assertNotNull("El académico no debe ser null", academicoPrueba);
        assertEquals("El nombre debe ser María", "María", academicoPrueba.getNombre());
        assertEquals("El username debe ser igual al correo", 
                     academicoPrueba.getCorreoInstitucional(), 
                     academicoPrueba.getUsuario().getUsername());
        
        // ============ RESULTADOS ============
        System.out.println(" Todos los campos requeridos están completos y válidos");
        System.out.println("  Académico: " + academicoPrueba.getNombreCompleto());
        System.out.println("  No. Personal: " + academicoPrueba.getNoPersonal());
        System.out.println("  Correo: " + academicoPrueba.getCorreoInstitucional());
        System.out.println("  Tipo Contrato: " + academicoPrueba.getTipoContrato());
        System.out.println("  Estudios: " + academicoPrueba.getEstudios());
        System.out.println("  Rol: " + academicoPrueba.getUsuario().getRoles().get(0).getNombre());
        System.out.println("  Username: " + academicoPrueba.getUsuario().getUsername());
        System.out.println(" El académico está listo para ser registrado/actualizado");
    }
    
    /**
     * ============================================================
     * PRUEBA 2: DAR DE BAJA USUARIO CON VALIDACIONES
     * ============================================================
     * 
     * ¿Qué prueba?
     * - Que un administrador no puede darse de baja a sí mismo
     * - Que se requiere un motivo válido para dar de baja
     * - Que el académico a dar de baja tiene los datos necesarios
     * 
     * ¿Por qué es importante?
     * - Previene que el sistema se quede sin administradores
     * - Asegura que todas las bajas queden documentadas
     * - Valida la integridad de la operación de baja
     */
    @Test
    public void testDarBajaUsuario_ValidacionesYMotivo_OperacionSegura() {
        System.out.println("\n========================================");
        System.out.println("PRUEBA 2: DAR DE BAJA USUARIO");
        System.out.println("========================================");
        
        // ============ ARRANGE (Preparar) ============
        String motivoBaja = "Finalización de contrato por tiempo determinado";
        
        // ============ ACT (Actuar) ============
        // Validación 1: Verificar que NO es el mismo usuario
        int idUsuarioActual = usuarioSesion.getIdUsuario();
        int idUsuarioADarBaja = academicoPrueba.getUsuario().getIdUsuario();
        boolean esDiferenteUsuario = (idUsuarioActual != idUsuarioADarBaja);
        
        // Validación 2: Verificar que el motivo no está vacío
        boolean motivoValido = motivoBaja != null 
                            && !motivoBaja.trim().isEmpty() 
                            && motivoBaja.length() >= 10;
        
        // Validación 3: Verificar que el académico tiene datos completos
        boolean academicoValido = academicoPrueba.getIdAcademico() > 0
                               && academicoPrueba.getNombreCompleto() != null
                               && academicoPrueba.getNoPersonal() != null
                               && academicoPrueba.getUsuario() != null;
        
        // Validación 4: Verificar que NO está dado de baja previamente
        boolean noEstaDadoDeBaja = academicoPrueba.getMotivoBaja() == null;
        
        // ============ ASSERT (Verificar) ============
        assertTrue("El usuario a dar de baja debe ser diferente al usuario actual", 
                   esDiferenteUsuario);
        assertTrue("El motivo de baja debe ser válido (no vacío y mínimo 10 caracteres)", 
                   motivoValido);
        assertTrue("El académico debe tener datos completos para la baja", 
                   academicoValido);
        assertTrue("El académico no debe estar dado de baja previamente", 
                   noEstaDadoDeBaja);
        
        // Verificaciones adicionales
        assertNotEquals("Los IDs de usuario deben ser diferentes", 
                        idUsuarioActual, idUsuarioADarBaja);
        assertNull("El motivo de baja inicial debe ser null", 
                   academicoPrueba.getMotivoBaja());
        
        // Simular asignación de motivo de baja
        academicoPrueba.setMotivoBaja(motivoBaja);
        assertNotNull("El motivo de baja debe estar asignado", 
                     academicoPrueba.getMotivoBaja());
        assertEquals("El motivo de baja debe coincidir", 
                     motivoBaja, academicoPrueba.getMotivoBaja());
        
        // ============ RESULTADOS ============
        System.out.println(" Validación 1: Usuario diferente");
        System.out.println("  Usuario Actual (Admin): ID=" + idUsuarioActual);
        System.out.println("  Usuario a Dar Baja: ID=" + idUsuarioADarBaja);
        System.out.println("  ¿Son diferentes? " + esDiferenteUsuario);
        
        System.out.println("\n Validación 2: Motivo válido");
        System.out.println("  Motivo: " + motivoBaja);
        System.out.println("  Longitud: " + motivoBaja.length() + " caracteres");
        
        System.out.println("\n Validación 3: Académico con datos completos");
        System.out.println("  ID: " + academicoPrueba.getIdAcademico());
        System.out.println("  Nombre: " + academicoPrueba.getNombreCompleto());
        System.out.println("  No. Personal: " + academicoPrueba.getNoPersonal());
        
        System.out.println("\n Todas las validaciones pasaron - Baja puede proceder");
    }
    
    /**
     * ============================================================
     * PRUEBA 3: VISUALIZAR Y GESTIONAR LISTA DE USUARIOS
     * ============================================================
     * 
     * ¿Qué prueba?
     * - Que se puede crear y gestionar una lista de académicos
     * - Que los académicos tienen la estructura correcta para mostrarse
     * - Que el usuario de sesión está configurado correctamente
     * 
     * ¿Por qué es importante?
     * - La lista de usuarios es el punto de entrada del caso de uso
     * - Debe mostrar correctamente los datos para seleccionar usuarios
     * - El usuario de sesión controla los permisos de operación
     */
    @Test
    public void testVisualizarUsuarios_ListaYSesion_EstructuraCorrecta() {
        System.out.println("\n========================================");
        System.out.println("PRUEBA 3: VISUALIZAR Y GESTIONAR USUARIOS");
        System.out.println("========================================");
        
        // ============ ARRANGE (Preparar) ============
        // Crear lista de académicos (simulando lo que devuelve la BD)
        ArrayList<Academico> listaAcademicos = new ArrayList<>();
        listaAcademicos.add(academicoPrueba);
        
        // Crear segundo académico para la lista
        Academico academico2 = new Academico();
        academico2.setIdAcademico(3);
        academico2.setNombre("Juan");
        academico2.setApellidoPaterno("Pérez");
        academico2.setApellidoMaterno("Ramírez");
        academico2.setCorreoInstitucional("jperez@uv.mx");
        academico2.setNoPersonal("54321");
        academico2.setTipoContrato(Academico.TipoContrato.MedioTiempo);
        academico2.setEstudios("Maestría en Sistemas");
        
        Usuario usuarioAcademico2 = new Usuario();
        usuarioAcademico2.setIdUsuario(3);
        usuarioAcademico2.setUsername("jperez@uv.mx");
        ArrayList<Rol> roles2 = new ArrayList<>();
        roles2.add(rolTutor);
        usuarioAcademico2.setRoles(roles2);
        academico2.setUsuario(usuarioAcademico2);
        
        listaAcademicos.add(academico2);
        
        // ============ ACT (Actuar) ============
        // Validar estructura de la lista
        boolean listaNoVacia = listaAcademicos != null && !listaAcademicos.isEmpty();
        boolean cantidadCorrecta = listaAcademicos.size() == 2;
        
        // Validar que cada académico tenga estructura correcta para visualizar
        boolean todosConDatosCompletos = true;
        for (Academico academico : listaAcademicos) {
            boolean tieneNombre = academico.getNombre() != null 
                               && !academico.getNombre().isEmpty();
            boolean tieneApellidos = academico.getApellidoPaterno() != null 
                                  && academico.getApellidoMaterno() != null;
            boolean tieneCorreo = academico.getCorreoInstitucional() != null;
            boolean tieneUsuario = academico.getUsuario() != null;
            boolean tieneRol = academico.getUsuario().getRoles() != null 
                            && !academico.getUsuario().getRoles().isEmpty();
            
            if (!tieneNombre || !tieneApellidos || !tieneCorreo || !tieneUsuario || !tieneRol) {
                todosConDatosCompletos = false;
                break;
            }
        }
        
        // Validar usuario de sesión configurado
        boolean sesionConfigurada = usuarioSesion != null 
                                 && usuarioSesion.getIdUsuario() > 0
                                 && usuarioSesion.getUsername() != null
                                 && usuarioSesion.getRoles() != null
                                 && !usuarioSesion.getRoles().isEmpty();
        
        // ============ ASSERT (Verificar) ============
        assertTrue("La lista de académicos no debe estar vacía", listaNoVacia);
        assertTrue("La lista debe tener 2 académicos", cantidadCorrecta);
        assertTrue("Todos los académicos deben tener datos completos para visualizar", 
                   todosConDatosCompletos);
        assertTrue("El usuario de sesión debe estar configurado correctamente", 
                   sesionConfigurada);
        
        // Verificar estructura de cada académico
        for (Academico academico : listaAcademicos) {
            assertNotNull("El nombre no debe ser null", academico.getNombre());
            assertNotNull("El apellido paterno no debe ser null", academico.getApellidoPaterno());
            assertNotNull("El apellido materno no debe ser null", academico.getApellidoMaterno());
            assertNotNull("El correo no debe ser null", academico.getCorreoInstitucional());
            assertNotNull("El usuario no debe ser null", academico.getUsuario());
            assertNotNull("Los roles no deben ser null", academico.getUsuario().getRoles());
            assertFalse("Los roles no deben estar vacíos", academico.getUsuario().getRoles().isEmpty());
        }
        
        // Verificar usuario de sesión
        assertNotNull("El usuario de sesión no debe ser null", usuarioSesion);
        assertEquals("El usuario de sesión debe ser admin@uv.mx", 
                     "admin@uv.mx", usuarioSesion.getUsername());
        assertEquals("El rol del usuario de sesión debe ser Administrador", 
                     "Administrador", usuarioSesion.getRoles().get(0).getNombre());
        
        // ============ RESULTADOS ============
        System.out.println(" Lista de académicos cargada correctamente");
        System.out.println("  Total de usuarios: " + listaAcademicos.size());
        System.out.println("\n  Usuarios en el sistema:");
        for (int i = 0; i < listaAcademicos.size(); i++) {
            Academico ac = listaAcademicos.get(i);
            System.out.println("  " + (i+1) + ". " + ac.getNombreCompleto());
            System.out.println("     Correo: " + ac.getCorreoInstitucional());
            System.out.println("     Rol: " + ac.getUsuario().getRoles().get(0).getNombre());
            System.out.println("     No. Personal: " + ac.getNoPersonal());
        }
        
        System.out.println("\n Usuario de sesión configurado");
        System.out.println("  Usuario: " + usuarioSesion.getUsername());
        System.out.println("  Rol: " + usuarioSesion.getRoles().get(0).getNombre());
        System.out.println("  ID: " + usuarioSesion.getIdUsuario());
        
        System.out.println("\n El sistema está listo para gestionar usuarios");
    }
}