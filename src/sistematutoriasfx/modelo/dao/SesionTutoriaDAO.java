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
import sistematutoriasfx.modelo.pojo.SesionTutoria;

public class SesionTutoriaDAO {
    
    public static boolean registrarSesion(SesionTutoria nuevaSesion) {
        boolean respuesta = false;
        Connection conexion = null;
        try {
            conexion = ConexionBD.abrirConexion();
            String query = "INSERT INTO sesiontutoria (idAcademico, idPeriodo, idFechaTutoria, hora, lugar, comentarios) " +
                           "VALUES (?, ?, ?, ?, ?, ?)";
            
            PreparedStatement ps = conexion.prepareStatement(query);
            ps.setInt(1, nuevaSesion.getIdAcademico());
            ps.setInt(2, nuevaSesion.getIdPeriodo());
            ps.setInt(3, nuevaSesion.getIdFechaTutoria());
            ps.setString(4, nuevaSesion.getHora());
            ps.setString(5, nuevaSesion.getLugar());
            ps.setString(6, nuevaSesion.getComentarios());
            
            int filasAfectadas = ps.executeUpdate();
            respuesta = (filasAfectadas > 0);
            
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(conexion != null){
                try { conexion.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
        return respuesta;
    }
    
    public static boolean actualizarSesion(SesionTutoria sesion) {
        boolean respuesta = false;
        Connection conexion = null;
        try {
            conexion = ConexionBD.abrirConexion();
            String query = "UPDATE sesiontutoria SET idPeriodo = ?, idFechaTutoria = ?, hora = ?, lugar = ?, comentarios = ? " +
                           "WHERE idSesion = ?";
            
            PreparedStatement ps = conexion.prepareStatement(query);
            ps.setInt(1, sesion.getIdPeriodo());
            ps.setInt(2, sesion.getIdFechaTutoria());
            ps.setString(3, sesion.getHora());
            ps.setString(4, sesion.getLugar());
            ps.setString(5, sesion.getComentarios());
            ps.setInt(6, sesion.getIdSesion());
            
            int filasAfectadas = ps.executeUpdate();
            respuesta = (filasAfectadas > 0);
            
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(conexion != null){
                try { conexion.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
        return respuesta;
    }

    public static ArrayList<SesionTutoria> obtenerSesionesPorTutor(int idAcademico) {
        ArrayList<SesionTutoria> sesiones = new ArrayList<>();
        Connection conexion = null;
        try {
            conexion = ConexionBD.abrirConexion();

            // ✅ MODIFICADO: Agregamos el conteo de total de alumnos
            String query = "SELECT s.*, p.nombre AS periodoNombre, ft.fechaSesion, ft.numSesion, " +
                           "(SELECT COUNT(*) FROM listaasistencia l WHERE l.idSesion = s.idSesion AND l.asistio = 1) AS totalAsistentes, " +
                           "(SELECT COUNT(*) FROM listaasistencia l WHERE l.idSesion = s.idSesion AND l.riesgoDetectado = 1) AS totalRiesgo, " +
                           // ✅ NUEVA LÍNEA: Contar total de alumnos asignados
                           "(SELECT COUNT(*) FROM asignaciontutor WHERE idAcademico = ? AND idPeriodo = s.idPeriodo) AS totalAlumnos " +
                           "FROM sesiontutoria s " +
                           "INNER JOIN periodoescolar p ON s.idPeriodo = p.idPeriodo " +
                           "INNER JOIN fechastutoria ft ON s.idFechaTutoria = ft.idFechaTutoria " +
                           "WHERE s.idAcademico = ?";

            PreparedStatement ps = conexion.prepareStatement(query);
            ps.setInt(1, idAcademico); // ✅ Para el subquery de totalAlumnos
            ps.setInt(2, idAcademico); // Para el WHERE principal
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                SesionTutoria sesion = new SesionTutoria();
                sesion.setIdSesion(rs.getInt("idSesion"));
                sesion.setIdPeriodo(rs.getInt("idPeriodo"));
                sesion.setIdFechaTutoria(rs.getInt("idFechaTutoria"));

                sesion.setFecha(rs.getString("fechaSesion")); 
                sesion.setNumSesion(rs.getInt("numSesion"));
                sesion.setPeriodo(rs.getString("periodoNombre"));

                sesion.setHora(rs.getString("hora"));
                sesion.setLugar(rs.getString("lugar"));
                sesion.setComentarios(rs.getString("comentarios"));

                sesion.setTotalAsistentes(rs.getInt("totalAsistentes"));
                sesion.setTotalRiesgo(rs.getInt("totalRiesgo"));
                sesion.setTotalAlumnos(rs.getInt("totalAlumnos")); // ✅ NUEVO

                sesiones.add(sesion);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(conexion != null){ try { conexion.close(); } catch (SQLException e) { e.printStackTrace(); } }
        }
        return sesiones;
    }
}
