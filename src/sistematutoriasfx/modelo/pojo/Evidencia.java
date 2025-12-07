/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistematutoriasfx.modelo.pojo;

/**
 *
 * @author Ana Georgina
 */

public class Evidencia {
    private int idEvidencia;
    private int idReporte; // FK hacia ReporteTutoria
    private String nombreArchivo; // Ej: "Lista_Asistencia_Marzo.pdf"
    private String rutaArchivo;   // Ej: "C:/SistemaTutorias/Evidencias/Lista_Asistencia_Marzo.pdf"
    private String fechaSubida;   // Fecha en que se subió

    public Evidencia() {
    }

    public Evidencia(int idReporte, String nombreArchivo, String rutaArchivo) {
        this.idReporte = idReporte;
        this.nombreArchivo = nombreArchivo;
        this.rutaArchivo = rutaArchivo;
    }

    public int getIdEvidencia() {
        return idEvidencia;
    }

    public void setIdEvidencia(int idEvidencia) {
        this.idEvidencia = idEvidencia;
    }

    public int getIdReporte() {
        return idReporte;
    }

    public void setIdReporte(int idReporte) {
        this.idReporte = idReporte;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public String getRutaArchivo() {
        return rutaArchivo;
    }

    public void setRutaArchivo(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }

    public String getFechaSubida() {
        return fechaSubida;
    }

    public void setFechaSubida(String fechaSubida) {
        this.fechaSubida = fechaSubida;
    }
    
    
    
    @Override
    public String toString() {
        return nombreArchivo;
    }
}