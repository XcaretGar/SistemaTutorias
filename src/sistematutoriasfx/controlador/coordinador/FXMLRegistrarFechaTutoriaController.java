/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package sistematutoriasfx.controlador.coordinador;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sistematutoriasfx.interfaces.IObservador;
import sistematutoriasfx.modelo.dao.FechaInstitucionalDAO;
import sistematutoriasfx.modelo.pojo.FechaInstitucional;
import utilidad.Utilidades;

/**
 * FXML Controller class
 *
 * @author JOANA XCARET
 */
public class FXMLRegistrarFechaTutoriaController implements Initializable, IObservador {

    @FXML
    private TableView<FechaInstitucional> tvFechasTutoria;
    @FXML
    private TableColumn colSesion;
    @FXML
    private TableColumn colPeriodo;
    @FXML
    private TableColumn colFecha;
    @FXML
    private TableColumn colDescripcion;
    
    private ObservableList<FechaInstitucional> fechas;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
        cargarFechas();
    }    
    
    private void configurarTabla() {
        colSesion.setCellValueFactory(new PropertyValueFactory("numSesion"));
        colPeriodo.setCellValueFactory(new PropertyValueFactory("idPeriodo"));
        colFecha.setCellValueFactory(new PropertyValueFactory("fechaSesion"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory("descripcion"));
    }

    private void cargarFechas() {
        ArrayList<FechaInstitucional> fechasBD = new ArrayList<>(FechaInstitucionalDAO.obtenerFechas());
        fechas = FXCollections.observableArrayList();
        fechas.addAll(fechasBD);
        tvFechasTutoria.setItems(fechas);
    }

    @FXML
    private void clicEstablecerNuevaFecha(ActionEvent event) {
        irFormulario(null);
    }

    @FXML
    private void clicActualizarFecha(ActionEvent event) {
        FechaInstitucional seleccionada = tvFechasTutoria.getSelectionModel().getSelectedItem();
        if (seleccionada != null) {
            irFormulario(seleccionada);
        } else {
            Utilidades.mostrarAlertaSimple("Aviso", 
                "Selecciona una fecha para actualizar", 
                Alert.AlertType.WARNING);
        }
    }
    
    private void irFormulario(FechaInstitucional fecha) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sistematutoriasfx/vista/coordinador/FXMLEstablecerNuevaFecha.fxml"));
            Parent vista = loader.load();
            FXMLEstablecerNuevaFechaController controlador = loader.getController();
            controlador.inicializarDatos(this, fecha); 
            Scene scene = new Scene(vista);
            Stage stage = new Stage();
            stage.setTitle("Establecer Fecha Tutoria");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            cargarFechas(); 
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    @Override
    public void notificarOperacionExitosa(String tipoOperacion, String nombre) {
        System.out.println("Operación: " + tipoOperacion);
        cargarFechas();
    }
}
