/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistematutoriasfx.modelo.pojo;

/**
 *
 * @author Ana Georgina
 */

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class FechasTutoria {
    private int idFechaTutoria;
    private int idPeriodo;
    private int numSesion;
    private LocalDate fechaSesion;
    private String descripcion;
    private String nombre;

    
    public FechasTutoria() {
    }
    
    public FechasTutoria(int idFechaTutoria, int idPeriodo, int numSesion, LocalDate fechaSesion, String descripcion, String nombre) {
        this.idFechaTutoria = idFechaTutoria;
        this.idPeriodo = idPeriodo;
        this.numSesion = numSesion;
        this.fechaSesion = fechaSesion;
        this.descripcion = descripcion;
        this.nombre = nombre;
    }
    
    // ... getters y setters (sin nombre) ...
    
    public int getIdFechaTutoria() {
        return idFechaTutoria;
    }
    
    public void setIdFechaTutoria(int idFechaTutoria) {
        this.idFechaTutoria = idFechaTutoria;
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
    
    @Override
    public String toString() {
        if(fechaSesion == null) {
            return "Sesión " + numSesion + " - Sin fecha";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return "Sesión " + numSesion + " - " + fechaSesion.format(formatter);
    }
}

