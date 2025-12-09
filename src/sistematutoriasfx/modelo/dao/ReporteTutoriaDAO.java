/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistematutoriasfx.modelo.dao;

/**
 *
 * @author Ana Georgina
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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
    
    public static boolean eliminarReporte(int idReporte) {
        boolean resultado = false;
        Connection conexion = null;

        try {
            conexion = ConexionBD.abrirConexion();
            String query = "DELETE FROM reportetutoria WHERE idReporte = ?";

            PreparedStatement ps = conexion.prepareStatement(query);
            ps.setInt(1, idReporte);

            int filas = ps.executeUpdate();
            resultado = (filas > 0);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(conexion != null) {
                try { conexion.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }

        return resultado;
    }

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
    
    public static int obtenerIdReporte(int idAcademico, int idFechaTutoria) {
        ReporteTutoria r = obtenerReportePorSesion(idAcademico, idFechaTutoria);
        return (r != null) ? r.getIdReporte() : 0;
    }

    public static ArrayList<ReporteTutoria> obtenerReportesPorTutor(int idAcademico) {
        ArrayList<ReporteTutoria> lista = new ArrayList<>();
        Connection conexion = null;
        try {
            conexion = ConexionBD.abrirConexion();
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
                r.setIdAcademico(rs.getInt("idAcademico"));
                r.setIdPeriodo(rs.getInt("idPeriodo"));        
                r.setIdSesion(rs.getInt("idSesion"));         
                r.setTotalAsistentes(rs.getInt("totalAsistentes"));
                r.setTotalEnRiesgo(rs.getInt("totalEnRiesgo"));
                r.setComentariosGenerales(rs.getString("comentariosGenerales"));
                r.setEstatus(rs.getString("estatus"));
                r.setFechaEntrega(rs.getString("fechaEntrega"));

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
    
    public static ArrayList<ReporteTutoria> obtenerTodosLosReportes() {
        ArrayList<ReporteTutoria> lista = new ArrayList<>();
        try (Connection conexion = ConexionBD.abrirConexion()) {
            String query = "SELECT r.*, p.nombre AS periodoNombre, f.fechaSesion, f.numSesion " +
                           "FROM reportetutoria r " +
                           "INNER JOIN periodoescolar p ON r.idPeriodo = p.idPeriodo " +
                           "INNER JOIN sesiontutoria s ON r.idSesion = s.idSesion " +
                           "INNER JOIN fechastutoria f ON s.idFechaTutoria = f.idFechaTutoria";

            PreparedStatement ps = conexion.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ReporteTutoria r = new ReporteTutoria();
                r.setIdReporte(rs.getInt("idReporte"));
                r.setIdAcademico(rs.getInt("idAcademico"));
                r.setIdPeriodo(rs.getInt("idPeriodo"));
                r.setIdSesion(rs.getInt("idSesion"));
                r.setTotalAsistentes(rs.getInt("totalAsistentes"));
                r.setTotalEnRiesgo(rs.getInt("totalEnRiesgo"));
                r.setComentariosGenerales(rs.getString("comentariosGenerales"));

                // Traducción visual
                String estatusBD = rs.getString("estatus");
                if (estatusBD.equals("Entregado")) {
                    r.setEstatus("Sin revisar");
                } else {
                    r.setEstatus(estatusBD);
                }

                r.setFechaEntrega(rs.getString("fechaEntrega"));
                r.setPeriodoNombre(rs.getString("periodoNombre"));
                r.setFechaSesion(rs.getString("fechaSesion"));
                r.setNumSesion(rs.getInt("numSesion"));

                lista.add(r);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    // Marcar como revisado
    public static boolean marcarComoRevisado(int idReporte) {
        try (Connection conexion = ConexionBD.abrirConexion()) {
            String query = "UPDATE reportetutoria SET estatus = 'Revisado' WHERE idReporte = ?";
            PreparedStatement ps = conexion.prepareStatement(query);
            ps.setInt(1, idReporte);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Leer contenido del TXT
    public static String leerReporteTxt(int idReporte) {
        File archivo = new File("reportes_txt/reporte_" + idReporte + ".txt");
        if (!archivo.exists()) return "No se encontró el archivo TXT.";
        StringBuilder contenido = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                contenido.append(linea).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contenido.toString();
    }

    // Guardar TXT automáticamente (para el tutor)
    public static boolean guardarReporteTxt(ReporteTutoria reporte, String contenido) {
        File carpeta = new File("reportes_txt");
        if (!carpeta.exists()) carpeta.mkdir();

        File archivo = new File(carpeta, "reporte_" + reporte.getIdReporte() + ".txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivo))) {
            writer.write(contenido);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static int obtenerIdReporteActual(int idAcademico, int idPeriodo) {
        int idReporte = 0;
        Connection conexionBD = null;
        try {
            conexionBD = ConexionBD.abrirConexion();
            if (conexionBD != null) {
                String consulta = "SELECT idReporte FROM reportetutoria WHERE idAcademico = ? AND idPeriodo = ?";
                PreparedStatement prepararSentencia = conexionBD.prepareStatement(consulta);
                prepararSentencia.setInt(1, idAcademico);
                prepararSentencia.setInt(2, idPeriodo);
                ResultSet resultado = prepararSentencia.executeQuery();
                if (resultado.next()) {
                    idReporte = resultado.getInt("idReporte");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { if (conexionBD != null) conexionBD.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return idReporte;
    }
}