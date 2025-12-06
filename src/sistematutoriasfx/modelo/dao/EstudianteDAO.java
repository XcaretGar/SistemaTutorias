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
            String query = "INSERT INTO estudiante (idEstudiante, matricula, nombre, apellidoPaterno, apellidoMaterno, " +
                "correoInstitucional, idProgramaEducativo, estatus) VALUES " +
                "(?,?,?,?,?,?,?,?)";
            PreparedStatement sentencia = conexionBD.prepareStatement(query);
            sentencia.setInt(1, estudiante.getIdEstudiante());
            sentencia.setString(2, estudiante.getMatricula());
            sentencia.setString(3, estudiante.getNombre());
            sentencia.setString(4, estudiante.getApellidoPaterno());
            sentencia.setString(5, estudiante.getApellidoMaterno());
            sentencia.setString(6, estudiante.getCorreoInstitucional());
            sentencia.setInt(7, estudiante.getIdProgramaEducativo());
            sentencia.setString(8, estudiante.getEstatus().name());
            return sentencia.executeUpdate();
        }
        throw new SQLException("No hay conexión a la base de datos");
    }
    
    public static int editar(Estudiante estudiante, Connection conexionBD) throws SQLException {
        if (conexionBD != null) {
            String actualizacion = "update alumno set nombre = ?, apellidoPaterno = ?, " +
                "apellidoMaterno = ?, correo = ?, fechaNacimiento = ?, " +
                "idCarrera = ?, foto = ? where idAlumno = ?";
            PreparedStatement sentencia = conexionBD.prepareStatement(actualizacion);
            sentencia.setString(1, alumno.getNombre());
            sentencia.setString(2, alumno.getApellidoPaterno());
            sentencia.setString(3, alumno.getApellidoMaterno());
            sentencia.setString(4, alumno.getCorreo());
            sentencia.setString(5, alumno.getFechaNacimiento());
            sentencia.setInt(6, alumno.getIdCarrera());
            sentencia.setBytes(7, alumno.getFoto());
            sentencia.setInt(8, alumno.getIdAlumno());
            return sentencia.executeUpdate();
        }
        throw new SQLException("No hay conexión a la base de datos");
    }
}
