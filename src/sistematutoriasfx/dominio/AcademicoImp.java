/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistematutoriasfx.dominio;

import java.util.HashMap;
import java.util.LinkedHashMap;
import sistematutoriasfx.modelo.dao.AcademicoDAO;
import sistematutoriasfx.modelo.pojo.Academico;
import sistematutoriasfx.modelo.pojo.Estudiante;
import sistematutoriasfx.modelo.pojo.Respuesta;

/**
 *
 * @author JOANA XCARET
 */
public class AcademicoImp {
    
    public static HashMap<String, Object> obtenerUsuarioPorId(int idUsuario) {
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        try {
            Academico academico = AcademicoDAO.obtenerAcademicoPorIdUsuario(idUsuario);
            respuesta.put("error", false);
            respuesta.put("estudiantes", academico);
        } catch (Exception e) {
            respuesta.put("error", true);
            respuesta.put("mensaje", e.getMessage());
        }
        return respuesta;
    }
    
    public static Respuesta registrar(Academico academico) {
        Respuesta respuesta = new Respuesta();
        respuesta.setError(true);
        try {
            boolean exito = AcademicoDAO.registrarUsuarioConRol(academico);
            if (exito) {
                respuesta.setError(false);
                respuesta.setMensaje("Información del usuario guardada correctamente");
            } else {
                respuesta.setMensaje("Lo sentimos, hubo un error al guardar la información");
            }
        } catch (Exception e) {
            respuesta.setMensaje(e.getMessage());
        }
        return respuesta;
    }
    
    public static HashMap<String, Object> actualizar(Academico academico) {
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        try {
            boolean exito = AcademicoDAO.actualizar(academico);
            if (exito) {
                respuesta.put("error", false);
                respuesta.put("mensaje", "El registro del usuario " 
                        + academico.getNombre() + " fue guardado correctamente");
            } else {
                respuesta.put("error", true);
                respuesta.put("mensaje", "Lo sentimos, no se pudo modificar la información "
                            + " del usuario, por favor inténtelo más tarde");
            }
        } catch (Exception e) {
            respuesta.put("error", true);
            respuesta.put("mensaje", e.getMessage());
        }
        return respuesta;
    }
}
