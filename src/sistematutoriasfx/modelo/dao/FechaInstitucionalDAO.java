/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistematutoriasfx.modelo.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import sistematutoriasfx.modelo.pojo.FechaInstitucional;
import sistematutoriasfx.modeloo.ConexionBD;

/**
 *
 * @author JOANA XCARET
 */
/*public class FechaInstitucionalDAO {
    
    public static boolean registrarFecha(FechaInstitucional fecha) {
        boolean exito = false;
        Connection conexion = null;
        try {
            conexion = ConexionBD.abrirConexion();
            String query = "INSERT INTO fechasinstitucionales (idPeriodo, numSesion, fechaSesion, descripcion) VALUES (?, ?, ?, ?)";
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

    public static List<FechaInstitucional> obtenerFechas() {
        List<FechaInstitucional> fechas = new ArrayList<>();
        Connection conexion = null;
        try {
            conexion = ConexionBD.abrirConexion();
            String query = "SELECT * FROM fechasinstitucionales";
            PreparedStatement ps = conexion.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                FechaInstitucional f = new FechaInstitucional();
                f.setIdFechaInstitucional(rs.getInt("idFechaInstitucional"));
                f.setIdPeriodo(rs.getInt("idPeriodo"));
                f.setNumSesion(rs.getInt("numSesion"));
                f.setFechaSesion(rs.getDate("fechaSesion").toLocalDate());
                f.setDescripcion(rs.getString("descripcion"));
                fechas.add(f);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conexion != null) try { conexion.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return fechas;
    }

    public static boolean actualizarFecha(FechaInstitucional fecha) {
        boolean exito = false;
        Connection conexion = null;
        try {
            conexion = ConexionBD.abrirConexion();
            String query = "UPDATE fechasinstitucionales SET idPeriodo = ?, numSesion = ?, fechaSesion = ?, descripcion = ? WHERE idFechaInstitucional = ?";
            PreparedStatement ps = conexion.prepareStatement(query);
            ps.setInt(1, fecha.getIdPeriodo());
            ps.setInt(2, fecha.getNumSesion());
            ps.setDate(3, Date.valueOf(fecha.getFechaSesion()));
            ps.setString(4, fecha.getDescripcion());
            ps.setInt(5, fecha.getIdFechaInstitucional());
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
                 "SELECT COUNT(*) AS total FROM fechasinstitucionales WHERE idPeriodo = ? AND fechaSesion = ? AND idFechaInstitucional <> ?")) {
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
}*/
