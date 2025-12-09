/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistematutoriasfx.modelo.pojo;

/**
 *
 * @author JOANA XCARET
 */

import javafx.scene.control.CheckBox;

public class Estudiante {
    private int idEstudiante;
    private String matricula;
    private String nombreEstudiante;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String correoInstitucional;
    private int idProgramaEducativo;
    private String programaEducativo;
    private Estatus estatus; 
    private String motivoBaja;
    private boolean asignado;
    
    // (No se guardan en BD, son solo para la pantalla)
    private CheckBox cbAsistencia; 
    private CheckBox cbRiesgo;
    
    public Estudiante() {
        this.cbAsistencia = new CheckBox();
        this.cbRiesgo = new CheckBox();
    }

    public int getIdEstudiante() { return idEstudiante; }
    public void setIdEstudiante(int idEstudiante) { this.idEstudiante = idEstudiante; }

    public String getMatricula() { return matricula; }
    public void setMatricula(String matricula) { this.matricula = matricula; }

    public String getNombreEstudiante() { return nombreEstudiante; }
    public void setNombreEstudiante(String nombreEstudiante) { this.nombreEstudiante = nombreEstudiante; }

    public String getApellidoPaterno() { return apellidoPaterno; }
    public void setApellidoPaterno(String apellidoPaterno) { this.apellidoPaterno = apellidoPaterno; }

    public String getApellidoMaterno() { return apellidoMaterno; }
    public void setApellidoMaterno(String apellidoMaterno) { this.apellidoMaterno = apellidoMaterno; }

    public String getCorreoInstitucional() { return correoInstitucional; }
    public void setCorreoInstitucional(String correoInstitucional) { this.correoInstitucional = correoInstitucional; }

    public int getIdProgramaEducativo() { return idProgramaEducativo; }
    public void setIdProgramaEducativo(int idProgramaEducativo) { this.idProgramaEducativo = idProgramaEducativo; }

    public String getProgramaEducativo() { return programaEducativo; }
    public void setProgramaEducativo(String programaEducativo) { this.programaEducativo = programaEducativo; }

    public Estatus getEstatus() { return estatus; }
    public void setEstatus(Estatus estatus) { this.estatus = estatus; }

    public String getMotivoBaja() { return motivoBaja; }
    public void setMotivoBaja(String motivoBaja) { this.motivoBaja = motivoBaja; }

    public boolean isAsignado() { return asignado; }
    public void setAsignado(boolean asignado) { this.asignado = asignado; }
    
    public String getAsignadoTexto() { return asignado ? "Sí" : "No"; }

    public CheckBox getCbAsistencia() { return cbAsistencia; }
    public void setCbAsistencia(CheckBox cbAsistencia) { this.cbAsistencia = cbAsistencia; }

    public CheckBox getCbRiesgo() { return cbRiesgo; }
    public void setCbRiesgo(CheckBox cbRiesgo) { this.cbRiesgo = cbRiesgo; }

    public String getNombreCompleto() {
        String nombre = nombreEstudiante != null ? nombreEstudiante : "";
        String paterno = apellidoPaterno != null ? apellidoPaterno : "";
        String materno = apellidoMaterno != null ? apellidoMaterno : "";
        return (nombre + " " + paterno + " " + materno).trim();
    }
    
    public enum Estatus {
        Activo, EnRiesgo, Baja, Egresado
    }
    
    @Override
    public String toString() {
        return getNombreCompleto();
    }
}