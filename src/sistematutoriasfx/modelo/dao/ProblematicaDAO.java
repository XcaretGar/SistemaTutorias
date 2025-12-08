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
import sistematutoriasfx.modelo.pojo.Problematica;

public class ProblematicaDAO {

    // 1. REGISTRAR
    public static boolean registrarProblematica(Problematica problema) {
        boolean resultado = false;
        Connection conexion = null;
        try {
            conexion = ConexionBD.abrirConexion();
            String query = "INSERT INTO problematica (idReporte, idEstudiante, titulo, descripcion, estatus) "
                         + "VALUES (?, ?, ?, ?, ?)";
            
            PreparedStatement ps = conexion.prepareStatement(query);
            ps.setInt(1, problema.getIdReporte());
            ps.setInt(2, problema.getIdEstudiante());
            ps.setString(3, problema.getTitulo());
            ps.setString(4, problema.getDescripcion());
            // Por defecto 'Registrada', o lo que venga en el objeto
            ps.setString(5, (problema.getEstatus() != null) ? problema.getEstatus() : "Registrada");
            
            int filas = ps.executeUpdate();
            resultado = (filas > 0);
            
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(conexion != null){ try { conexion.close(); } catch (SQLException e) {} }
        }
        return resultado;
    }
    
    // 2. ACTUALIZAR
    public static boolean actualizarProblematica(Problematica problema) {
        boolean resultado = false;
        Connection conexion = null;
        try {
            conexion = ConexionBD.abrirConexion();
            String query = "UPDATE problematica SET titulo = ?, descripcion = ?, estatus = ? "
                         + "WHERE idProblematica = ?";
            
            PreparedStatement ps = conexion.prepareStatement(query);
            ps.setString(1, problema.getTitulo());
            ps.setString(2, problema.getDescripcion());
            ps.setString(3, problema.getEstatus());
            ps.setInt(4, problema.getIdProblematica());
            
            int filas = ps.executeUpdate();
            resultado = (filas > 0);
            
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(conexion != null){ try { conexion.close(); } catch (SQLException e) {} }
        }
        return resultado;
    }

    // 3. ELIMINAR
    public static boolean eliminarProblematica(int idProblematica) {
        boolean resultado = false;
        Connection conexion = null;
        try {
            conexion = ConexionBD.abrirConexion();
            String query = "DELETE FROM problematica WHERE idProblematica = ?";
            PreparedStatement ps = conexion.prepareStatement(query);
            ps.setInt(1, idProblematica);
            
            int filas = ps.executeUpdate();
            resultado = (filas > 0);
            
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(conexion != null){ try { conexion.close(); } catch (SQLException e) {} }
        }
        return resultado;
    }

    // 4. CONSULTAR POR REPORTE (Para llenar la tabla)
    public static ArrayList<Problematica> obtenerProblematicasPorReporte(int idReporte) {
        ArrayList<Problematica> lista = new ArrayList<>();
        Connection conexion = null;
        try {
            conexion = ConexionBD.abrirConexion();
            
            // OJO AQUÍ: Traemos nombre, paterno y materno de la tabla 'estudiante'
            String query = "SELECT p.*, e.nombreEstudiante, e.apellidoPaterno, e.apellidoMaterno "
                         + "FROM problematica p "
                         + "INNER JOIN estudiante e ON p.idEstudiante = e.idEstudiante "
                         + "WHERE p.idReporte = ?";
            
            PreparedStatement ps = conexion.prepareStatement(query);
            ps.setInt(1, idReporte);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Problematica p = new Problematica();
                p.setIdProblematica(rs.getInt("idProblematica"));
                p.setIdReporte(rs.getInt("idReporte"));
                p.setIdEstudiante(rs.getInt("idEstudiante"));
                p.setTitulo(rs.getString("titulo"));
                p.setDescripcion(rs.getString("descripcion"));
                p.setEstatus(rs.getString("estatus"));
                
                // AQUÍ CONSTRUIMOS EL NOMBRE COMPLETO
                String nombreCompleto = rs.getString("nombreEstudiante") + " " 
                                      + rs.getString("apellidoPaterno") + " " 
                                      + (rs.getString("apellidoMaterno") != null ? rs.getString("apellidoMaterno") : "");
                
                p.setNombreEstudiante(nombreCompleto);
                
                lista.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(conexion != null){ try { conexion.close(); } catch (SQLException e) {} }
        }
        return lista;
    }
}
