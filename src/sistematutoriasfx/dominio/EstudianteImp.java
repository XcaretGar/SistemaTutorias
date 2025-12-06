/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistematutoriasfx.dominio;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import sistematutoriasfx.modelo.dao.EstudianteDAO;
import sistematutoriasfx.modelo.pojo.Estudiante;
import sistematutoriasfx.modelo.pojo.Estudiante.Estatus;
import sistematutoriasfx.modelo.pojo.Respuesta;
import sistematutoriasfx.modeloo.ConexionBD;

/**
 *
 * @author JOANA XCARET
 */
public class EstudianteImp {
    
    public static HashMap<String, Object> obtenerEstudiantes() {
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        try {
            ArrayList<Estudiante> estudiantes = EstudianteDAO.obtenerEstudiantes();
            respuesta.put("error", false);
            respuesta.put("estudiantes", estudiantes);
        } catch (Exception e) {
            respuesta.put("error", true);
            respuesta.put("mensaje", e.getMessage());
        }
        return respuesta;
    }
    
    public static Respuesta registrar(Estudiante estudiante) {
        Respuesta respuesta = new Respuesta();
        respuesta.setError(true);
        try {
            boolean exito = EstudianteDAO.registrar(estudiante);
            if (exito) {
                respuesta.setError(false);
                respuesta.setMensaje("Información del tutorado(a) guardada correctamente");
            } else {
                respuesta.setMensaje("Lo sentimos, hubo un error al guardar la información");
            }
        } catch (Exception e) {
            respuesta.setMensaje(e.getMessage());
        }
        return respuesta;
    }
    
    public static HashMap<String, Object> editar(Estudiante estudiante) {
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        try {
            boolean exito = EstudianteDAO.editar(estudiante);
            if (exito) {
                respuesta.put("error", false);
                respuesta.put("mensaje", "El registro del tutorado(a) " 
                        + estudiante.getNombreEstudiante() + " fue guardado correctamente");
            } else {
                respuesta.put("error", true);
                respuesta.put("mensaje", "Lo sentimos, no se pudo modificar la información "
                            + " del tutorado, por favor inténtelo más tarde");
            }
        } catch (Exception e) {
            respuesta.put("error", true);
            respuesta.put("mensaje", e.getMessage());
        }
        return respuesta;
    }
}
