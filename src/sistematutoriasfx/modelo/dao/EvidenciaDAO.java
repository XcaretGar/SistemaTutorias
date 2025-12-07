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
import sistematutoriasfx.modelo.pojo.Evidencia;

public class EvidenciaDAO {

    public static boolean registrarEvidencia(Evidencia evidencia) {
        boolean resultado = false;
        Connection conexion = null;
        try {
            conexion = ConexionBD.abrirConexion();
            String query = "INSERT INTO evidencia (idReporte, nombreArchivo, rutaArchivo) VALUES (?, ?, ?)";
            
            PreparedStatement ps = conexion.prepareStatement(query);
            ps.setInt(1, evidencia.getIdReporte());
            ps.setString(2, evidencia.getNombreArchivo());
            ps.setString(3, evidencia.getRutaArchivo());
            
            int filas = ps.executeUpdate();
            resultado = (filas > 0);
            
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(conexion != null){
                try { conexion.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
        return resultado;
    }

    public static boolean actualizarEvidencia(Evidencia evidencia) {
        boolean resultado = false;
        Connection conexion = null;
        try {
            conexion = ConexionBD.abrirConexion();
            String query = "UPDATE evidencia SET nombreArchivo = ?, rutaArchivo = ? WHERE idEvidencia = ?";           
            PreparedStatement ps = conexion.prepareStatement(query);
            ps.setString(1, evidencia.getNombreArchivo());
            ps.setString(2, evidencia.getRutaArchivo());
            ps.setInt(3, evidencia.getIdEvidencia());
            
            int filas = ps.executeUpdate();
            resultado = (filas > 0);
            
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(conexion != null){
                try { conexion.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
        return resultado;
    }

    public static ArrayList<Evidencia> obtenerEvidenciasPorReporte(int idReporte) {
        ArrayList<Evidencia> lista = new ArrayList<>();
        Connection conexion = null;
        try {
            conexion = ConexionBD.abrirConexion();
            String query = "SELECT * FROM evidencia WHERE idReporte = ?";
            
            PreparedStatement ps = conexion.prepareStatement(query);
            ps.setInt(1, idReporte);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Evidencia ev = new Evidencia();
                ev.setIdEvidencia(rs.getInt("idEvidencia"));
                ev.setIdReporte(rs.getInt("idReporte"));
                ev.setNombreArchivo(rs.getString("nombreArchivo"));
                ev.setRutaArchivo(rs.getString("rutaArchivo"));
                ev.setFechaSubida(rs.getString("fechaSubida"));
                
                lista.add(ev);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(conexion != null){
                try { conexion.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
        return lista;
    }

    public static boolean eliminarEvidencia(int idEvidencia) {
        boolean resultado = false;
        Connection conexion = null;
        try {
            conexion = ConexionBD.abrirConexion();
            String query = "DELETE FROM evidencia WHERE idEvidencia = ?";
            
            PreparedStatement ps = conexion.prepareStatement(query);
            ps.setInt(1, idEvidencia);
            
            int filas = ps.executeUpdate();
            resultado = (filas > 0);
            
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(conexion != null){
                try { conexion.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
        return resultado;
    }
}