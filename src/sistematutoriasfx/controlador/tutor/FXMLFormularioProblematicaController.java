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

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sistematutoriasfx.modelo.dao.ProblematicaDAO;
import sistematutoriasfx.modelo.dao.ReporteTutoriaDAO;
import sistematutoriasfx.modelo.pojo.Estudiante;
import sistematutoriasfx.modelo.pojo.Problematica;
import sistematutoriasfx.modelo.pojo.Usuario;
import utilidad.Utilidades;

public class FXMLFormularioProblematicaController implements Initializable {

    @FXML private TextField tfTituloProblematica;
    @FXML private TextArea taDescripcion;
    @FXML private ComboBox<String> cbEstatus;
    @FXML private TextField tfEstudianteSeleccionado;
    @FXML private Button btnBuscarAlumno;
    @FXML private Label lbFechaMostrada;
    
    @FXML private Button btnGuardar;
    @FXML private Button btnCancelar;

    private int idAcademico;
    private boolean esEdicion = false;
    private Problematica problemaEdicion;
    private Estudiante estudianteSeleccionado;

    
    public void inicializarTutor(int idAcademico){
        this.idAcademico = idAcademico;
    }
    
    public void inicializarEdicion(Problematica problema){
        this.esEdicion = true;
        this.problemaEdicion = problema;
        
        tfTituloProblematica.setText(problema.getTitulo());
        taDescripcion.setText(problema.getDescripcion());
        cbEstatus.setValue(problema.getEstatus());
        tfEstudianteSeleccionado.setText(problema.getNombreEstudiante());
        
        btnBuscarAlumno.setDisable(true);
    }
    
    public void setEstudianteSeleccionado(Estudiante estudiante){
        this.estudianteSeleccionado = estudiante;
        if(estudiante != null){
            tfEstudianteSeleccionado.setText(estudiante.getNombreCompleto());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cbEstatus.setItems(FXCollections.observableArrayList("Registrada", "EnSeguimiento", "Resuelta"));
        cbEstatus.getSelectionModel().selectFirst();
    }

    @FXML
    private void clicBuscarAlumno(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sistematutoriasfx/vista/tutor/FXMLSeleccionarEstudianteProblematica.fxml"));
            Parent root = loader.load();
            
            FXMLSeleccionarEstudianteProblematicaController controlador = loader.getController();
            controlador.inicializar(idAcademico, this);
            
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.setTitle("Seleccionar Estudiante");
            stage.showAndWait();
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void clicGuardar(ActionEvent event) {
        if (tfTituloProblematica.getText().isEmpty() || taDescripcion.getText().isEmpty()) {
            Utilidades.mostrarAlertaSimple("Campos vacíos", 
                "Por favor llena el título y la descripción de la problemática.", 
                Alert.AlertType.WARNING);
            return;
        }

        if (!esEdicion && estudianteSeleccionado == null) {
            Utilidades.mostrarAlertaSimple("Falta estudiante", 
                "Debes buscar y seleccionar un estudiante para registrar la problemática.", 
                Alert.AlertType.WARNING);
            return;
        }
        
        // A. Definimos el periodo actual 
        int idPeriodoActual = 2; 
        
 
        int idReporteEncontrado = ReporteTutoriaDAO.obtenerIdReporteActual(idAcademico, idPeriodoActual);
        
        if (idReporteEncontrado <= 0) {
            Utilidades.mostrarAlertaSimple("Reporte no encontrado", 
                "No tienes un Reporte de Tutoría asignado para el periodo actual.\n" +
                "Contacta a tu coordinador para que genere tu reporte antes de registrar problemáticas.", 
                Alert.AlertType.ERROR);
            return; 
        }

        Problematica problema = new Problematica();
        problema.setTitulo(tfTituloProblematica.getText());
        problema.setDescripcion(taDescripcion.getText());
        problema.setEstatus(cbEstatus.getValue());
        
        problema.setIdReporte(idReporteEncontrado); 

        boolean resultado;

        if (esEdicion) {
            problema.setIdProblematica(problemaEdicion.getIdProblematica());
            resultado = ProblematicaDAO.actualizarProblematica(problema);
        } else {
            problema.setIdEstudiante(estudianteSeleccionado.getIdEstudiante());
            resultado = ProblematicaDAO.registrarProblematica(problema);
        }

        if (resultado) {
            Utilidades.mostrarAlertaSimple("Éxito", 
                "La problemática se guardó correctamente.", 
                Alert.AlertType.INFORMATION);
            cerrarVentana();
        } else {
            Utilidades.mostrarAlertaSimple("Error", 
                "Ocurrió un error al guardar la información en la base de datos.", 
                Alert.AlertType.ERROR);
        }
    }

    @FXML private void clicCancelar(ActionEvent event) { cerrarVentana(); }
    @FXML private void clicRegresar(ActionEvent event) { cerrarVentana(); }
    
    public void configurarModoConsulta() {
        tfTituloProblematica.setEditable(false);
        taDescripcion.setEditable(false);
        cbEstatus.setDisable(true);
        
        btnGuardar.setVisible(false);
        btnCancelar.setVisible(false);
        btnBuscarAlumno.setVisible(false);
    }

    private void cerrarVentana() {
        ((Stage) tfTituloProblematica.getScene().getWindow()).close();
    }
}