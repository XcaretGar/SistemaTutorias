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
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sistematutoriasfx.modelo.dao.AcademicoDAO;
import sistematutoriasfx.modelo.dao.ProblematicaDAO;
import sistematutoriasfx.modelo.dao.ReporteTutoriaDAO; // IMPORTANTE: Importar este DAO
import sistematutoriasfx.modelo.pojo.Academico;
import sistematutoriasfx.modelo.pojo.Problematica;
import sistematutoriasfx.modelo.pojo.Usuario;
import utilidad.Utilidades;

public class FXMLGestionarProblematicaController implements Initializable {

    @FXML private TableView<Problematica> tvReportes;
    @FXML private TableColumn<Problematica, String> colMatricula;
    @FXML private TableColumn<Problematica, String> colNombreEstudiante;
    @FXML private TableColumn<Problematica, String> colProgramaEducativo; // Si la tienes en la vista
    @FXML private TableColumn<Problematica, String> colTitulo;
    @FXML private TableColumn<Problematica, String> colDescripcion;
    @FXML private Label lbFechaMostrada;
    @FXML private Label lbEstatusProblematica;

    private Usuario usuarioSesion;
    private Academico academicoSesion;
    private ObservableList<Problematica> listaProblematicas;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
    }

    public void configurarEscena(Usuario usuario) {
        this.usuarioSesion = usuario;
        
        this.academicoSesion = AcademicoDAO.obtenerAcademicoPorIdUsuario(usuario.getIdUsuario());
        
        if(this.academicoSesion == null) {
            Utilidades.mostrarAlertaSimple("Error", "No se encontró información del tutor asociado al usuario.", Alert.AlertType.ERROR);
            return;
        }
        cargarTabla();
    }

    private void configurarTabla() {
        colMatricula.setCellValueFactory(new PropertyValueFactory<>("matricula"));
        colNombreEstudiante.setCellValueFactory(new PropertyValueFactory<>("nombreEstudiante"));
        colProgramaEducativo.setCellValueFactory(new PropertyValueFactory<>("programaEducativo"));
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        
        // Listener para actualizar la etiqueta de estatus al seleccionar una fila
        tvReportes.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> {
                if(newValue != null) {
                    lbEstatusProblematica.setText("Estatus: " + newValue.getEstatus());
                } else {
                    lbEstatusProblematica.setText("Estatus: ---");
                }
            }
        );
    }

    private void cargarTabla() {
        if(academicoSesion == null) return;
        
        // 1. Definimos el periodo actual (En tu base de datos es el 2)
        int idPeriodoActual = 2; 
        
        int idReporteActual = ReporteTutoriaDAO.obtenerIdReporteActual(academicoSesion.getIdAcademico(), idPeriodoActual);
        
        if (idReporteActual == 0) {
            // Puedes mostrar una lista vacía o un mensaje en consola
            tvReportes.setItems(FXCollections.observableArrayList());
            System.out.println("Este tutor no tiene reporte asignado para el periodo " + idPeriodoActual);
            return;
        }
        
        ArrayList<Problematica> listaDB = ProblematicaDAO.obtenerProblematicasPorReporte(idReporteActual);
        listaProblematicas = FXCollections.observableArrayList(listaDB);
        tvReportes.setItems(listaProblematicas);
    }

    @FXML
    private void clicRegistrar(ActionEvent event) {
        abrirFormulario("Registrar", null);
    }

    @FXML
    private void clicModificar(ActionEvent event) {
        Problematica seleccionada = tvReportes.getSelectionModel().getSelectedItem();
        if(seleccionada == null) {
            Utilidades.mostrarAlertaSimple("Atención", "Selecciona una problemática para modificar.", Alert.AlertType.WARNING);
            return;
        }
        abrirFormulario("Modificar", seleccionada);
    }

    @FXML
    private void clicConsultar(ActionEvent event) {
        Problematica seleccionada = tvReportes.getSelectionModel().getSelectedItem();
        if(seleccionada == null) {
            Utilidades.mostrarAlertaSimple("Atención", "Selecciona una problemática para consultar.", Alert.AlertType.WARNING);
            return;
        }
        abrirFormulario("Consultar", seleccionada);
    }

    @FXML
    private void clicEliminar(ActionEvent event) {
        Problematica seleccionada = tvReportes.getSelectionModel().getSelectedItem();
        if(seleccionada == null) {
            Utilidades.mostrarAlertaSimple("Atención", "Selecciona una problemática para eliminar.", Alert.AlertType.WARNING);
            return;
        }
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar eliminación");
        alert.setHeaderText("¿Deseas eliminar la problemática seleccionada?");
        alert.setContentText("Esta acción no se puede deshacer.");
        
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK){
            boolean exito = ProblematicaDAO.eliminarProblematica(seleccionada.getIdProblematica());
            if(exito){
                Utilidades.mostrarAlertaSimple("Éxito", "Problemática eliminada correctamente.", Alert.AlertType.INFORMATION);
                cargarTabla();
            } else {
                Utilidades.mostrarAlertaSimple("Error", "No se pudo eliminar la problemática.", Alert.AlertType.ERROR);
            }
        }
    }

    private void abrirFormulario(String modo, Problematica problematica) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sistematutoriasfx/vista/tutor/FXMLFormularioProblematica.fxml"));
            Parent root = loader.load();
            
            FXMLFormularioProblematicaController controlador = loader.getController();
            controlador.inicializarTutor(academicoSesion.getIdAcademico());
            
            if (problematica != null) {
                controlador.inicializarEdicion(problematica);
            }
            
            if ("Consultar".equalsIgnoreCase(modo)) {
                controlador.configurarModoConsulta();
            }
            
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.setTitle(modo + " Problemática"); 
            stage.showAndWait();
            
            if (!"Consultar".equalsIgnoreCase(modo)) {
                cargarTabla();
            }
            
        } catch (IOException ex) {
            ex.printStackTrace();
            Utilidades.mostrarAlertaSimple("Error", "No se pudo cargar la ventana.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void clicRegresar(ActionEvent event) {
        // Cierra la ventana actual
        Stage stage = (Stage) tvReportes.getScene().getWindow();
        stage.close();
    }
}