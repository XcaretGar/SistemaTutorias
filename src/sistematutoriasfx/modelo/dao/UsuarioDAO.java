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
import java.util.ArrayList;
import sistematutoriasfx.modeloo.ConexionBD;
import sistematutoriasfx.modelo.pojo.Rol;
import sistematutoriasfx.modelo.pojo.Usuario;

public class UsuarioDAO {

    public static Usuario verificarSesion(String username, String password) {
        Usuario usuarioVerificado = null;
        Connection conexion = null;
        try {
            conexion = ConexionBD.abrirConexion();
            String query = "SELECT idUsuario, username, password FROM usuario WHERE username = ? AND password = ?";
            PreparedStatement ps = conexion.prepareStatement(query);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                usuarioVerificado = new Usuario();
                usuarioVerificado.setIdUsuario(rs.getInt("idUsuario"));
                usuarioVerificado.setUsername(rs.getString("username"));
                usuarioVerificado.setPassword(rs.getString("password"));
                usuarioVerificado.setRoles(obtenerRolesDeUsuario(usuarioVerificado.getIdUsuario()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(conexion != null){ try { conexion.close(); } catch (SQLException e) { e.printStackTrace(); } }
        }
        return usuarioVerificado;
    }
    
    public static ArrayList<Rol> obtenerRolesDeUsuario(int idUsuario) {
        ArrayList<Rol> roles = new ArrayList<>();
        Connection conexion = null;
        try {
            conexion = ConexionBD.abrirConexion();
            // JOIN entre Rol y UsuarioRol para sacar los nombres reales
            String query = "SELECT r.idRol, r.nombre FROM rol r " +
                           "INNER JOIN usuariorol ur ON r.idRol = ur.idRol " +
                           "WHERE ur.idUsuario = ?";
            PreparedStatement ps = conexion.prepareStatement(query);
            ps.setInt(1, idUsuario);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Rol rol = new Rol();
                rol.setIdRol(rs.getInt("idRol"));
                rol.setNombre(rs.getString("nombre"));
                roles.add(rol);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(conexion != null){ try { conexion.close(); } catch (SQLException e) { e.printStackTrace(); } }
        }
        return roles;
    }
}