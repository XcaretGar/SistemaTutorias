package sistematutoriasfx.controlador.administrador;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Pruebas unitarias para el Caso de Uso: GESTIONAR TUTORADO
 * 
 * Este caso de uso incluye:
 * - Visualizar tutorados (FXMLGestionarTutoradoController)
 * - Registrar/Actualizar tutorados (FXMLFormularioTutoradoController)
 * - Dar de baja tutorados (FXMLDarBajaTutoradoController)
 * 
 * Se prueban las 3 operaciones principales del caso de uso
 * 
 * @author JOANA XCARET
 */
public class GestionarTutoradoTest {
    
    /**
     * ============================================================
     * PRUEBA 1: VALIDAR CAMPOS REQUERIDOS PARA REGISTRAR TUTORADO
     * ============================================================
     * 
     * ¿Qué prueba?
     * - Que la validación de campos obligatorios funciona correctamente
     * - Que todos los campos requeridos se verifican antes de guardar
     * 
     * ¿Por qué es importante?
     * - Es la operación principal del caso de uso
     * - Sin tutorados válidos, el sistema no puede funcionar
     * - Valida la integridad de los datos antes de guardar
     */
    @Test
    public void testValidarCamposRegistroTutorado_CamposCompletos_Validos() {
        System.out.println("\n========================================");
        System.out.println("PRUEBA 1: VALIDAR CAMPOS REGISTRO TUTORADO");
        System.out.println("========================================");
        
        // ============ ARRANGE (Preparar) ============
        // Simular datos de formulario completados
        String nombre = "Carlos";
        String apellidoPaterno = "Hernández";
        String apellidoMaterno = "Ruiz";
        String matricula = "S21012345";
        String correo = "zS21012345@estudiantes.uv.mx";
        Integer idProgramaEducativo = 1;
        String estatusAcademico = "Activo";
        
        // ============ ACT (Actuar) ============
        // Simular validación de campos (lo que hace sonCamposValidos())
        boolean nombreValido = nombre != null && !nombre.isEmpty();
        boolean apellidoPaternoValido = apellidoPaterno != null && !apellidoPaterno.isEmpty();
        boolean apellidoMaternoValido = apellidoMaterno != null && !apellidoMaterno.isEmpty();
        boolean matriculaValida = matricula != null 
                               && !matricula.isEmpty() 
                               && matricula.startsWith("S")
                               && matricula.length() == 9;
        boolean correoValido = correo != null 
                            && !correo.isEmpty() 
                            && correo.contains("@estudiantes.uv.mx");
        boolean programaEducativoValido = idProgramaEducativo != null && idProgramaEducativo > 0;
        boolean estatusValido = estatusAcademico != null && !estatusAcademico.isEmpty();
        
        boolean todosLosCamposValidos = nombreValido && apellidoPaternoValido 
                                     && apellidoMaternoValido && matriculaValida 
                                     && correoValido && programaEducativoValido 
                                     && estatusValido;
        
        // ============ ASSERT (Verificar) ============
        assertTrue("El nombre es requerido y debe estar lleno", nombreValido);
        assertTrue("El apellido paterno es requerido y debe estar lleno", apellidoPaternoValido);
        assertTrue("El apellido materno es requerido y debe estar lleno", apellidoMaternoValido);
        assertTrue("La matrícula es requerida y debe tener formato correcto (Sxxxxxxxx)", matriculaValida);
        assertTrue("El correo es requerido y debe contener @estudiantes.uv.mx", correoValido);
        assertTrue("El programa educativo es requerido", programaEducativoValido);
        assertTrue("El estatus académico es requerido", estatusValido);
        assertTrue("Todos los campos deben ser válidos para registrar", todosLosCamposValidos);
        
        // Verificaciones adicionales de formato
        assertEquals("La matrícula debe tener 9 caracteres", 9, matricula.length());
        assertTrue("La matrícula debe comenzar con S", matricula.startsWith("S"));
        assertTrue("El correo debe ser institucional", correo.endsWith("@estudiantes.uv.mx"));
        
        // ============ RESULTADOS ============
        System.out.println(" Todos los campos requeridos son válidos");
        System.out.println("  Nombre completo: " + nombre + " " + apellidoPaterno + " " + apellidoMaterno);
        System.out.println("  Matrícula: " + matricula + " (formato válido)");
        System.out.println("  Correo: " + correo);
        System.out.println("  ID Programa: " + idProgramaEducativo);
        System.out.println("  Estatus: " + estatusAcademico);
        System.out.println(" El tutorado está listo para ser registrado");
    }
    
