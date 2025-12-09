/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package sistematutoriasfx.controlador.coordinador;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sistematutoriasfx.interfaces.IObservador;
import sistematutoriasfx.modelo.dao.AcademicoDAO;
import sistematutoriasfx.modelo.dao.AsignacionTutorDAO;
import sistematutoriasfx.modelo.pojo.Academico;
import sistematutoriasfx.modelo.pojo.Estudiante;
import utilidad.Utilidades;

/**
 * FXML Controller class
 *
 * @author JOANA XCARET
 */
public class FXMLFormularioAsignarTutoradoController implements Initializable {

    @FXML
    private TableView<Academico> tvTutores;
    @FXML
    private TableColumn colNombre;
    @FXML
    private TableColumn colApPaterno;
    @FXML
    private TableColumn colApMaterno;
    @FXML
    private TableColumn colTipoContrato;
    @FXML
    private TableColumn colCarrera;
    @FXML
    private TableColumn colCargaAcademica;
    @FXML
    private Label lbProgramaEducativo;
    @FXML
    private Label lbMatricula;
    @FXML
    private Label lbNombreCompleto;
    
    private IObservador observador;
    private Estudiante estudianteSeleccionado;
    private ObservableList<Academico> tutores;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
        cargarTutores();
    }    
    
    public void inicializarDatos(IObservador observador, Estudiante estudiante) {
        this.observador = observador;
        this.estudianteSeleccionado = estudiante;
        lbNombreCompleto.setText(estudiante.getNombreCompleto());
        lbMatricula.setText(estudiante.getMatricula());
        lbProgramaEducativo.setText(estudiante.getProgramaEducativo());
    }
    
     private void configurarTabla() {
        colNombre.setCellValueFactory(new PropertyValueFactory("nombre"));
        colApPaterno.setCellValueFactory(new PropertyValueFactory("apellidoPaterno"));
        colApMaterno.setCellValueFactory(new PropertyValueFactory("apellidoMaterno"));
        colTipoContrato.setCellValueFactory(new PropertyValueFactory("tipoContrato"));
        colCarrera.setCellValueFactory(new PropertyValueFactory("estudios"));
        colCargaAcademica.setCellValueFactory(new PropertyValueFactory("cargaAcademica"));
    }
    
    private void cargarTutores() {
        ArrayList<Academico> lista = AcademicoDAO.obtenerTodos();
        tutores = FXCollections.observableArrayList(lista);
        tvTutores.setItems(tutores);
    }

    @FXML
    private void clicSeleccionar(ActionEvent event) {
        Academico tutorSeleccionado = tvTutores.getSelectionModel().getSelectedItem();
        if (tutorSeleccionado == null) {
            Utilidades.mostrarAlertaSimple("Selecciona un tutor",
                "Debes seleccionar un tutor", Alert.AlertType.WARNING);
            return;
        }
        
        if (tutorSeleccionado.getCargaAcademica() >= 20) {
            Utilidades.mostrarAlertaSimple("Capacidad máxima alcanzada",
                "El tutor ha alcanzado su capacidad máxima de tutorados. Selecciona otro tutor",
                Alert.AlertType.WARNING);
            return;
        }

        if (!estudianteSeleccionado.isAsignado()) {
            boolean exito = AsignacionTutorDAO.asignarTutor(
                estudianteSeleccionado.getIdEstudiante(),
                tutorSeleccionado.getIdAcademico());
            if (exito) {
                Utilidades.mostrarAlertaSimple("Operación exitosa",
                    "Tutor asignado exitosamente", Alert.AlertType.INFORMATION);
                if (observador != null) {
                    observador.notificarOperacionExitosa("Asignación", estudianteSeleccionado.getNombreCompleto());
                }
            } else {
            Utilidades.mostrarAlertaSimple("Error",
                "No se pudo completar la asignación", Alert.AlertType.ERROR);
            }
        } else {
            Academico tutorActual = AsignacionTutorDAO.obtenerTutorActual(estudianteSeleccionado.getIdEstudiante());
            if (tutorActual != null && tutorActual.getIdAcademico() == tutorSeleccionado.getIdAcademico()) {
                Utilidades.mostrarAlertaSimple("Selección inválida",
                    "No puedes seleccionar al mismo tutor que ya está asignado",
                    Alert.AlertType.WARNING);
                return;
            }
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/sistematutoriasfx/vista/coordinador/FXMLActualizarTutor.fxml"));
                Parent vista = loader.load();
                FXMLActualizarTutorController controladorActualizar = loader.getController();
                controladorActualizar.inicializarDatos(estudianteSeleccionado, tutorSeleccionado);
                Scene scene = new Scene(vista);
                Stage stage = new Stage();
                stage.setTitle("Actualizar Tutor");
                stage.setScene(scene);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.showAndWait();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }  
    }
    
    @FXML
    private void clicRegresar(ActionEvent event) {
        Stage stage = (Stage) tvTutores.getScene().getWindow();
        stage.close();
    }
}
