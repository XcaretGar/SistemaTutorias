/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistematutoriasfx.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.scene.control.CheckBox;
import sistematutoriasfx.modelo.pojo.Estudiante;
import sistematutoriasfx.modeloo.ConexionBD;

/**
 *
 * @author JOANA XCARET
 */
public class EstudianteDAO {
      
    public static ArrayList<Estudiante> obtenerEstudiantes() {
        ArrayList<Estudiante> estudiantes = new ArrayList<>();
        Connection conexion = null;
        try {
            conexion = ConexionBD.abrirConexion();
            
            String query = "SELECT idEstudiante, matricula, nombreEstudiante, apellidoPaterno, " +
                "apellidoMaterno, correoInstitucional, idProgramaEducativo, programaEducativo.nombre AS programaEducativo, estatus, motivoBaja " +
                "FROM estudiante " +
                "INNER JOIN programaEducativo programaEducativo ON programaEducativo.idPrograma = estudiante.idProgramaEducativo";
            PreparedStatement ps = conexion.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Estudiante estudiante = new Estudiante();
                estudiante.setIdEstudiante(rs.getInt("idEstudiante"));
                estudiante.setMatricula(rs.getString("matricula"));
                estudiante.setNombreEstudiante(rs.getString("nombreEstudiante"));
                estudiante.setApellidoPaterno(rs.getString("apellidoPaterno"));
                estudiante.setApellidoMaterno(rs.getString("apellidoMaterno"));
                estudiante.setCorreoInstitucional(rs.getString("correoInstitucional"));
                estudiante.setIdProgramaEducativo(rs.getInt("idProgramaEducativo"));
                estudiante.setProgramaEducativo(rs.getString("programaEducativo"));
                
                // Manejo seguro del Enum
                try {
                    estudiante.setEstatus(Estudiante.Estatus.valueOf(rs.getString("estatus")));
                } catch (IllegalArgumentException | NullPointerException e) {
                    estudiante.setEstatus(Estudiante.Estatus.Activo); // Valor por defecto si falla
                }
                
                estudiante.setMotivoBaja(rs.getString("motivoBaja"));
                boolean asignado = AsignacionTutorDAO.existeAsignacion(estudiante.getIdEstudiante());
                estudiante.setAsignado(asignado);

                estudiantes.add(estudiante);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(conexion != null){ try { conexion.close(); } catch (SQLException e) { e.printStackTrace(); } }
        }
        return estudiantes;
    }
    
