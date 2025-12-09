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
                estudiante.setEstatus(Estudiante.Estatus.valueOf(rs.getString("estatus")));
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

            // ✅ MODIFICAR: Concatenar nombre completo en la query
            String query = "SELECT e.*, " +
                           "CONCAT(e.nombreEstudiante, ' ', e.apellidoPaterno, ' ', " +
                           "COALESCE(e.apellidoMaterno, '')) AS nombreCompleto " +
                           "FROM estudiante e " +
                           "INNER JOIN asignaciontutor a ON e.idEstudiante = a.idEstudiante " +
                           "WHERE a.idAcademico = ? AND a.idPeriodo = ?";

            PreparedStatement ps = conexion.prepareStatement(query);
            ps.setInt(1, idAcademico);
            ps.setInt(2, idPeriodo);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                Estudiante est = new Estudiante();
                est.setIdEstudiante(rs.getInt("idEstudiante"));
                est.setMatricula(rs.getString("matricula"));

                // ✅ OPCIÓN 1: Usar el nombre completo concatenado
                est.setNombreEstudiante(rs.getString("nombreCompleto"));

                // También guarda los campos individuales si los necesitas
                // est.setApellidoPaterno(rs.getString("apellidoPaterno"));
                // est.setApellidoMaterno(rs.getString("apellidoMaterno"));

                // Crear CheckBoxes
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
}
