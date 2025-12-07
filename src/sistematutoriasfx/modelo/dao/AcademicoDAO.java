/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistematutoriasfx.modelo.dao;

/**
 *
 * @author Ana Georgina
 */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import sistematutoriasfx.modelo.pojo.Academico;
import sistematutoriasfx.modelo.pojo.Academico.TipoContrato;
import sistematutoriasfx.modelo.pojo.Usuario;
import sistematutoriasfx.modeloo.ConexionBD;

public class AcademicoDAO {
    
    public static Academico obtenerAcademicoPorIdUsuario(int idUsuario) {
        Academico academico = null;
        Connection conexion = null;
        try {
            conexion = ConexionBD.abrirConexion();
            String consulta = "SELECT * FROM Academico WHERE idUsuario = ?";
            PreparedStatement ps = conexion.prepareStatement(consulta);
            ps.setInt(1, idUsuario);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                academico = new Academico();
                academico.setIdAcademico(rs.getInt("idAcademico"));
                academico.setNombre(rs.getString("nombre"));
                academico.setApellidoPaterno(rs.getString("apellidoPaterno"));
                academico.setApellidoMaterno(rs.getString("apellidoMaterno"));
                academico.setNoPersonal(rs.getString("noPersonal"));
                academico.setCorreoInstitucional(rs.getString("correoInstitucional"));
                academico.setTipoContrato(TipoContrato.valueOf(rs.getString("tipoContrato")));
                academico.setEstudios(rs.getString("estudios"));
                //Objeto para que académico puedo acceder a todo lo que le pertenece a usuario
                Usuario usuario = new Usuario();
                usuario.setIdUsuario(rs.getInt("idUsuario"));
                academico.setUsuario(usuario);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(conexion != null){
                try { conexion.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
        return academico;
    }
    
    public static boolean registrarUsuarioConRol(Academico academico) {
        boolean resultado = false;
        Connection conexion = null;
        try {
            conexion = ConexionBD.abrirConexion();

            int idUsuario = insertarUsuario(academico.getUsuario(), conexion);
            insertarAcademico(academico, idUsuario, conexion);
            int idRol = academico.getUsuario().getRoles().get(0).getIdRol();
            insertarRolUsuario(idUsuario, idRol, conexion);
            resultado = true;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conexion != null) try { conexion.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return resultado;
    }

    private static int insertarUsuario(Usuario usuario, Connection conexion) throws SQLException {
        String query = "INSERT INTO usuario(username, password) VALUES(?, ?)";
        PreparedStatement ps = conexion.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, usuario.getUsername());
        ps.setString(2, usuario.getPassword());
        ps.executeUpdate();
        ResultSet rs = ps.getGeneratedKeys();
        if (!rs.next()) throw new SQLException("No se pudo obtener el ID del usuario");
        return rs.getInt(1);
    }

    private static void insertarAcademico(Academico academico, int idUsuario, Connection conexion) throws SQLException {
        String query = "INSERT INTO academico(noPersonal, nombre, apellidoPaterno, apellidoMaterno, correoInstitucional, " +
                " tipoContrato, estudios, idUsuario) VALUES(?,?,?,?,?,?,?,?)";
        PreparedStatement ps = conexion.prepareStatement(query);
        ps.setString(1, academico.getNoPersonal());
        ps.setString(2, academico.getNombre());
        ps.setString(3, academico.getApellidoPaterno());
        ps.setString(4, academico.getApellidoMaterno());
        ps.setString(5, academico.getCorreoInstitucional());
        ps.setString(6, academico.getTipoContrato().name());
        ps.setString(7, academico.getEstudios());
        ps.setInt(8, idUsuario);
        ps.executeUpdate();
    }

    private static void insertarRolUsuario(int idUsuario, int idRol, Connection conexion) throws SQLException {
        String query = "INSERT INTO usuariorol(idUsuario, idRol) VALUES(?, ?)";
        PreparedStatement ps = conexion.prepareStatement(query);
        ps.setInt(1, idUsuario);
        ps.setInt(2, idRol);
        ps.executeUpdate();
    }
    
    public static boolean actualizar(Academico academico) {
        boolean respuesta = false;
        Connection conexion = null;
        try {
            conexion = ConexionBD.abrirConexion();
            String query = "UPDATE academico SET noPersonal = ?, nombre = ?, apellidoPaterno = ?, " +
                           "apellidoMaterno = ?, correoInstitucional = ?, tipoContrato = ?, estudios = ? " +
                           "WHERE idEstudiante = ?";
            PreparedStatement ps = conexion.prepareStatement(query);
            ps.setString(1, academico.getNoPersonal());
            ps.setString(2, academico.getNombre());
            ps.setString(3, academico.getApellidoPaterno());
            ps.setString(4, academico.getApellidoMaterno());
            ps.setString(5, academico.getCorreoInstitucional());
            ps.setString(6, academico.getTipoContrato().name());
            ps.setString(7, academico.getEstudios());
            ps.setInt(8, academico.getIdAcademico()); 
            int filasAfectadas = ps.executeUpdate();
            respuesta = (filasAfectadas > 0);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conexion != null) {
                try { conexion.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
        return respuesta;
    }
    
    public static boolean darDeBaja(int idAcademico, String motivo) {
        boolean resultado = false;
        Connection conexion = null;
        try {
            conexion = ConexionBD.abrirConexion();
            String query = "UPDATE estudiante SET estatus = ? WHERE idEstudiante = ?";
            PreparedStatement ps = conexion.prepareStatement(query);
            ps.setString(1, motivo);
            ps.setInt(2, idAcademico);
            resultado = ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conexion != null) {
                try { conexion.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
        return resultado;
    }
}