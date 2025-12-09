/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistematutoriasfx.modelo.pojo;

import java.util.ArrayList;

/**
 *
 * @author Ana Georgina
 */
public class ReporteTutoria {
    private int idReporte;
    private int idAcademico;
    private int idPeriodo;
    private int idSesion;
    private int totalAsistentes;
    private int totalEnRiesgo;
    private String comentariosGenerales;
    private String estatus;     
    private String fechaEntrega; // Puede ser null si aun no se entrega
    
    //Campos auxiliares
    private String periodoNombre;
    private String fechaSesion;
    private int numSesion;

    public ReporteTutoria() {
        
    }

    public int getIdReporte() {
        return idReporte;
    }

    public void setIdReporte(int idReporte) {
        this.idReporte = idReporte;
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

    public int getIdSesion() {
        return idSesion;
    }

    public void setIdSesion(int idSesion) {
        this.idSesion = idSesion;
    }

    public int getTotalAsistentes() {
        return totalAsistentes;
    }

    public void setTotalAsistentes(int totalAsistentes) {
        this.totalAsistentes = totalAsistentes;
    }

    public int getTotalEnRiesgo() {
        return totalEnRiesgo;
    }

    public void setTotalEnRiesgo(int totalEnRiesgo) {
        this.totalEnRiesgo = totalEnRiesgo;
    }

    public String getComentariosGenerales() {
        return comentariosGenerales;
    }

    public void setComentariosGenerales(String comentariosGenerales) {
        this.comentariosGenerales = comentariosGenerales;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public String getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(String fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public String getPeriodoNombre() {
        return periodoNombre;
    }

    public void setPeriodoNombre(String periodoNombre) {
        this.periodoNombre = periodoNombre;
    }

    public String getFechaSesion() {
        return fechaSesion;
    }

    public void setFechaSesion(String fechaSesion) {
        this.fechaSesion = fechaSesion;
    }

    public int getNumSesion() {
        return numSesion;
    }

    public void setNumSesion(int numSesion) {
        this.numSesion = numSesion;
    }  

    @Override
    public String toString() {
        return "Reporte #" + idReporte + " - " + estatus;
    }
}
