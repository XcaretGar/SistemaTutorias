/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistematutoriasfx.dominio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import sistematutoriasfx.modelo.dao.AcademicoDAO;
import sistematutoriasfx.modelo.pojo.Academico;
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
            respuesta.put("academico", academico);
        } catch (Exception e) {
            respuesta.put("error", true);
            respuesta.put("mensaje", e.getMessage());
        }
        return respuesta;
    }
    
    public static HashMap<String, Object> obtenerUsuarios() {
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        try {
            ArrayList<Academico> academicos = AcademicoDAO.obtenerTodos();
            respuesta.put("error", false);
            respuesta.put("academicos", academicos);
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
    
    public static Respuesta actualizar(Academico academico) {
        Respuesta respuesta = new Respuesta();
        try {
            boolean exito = AcademicoDAO.actualizar(academico);
            if (exito) {
                respuesta.setError(false);
                respuesta.setMensaje("El registro del usuario " 
                        + academico.getNombre() + " fue actualizado correctamente");
            } else {
                respuesta.setError(true);
                respuesta.setMensaje("Lo sentimos, no se pudo actualizar la información "
                            + " del usuario, por favor inténtelo más tarde");
            }
        } catch (Exception e) {
            respuesta.setError(true);
            respuesta.setMensaje(e.getMessage());
        }
        return respuesta;
    }
}
