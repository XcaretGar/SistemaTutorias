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
import sistematutoriasfx.modelo.dao.AcademicoDAO;
import sistematutoriasfx.modelo.pojo.Academico;
import sistematutoriasfx.modelo.pojo.Rol;
import utilidad.Utilidades;

/**
 * FXML Controller class
 *
 * @author JOANA XCARET
 */
public class FXMLDarBajaUsuarioController implements Initializable {

    @FXML
    private Label lbUsuario;
    @FXML
    private Label lbIdentificador;
    @FXML
    private Label lbTipo;
    @FXML
    private TextArea taMotivoBaja;

    private Academico academicoSeleccionado;
    private IObservador observador;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }   

    public void inicializarDatos(IObservador observador, Academico academico, Rol rol) {
        this.observador = observador;
        this.academicoSeleccionado = academico;
        lbUsuario.setText(academico.getNombreCompleto());
        lbIdentificador.setText(academico.getNoPersonal());
        lbTipo.setText(rol.getNombre());
    }


    @FXML
    private void clicCancelar(ActionEvent event) {
        Stage stage = (Stage) lbUsuario.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void clicConfirmarBaja(ActionEvent event) {
        String motivo = taMotivoBaja.getText().trim();

        boolean exito = AcademicoDAO.darDeBaja(academicoSeleccionado.getIdAcademico(), motivo);

        if (exito) {
            Utilidades.mostrarAlertaSimple("Baja confirmada",
                "Usuario dado de baja exitosamente", Alert.AlertType.INFORMATION);
            observador.notificarOperacionExitosa("Baja", academicoSeleccionado.getNombreCompleto());
            Stage stage = (Stage) lbUsuario.getScene().getWindow();
            stage.close();
        } else {
            Utilidades.mostrarAlertaSimple("Error",
                "No se pudo dar de baja al tutorado.", Alert.AlertType.ERROR);
        }
    }
}
   
