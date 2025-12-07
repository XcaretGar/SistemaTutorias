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
import java.sql.SQLException;
import sistematutoriasfx.modeloo.ConexionBD;
import sistematutoriasfx.modelo.pojo.ListaAsistencia;

public class ListaAsistenciaDAO {

    public static boolean registrarAsistencia(ListaAsistencia lista) {
        boolean resultado = false;
        Connection conexion = null;
        try {
            conexion = ConexionBD.abrirConexion();
            String query = "INSERT INTO listaasistencia (idSesion, idEstudiante, asistio, riesgoDetectado) "
                         + "VALUES (?, ?, ?, ?)";
            
            PreparedStatement ps = conexion.prepareStatement(query);
            ps.setInt(1, lista.getIdSesion());
            ps.setInt(2, lista.getIdEstudiante());
            ps.setBoolean(3, lista.isAsistio());
            ps.setBoolean(4, lista.isRiesgoDetectado());
            
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
    
    // Método opcional: Borrar asistencia previa de una sesión
    // Útil si el tutor se equivocó y quiere volver a pasar lista (limpiar y re-insertar)
    public static boolean eliminarAsistenciaPorSesion(int idSesion) {
        boolean resultado = false;
        Connection conexion = null;
        try {
            conexion = ConexionBD.abrirConexion();
            String query = "DELETE FROM listaasistencia WHERE idSesion = ?";
            
            PreparedStatement ps = conexion.prepareStatement(query);
            ps.setInt(1, idSesion);
            
            int filas = ps.executeUpdate();
            resultado = (filas >= 0); // >= 0 porque si no había nada que borrar, no es error
            
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