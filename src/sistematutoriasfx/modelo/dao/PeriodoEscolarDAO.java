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
import sistematutoriasfx.modelo.pojo.PeriodoEscolar;

public class PeriodoEscolarDAO {
    
    public static ArrayList<PeriodoEscolar> obtenerPeriodos() {
        ArrayList<PeriodoEscolar> periodos = new ArrayList<>();
        Connection conexion = null;
        try {
            conexion = ConexionBD.abrirConexion();
            String query = "SELECT * FROM PeriodoEscolar ORDER BY nombre DESC";
            PreparedStatement ps = conexion.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                PeriodoEscolar periodo = new PeriodoEscolar();
                periodo.setIdPeriodo(rs.getInt("idPeriodo"));
                periodo.setNombre(rs.getString("nombre"));
                periodo.setFechaInicio(rs.getString("fechaInicio"));
                periodo.setFechaFin(rs.getString("fechaFin"));
                periodos.add(periodo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(conexion != null){ try { conexion.close(); } catch (SQLException e) { e.printStackTrace(); } }
        }
        return periodos;
    }
}