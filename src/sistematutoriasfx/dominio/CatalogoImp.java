/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistematutoriasfx.dominio;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import sistematutoriasfx.modelo.dao.CatalogoDAO;
import sistematutoriasfx.modelo.pojo.PeriodoEscolar;
import sistematutoriasfx.modelo.pojo.ProgramaEducativo;
import sistematutoriasfx.modelo.pojo.Rol;
import sistematutoriasfx.modeloo.ConexionBD;

/**
 *
 * @author JOANA XCARET
 */
public class CatalogoImp {
    public static HashMap<String, Object> obtenerProgramasEducativos(){  
        HashMap<String,Object> respuesta = new LinkedHashMap<>(); 
        Connection conexion = null;
        try {  
            conexion = ConexionBD.abrirConexion();
            ResultSet rs = CatalogoDAO.obtenerProgramasEducativos(conexion);
            List<ProgramaEducativo> programasEducativos = new ArrayList<>();
            
            while(rs.next()){  
                ProgramaEducativo programaEducativo = new ProgramaEducativo();  
                programaEducativo.setIdPrograma(rs.getInt("idPrograma"));
                programaEducativo.setNombre(rs.getString("nombre"));
                programasEducativos.add(programaEducativo);  
            }  
            
            respuesta.put("error", false);  
            respuesta.put("programas", programasEducativos);  
        } catch (SQLException e) {  
            respuesta.put("error", true);  
            respuesta.put("mensaje", e.getMessage());
        } finally {
            if (conexion != null) {
                try { conexion.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
        return respuesta;  
    }
    
    public static HashMap<String, Object> obtenerTiposUsuarios(){  
        HashMap<String,Object> respuesta = new LinkedHashMap<>(); 
        Connection conexion = null;
        try {  
            conexion = ConexionBD.abrirConexion();
            ResultSet rs = CatalogoDAO.obtenerTiposUsuarios(conexion);
            List<Rol> roles = new ArrayList<>();
            
            while(rs.next()){  
                Rol rol = new Rol();  
                rol.setIdRol(rs.getInt("idRol"));
                rol.setNombre(rs.getString("nombre"));
                roles.add(rol);  
            }  
            
            respuesta.put("error", false);  
            respuesta.put("tiposUsuarios", roles);  
        } catch (SQLException e) {  
            respuesta.put("error", true);  
            respuesta.put("mensaje", e.getMessage());
        } finally {
            if (conexion != null) {
                try { conexion.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
        return respuesta;  
    }
    
    public static HashMap<String, Object> obtenerPeriodos() {
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        List<PeriodoEscolar> periodos = new ArrayList<>();

        Connection conexion = null;
        try {
            conexion = ConexionBD.abrirConexion();
            ResultSet rs = CatalogoDAO.obtenerPeriodosEscolares(conexion);

            while (rs.next()) {
                PeriodoEscolar periodo = new PeriodoEscolar();
                periodo.setIdPeriodo(rs.getInt("idPeriodo"));
                periodo.setNombre(rs.getString("nombre")); 
                periodos.add(periodo);
            }

            respuesta.put("error", false);
            respuesta.put("periodos", periodos);

        } catch (SQLException e) {
            respuesta.put("error", true);
            respuesta.put("mensaje", e.getMessage());
        } finally {
            if (conexion != null) {
                try { conexion.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
        return respuesta;
    }
}
