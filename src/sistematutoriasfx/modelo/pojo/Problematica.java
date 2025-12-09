/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistematutoriasfx.modelo.pojo;

/**
 *
 * @author Ana Georgina
 */
public class Problematica {
    private int idProblematica;
    private int idReporte;     
    private int idEstudiante;  
    private String titulo;
    private String descripcion;
    private String estatus;

    // Atributos auxiliares (NO están en la tabla problematica, pero sirven para la interfaz)
    private String nombreEstudiante;
    private String matricula;
    private String programaEducativo;

    public Problematica() {
    }

    public int getIdProblematica() { return idProblematica; }
    public void setIdProblematica(int idProblematica) { this.idProblematica = idProblematica; }

    public int getIdReporte() { return idReporte; }
    public void setIdReporte(int idReporte) { this.idReporte = idReporte; }

    public int getIdEstudiante() { return idEstudiante; }
    public void setIdEstudiante(int idEstudiante) { this.idEstudiante = idEstudiante; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getEstatus() { return estatus; }
    public void setEstatus(String estatus) { this.estatus = estatus; }

    public String getNombreEstudiante() { return nombreEstudiante; }
    public void setNombreEstudiante(String nombreEstudiante) { this.nombreEstudiante = nombreEstudiante; }

    public String getMatricula() { return matricula; }
    public void setMatricula(String matricula) { this.matricula = matricula; }

    public String getProgramaEducativo() {
        return programaEducativo;
    }

    public void setProgramaEducativo(String programaEducativo) {
        this.programaEducativo = programaEducativo;
    }
    
    

    @Override
    public String toString() {
        return titulo;
    }
}