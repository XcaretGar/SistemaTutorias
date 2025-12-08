/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistematutoriasfx.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import sistematutoriasfx.modelo.pojo.Academico;
import sistematutoriasfx.modeloo.ConexionBD;

/**
 *
 * @author JOANA XCARET
 */
public class AsignacionTutorDAO {
    
    public static boolean existeAsignacion(int idEstudiante) {
        boolean asignado = false;
        Connection conexion = null;
        try {
            conexion = ConexionBD.abrirConexion();
            String query = "SELECT COUNT(*) FROM asignaciontutor WHERE idEstudiante = ?";
            PreparedStatement ps = conexion.prepareStatement(query);
            ps.setInt(1, idEstudiante);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                asignado = rs.getInt(1) > 0;
            }
        } catch (SQLException e) { e.printStackTrace();
        } finally { if (conexion != null) try { conexion.close(); } catch (SQLException e) { e.printStackTrace(); } }
        return asignado;
    }

    public static boolean asignarTutor(int idEstudiante, int idAcademico) {
        boolean resultado = false;
        Connection conexion = null;
        try {
            conexion = ConexionBD.abrirConexion();
            String sqlPeriodo = "SELECT idPeriodo FROM PeriodoEscolar ORDER BY fechaInicio DESC LIMIT 1";
            PreparedStatement psPeriodo = conexion.prepareStatement(sqlPeriodo);
            ResultSet rsPeriodo = psPeriodo.executeQuery();
            int idPeriodo = -1;
            if (rsPeriodo.next()) {
                idPeriodo = rsPeriodo.getInt("idPeriodo");
            }
        
            String query = "INSERT INTO asignaciontutor(idEstudiante, idAcademico, idPeriodo) VALUES (?, ?, ?)";
            PreparedStatement ps = conexion.prepareStatement(query);
            ps.setInt(1, idEstudiante);
            ps.setInt(2, idAcademico);
            ps.setInt(3, idPeriodo);
            resultado = ps.executeUpdate() > 0;
        } catch (SQLException e) { 
            e.printStackTrace(); 
        } finally { 
            if (conexion != null) try { conexion.close(); } catch (SQLException e) { e.printStackTrace(); 
            } 
        }
        return resultado;
    }

    public static boolean actualizarAsignacion(int idEstudiante, int idNuevoTutor) {
        boolean resultado = false;
        Connection conexion = null;
        try {
            conexion = ConexionBD.abrirConexion();
            String query = "UPDATE asignaciontutor SET idAcademico = ? WHERE idEstudiante = ?";
            PreparedStatement ps = conexion.prepareStatement(query);
            ps.setInt(1, idNuevoTutor);
            ps.setInt(2, idEstudiante);
            resultado = ps.executeUpdate() > 0;
        } catch (SQLException e) { 
            e.printStackTrace(); 
        } finally { 
            if (conexion != null) try { conexion.close(); } catch (SQLException e) { e.printStackTrace(); 
            } 
        }
        return resultado;
    }

    public static Academico obtenerTutorActual(int idEstudiante) {
        Academico tutor = null;
        Connection conexion = null;
        try {
            conexion = ConexionBD.abrirConexion();
            String query = "SELECT academico.* FROM asignaciontutor " +
                           "INNER JOIN academico ON asignaciontutor.idAcademico = academico.idAcademico " +
                           "WHERE asignaciontutor.idEstudiante = ?";
            PreparedStatement ps = conexion.prepareStatement(query);
            ps.setInt(1, idEstudiante);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                tutor = new Academico();
                tutor.setIdAcademico(rs.getInt("idAcademico"));
                tutor.setNombre(rs.getString("nombre"));
                tutor.setApellidoPaterno(rs.getString("apellidoPaterno"));
                tutor.setApellidoMaterno(rs.getString("apellidoMaterno"));
            }
        } catch (SQLException e) { 
            e.printStackTrace(); 
        } finally { 
            if (conexion != null) try { conexion.close(); } catch (SQLException e) { e.printStackTrace(); 
            }
        }
        return tutor;
    }
}
