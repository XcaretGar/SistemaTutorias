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
import sistematutoriasfx.modelo.pojo.Problematica;
import sistematutoriasfx.modeloo.ConexionBD;

public class ProblematicaDAO {

    public static boolean registrarProblematica(Problematica problema) {
        boolean respuesta = false;
        Connection conexionBD = null;
        try {
            conexionBD = ConexionBD.abrirConexion();
            if (conexionBD != null) {
                String sentencia = "INSERT INTO problematica (idReporte, idEstudiante, titulo, descripcion, estatus) "
                        + "VALUES (?, ?, ?, ?, ?)";
                PreparedStatement prepararSentencia = conexionBD.prepareStatement(sentencia);
                
                prepararSentencia.setInt(1, problema.getIdReporte());
                prepararSentencia.setInt(2, problema.getIdEstudiante());
                prepararSentencia.setString(3, problema.getTitulo());
                prepararSentencia.setString(4, problema.getDescripcion());
                prepararSentencia.setString(5, problema.getEstatus());
                
                int filasAfectadas = prepararSentencia.executeUpdate();
                respuesta = (filasAfectadas > 0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (conexionBD != null) conexionBD.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return respuesta;
    }

    public static boolean actualizarProblematica(Problematica problema) {
        boolean respuesta = false;
        Connection conexionBD = null;
        try {
            conexionBD = ConexionBD.abrirConexion();
            if (conexionBD != null) {
                String sentencia = "UPDATE problematica SET titulo = ?, descripcion = ?, estatus = ? "
                        + "WHERE idProblematica = ?";
                PreparedStatement prepararSentencia = conexionBD.prepareStatement(sentencia);
                prepararSentencia.setString(1, problema.getTitulo());
                prepararSentencia.setString(2, problema.getDescripcion());
                prepararSentencia.setString(3, problema.getEstatus());
                prepararSentencia.setInt(4, problema.getIdProblematica());
                
                int filasAfectadas = prepararSentencia.executeUpdate();
                respuesta = (filasAfectadas > 0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (conexionBD != null) conexionBD.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return respuesta;
    }

    public static boolean eliminarProblematica(int idProblematica) {
        boolean respuesta = false;
        Connection conexionBD = null;
        try {
            conexionBD = ConexionBD.abrirConexion();
            if (conexionBD != null) {
                String sentencia = "DELETE FROM problematica WHERE idProblematica = ?";
                PreparedStatement prepararSentencia = conexionBD.prepareStatement(sentencia);
                prepararSentencia.setInt(1, idProblematica);
                int filasAfectadas = prepararSentencia.executeUpdate();
                respuesta = (filasAfectadas > 0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (conexionBD != null) conexionBD.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return respuesta;
    }

    public static ArrayList<Problematica> obtenerProblematicasPorReporte(int idReporte) {
        ArrayList<Problematica> problematicas = new ArrayList<>();
        Connection conexionBD = null;
        try {
            conexionBD = ConexionBD.abrirConexion();
            if (conexionBD != null) {
                String consulta = "SELECT p.idProblematica, p.titulo, p.descripcion, p.estatus, p.idReporte, p.idEstudiante, "
                        + "e.nombreEstudiante, e.apellidoPaterno, e.apellidoMaterno, e.matricula, "
                        + "pe.nombre AS nombrePrograma " 
                        + "FROM problematica p "
                        + "INNER JOIN estudiante e ON p.idEstudiante = e.idEstudiante "
                        + "INNER JOIN programaeducativo pe ON e.idProgramaEducativo = pe.idPrograma " 
                        + "WHERE p.idReporte = ?";
                
                PreparedStatement prepararSentencia = conexionBD.prepareStatement(consulta);
                prepararSentencia.setInt(1, idReporte);
                ResultSet resultado = prepararSentencia.executeQuery();
                
                while (resultado.next()) {
                    Problematica problema = new Problematica();
                    problema.setIdProblematica(resultado.getInt("idProblematica"));
                    problema.setTitulo(resultado.getString("titulo"));
                    problema.setDescripcion(resultado.getString("descripcion"));
                    problema.setEstatus(resultado.getString("estatus"));
                    problema.setIdReporte(resultado.getInt("idReporte"));
                    problema.setIdEstudiante(resultado.getInt("idEstudiante"));
                    
                    // Datos del Estudiante
                    String nombreCompleto = resultado.getString("nombreEstudiante") + " " + 
                                            resultado.getString("apellidoPaterno") + " " + 
                                            resultado.getString("apellidoMaterno");
                    problema.setNombreEstudiante(nombreCompleto);
                    problema.setMatricula(resultado.getString("matricula"));
                    
                    // Dato del Programa Educativo
                    problema.setProgramaEducativo(resultado.getString("nombrePrograma")); // Guardamos el dato
                    
                    problematicas.add(problema);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { if (conexionBD != null) conexionBD.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return problematicas;
    }
}