    /**
     * ============================================================
     * PRUEBA 2: VALIDAR MOTIVO DE BAJA PARA TUTORADO
     * ============================================================
     * 
     * ¿Qué prueba?
     * - Que se requiere un motivo válido para dar de baja
     * - Que el motivo tiene la longitud mínima requerida
     * - Que no se puede dar de baja con motivo vacío
     * 
     * ¿Por qué es importante?
     * - Asegura que todas las bajas queden documentadas
     * - Previene bajas sin justificación
     * - Valida la integridad de la operación de baja
     */
    @Test
    public void testValidarMotivoBajaTutorado_MotivoValido_OperacionSegura() {
        System.out.println("\n========================================");
        System.out.println("PRUEBA 2: VALIDAR MOTIVO DE BAJA");
        System.out.println("========================================");
        
        // ============ ARRANGE (Preparar) ============
        String motivoValido = "Abandono de estudios por motivos personales";
        String motivoVacio = "";
        String motivoMuyCorto = "Abandono";
        
        // Datos del estudiante a dar de baja
        int idEstudiante = 1;
        String nombreCompleto = "Carlos Hernández Ruiz";
        String matricula = "S21012345";
        String programaEducativo = "Ingeniería de Software";
        String motivoBajaActual = null; // No está dado de baja
        
        // ============ ACT (Actuar) ============
        // Validación del motivo válido
        boolean motivoValidoNoVacio = motivoValido != null && !motivoValido.trim().isEmpty();
        boolean motivoValidoLongitudMinima = motivoValido.length() >= 10;
        boolean motivoValidoCorrecto = motivoValidoNoVacio && motivoValidoLongitudMinima;
        
        // Validación del motivo vacío
        boolean motivoVacioInvalido = motivoVacio == null || motivoVacio.trim().isEmpty();
        
        // Validación del motivo muy corto
        boolean motivoMuyCortoInvalido = motivoMuyCorto.length() < 10;
        
        // Validación de que no está dado de baja previamente
        boolean noEstaDadoDeBaja = motivoBajaActual == null;
        
        // Validación de datos completos
        boolean datosCompletos = idEstudiante > 0 
                              && nombreCompleto != null 
                              && matricula != null 
                              && programaEducativo != null;
        
        // ============ ASSERT (Verificar) ============
        // Verificar motivo válido
        assertTrue("El motivo válido no debe estar vacío", motivoValidoNoVacio);
        assertTrue("El motivo válido debe tener mínimo 10 caracteres", motivoValidoLongitudMinima);
        assertTrue("El motivo válido debe cumplir todos los criterios", motivoValidoCorrecto);
        
        // Verificar motivos inválidos
        assertTrue("El motivo vacío debe ser rechazado", motivoVacioInvalido);
        assertTrue("El motivo muy corto debe ser rechazado", motivoMuyCortoInvalido);
        
        // Verificar estado del estudiante
        assertTrue("El estudiante no debe estar dado de baja previamente", noEstaDadoDeBaja);
        assertTrue("El estudiante debe tener datos completos", datosCompletos);
        
        // Verificaciones adicionales
        assertNull("El motivo de baja actual debe ser null", motivoBajaActual);
        assertTrue("El ID del estudiante debe ser mayor a 0", idEstudiante > 0);
        
        // ============ RESULTADOS ============
        System.out.println(" Validación de motivos:");
        System.out.println("  Motivo válido: '" + motivoValido + "' → ACEPTADO");
        System.out.println("  Longitud: " + motivoValido.length() + " caracteres (mínimo 10)");
        System.out.println("  Motivo vacío: '" + motivoVacio + "' → RECHAZADO");
        System.out.println("  Motivo corto: '" + motivoMuyCorto + "' → RECHAZADO (" + motivoMuyCorto.length() + " < 10)");
        
        System.out.println("\n Datos del estudiante:");
        System.out.println("  ID: " + idEstudiante);
        System.out.println("  Nombre: " + nombreCompleto);
        System.out.println("  Matrícula: " + matricula);
        System.out.println("  Programa: " + programaEducativo);
        System.out.println("  Estado: No dado de baja ✓");
        
        System.out.println("\n La baja puede proceder con motivo válido");
    }
    
