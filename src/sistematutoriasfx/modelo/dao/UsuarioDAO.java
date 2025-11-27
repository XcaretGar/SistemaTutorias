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
import sistematutoriasfx.modeloo.ConexionBD;
import sistematutoriasfx.modelo.pojo.Usuario;

public class UsuarioDAO {

    public static Usuario verificarSesion(String username, String password) {
        Usuario usuarioVerificado = null;
        Connection conexion = null;
        try {
            conexion = ConexionBD.abrirConexion();
            // Hacemos un JOIN con Rol para saber qué permisos tendrá
            String consulta = "SELECT u.idUsuario, u.username, u.password, u.idRol, r.nombre AS nombreRol " +
                              "FROM Usuario u " +
                              "INNER JOIN Rol r ON u.idRol = r.idRol " +
                              "WHERE u.username = ? AND u.password = ?";
            
            PreparedStatement preparedStatement = conexion.prepareStatement(consulta);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            
            ResultSet resultado = preparedStatement.executeQuery();
            
            if (resultado.next()) {
                usuarioVerificado = new Usuario();
                usuarioVerificado.setIdUsuario(resultado.getInt("idUsuario"));
                usuarioVerificado.setUsername(resultado.getString("username"));
                usuarioVerificado.setPassword(resultado.getString("password"));
                usuarioVerificado.setIdRol(resultado.getInt("idRol"));
                usuarioVerificado.setNombreRol(resultado.getString("nombreRol"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            // Es buena práctica cerrar la conexión, aunque para este ejemplo 
            // simple lo dejaremos así por ahora para no complicar el código
            if (conexion != null) {
                try {
                    conexion.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return usuarioVerificado;
    }
}
