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
            ResultSet resultado =
                    EstudianteDAO.obtenerEstudiantes(ConexionBD.abrirConexionBD());
            ArrayList<Estudiante> estudiantes = new ArrayList();
            while (resultado.next()) {
                Estudiante estudiante = new Estudiante();
                estudiante.setIdEstudiante(resultado.getInt("idEstudiante"));
                estudiante.setMatricula(resultado.getString("matricula"));
                estudiante.setNombreEstudiante(resultado.getString("nombreEstudiante"));
                estudiante.setApellidoPaterno(resultado.getString("apellidoPaterno"));
                estudiante.setApellidoMaterno(resultado.getString("apellidoMaterno"));
                estudiante.setCorreoInstitucional(resultado.getString("correoInstitucional"));
                estudiante.setIdProgramaEducativo(resultado.getInt("idProgramaEducativo"));
                estudiante.setProgramaEducativo(resultado.getString("programaEducativo"));
                //Mapeo del enum estatus
                estudiante.setEstatus(Estatus.valueOf(resultado.getString("estatus")));
                estudiantes.add(estudiante);
            }
            respuesta.put("error", false);
            respuesta.put("estudiantes", estudiantes);
            ConexionBD.cerrarConexionBD();
        } catch (SQLException e) {
            respuesta.put("error", true);
            respuesta.put("mensaje", e.getMessage());
        }
        return respuesta;
    }
    
    public static Respuesta registrar(Estudiante estudiante) {
        Respuesta respuesta = new Respuesta();
        respuesta.setError(true);
        try {
            int filasAfectadas = EstudianteDAO.registrar(estudiante, ConexionBD.abrirConexionBD());
            if (filasAfectadas > 0) {
                respuesta.setError(false);
                respuesta.setMensaje("Información del tutorado(a) guardado correctamente");
            } else {
                respuesta.setMensaje("Lo sentimos hubo un error al guardar la información");
            }
            ConexionBD.cerrarConexionBD();
        } catch (SQLException e) {
            respuesta.setMensaje(e.getMessage());
        }
        return respuesta;
    }
    
    public static HashMap<String, Object> editar(Estudiante estudiante) {
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        try {
            int filasAfectadas = EstudianteDAO.editar(estudiante, ConexionBD.abrirConexionBD());
            if (filasAfectadas > 0) {
                respuesta.put("error", false);
                respuesta.put("mensaje", "El registro del tutorado(a) " 
                        + estudiante.getNombreEstudiante() + " fue guardado correctamente");
            } else {
                respuesta.put("error", true);
                respuesta.put("mensaje", "Lo sentimos, no se pudo modificar la información "
                            + " del tutorado, por favor inténtelo más tarde");
            }
            ConexionBD.cerrarConexionBD();
        } catch (SQLException e) {
            respuesta.put("error", true);
            respuesta.put("mensaje", e.getMessage());
        }
        return respuesta;
    }
}
