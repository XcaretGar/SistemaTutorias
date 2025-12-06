/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistematutoriasfx.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author JOANA XCARET
 */
public class CatalogoDAO {
    public static ResultSet obtenerProgramasEducativos(Connection conexionBD) throws SQLException {
        if (conexionBD != null) {
            String query = "SELECT * FROM programaEducativo";
            PreparedStatement ps = conexionBD.prepareStatement(query);
            return ps.executeQuery();
        }
        throw new SQLException("Lo sentimos no hay conexión a la base de datos");
    }
}
