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
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
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
            String query = "SELECT idFechaTutoria, idPeriodo, numSesion, fechaSesion, descripcion " +
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
                fecha.setFechaSesion(rs.getDate("fechaSesion").toLocalDate()); 
                fecha.setDescripcion(rs.getString("descripcion"));
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
    
    public static boolean registrarFecha(FechasTutoria fecha) {
        boolean exito = false;
        Connection conexion = null;
        try {
            conexion = ConexionBD.abrirConexion();
            String query = "INSERT INTO fechastutoria (idPeriodo, numSesion, fechaSesion, descripcion) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = conexion.prepareStatement(query);
            ps.setInt(1, fecha.getIdPeriodo());
            ps.setInt(2, fecha.getNumSesion());
            ps.setDate(3, Date.valueOf(fecha.getFechaSesion()));
            ps.setString(4, fecha.getDescripcion());
            exito = ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conexion != null) try { conexion.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return exito;
    }

    public static boolean actualizarFecha(FechasTutoria fecha) {
        boolean exito = false;
        Connection conexion = null;
        try {
            conexion = ConexionBD.abrirConexion();
            String query = "UPDATE fechastutoria SET idPeriodo = ?, numSesion = ?, fechaSesion = ?, descripcion = ? WHERE idFechaTutoria = ?";
            PreparedStatement ps = conexion.prepareStatement(query);
            ps.setInt(1, fecha.getIdPeriodo());
            ps.setInt(2, fecha.getNumSesion());
            ps.setDate(3, Date.valueOf(fecha.getFechaSesion()));
            ps.setString(4, fecha.getDescripcion());
            ps.setInt(5, fecha.getIdFechaTutoria());
            exito = ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conexion != null) try { conexion.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return exito;
    }
    
    public static boolean existeTraslape(int idPeriodo, LocalDate fecha, Integer idFechaActual) {
        boolean traslape = false;
        try (Connection conexion = ConexionBD.abrirConexion();
             PreparedStatement ps = conexion.prepareStatement(
                 "SELECT COUNT(*) AS total FROM fechastutoria WHERE idPeriodo = ? AND fechaSesion = ? AND idFechaTutoria <> ?")) {
            ps.setInt(1, idPeriodo);
            ps.setDate(2, Date.valueOf(fecha));
            ps.setInt(3, idFechaActual == null ? -1 : idFechaActual); 
            ResultSet rs = ps.executeQuery();
            if (rs.next() && rs.getInt("total") > 0) {
                traslape = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return traslape;
    }
}