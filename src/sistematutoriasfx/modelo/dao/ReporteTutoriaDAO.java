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
import sistematutoriasfx.modelo.pojo.ReporteTutoria;

public class ReporteTutoriaDAO {

    // 1. CREAR NUEVO REPORTE (Borrador)
    public static boolean registrarReporte(ReporteTutoria reporte) {
        boolean resultado = false;
        Connection conexion = null;
        try {
            conexion = ConexionBD.abrirConexion();
            String query = "INSERT INTO reportetutoria "
                    + "(idAcademico, idPeriodo, idSesion, totalAsistentes, totalEnRiesgo, comentariosGenerales, estatus) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?)";
            
            PreparedStatement ps = conexion.prepareStatement(query);
            ps.setInt(1, reporte.getIdAcademico());
            ps.setInt(2, reporte.getIdPeriodo());
            ps.setInt(3, reporte.getIdSesion());
            ps.setInt(4, reporte.getTotalAsistentes());
            ps.setInt(5, reporte.getTotalEnRiesgo());
            ps.setString(6, reporte.getComentariosGenerales());
            ps.setString(7, "EnEdicion"); // Siempre nace en edición
            
            int filas = ps.executeUpdate();
            resultado = (filas > 0);
            
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(conexion != null){ try { conexion.close(); } catch (SQLException e) {} }
        }
        return resultado;
    }

    // 2. ACTUALIZAR UN REPORTE EXISTENTE
    public static boolean actualizarReporte(ReporteTutoria reporte) {
        boolean resultado = false;
        Connection conexion = null;
        try {
            conexion = ConexionBD.abrirConexion();
            String query = "UPDATE reportetutoria SET "
                    + "totalAsistentes = ?, totalEnRiesgo = ?, comentariosGenerales = ? "
                    + "WHERE idReporte = ?";
            
            PreparedStatement ps = conexion.prepareStatement(query);
            ps.setInt(1, reporte.getTotalAsistentes());
            ps.setInt(2, reporte.getTotalEnRiesgo());
            ps.setString(3, reporte.getComentariosGenerales());
            ps.setInt(4, reporte.getIdReporte());
            
            int filas = ps.executeUpdate();
            resultado = (filas > 0);
            
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(conexion != null){ try { conexion.close(); } catch (SQLException e) {} }
        }
        return resultado;
    }

    // 3. BUSCAR REPORTE POR SESIÓN (Para ver si ya existe y cargarlo)
    public static ReporteTutoria obtenerReportePorSesion(int idAcademico, int idFechaTutoria) {
        ReporteTutoria reporte = null;
        Connection conexion = null;
        try {
            conexion = ConexionBD.abrirConexion();
            
            // Hacemos JOIN con SesionTutoria para filtrar por idFechaTutoria
            String query = "SELECT r.* FROM reportetutoria r "
                         + "INNER JOIN sesiontutoria s ON r.idSesion = s.idSesion "
                         + "WHERE r.idAcademico = ? AND s.idFechaTutoria = ?";
            
            PreparedStatement ps = conexion.prepareStatement(query);
            ps.setInt(1, idAcademico);
            ps.setInt(2, idFechaTutoria);
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                reporte = new ReporteTutoria();
                reporte.setIdReporte(rs.getInt("idReporte"));
                reporte.setIdAcademico(rs.getInt("idAcademico"));
                reporte.setIdPeriodo(rs.getInt("idPeriodo"));
                reporte.setIdSesion(rs.getInt("idSesion"));
                reporte.setTotalAsistentes(rs.getInt("totalAsistentes"));
                reporte.setTotalEnRiesgo(rs.getInt("totalEnRiesgo"));
                reporte.setComentariosGenerales(rs.getString("comentariosGenerales"));
                reporte.setEstatus(rs.getString("estatus"));
                reporte.setFechaEntrega(rs.getString("fechaEntrega"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(conexion != null){ try { conexion.close(); } catch (SQLException e) {} }
        }
        return reporte;
    }
    
    // 4. MÉTODO RÁPIDO PARA OBTENER SOLO EL ID (Para Evidencias)
    public static int obtenerIdReporte(int idAcademico, int idFechaTutoria) {
        int id = 0;
        ReporteTutoria reporte = obtenerReportePorSesion(idAcademico, idFechaTutoria);
        if(reporte != null) {
            id = reporte.getIdReporte();
        }
        return id;
    }
}
