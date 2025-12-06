/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistematutoriasfx.modelo.pojo;

/**
 *
 * @author JOANA XCARET
 */
public class ProgramaEducativo {
    private int idPrograma;
    private String nombre;
    
    public ProgramaEducativo() {
    }

    public ProgramaEducativo(int idPrograma, String nombre) {
        this.idPrograma = idPrograma;
        this.nombre = nombre;
    }
    
    public int getIdPrograma() {
        return idPrograma;
    }

    public void setIdPrograma(int idPrograma) {
        this.idPrograma = idPrograma;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
