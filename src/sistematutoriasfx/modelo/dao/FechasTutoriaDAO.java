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
import sistematutoriasfx.modelo.pojo.FechasTutoria;

public class FechasTutoriaDAO {
    
    public static ArrayList<FechasTutoria> obtenerFechasPorPeriodo(int idPeriodo) {
        ArrayList<FechasTutoria> fechas = new ArrayList<>();
        Connection conexion = null;
        try {
            conexion = ConexionBD.abrirConexion();
            
            // Consultamos las fechas oficiales asignadas a ese periodo
            String query = "SELECT idFechaTutoria, idPeriodo, numSesion, fechaSesion " +
                           "FROM fechastutoria " +
                           "WHERE idPeriodo = ? " +
                           "ORDER BY numSesion ASC";
            
            PreparedStatement ps = conexion.prepareStatement(query);
            ps.setInt(1, idPeriodo);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                FechasTutoria fecha = new FechasTutoria();
                fecha.setIdFechaTutoria(rs.getInt("idFechaTutoria"));
                fecha.setIdPeriodo(rs.getInt("idPeriodo"));
                fecha.setNumSesion(rs.getInt("numSesion"));
                fecha.setFechaSesion(rs.getString("fechaSesion")); // MySQL devuelve fecha como String (YYYY-MM-DD)
                fechas.add(fecha);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(conexion != null){
                try { 
                    conexion.close(); 
                } catch (SQLException e) { 
                    e.printStackTrace(); 
                }
            }
        }
        return fechas;
    }
}