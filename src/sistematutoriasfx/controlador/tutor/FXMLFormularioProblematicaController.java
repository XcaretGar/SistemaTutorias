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
import sistematutoriasfx.modelo.pojo.Estudiante;
import sistematutoriasfx.modelo.pojo.Problematica;
import utilidad.Utilidades;

public class FXMLFormularioProblematicaController implements Initializable {

    @FXML private TextField tfTituloProblematica;
    @FXML private TextArea taDescripcion;
    @FXML private ComboBox<String> cbEstatus;
    @FXML private TextField tfEstudianteSeleccionado;
    @FXML private Button btnBuscarAlumno;

    private int idAcademico;
    private boolean esEdicion = false;
    private Problematica problemaEdicion;
    private Estudiante estudianteSeleccionado; // Aquí guardamos al alumno elegido

    // MÉTODO PARA RECIBIR EL ALUMNO DESDE LA VENTANA POPUP
    public void setEstudianteSeleccionado(Estudiante estudiante) {
        this.estudianteSeleccionado = estudiante;
        if (estudiante != null) {
            tfEstudianteSeleccionado.setText(estudiante.getNombreCompleto());
        }
    }

    public void inicializarTutor(int idAcademico) {
        this.idAcademico = idAcademico;
        this.esEdicion = false;
    }
    
    public void inicializarEdicion(Problematica problema) {
        this.esEdicion = true;
        this.problemaEdicion = problema;
        
        tfTituloProblematica.setText(problema.getTitulo());
        taDescripcion.setText(problema.getDescripcion());
        cbEstatus.setValue(problema.getEstatus());
        tfEstudianteSeleccionado.setText(problema.getNombreEstudiante());
        
        // Bloqueamos la búsqueda porque en edición NO se cambia de alumno
        btnBuscarAlumno.setDisable(true); 
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cbEstatus.setItems(FXCollections.observableArrayList("Registrada", "EnSeguimiento", "Resuelta"));
        cbEstatus.getSelectionModel().selectFirst();
    }

    @FXML
    private void clicBuscarAlumno(ActionEvent event) {
        try {
            // Cargar la ventana de selección
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sistematutoriasfx/vista/tutor/FXMLSeleccionarEstudianteProblematica.fxml")); // Verifica nombre
            Parent root = loader.load();
            
            FXMLSeleccionarEstudianteProblematicaController controlador = loader.getController();
            // Le pasamos 'this' (este controlador) para que sepa a quién regresarle el dato
            controlador.inicializar(this.idAcademico, this);
            
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Seleccionar Estudiante");
            stage.setScene(scene);
            stage.showAndWait();
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void clicGuardar(ActionEvent event) {
        if(tfTituloProblematica.getText().isEmpty()) {
            Utilidades.mostrarAlertaSimple("Faltan datos", "Escribe un título.", Alert.AlertType.WARNING);
            return;
        }
        
        // Si es nuevo y no eligió alumno
        if(!esEdicion && estudianteSeleccionado == null) {
            Utilidades.mostrarAlertaSimple("Faltan datos", "Debes buscar y seleccionar un alumno.", Alert.AlertType.WARNING);
            return;
        }
        
        Problematica problema = new Problematica();
        problema.setTitulo(tfTituloProblematica.getText());
        problema.setDescripcion(taDescripcion.getText());
        problema.setEstatus(cbEstatus.getValue());
        
        // Aquí necesitamos un idReporte. Puedes usar un reporte activo o dejarlo pendiente.
        // Por simplicidad usaremos 1, o puedes buscar el reporte actual del periodo.
        problema.setIdReporte(1); 

        boolean exito;
        if(esEdicion) {
            problema.setIdProblematica(problemaEdicion.getIdProblematica());
            exito = ProblematicaDAO.actualizarProblematica(problema);
        } else {
            problema.setIdEstudiante(estudianteSeleccionado.getIdEstudiante());
            exito = ProblematicaDAO.registrarProblematica(problema);
        }
        
        if(exito) {
            Utilidades.mostrarAlertaSimple("Éxito", "Guardado correctamente.", Alert.AlertType.INFORMATION);
            cerrarVentana();
        } else {
            Utilidades.mostrarAlertaSimple("Error", "No se pudo guardar.", Alert.AlertType.ERROR);
        }
    }

    @FXML private void clicCancelar(ActionEvent event) { cerrarVentana(); }
    @FXML private void clicRegresar(ActionEvent event) { cerrarVentana(); }
    
    private void cerrarVentana() {
        ((Stage) tfTituloProblematica.getScene().getWindow()).close();
    }
}