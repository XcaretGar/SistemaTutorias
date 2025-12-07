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
import sistematutoriasfx.modelo.pojo.ReporteTutoria;

public class ReporteTutoriaDAO {

    // 1. REGISTRAR
    public static boolean registrarReporte(ReporteTutoria reporte) {
        boolean resultado = false;
        Connection conexion = null;
        try {
            conexion = ConexionBD.abrirConexion();
            String query = "INSERT INTO reportetutoria (idAcademico, idPeriodo, idSesion, totalAsistentes, totalEnRiesgo, comentariosGenerales, estatus) "
                         + "VALUES (?, ?, ?, ?, ?, ?, ?)";
            
            PreparedStatement ps = conexion.prepareStatement(query);
            ps.setInt(1, reporte.getIdAcademico());
            ps.setInt(2, reporte.getIdPeriodo());
            ps.setInt(3, reporte.getIdSesion());
            ps.setInt(4, reporte.getTotalAsistentes());
            ps.setInt(5, reporte.getTotalEnRiesgo());
            ps.setString(6, reporte.getComentariosGenerales());
            ps.setString(7, "EnEdicion");
            
            resultado = ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(conexion != null){ try { conexion.close(); } catch (SQLException e) {} }
        }
        return resultado;
    }

    // 2. ACTUALIZAR
    public static boolean actualizarReporte(ReporteTutoria reporte) {
        boolean resultado = false;
        Connection conexion = null;
        try {
            conexion = ConexionBD.abrirConexion();
            String query = "UPDATE reportetutoria SET totalAsistentes = ?, totalEnRiesgo = ?, comentariosGenerales = ? WHERE idReporte = ?";
            
            PreparedStatement ps = conexion.prepareStatement(query);
            ps.setInt(1, reporte.getTotalAsistentes());
            ps.setInt(2, reporte.getTotalEnRiesgo());
            ps.setString(3, reporte.getComentariosGenerales());
            ps.setInt(4, reporte.getIdReporte());
            
            resultado = ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(conexion != null){ try { conexion.close(); } catch (SQLException e) {} }
        }
        return resultado;
    }

    // 3. OBTENER POR SESIÓN (Para el formulario)
    public static ReporteTutoria obtenerReportePorSesion(int idAcademico, int idFechaTutoria) {
        ReporteTutoria reporte = null;
        Connection conexion = null;
        try {
            conexion = ConexionBD.abrirConexion();
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
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(conexion != null){ try { conexion.close(); } catch (SQLException e) {} }
        }
        return reporte;
    }
    
    // 4. OBTENER ID (Auxiliar)
    public static int obtenerIdReporte(int idAcademico, int idFechaTutoria) {
        ReporteTutoria r = obtenerReportePorSesion(idAcademico, idFechaTutoria);
        return (r != null) ? r.getIdReporte() : 0;
    }

    // 5. OBTENER LISTA PARA TABLA (NUEVO)
    public static ArrayList<ReporteTutoria> obtenerReportesPorTutor(int idAcademico) {
        ArrayList<ReporteTutoria> lista = new ArrayList<>();
        Connection conexion = null;
        try {
            conexion = ConexionBD.abrirConexion();
            // JOIN TRIPLE para sacar nombres bonitos
            String query = "SELECT r.*, p.nombre AS periodoNombre, f.fechaSesion, f.numSesion "
                         + "FROM reportetutoria r "
                         + "INNER JOIN periodoescolar p ON r.idPeriodo = p.idPeriodo "
                         + "INNER JOIN sesiontutoria s ON r.idSesion = s.idSesion "
                         + "INNER JOIN fechastutoria f ON s.idFechaTutoria = f.idFechaTutoria "
                         + "WHERE r.idAcademico = ?";
            
            PreparedStatement ps = conexion.prepareStatement(query);
            ps.setInt(1, idAcademico);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                ReporteTutoria r = new ReporteTutoria();
                r.setIdReporte(rs.getInt("idReporte"));
                r.setEstatus(rs.getString("estatus"));
                r.setTotalAsistentes(rs.getInt("totalAsistentes"));
                r.setTotalEnRiesgo(rs.getInt("totalEnRiesgo"));
                r.setComentariosGenerales(rs.getString("comentariosGenerales"));
                
                // Datos virtuales
                r.setPeriodoNombre(rs.getString("periodoNombre"));
                r.setFechaSesion(rs.getString("fechaSesion"));
                r.setNumSesion(rs.getInt("numSesion"));
                
                lista.add(r);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(conexion != null){ try { conexion.close(); } catch (SQLException e) {} }
        }
        return lista;
    }
}