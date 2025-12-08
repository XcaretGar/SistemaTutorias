/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistematutoriasfx.modelo.pojo;

/**
 *
 * @author Ana Georgina
 */

public class SesionTutoria {
    private int idSesion;
    private int idAcademico;
    private int idPeriodo;
    private int idFechaTutoria; 
    private String hora; 
    private String lugar;
    private String comentarios;
    
    //Atributos de fechastutoria
    //Se llenan con los INNER JOIN del DAO
    private int numSesion;      
    private String fecha;       
    private String periodo;
    
    //Atributos de listaasistencia
    private int totalAsistentes;
    private int totalRiesgo;
    private int totalAlumnos;

    public SesionTutoria() {
    }

    public SesionTutoria(int idSesion, int idAcademico, int idPeriodo, int idFechaTutoria, String hora, String lugar, String comentarios, int numSesion, String fecha, String periodo) {
        this.idSesion = idSesion;
        this.idAcademico = idAcademico;
        this.idPeriodo = idPeriodo;
        this.idFechaTutoria = idFechaTutoria;
        this.hora = hora;
        this.lugar = lugar;
        this.comentarios = comentarios;
        this.numSesion = numSesion;
        this.fecha = fecha;
        this.periodo = periodo;
    }

    public int getIdSesion() {
        return idSesion;
    }

    public void setIdSesion(int idSesion) {
        this.idSesion = idSesion;
    }

    public int getIdAcademico() {
        return idAcademico;
    }

    public void setIdAcademico(int idAcademico) {
        this.idAcademico = idAcademico;
    }

    public int getIdPeriodo() {
        return idPeriodo;
    }

    public void setIdPeriodo(int idPeriodo) {
        this.idPeriodo = idPeriodo;
    }

    public int getIdFechaTutoria() {
        return idFechaTutoria;
    }

    public void setIdFechaTutoria(int idFechaTutoria) {
        this.idFechaTutoria = idFechaTutoria;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    public int getNumSesion() {
        return numSesion;
    }

    public void setNumSesion(int numSesion) {
        this.numSesion = numSesion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public int getTotalAsistentes() {
        return totalAsistentes;
    }

    public void setTotalAsistentes(int totalAsistentes) {
        this.totalAsistentes = totalAsistentes;
    }

    public int getTotalRiesgo() {
        return totalRiesgo;
    }

    public void setTotalRiesgo(int totalRiesgo) {
        this.totalRiesgo = totalRiesgo;
    }

    public int getTotalAlumnos() {
        return totalAlumnos;
    }

    public void setTotalAlumnos(int totalAlumnos) {
        this.totalAlumnos = totalAlumnos;
    }
    
    
    
}