    public static boolean registrar(Estudiante estudiante) {
        boolean respuesta = false;
        Connection conexion = null;
        try {
            conexion = ConexionBD.abrirConexion();

            String query = "INSERT INTO estudiante (matricula, nombreEstudiante, apellidoPaterno, apellidoMaterno, " +
                           "correoInstitucional, idProgramaEducativo, estatus) VALUES (?,?,?,?,?,?,?)";

            PreparedStatement ps = conexion.prepareStatement(query);
            ps.setString(1, estudiante.getMatricula());
            ps.setString(2, estudiante.getNombreEstudiante());
            ps.setString(3, estudiante.getApellidoPaterno());
            ps.setString(4, estudiante.getApellidoMaterno());
            ps.setString(5, estudiante.getCorreoInstitucional());
            ps.setInt(6, estudiante.getIdProgramaEducativo());
            ps.setString(7, estudiante.getEstatus().name());

            int filasAfectadas = ps.executeUpdate();
            respuesta = (filasAfectadas > 0);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conexion != null) {
                try { conexion.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
        return respuesta;
    }

    
    public static boolean actualizar(Estudiante estudiante) {
        boolean respuesta = false;
        Connection conexion = null;
        try {
            conexion = ConexionBD.abrirConexion();

            String query = "UPDATE estudiante SET matricula = ?, nombreEstudiante = ?, apellidoPaterno = ?, " +
                           "apellidoMaterno = ?, correoInstitucional = ?, idProgramaEducativo = ?, estatus = ?, motivoBaja = ? " +
                           "WHERE idEstudiante = ?";

            PreparedStatement ps = conexion.prepareStatement(query);
            ps.setString(1, estudiante.getMatricula());
            ps.setString(2, estudiante.getNombreEstudiante());
            ps.setString(3, estudiante.getApellidoPaterno());
            ps.setString(4, estudiante.getApellidoMaterno());
            ps.setString(5, estudiante.getCorreoInstitucional());
            ps.setInt(6, estudiante.getIdProgramaEducativo());
            ps.setString(7, estudiante.getEstatus().name());
            ps.setString(8, estudiante.getMotivoBaja());
            ps.setInt(9, estudiante.getIdEstudiante());

            int filasAfectadas = ps.executeUpdate();
            respuesta = (filasAfectadas > 0);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conexion != null) {
                try { conexion.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
        return respuesta;
    }
    
    public static boolean darDeBaja(int idEstudiante, String motivo) {
        boolean resultado = false;
        Connection conexion = null;
        try {
            conexion = ConexionBD.abrirConexion();
            String query = "UPDATE estudiante SET estatus = ?, motivoBaja = ? WHERE idEstudiante = ?";
            PreparedStatement ps = conexion.prepareStatement(query);
            ps.setString(1, "Baja");
            ps.setString(2, motivo.isEmpty() ? null : motivo);
            ps.setInt(3, idEstudiante);
            resultado = ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conexion != null) {
                try { conexion.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
        return resultado;
    }
    
    public static ArrayList<Estudiante> obtenerEstudiantesPorTutor(int idAcademico, int idPeriodo) {
        ArrayList<Estudiante> lista = new ArrayList<>();
        Connection conexion = null;

        try {
            conexion = ConexionBD.abrirConexion();
            // Consulta homologada con la estructura de tu compañera (JOINs correctos)
            String query = "SELECT e.*, p.nombre AS programaEducativo " +
                           "FROM estudiante e " +
                           "INNER JOIN asignaciontutor a ON e.idEstudiante = a.idEstudiante " +
                           "INNER JOIN programaeducativo p ON e.idProgramaEducativo = p.idPrograma " +
                           "WHERE a.idAcademico = ? AND a.idPeriodo = ?";

            PreparedStatement ps = conexion.prepareStatement(query);
            ps.setInt(1, idAcademico);
            ps.setInt(2, idPeriodo);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                Estudiante est = new Estudiante();
                est.setIdEstudiante(rs.getInt("idEstudiante"));
                est.setMatricula(rs.getString("matricula"));
                est.setNombreEstudiante(rs.getString("nombreEstudiante"));
                est.setApellidoPaterno(rs.getString("apellidoPaterno"));
                est.setApellidoMaterno(rs.getString("apellidoMaterno"));
                est.setCorreoInstitucional(rs.getString("correoInstitucional"));
                est.setIdProgramaEducativo(rs.getInt("idProgramaEducativo"));
                est.setProgramaEducativo(rs.getString("programaEducativo"));

                // Manejo de Enum seguro
                try {
                    est.setEstatus(Estudiante.Estatus.valueOf(rs.getString("estatus")));
                } catch (Exception ex) {
                    est.setEstatus(Estudiante.Estatus.Activo);
                }

                // Inicializamos los checkboxes por si se usan en la vista
                est.setCbAsistencia(new CheckBox());
                est.setCbRiesgo(new CheckBox());

                lista.add(est);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(conexion != null) {
                try { conexion.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
        return lista;
    }
    
    public static Estudiante obtenerEstudiantePorId(int idEstudiante) {
        Estudiante estudiante = null;
        Connection conexion = null;
        try {
            conexion = ConexionBD.abrirConexion();
            String query = "SELECT e.*, p.nombre AS programaEducativo " +
                           "FROM estudiante e " +
                           "INNER JOIN programaEducativo p ON e.idProgramaEducativo = p.idPrograma " +
                           "WHERE e.idEstudiante = ?";
            PreparedStatement ps = conexion.prepareStatement(query);
            ps.setInt(1, idEstudiante);
            ResultSet rs = ps.executeQuery();

            if(rs.next()) {
                estudiante = new Estudiante();
                estudiante.setIdEstudiante(rs.getInt("idEstudiante"));
                estudiante.setMatricula(rs.getString("matricula"));
                estudiante.setNombreEstudiante(rs.getString("nombreEstudiante"));
                estudiante.setApellidoPaterno(rs.getString("apellidoPaterno"));
                estudiante.setApellidoMaterno(rs.getString("apellidoMaterno"));
                estudiante.setCorreoInstitucional(rs.getString("correoInstitucional"));
                estudiante.setIdProgramaEducativo(rs.getInt("idProgramaEducativo"));
                estudiante.setProgramaEducativo(rs.getString("programaEducativo"));
                try {
                    estudiante.setEstatus(Estudiante.Estatus.valueOf(rs.getString("estatus")));
                } catch (Exception e) {
                    estudiante.setEstatus(Estudiante.Estatus.Activo);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(conexion != null) {
                try { conexion.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
        return estudiante;
    }

    public static ArrayList<Estudiante> obtenerTutoradosPorAcademico(int idAcademico) {
        ArrayList<Estudiante> lista = new ArrayList<>();
        Connection conexion = null;

        try {
            conexion = ConexionBD.abrirConexion();

            String query = "SELECT DISTINCT e.idEstudiante, e.matricula, e.nombreEstudiante, " +
                           "e.apellidoPaterno, e.apellidoMaterno, e.correoInstitucional, " +
                           "e.idProgramaEducativo, p.nombre AS programaEducativo " +
                           "FROM estudiante e " +
                           "INNER JOIN asignaciontutor a ON e.idEstudiante = a.idEstudiante " +
                           "INNER JOIN programaEducativo p ON e.idProgramaEducativo = p.idPrograma " +
                           "WHERE a.idAcademico = ? AND e.estatus = 'Activo' " +
                           "ORDER BY e.apellidoPaterno, e.apellidoMaterno, e.nombreEstudiante";

            PreparedStatement ps = conexion.prepareStatement(query);
            ps.setInt(1, idAcademico);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                Estudiante est = new Estudiante();
                est.setIdEstudiante(rs.getInt("idEstudiante"));
                est.setMatricula(rs.getString("matricula"));
                est.setNombreEstudiante(rs.getString("nombreEstudiante"));
                est.setApellidoPaterno(rs.getString("apellidoPaterno"));
                est.setApellidoMaterno(rs.getString("apellidoMaterno"));
                est.setCorreoInstitucional(rs.getString("correoInstitucional"));
                est.setIdProgramaEducativo(rs.getInt("idProgramaEducativo"));
                est.setProgramaEducativo(rs.getString("programaEducativo"));

                lista.add(est);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(conexion != null) {
                try { conexion.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
        return lista;
    }

}