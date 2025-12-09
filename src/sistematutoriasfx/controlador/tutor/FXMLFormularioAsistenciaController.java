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
import javafx.scene.control.Button;
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

    @FXML private Label lbFechaSesion;
    @FXML private TableView<Estudiante> tvAlumnos;
    @FXML private TableColumn colMatricula;
    @FXML private TableColumn colNombre;
    @FXML private TableColumn colAsistencia;
    @FXML private TableColumn colRiesgo;
    @FXML private Button btnGuardar;
    @FXML private Button btnCancelar;

    private int idAcademico;
    private FechasTutoria fechaSesion;
    private int idSesionReal;
    private boolean modoConsulta = false;

    public void inicializarAsistencia(int idAcademico, FechasTutoria fecha, boolean soloConsulta) {
        this.idAcademico = idAcademico;
        this.fechaSesion = fecha;
        this.modoConsulta = soloConsulta;
        
        if(lbFechaSesion != null && fecha != null) {
            lbFechaSesion.setText("Fecha: " + fecha.getFechaSesion());
        }
        
        this.idSesionReal = buscarIdSesion(fecha.getIdFechaTutoria());
        
        cargarAlumnos();
        
        if(idSesionReal > 0) {
            cargarAsistenciaPrevia();
        }
        
        if(modoConsulta) {
            deshabilitarEdicion();
        }
    }
    
    private int buscarIdSesion(int idFechaTutoria) {
        java.util.ArrayList<SesionTutoria> sesiones = 
                SesionTutoriaDAO.obtenerSesionesPorTutor(idAcademico);
        
        for (SesionTutoria s : sesiones) {
            if (s.getIdFechaTutoria() == idFechaTutoria) {
                return s.getIdSesion();
            }
        }
        return 0; 
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarColumnas();
    }
    
    private void configurarColumnas() {
        colMatricula.setCellValueFactory(new PropertyValueFactory("matricula"));
        colNombre.setCellValueFactory(new PropertyValueFactory("nombreEstudiante")); 
        colAsistencia.setCellValueFactory(new PropertyValueFactory("cbAsistencia"));
        colRiesgo.setCellValueFactory(new PropertyValueFactory("cbRiesgo"));

        colAsistencia.setCellFactory(col -> new TableCell<Estudiante, CheckBox>() {
            @Override
            protected void updateItem(CheckBox item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    setGraphic(item);
                    setText(null);
                }
            }
        });

        colRiesgo.setCellFactory(col -> new TableCell<Estudiante, CheckBox>() {
            @Override
            protected void updateItem(CheckBox item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    setGraphic(item);
                    setText(null);
                }
            }
        });
    }
    
    private void cargarAlumnos() {
        if(idSesionReal > 0) {
            ObservableList<Estudiante> lista = FXCollections.observableArrayList(
                EstudianteDAO.obtenerEstudiantesPorTutor(idAcademico, fechaSesion.getIdPeriodo())
            );
            
            if(tvAlumnos != null) {
                tvAlumnos.setItems(lista);
            } else {
                System.err.println("ERROR: tvAlumnos es null");
            }
        } else {
            Utilidades.mostrarAlertaSimple("Horario No Encontrado", 
                "No has registrado un horario para esta fecha oficial.\n" +
                "Primero ve a 'Registrar Horario' y configura esta sesión.", 
                Alert.AlertType.WARNING);
        }
    }

    private void cargarAsistenciaPrevia() {
        java.util.ArrayList<ListaAsistencia> asistenciaPrevia = 
            ListaAsistenciaDAO.obtenerAsistenciaPorSesion(idSesionReal);
        
        if(asistenciaPrevia == null || asistenciaPrevia.isEmpty()) {
            return; 
        }
        
        // Recorrer la tabla y marcar los CheckBox
        for (Estudiante est : tvAlumnos.getItems()) {
            for (ListaAsistencia asist : asistenciaPrevia) {
                if (est.getIdEstudiante() == asist.getIdEstudiante()) {
                    est.getCbAsistencia().setSelected(asist.isAsistio());
                    est.getCbRiesgo().setSelected(asist.isRiesgoDetectado());
                    break;
                }
            }
        }
    }

    private void deshabilitarEdicion() {
        // Deshabilitar CheckBox
        for (Estudiante est : tvAlumnos.getItems()) {
            if(est.getCbAsistencia() != null) {
                est.getCbAsistencia().setDisable(true);
            }
            if(est.getCbRiesgo() != null) {
                est.getCbRiesgo().setDisable(true);
            }
        }
        // Ocultar botones
        if(btnGuardar != null) btnGuardar.setVisible(false);
        if(btnCancelar != null) btnCancelar.setVisible(false);
    }

    @FXML
    private void clicGuardar(ActionEvent event) {
        if(tvAlumnos.getItems().isEmpty()) {
            Utilidades.mostrarAlertaSimple("Tabla vacía", 
                "No hay estudiantes asignados a este periodo.\n" +
                "Verifica que hayas registrado asignaciones en el sistema.", 
                Alert.AlertType.WARNING);
            return;
        }
        if(idSesionReal <= 0) {
            Utilidades.mostrarAlertaSimple("Error de sesión", 
                "No se encontró una sesión válida para esta fecha.", 
                Alert.AlertType.ERROR);
            return;
        }
        
        ListaAsistenciaDAO.eliminarAsistenciaPorSesion(idSesionReal);
        
        boolean error = false;
        for (Estudiante est : tvAlumnos.getItems()) {
            boolean asistio = est.getCbAsistencia().isSelected();
            boolean riesgo = est.getCbRiesgo().isSelected();
            
            ListaAsistencia lista = new ListaAsistencia();
            lista.setIdSesion(idSesionReal);
            lista.setIdEstudiante(est.getIdEstudiante());
            lista.setAsistio(asistio);
            lista.setRiesgoDetectado(riesgo);
            
            boolean exito = ListaAsistenciaDAO.registrarAsistencia(lista);
            if(!exito) error = true;
        }
        
        if (!error) {
            Utilidades.mostrarAlertaSimple("Éxito", "Asistencia guardada.", Alert.AlertType.INFORMATION);
            cerrarVentana();
        } else {
            Utilidades.mostrarAlertaSimple("Advertencia", "Error al guardar algunos registros.", Alert.AlertType.WARNING);
        }
    }

    @FXML 
    private void clicCancelar(ActionEvent event) { cerrarVentana(); }
    
    @FXML 
    private void clicRegresar(ActionEvent event) { cerrarVentana(); }
    
    private void cerrarVentana() {
        if (tvAlumnos != null && tvAlumnos.getScene() != null) {
            Stage stage = (Stage) tvAlumnos.getScene().getWindow();
            if (stage != null) {
                stage.close();
            }
        }
    }
}