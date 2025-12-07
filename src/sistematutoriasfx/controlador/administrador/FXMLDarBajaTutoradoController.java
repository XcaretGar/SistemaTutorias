/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package sistematutoriasfx.controlador.administrador;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import sistematutoriasfx.interfaces.IObservador;
import sistematutoriasfx.modelo.dao.EstudianteDAO;
import sistematutoriasfx.modelo.pojo.Estudiante;
import utilidad.Utilidades;

/**
 * FXML Controller class
 *
 * @author JOANA XCARET
 */
public class FXMLDarBajaTutoradoController implements Initializable {

    @FXML
    private Label lbTutorado;
    @FXML
    private Label lbMatricula;
    @FXML
    private Label lbPrograma;
    @FXML
    private TextArea taMotivoBaja;
    
    private Estudiante estudianteSeleccionado;
    private IObservador observador;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    public void inicializarDatos(IObservador observador, Estudiante estudiante) {
        this.observador = observador;
        this.estudianteSeleccionado = estudiante;
        lbTutorado.setText(estudiante.getNombreCompleto());
        lbMatricula.setText(estudiante.getMatricula());
        lbPrograma.setText(estudiante.getProgramaEducativo());
    }

    @FXML
    private void clicCancelar(ActionEvent event) {
        Stage stage = (Stage) lbTutorado.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void clicConfirmarBaja(ActionEvent event) {
        String motivo = taMotivoBaja.getText().trim();

        boolean exito = EstudianteDAO.darDeBaja(estudianteSeleccionado.getIdEstudiante(), motivo);

        if (exito) {
            Utilidades.mostrarAlertaSimple("Baja confirmada",
                "El tutorado fue dado de baja correctamente.", Alert.AlertType.INFORMATION);
            observador.notificarOperacionExitosa("Baja", estudianteSeleccionado.getNombreCompleto());
            Stage stage = (Stage) lbTutorado.getScene().getWindow();
            stage.close();
        } else {
            Utilidades.mostrarAlertaSimple("Error",
                "No se pudo dar de baja al tutorado.", Alert.AlertType.ERROR);
        }
    }
}
