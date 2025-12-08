/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package sistematutoriasfx.controlador.coordinador;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import sistematutoriasfx.modelo.dao.AsignacionTutorDAO;
import sistematutoriasfx.modelo.pojo.Academico;
import sistematutoriasfx.modelo.pojo.Estudiante;
import utilidad.Utilidades;

/**
 * FXML Controller class
 *
 * @author JOANA XCARET
 */
public class FXMLActualizarTutorController implements Initializable {

    @FXML
    private Label lbTutorado;
    @FXML
    private Label lbTutorActual;
    @FXML
    private Label lbNuevoTutor;
    
    private Estudiante estudiante;
    private Academico nuevoTutor;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
     public void inicializarDatos(Estudiante estudiante, Academico nuevoTutor) {
        this.estudiante = estudiante;
        this.nuevoTutor = nuevoTutor;

        lbTutorado.setText(estudiante.getNombreCompleto());
        lbNuevoTutor.setText(nuevoTutor.getNombreCompleto());

        Academico tutorActual = AsignacionTutorDAO.obtenerTutorActual(estudiante.getIdEstudiante());
        if (tutorActual != null) {
            lbTutorActual.setText(tutorActual.getNombreCompleto());
        } else {
            lbTutorActual.setText("Sin tutor asignado");
        }
    }

    @FXML
    private void clicCancelar(ActionEvent event) {
        cerrarVentana();
    }

    @FXML
    private void clicConfirmarCambio(ActionEvent event) {
        boolean exito = AsignacionTutorDAO.actualizarAsignacion(
            estudiante.getIdEstudiante(), nuevoTutor.getIdAcademico());

        if (exito) {
            Utilidades.mostrarAlertaSimple("Asignación actualizada",
                "El tutor ha sido cambiado correctamente", Alert.AlertType.INFORMATION);
            cerrarVentana();
        } else {
            Utilidades.mostrarAlertaSimple("Error",
                "No se pudo actualizar la asignación", Alert.AlertType.ERROR);
        }
    }
    
    private void cerrarVentana() {
        ((Stage) lbTutorado.getScene().getWindow()).close();
    }
}
