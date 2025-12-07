/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistematutoriasfx.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import sistematutoriasfx.modelo.pojo.Rol;
import sistematutoriasfx.modeloo.ConexionBD;

/**
 *
 * @author JOANA XCARET
 */
public class RolDAO {
    
    public static ArrayList<Rol> obtenerRolesPorIdUsuario(int idUsuario) {
        ArrayList<Rol> roles = new ArrayList<>();
        Connection conexion = null;
        try {
            conexion = ConexionBD.abrirConexion();
            String query = "SELECT r.idRol, r.nombre FROM rol r " +
                           "JOIN usuariorol ur ON r.idRol = ur.idRol " +
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
            if (conexion != null) try { conexion.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return roles;
    } 
}
