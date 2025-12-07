/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package sistematutoriasfx.controlador.tutor;

/**
 * FXML Controller class
 *
 * @author Ana Georgina
 */

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sistematutoriasfx.modelo.dao.EstudianteDAO;
import sistematutoriasfx.modelo.dao.ListaAsistenciaDAO;
import sistematutoriasfx.modelo.dao.SesionTutoriaDAO;
import sistematutoriasfx.modelo.pojo.Estudiante;
import sistematutoriasfx.modelo.pojo.FechasTutoria;
import sistematutoriasfx.modelo.pojo.ListaAsistencia;
import sistematutoriasfx.modelo.pojo.SesionTutoria;
import utilidad.Utilidades;

public class FXMLFormularioAsistenciaController implements Initializable {

    @FXML private Label lbFechaSesion; // Si le pusiste fx:id en el FXML
    @FXML private TableView<Estudiante> tvAlumnos;
    @FXML private TableColumn colMatricula;
    @FXML private TableColumn colNombre;
    @FXML private TableColumn colAsistencia;
    @FXML private TableColumn colRiesgo;

    private int idAcademico;
    private FechasTutoria fechaSesion;
    private int idSesionReal; // El ID de la cita en 'sesiontutoria'

    // --- MÉTODO DE INICIALIZACIÓN (Lo llama la ventana anterior) ---
    public void inicializarAsistencia(int idAcademico, FechasTutoria fecha) {
        this.idAcademico = idAcademico;
        this.fechaSesion = fecha;
        
        // Ponemos la fecha en el título (si tienes el label en el FXML)
        if(lbFechaSesion != null) {
            lbFechaSesion.setText("Fecha: " + fecha.getFechaSesion());
        }
        
        // 1. Buscamos el ID real de la sesión (porque en el combo elegimos una Fecha Oficial)
        this.idSesionReal = buscarIdSesion(fecha.getIdFechaTutoria());
        
        // 2. Cargamos la lista de estudiantes
        cargarAlumnos();
    }
    
    private int buscarIdSesion(int idFechaTutoria) {
         java.util.ArrayList<SesionTutoria> sesiones = 
                SesionTutoriaDAO.obtenerSesionesPorTutor(idAcademico);
         
        for (SesionTutoria s : sesiones) {
            if (s.getIdFechaTutoria() == idFechaTutoria) {
                return s.getIdSesion();
            }
        }
        return 0; // 0 significa que no agendó horario para esta fecha
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarColumnas();
    }
    
    private void configurarColumnas() {
        colMatricula.setCellValueFactory(new PropertyValueFactory("matricula"));
        // Asegúrate que Estudiante tenga un getter getNombreCompleto o similar, o usa "nombre"
        colNombre.setCellValueFactory(new PropertyValueFactory("nombreCompleto")); 
        
        // --- MAGIA DE LOS CHECKBOXES ---
        // Enlazamos con los atributos booleanos del POJO Estudiante
        colAsistencia.setCellValueFactory(new PropertyValueFactory("cbAsistencia"));
        colRiesgo.setCellValueFactory(new PropertyValueFactory("cbRiesgo"));
    }
    
    private void cargarAlumnos() {
        if(idSesionReal > 0) {
            // Traemos los alumnos asignados a este tutor en este periodo
            ObservableList<Estudiante> lista = FXCollections.observableArrayList(
                EstudianteDAO.obtenerEstudiantesPorTutor(idAcademico, fechaSesion.getIdPeriodo())
            );
            tvAlumnos.setItems(lista);
        } else {
            Utilidades.mostrarAlertaSimple("Error de Lógica", 
                "No has registrado un horario para esta fecha oficial.\nVe a 'Horarios' primero.", 
                Alert.AlertType.ERROR);
            cerrarVentana();
        }
    }

    @FXML
    private void clicGuardar(ActionEvent event) {
        // 1. Borramos la asistencia anterior (si existía) para evitar duplicados
        ListaAsistenciaDAO.eliminarAsistenciaPorSesion(idSesionReal);
        
        boolean error = false;
        
        // 2. Recorremos la tabla visual fila por fila
        for (Estudiante est : tvAlumnos.getItems()) {
            // Obtenemos el estado de los checkboxes
            boolean asistio = est.getCbAsistencia().isSelected();
            boolean riesgo = est.getCbRiesgo().isSelected();
            
            // Creamos el objeto para guardar en BD
            ListaAsistencia lista = new ListaAsistencia();
            lista.setIdSesion(idSesionReal);
            lista.setIdEstudiante(est.getIdEstudiante());
            lista.setAsistio(asistio);
            lista.setRiesgoDetectado(riesgo);
            
            // Guardamos
            boolean exito = ListaAsistenciaDAO.registrarAsistencia(lista);
            if(!exito) error = true;
        }
        
        if (!error) {
            Utilidades.mostrarAlertaSimple("Éxito", "Lista de asistencia guardada correctamente.", Alert.AlertType.INFORMATION);
            cerrarVentana();
        } else {
            Utilidades.mostrarAlertaSimple("Advertencia", "Hubo problemas al guardar algunos alumnos.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void clicCancelar(ActionEvent event) {
        cerrarVentana();
    }
    
    @FXML
    private void clicRegresar(ActionEvent event) {
        cerrarVentana();
    }
    
    private void cerrarVentana() {
        if (tvAlumnos.getScene() != null) {
             ((Stage) tvAlumnos.getScene().getWindow()).close();
        }
    }
}