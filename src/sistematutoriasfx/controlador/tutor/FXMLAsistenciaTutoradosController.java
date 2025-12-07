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
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty; // <--- IMPORTANTE
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sistematutoriasfx.modelo.dao.AcademicoDAO;
import sistematutoriasfx.modelo.dao.FechasTutoriaDAO;
import sistematutoriasfx.modelo.dao.PeriodoEscolarDAO;
import sistematutoriasfx.modelo.dao.SesionTutoriaDAO;
import sistematutoriasfx.modelo.pojo.Academico;
import sistematutoriasfx.modelo.pojo.FechasTutoria;
import sistematutoriasfx.modelo.pojo.PeriodoEscolar;
import sistematutoriasfx.modelo.pojo.SesionTutoria;
import sistematutoriasfx.modelo.pojo.Usuario;
import utilidad.Utilidades;

public class FXMLAsistenciaTutoradosController implements Initializable {

    @FXML private ComboBox<PeriodoEscolar> cbPeriodo;
    @FXML private ComboBox<FechasTutoria> cbFechaSesion;
    
    @FXML private TableView<SesionTutoria> tvListaAsistencia;
    @FXML private TableColumn colFechaSesion;
    @FXML private TableColumn colSesion;
    @FXML private TableColumn colTotalAlumnos;
    @FXML private TableColumn colTotalAsistentes;
    @FXML private TableColumn colEnRiesgo;
    @FXML private TableColumn<SesionTutoria, String> colEstado; // Especificamos el tipo

    private Usuario usuarioSesion;
    private Academico academicoSesion;

    public void configurarEscena(Usuario usuario) {
        this.usuarioSesion = usuario;
        this.academicoSesion = AcademicoDAO.obtenerAcademicoPorIdUsuario(usuario.getIdUsuario());
        cargarTabla();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarColumnas();
        cargarPeriodos();
        configurarListeners();
    }
    
    private void configurarColumnas() {
        colFechaSesion.setCellValueFactory(new PropertyValueFactory("fecha"));
        colSesion.setCellValueFactory(new PropertyValueFactory("numSesion"));
        colTotalAsistentes.setCellValueFactory(new PropertyValueFactory("totalAsistentes"));
        colEnRiesgo.setCellValueFactory(new PropertyValueFactory("totalRiesgo"));
        
        // --- AQUÍ ESTÁ LA LÓGICA QUE PEDISTE ---
        colEstado.setCellValueFactory(cellData -> {
            SesionTutoria s = cellData.getValue();
            // Si hay asistentes o riesgo registrado, asumimos que ya se pasó lista
            boolean registrada = s.getTotalAsistentes() > 0 || s.getTotalRiesgo() > 0;
            return new SimpleStringProperty(registrada ? "Registrada" : "Pendiente");
        });
        
        // Total Alumnos (Placeholder, idealmente se calcula con otro DAO)
        colTotalAlumnos.setCellValueFactory(cellData -> new SimpleStringProperty("N/A")); 
    }
    
    private void cargarPeriodos() {
        cbPeriodo.setItems(FXCollections.observableArrayList(PeriodoEscolarDAO.obtenerPeriodos()));
    }
    
    private void configurarListeners() {
        cbPeriodo.valueProperty().addListener((obs, oldVal, newVal) -> {
            if(newVal != null) {
                cbFechaSesion.setItems(FXCollections.observableArrayList(
                    FechasTutoriaDAO.obtenerFechasPorPeriodo(newVal.getIdPeriodo())
                ));
            }
        });
    }
    
    private void cargarTabla() {
        if(academicoSesion != null) {
            // Usamos el método que ya incluye los conteos (totalAsistentes)
            ArrayList<SesionTutoria> lista = SesionTutoriaDAO.obtenerSesionesPorTutor(academicoSesion.getIdAcademico());
            tvListaAsistencia.setItems(FXCollections.observableArrayList(lista));
        }
    }

    // --- ACCIONES DE BOTONES ---

    @FXML
    private void clicPasarLista(ActionEvent event) {
        if(cbFechaSesion.getValue() != null) {
            abrirFormulario(cbFechaSesion.getValue());
        } else {
            // Si no seleccionó fecha en el combo, intentamos usar la selección de la tabla
            SesionTutoria seleccion = tvListaAsistencia.getSelectionModel().getSelectedItem();
            if(seleccion != null) {
                // Convertimos SesionTutoria a FechasTutoria temporal para abrir el form
                FechasTutoria fechaTemp = new FechasTutoria();
                fechaTemp.setIdFechaTutoria(seleccion.getIdFechaTutoria());
                fechaTemp.setFechaSesion(seleccion.getFecha());
                fechaTemp.setIdPeriodo(seleccion.getIdPeriodo());
                abrirFormulario(fechaTemp);
            } else {
                Utilidades.mostrarAlertaSimple("Selección requerida", "Selecciona una sesión de la tabla o del combo.", Alert.AlertType.WARNING);
            }
        }
    }

    @FXML
    private void clicModificar(ActionEvent event) {
        clicPasarLista(event); // Es la misma pantalla
    }
    
    @FXML
    private void clicConsultar(ActionEvent event) {
        clicPasarLista(event); // Es la misma pantalla
    }

    @FXML
    private void clicEliminar(ActionEvent event) {
        Utilidades.mostrarAlertaSimple("Pendiente", "Funcionalidad de eliminar lista en construcción.", Alert.AlertType.INFORMATION);
    }

    @FXML
    private void clicRegresar(ActionEvent event) {
        ((Stage) cbPeriodo.getScene().getWindow()).close();
    }
    
    private void abrirFormulario(FechasTutoria fechaSeleccionada) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sistematutoriasfx/vista/tutor/FXMLFormularioAsistencia.fxml"));
            Parent root = loader.load();
            
            FXMLFormularioAsistenciaController controlador = loader.getController();
            controlador.inicializarAsistencia(academicoSesion.getIdAcademico(), fechaSeleccionada);
            
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.setTitle("Lista de Asistencia");
            stage.showAndWait();
            
            cargarTabla(); // Recargar tabla al cerrar para actualizar el estado "Pendiente/Registrada"
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    @FXML private void clicRegistrar(ActionEvent event) { clicPasarLista(event); }
}