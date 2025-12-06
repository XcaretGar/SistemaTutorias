/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistematutoriasfx.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import sistematutoriasfx.modelo.pojo.Estudiante;

/**
 *
 * @author JOANA XCARET
 */
public class EstudianteDAO {
    
   // XCA CHECA ESTA PARTE!!! NO USES CONEXIONBD, USA LOS QUE TIENES Y CIERRAS LA CONEXION
    public static ResultSet obtenerEstudiantes(Connection conexionBD) throws SQLException {
        if (conexionBD != null) {
            String query = "SELECT idEstudiante, matricula, nombreEstudiante, apellidoPaterno, " +
                "apellidoMaterno, correoInstitucional, idProgramaEducativo, programaEducativo.nombre, estatus " +
                "FROM estudiante " +
                "INNER JOIN programaEducativo programaEducativo ON programaEducativo.idPrograma = estudiante.idProgramaEducativo";
            PreparedStatement sentencia = conexionBD.prepareStatement(query);
            return sentencia.executeQuery();
        }
        throw new SQLException("No hay conexión a la Base de Datos");
    }
    
    public static int registrar(Estudiante estudiante, Connection conexionBD) throws SQLException {
        if (conexionBD != null) {
            String query = "INSERT INTO estudiante (matricula, nombreEstudiante, apellidoPaterno, apellidoMaterno, " +
                "correoInstitucional, idProgramaEducativo, estatus) VALUES " +
                "(?,?,?,?,?,?,?)";
            PreparedStatement sentencia = conexionBD.prepareStatement(query);
            sentencia.setString(1, estudiante.getMatricula());
            sentencia.setString(2, estudiante.getNombreEstudiante());
            sentencia.setString(3, estudiante.getApellidoPaterno());
            sentencia.setString(4, estudiante.getApellidoMaterno());
            sentencia.setString(5, estudiante.getCorreoInstitucional());
            sentencia.setInt(6, estudiante.getIdProgramaEducativo());
            sentencia.setString(7, estudiante.getEstatus().name());
            return sentencia.executeUpdate();
        }
        throw new SQLException("No hay conexión a la base de datos");
    }
    
    public static int editar(Estudiante estudiante, Connection conexionBD) throws SQLException {
        if (conexionBD != null) {
            String query = "UPDATE estudiante SET matricula = ?, nombreEstudiante = ?, apellidoPaterno = ?, " +
                "apellidoMaterno = ?, correoInstitucional = ?, idProgramaEducativo = ?, " +
                "estatus = ? WHERE idEstudiante = ?";
            PreparedStatement sentencia = conexionBD.prepareStatement(query);
            sentencia.setString(1, estudiante.getMatricula());
            sentencia.setString(2, estudiante.getNombreEstudiante());
            sentencia.setString(3, estudiante.getApellidoPaterno());
            sentencia.setString(4, estudiante.getApellidoMaterno());
            sentencia.setString(5, estudiante.getCorreoInstitucional());
            sentencia.setInt(6, estudiante.getIdProgramaEducativo());
            sentencia.setString(7, estudiante.getEstatus().name());
            sentencia.setInt(8, estudiante.getIdEstudiante());
            return sentencia.executeUpdate();
        }
        throw new SQLException("No hay conexión a la base de datos");
    }
}