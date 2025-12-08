/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package sistematutoriasfx.controlador.coordinador;

import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sistematutoriasfx.dominio.CatalogoImp;
import sistematutoriasfx.interfaces.IObservador;
import sistematutoriasfx.modelo.dao.FechasTutoriaDAO;
import sistematutoriasfx.modelo.pojo.FechasTutoria;
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
    
    private IObservador observador;
    private FechasTutoria fechaSeleccionada;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarPeriodosEscolares();
    }    
    
    public void inicializarDatos(IObservador observador, FechasTutoria fecha) {
        this.observador = observador;
        this.fechaSeleccionada = fecha;
        if (fecha != null) {
            tfNoSesion.setText(String.valueOf(fecha.getNumSesion()));
            dpFecha.setValue(fecha.getFechaSesion());
            tfDescripcion.setText(fecha.getDescripcion());
            cbPeriodo.setValue(new PeriodoEscolar(
                    fecha.getIdPeriodo(),
                    fecha.getNombre()
            ));
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
        PeriodoEscolar periodo = cbPeriodo.getSelectionModel().getSelectedItem();
        if (periodo == null || tfNoSesion.getText().isEmpty() || dpFecha.getValue() == null || tfDescripcion.getText().isEmpty()) {
            Utilidades.mostrarAlertaSimple("Datos incompletos", "Completa todos los campos", Alert.AlertType.WARNING);
            return;
        }

        int numeroSesion;
        try {
            numeroSesion = Integer.parseInt(tfNoSesion.getText());
        } catch (NumberFormatException e) {
            Utilidades.mostrarAlertaSimple("Sesión inválida", "El número de sesión debe ser numérico", Alert.AlertType.WARNING);
            return;
        }
        
        boolean traslape = FechasTutoriaDAO.existeTraslape(
                periodo.getIdPeriodo(),
                dpFecha.getValue(),
                fechaSeleccionada == null ? null : fechaSeleccionada.getIdFechaTutoria()
        );
        if (traslape) {
            Utilidades.mostrarAlertaSimple("Traslape detectado",
                    "La fecha se traslapa con otra tutoría programada. Corrige la fecha.",
                    Alert.AlertType.WARNING);
            return;
        }

        boolean exito;
        if (fechaSeleccionada == null) {
            FechasTutoria nueva = new FechasTutoria();
            nueva.setIdPeriodo(periodo.getIdPeriodo());
            nueva.setNumSesion(numeroSesion);
            nueva.setFechaSesion(dpFecha.getValue());
            nueva.setDescripcion(tfDescripcion.getText());
            exito = FechasTutoriaDAO.registrarFecha(nueva);
        } else {
            fechaSeleccionada.setIdPeriodo(periodo.getIdPeriodo());
            fechaSeleccionada.setNumSesion(numeroSesion);
            fechaSeleccionada.setFechaSesion(dpFecha.getValue());
            fechaSeleccionada.setDescripcion(tfDescripcion.getText());
            exito = FechasTutoriaDAO.actualizarFecha(fechaSeleccionada);
        }

        if (exito) {
            Utilidades.mostrarAlertaSimple("Éxito", fechaSeleccionada == null ? "Fecha registrada" : "Fecha actualizada", Alert.AlertType.INFORMATION);
            cerrarVentana();
        } else {
            Utilidades.mostrarAlertaSimple("Error", "No se pudo guardar la fecha", Alert.AlertType.ERROR);
        }
    }
    
    private void cargarPeriodosEscolares() {
        HashMap<String, Object> respuesta = CatalogoImp.obtenerPeriodos();
        if (!(boolean) respuesta.get("error")) {
           List<PeriodoEscolar> periodosEscolaresBD = (List<PeriodoEscolar>) respuesta.get("periodos");
            cbPeriodo.setItems(FXCollections.observableArrayList(periodosEscolaresBD));
        } else {
            Utilidades.mostrarAlertaSimple("Error al cargar los periodos escolares",
                    respuesta.get("mensaje").toString(), Alert.AlertType.ERROR);
        }    
    }
    
    private void cerrarVentana() {
        ((Stage) tfDescripcion.getScene().getWindow()).close();
    }
}
