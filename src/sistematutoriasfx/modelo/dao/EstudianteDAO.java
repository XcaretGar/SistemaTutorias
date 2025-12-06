/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistematutoriasfx.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import sistematutoriasfx.modelo.pojo.Estudiante;

/**
 *
 * @author JOANA XCARET
 */
public class EstudianteDAO {
    
    public static int registrar(Estudiante estudiante, Connection conexionBD) throws SQLException {
        if (conexionBD != null) {
            String query = "INSERT INTO estudiante (matricula, nombre, apellidoPaterno, apellidoMaterno, " +
                "correoInstitucional, idProgramaEducativo, estatus) VALUES " +
                "(?,?,?,?,?,?,?)";
            PreparedStatement sentencia = conexionBD.prepareStatement(query);
            sentencia.setString(1, estudiante.getMatricula());
            sentencia.setString(2, estudiante.getNombre());
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
            String query = "UPDATE estudiante SET nombre = ?, apellidoPaterno = ?, " +
                "apellidoMaterno = ?, correoInstitucional = ?, idProgramaEducativo = ?, " +
                "estatus = ? WHERE idEstudiante = ?";
            PreparedStatement sentencia = conexionBD.prepareStatement(query);
            sentencia.setString(1, estudiante.getMatricula());
            sentencia.setString(2, estudiante.getNombre());
            sentencia.setString(3, estudiante.getApellidoPaterno());
            sentencia.setString(4, estudiante.getApellidoMaterno());
            sentencia.setString(5, estudiante.getCorreoInstitucional());
            sentencia.setInt(6, estudiante.getIdProgramaEducativo());
            sentencia.setString(7, estudiante.getEstatus().name());
            return sentencia.executeUpdate();
        }
        throw new SQLException("No hay conexión a la base de datos");
    }
}