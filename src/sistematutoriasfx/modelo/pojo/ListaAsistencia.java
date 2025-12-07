/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistematutoriasfx.modelo.pojo;

/**
 *
 * @author Ana Georgina
 */
public class ListaAsistencia {
    private int idLista;
    private int idSesion;
    private int idEstudiante;
    private boolean asistio;
    private boolean riesgoDetectado;

    public ListaAsistencia() {
    }

    public ListaAsistencia(int idSesion, int idEstudiante, boolean asistio, boolean riesgoDetectado) {
        this.idSesion = idSesion;
        this.idEstudiante = idEstudiante;
        this.asistio = asistio;
        this.riesgoDetectado = riesgoDetectado;
    }

    public int getIdLista() {
        return idLista;
    }

    public void setIdLista(int idLista) {
        this.idLista = idLista;
    }

    public int getIdSesion() {
        return idSesion;
    }

    public void setIdSesion(int idSesion) {
        this.idSesion = idSesion;
    }

    public int getIdEstudiante() {
        return idEstudiante;
    }

    public void setIdEstudiante(int idEstudiante) {
        this.idEstudiante = idEstudiante;
    }

    public boolean isAsistio() {
        return asistio;
    }

    public void setAsistio(boolean asistio) {
        this.asistio = asistio;
    }

    public boolean isRiesgoDetectado() {
        return riesgoDetectado;
    }

    public void setRiesgoDetectado(boolean riesgoDetectado) {
        this.riesgoDetectado = riesgoDetectado;
    }
}
