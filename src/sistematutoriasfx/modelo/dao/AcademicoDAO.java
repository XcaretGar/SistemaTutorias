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
import sistematutoriasfx.modelo.pojo.Academico;
import sistematutoriasfx.modelo.pojo.Academico.TipoContrato;
import sistematutoriasfx.modeloo.ConexionBD;

public class AcademicoDAO {
    
    public static Academico obtenerAcademicoPorIdUsuario(int idUsuario) {
        Academico academico = null;
        Connection conexion = null;
        try {
            conexion = ConexionBD.abrirConexion();
            String consulta = "SELECT * FROM Academico WHERE idUsuario = ?";
            PreparedStatement ps = conexion.prepareStatement(consulta);
            ps.setInt(1, idUsuario);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                academico = new Academico();
                academico.setIdAcademico(rs.getInt("idAcademico"));
                academico.setNombre(rs.getString("nombre"));
                academico.setApellidoPaterno(rs.getString("apellidoPaterno"));
                academico.setApellidoMaterno(rs.getString("apellidoMaterno"));
                academico.setNoPersonal(rs.getString("noPersonal"));
                academico.setCorreoInstitucional(rs.getString("correoInstitucional"));
                academico.setTipoContrato(TipoContrato.valueOf(rs.getString("tipoContrato")));
                academico.setEstudios(rs.getString("estudios"));
                academico.setIdUsuario(rs.getInt("idUsuario"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(conexion != null){
                try { conexion.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
        return academico;
    }
}