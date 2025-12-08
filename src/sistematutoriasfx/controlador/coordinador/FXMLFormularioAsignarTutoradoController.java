/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package sistematutoriasfx.controlador.coordinador;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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
        colCarrera.setCellValueFactory(new PropertyValueFactory("carrera"));
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

        boolean exito;
        if (!estudianteSeleccionado.isAsignado()) {
            exito = AsignacionTutorDAO.asignarTutor(
                estudianteSeleccionado.getIdEstudiante(),
                tutorSeleccionado.getIdAcademico());
        } else {
            exito = AsignacionTutorDAO.actualizarAsignacion(
                estudianteSeleccionado.getIdEstudiante(),
                tutorSeleccionado.getIdAcademico());
        }

        if (exito) {
            String mensaje = (!estudianteSeleccionado.isAsignado())
                ? "Tutor asignado exitosamente"
                : "Tutor actualizado correctamente";
            Utilidades.mostrarAlertaSimple("Operación exitosa", mensaje, Alert.AlertType.INFORMATION);

            if (observador != null) {
                observador.notificarOperacionExitosa("Asignación", estudianteSeleccionado.getNombreCompleto());
            }
            cerrarVentana();
        } else {
            Utilidades.mostrarAlertaSimple("Error",
                "No se pudo completar la operación", Alert.AlertType.ERROR);
        }
    }
    
    private void cerrarVentana() {
        ((Stage) lbNombreCompleto.getScene().getWindow()).close();
    }
}
