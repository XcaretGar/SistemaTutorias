/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package sistematutoriasfx.controlador.coordinador;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sistematutoriasfx.modelo.dao.FechaInstitucionalDAO;
import sistematutoriasfx.modelo.dao.PeriodoEscolarDAO;
import sistematutoriasfx.modelo.pojo.FechaInstitucional;
import sistematutoriasfx.modelo.pojo.PeriodoEscolar;
import utilidad.Utilidades;

/**
 * FXML Controller class
 *
 * @author JOANA XCARET
 */
public class FXMLEstablecerNuevaFechaController implements Initializable {

    @FXML
    private DatePicker dpFecha;
    @FXML
    private ComboBox<PeriodoEscolar> cbPeriodo;
    @FXML
    private TextField tfNoSesion;
    @FXML
    private TextField tfDescripcion;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cbPeriodo.getItems().setAll(PeriodoEscolarDAO.obtenerPeriodos());
    }    
    
    public void inicializarDatos(FechaInstitucional fecha) {
        if (fecha != null) {
        cbPeriodo.getSelectionModel().select(fecha.getIdPeriodo());
        tfNoSesion.setText(String.valueOf(fecha.getNumSesion()));
        dpFecha.setValue(fecha.getFechaSesion());
        tfDescripcion.setText(fecha.getDescripcion());
        } else {
            cbPeriodo.getSelectionModel().clearSelection();
            tfNoSesion.clear();
            dpFecha.setValue(null);
            tfDescripcion.clear();
        }
    }

    @FXML
    private void clicCancelar(ActionEvent event) {
        cerrarVentana();
    }

    @FXML
    private void clicGuardarFecha(ActionEvent event) {
        PeriodoEscolar periodo = (PeriodoEscolar) cbPeriodo.getSelectionModel().getSelectedItem();
        String sesionTexto = tfNoSesion.getText();
        LocalDate fecha = dpFecha.getValue();
        String descripcion = tfDescripcion.getText();

        if (periodo == null || sesionTexto.isEmpty() || fecha == null || descripcion.isEmpty()) {
            Utilidades.mostrarAlertaSimple("Datos incompletos", 
                    "Completa todos los campos", Alert.AlertType.WARNING);
            return;
        }

        int numeroSesion;
        try {
            numeroSesion = Integer.parseInt(sesionTexto);
        } catch (NumberFormatException e) {
            Utilidades.mostrarAlertaSimple("Sesión inválida", 
                    "El número de sesión debe ser numérico", Alert.AlertType.WARNING);
            return;
        }

        FechaInstitucional nuevaFecha = new FechaInstitucional();
        nuevaFecha.setIdPeriodo(periodo.getIdPeriodo());
        nuevaFecha.setNumSesion(numeroSesion);
        nuevaFecha.setFechaSesion(fecha);
        nuevaFecha.setDescripcion(descripcion);

        boolean exito = FechaInstitucionalDAO.registrarFecha(nuevaFecha);
        if (exito) {
            Utilidades.mostrarAlertaSimple("Registro exitoso", 
                    "Fecha institucional registrada exitosamente", Alert.AlertType.INFORMATION);
            cerrarVentana();
        } else {
            Utilidades.mostrarAlertaSimple("Error", 
                    "No se pudo registrar la fecha", Alert.AlertType.ERROR);
        }
    }
    
    private void cerrarVentana() {
        ((Stage) tfDescripcion.getScene().getWindow()).close();
    }
}