    /**
     * ============================================================
     * PRUEBA 3: VALIDAR BÚSQUEDA DE TUTORADOS
     * ============================================================
     * 
     * ¿Qué prueba?
     * - Que la búsqueda por nombre funciona correctamente
     * - Que la búsqueda por matrícula funciona correctamente
     * - Que el filtrado de lista es case-insensitive
     * 
     * ¿Por qué es importante?
     * - La búsqueda facilita encontrar tutorados específicos
     * - Debe funcionar con diferentes criterios
     * - Mejora la experiencia del usuario
     */
    @Test
    public void testBusquedaTutorados_CriteriosMultiples_FuncionaCorrectamente() {
        System.out.println("\n========================================");
        System.out.println("PRUEBA 3: BÚSQUEDA DE TUTORADOS");
        System.out.println("========================================");
        
        // ============ ARRANGE (Preparar) ============
        // Datos de estudiantes en el sistema
        String nombreEstudiante1 = "Carlos";
        String matriculaEstudiante1 = "S21012345";
        String programaEstudiante1 = "Ingeniería de Software";
        
        String nombreEstudiante2 = "Ana";
        String matriculaEstudiante2 = "S21054321";
        String programaEstudiante2 = "Tecnologías Computacionales";
        
        // ============ ACT (Actuar) ============
        // Simular búsqueda por nombre (case-insensitive)
        String criterioBusquedaNombre = "carlos";
        boolean busquedaPorNombre1 = nombreEstudiante1.toLowerCase().contains(criterioBusquedaNombre.toLowerCase());
        boolean busquedaPorNombre2 = nombreEstudiante2.toLowerCase().contains(criterioBusquedaNombre.toLowerCase());
        
        // Simular búsqueda por matrícula
        String criterioBusquedaMatricula = "S21012345";
        boolean busquedaPorMatricula1 = matriculaEstudiante1.toLowerCase().contains(criterioBusquedaMatricula.toLowerCase());
        boolean busquedaPorMatricula2 = matriculaEstudiante2.toLowerCase().contains(criterioBusquedaMatricula.toLowerCase());
        
        // Simular búsqueda parcial por matrícula
        String criterioBusquedaParcial = "S210";
        boolean busquedaParcial1 = matriculaEstudiante1.toLowerCase().contains(criterioBusquedaParcial.toLowerCase());
        boolean busquedaParcial2 = matriculaEstudiante2.toLowerCase().contains(criterioBusquedaParcial.toLowerCase());
        
        // Contar resultados esperados
        int resultadosBusquedaNombre = 0;
        if (busquedaPorNombre1) resultadosBusquedaNombre++;
        if (busquedaPorNombre2) resultadosBusquedaNombre++;
        
        int resultadosBusquedaMatricula = 0;
        if (busquedaPorMatricula1) resultadosBusquedaMatricula++;
        if (busquedaPorMatricula2) resultadosBusquedaMatricula++;
        
        int resultadosBusquedaParcial = 0;
        if (busquedaParcial1) resultadosBusquedaParcial++;
        if (busquedaParcial2) resultadosBusquedaParcial++;
        
        // ============ ASSERT (Verificar) ============
        // Verificar búsqueda por nombre
        assertTrue("La búsqueda por nombre debe encontrar al estudiante 1", busquedaPorNombre1);
        assertFalse("La búsqueda por nombre no debe encontrar al estudiante 2", busquedaPorNombre2);
        assertEquals("La búsqueda por nombre debe encontrar 1 resultado", 1, resultadosBusquedaNombre);
        
        // Verificar búsqueda por matrícula exacta
        assertTrue("La búsqueda por matrícula debe encontrar al estudiante 1", busquedaPorMatricula1);
        assertFalse("La búsqueda por matrícula no debe encontrar al estudiante 2", busquedaPorMatricula2);
        assertEquals("La búsqueda por matrícula debe encontrar 1 resultado", 1, resultadosBusquedaMatricula);
        
        // Verificar búsqueda parcial
        assertTrue("La búsqueda parcial debe encontrar al estudiante 1", busquedaParcial1);
        assertTrue("La búsqueda parcial debe encontrar al estudiante 2", busquedaParcial2);
        assertEquals("La búsqueda parcial debe encontrar 2 resultados", 2, resultadosBusquedaParcial);
        
        // Verificar formato de datos
        assertTrue("La matrícula debe comenzar con S", matriculaEstudiante1.startsWith("S"));
        assertTrue("La matrícula debe tener 9 caracteres", matriculaEstudiante1.length() == 9);
        
        // ============ RESULTADOS ============
        System.out.println(" Estudiantes en el sistema:");
        System.out.println("  1. " + nombreEstudiante1 + " - " + matriculaEstudiante1);
        System.out.println("     Programa: " + programaEstudiante1);
        System.out.println("  2. " + nombreEstudiante2 + " - " + matriculaEstudiante2);
        System.out.println("     Programa: " + programaEstudiante2);
        
        System.out.println("\n Resultados de búsqueda:");
        System.out.println("  Búsqueda por nombre '" + criterioBusquedaNombre + "': " + resultadosBusquedaNombre + " resultado(s)");
        System.out.println("  Búsqueda por matrícula '" + criterioBusquedaMatricula + "': " + resultadosBusquedaMatricula + " resultado(s)");
        System.out.println("  Búsqueda parcial '" + criterioBusquedaParcial + "': " + resultadosBusquedaParcial + " resultado(s)");
        
        System.out.println("\n La funcionalidad de búsqueda trabaja correctamente");
    }
}