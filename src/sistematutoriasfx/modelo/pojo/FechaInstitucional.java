/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistematutoriasfx.modelo.pojo;

import java.time.LocalDate;

/**
 *
 * @author JOANA XCARET
 */
public class FechaInstitucional {
    
    private int idFechaInstitucional;
    private int idPeriodo;
    private int numSesion;
    private LocalDate fechaSesion;
    private String descripcion;
    private String nombre;

    public int getIdFechaInstitucional() {
        return idFechaInstitucional;
    }

    public void setIdFechaInstitucional(int idFechaInstitucional) {
        this.idFechaInstitucional = idFechaInstitucional;
    }

    public int getIdPeriodo() {
        return idPeriodo;
    }

    public void setIdPeriodo(int idPeriodo) {
        this.idPeriodo = idPeriodo;
    }

    public int getNumSesion() {
        return numSesion;
    }

    public void setNumSesion(int numSesion) {
        this.numSesion = numSesion;
    }

    public LocalDate getFechaSesion() {
        return fechaSesion;
    }

    public void setFechaSesion(LocalDate fechaSesion) {
        this.fechaSesion = fechaSesion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
