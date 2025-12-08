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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sistematutoriasfx.modelo.dao.AcademicoDAO;
import sistematutoriasfx.modelo.dao.EvidenciaDAO;
import sistematutoriasfx.modelo.dao.FechasTutoriaDAO;
import sistematutoriasfx.modelo.dao.PeriodoEscolarDAO;
import sistematutoriasfx.modelo.dao.ReporteTutoriaDAO;
import sistematutoriasfx.modelo.pojo.Academico;
import sistematutoriasfx.modelo.pojo.Evidencia;
import sistematutoriasfx.modelo.pojo.FechasTutoria;
import sistematutoriasfx.modelo.pojo.PeriodoEscolar;
import sistematutoriasfx.modelo.pojo.Usuario;
import utilidad.Utilidades;

/*public class FXMLGestionarEvidenciaController implements Initializable {

    @FXML 
    private ComboBox<PeriodoEscolar> cbPeriodo;
    @FXML 
    private ComboBox<FechasTutoria> cbFechaSesion;
    @FXML 
    private TableView<Evidencia> tvEvidencias;
    @FXML  
    private TableColumn colFechaSubida;
    @FXML 
    private TableColumn colTamano; // Tipo de archivo
    @FXML 
    private TableColumn colNombreArchivo;

    private Usuario usuarioSesion;
    private Academico academicoSesion;
    private int idReporteActual = 0; 
    @FXML
    private Label lbFechaMostrada;
    
    public void configurarEscena(Usuario usuario) {
        this.usuarioSesion = usuario;
        this.academicoSesion = AcademicoDAO.obtenerAcademicoPorIdUsuario(usuario.getIdUsuario());
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarColumnas();
        cargarPeriodos();
        configurarListeners();
    }
    
    private void configurarColumnas() {
        colNombreArchivo.setCellValueFactory(new PropertyValueFactory("nombreArchivo"));
        colFechaSubida.setCellValueFactory(new PropertyValueFactory("fechaSubida"));
        colTamano.setCellValueFactory(new PropertyValueFactory("tipoArchivo")); 
    }
    
    private void cargarPeriodos() {
        cbPeriodo.setItems(FXCollections.observableArrayList(PeriodoEscolarDAO.obtenerPeriodos()));
    }
    
    private void configurarListeners() {
        // Listener para el ComboBox de Período
        cbPeriodo.valueProperty().addListener((obs, oldVal, newVal) -> {
            if(newVal != null) {
                // Cargar fechas del periodo seleccionado
                cbFechaSesion.setItems(FXCollections.observableArrayList(
                    FechasTutoriaDAO.obtenerFechasPorPeriodo(newVal.getIdPeriodo())
                ));
                // Limpiar tabla y resetear ID
                tvEvidencias.getItems().clear();
                idReporteActual = 0;
                // ✅ Limpiar la fecha mostrada
                lbFechaMostrada.setText("");
            } else {
                // Si se deselecciona el periodo, limpiar todo
                cbFechaSesion.setItems(FXCollections.observableArrayList());
                lbFechaMostrada.setText("");
                tvEvidencias.getItems().clear();
                idReporteActual = 0;
            }
        });

        // Listener para el ComboBox de Fecha de Sesión
        cbFechaSesion.valueProperty().addListener((obs, oldVal, newVal) -> {
            if(newVal != null) {
                // ✅ Mostrar la fecha seleccionada en el label azul
                lbFechaMostrada.setText(newVal.getFechaSesion());

                // Buscar el reporte asociado a esta fecha
                if(academicoSesion != null) {
                    buscarReporte(newVal.getIdFechaTutoria());
                }
            } else {
                // Si se deselecciona la fecha, limpiar
                lbFechaMostrada.setText("");
                tvEvidencias.getItems().clear();
                idReporteActual = 0;
            }
        });
    }
      
    private void cargarTablaEvidencias() {
        if(idReporteActual > 0) {
            tvEvidencias.setItems(FXCollections.observableArrayList(
                EvidenciaDAO.obtenerEvidenciasPorReporte(idReporteActual)
            ));
        }
    }

    private void buscarReporte(int idFechaTutoria) {
        // Usamos el DAO para buscar el ID real en la BD
        this.idReporteActual = ReporteTutoriaDAO.obtenerIdReporte(
                academicoSesion.getIdAcademico(), 
                idFechaTutoria
        );
        
        if (this.idReporteActual > 0) {
            cargarTablaEvidencias();
        } else {
            // Si no hay reporte, limpiamos la tabla y avisamos (opcional)
            tvEvidencias.getItems().clear();
            // Utilidades.mostrarAlertaSimple("Aviso", "No se ha creado el reporte para esta fecha.", Alert.AlertType.INFORMATION);
        }
    }
    
    @FXML
    private void clicSubirArchivo(ActionEvent event) {
        if (idReporteActual > 0) {
            abrirFormulario(null); 
        } else {
            // AHORA SÍ ESTE MENSAJE TENDRÁ SENTIDO
            Utilidades.mostrarAlertaSimple("Falta Reporte", 
                "No puedes subir evidencia porque aún no has generado el reporte de esta sesión.", 
                Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void clicActualizarArchivo(ActionEvent event) {
        Evidencia seleccion = tvEvidencias.getSelectionModel().getSelectedItem();
        
        if (seleccion != null) {
            abrirFormulario(seleccion); 
        } else {
            Utilidades.mostrarAlertaSimple("Selección requerida", "Selecciona un archivo para actualizar.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void clicEliminarArchivo(ActionEvent event) {
        Evidencia seleccion = tvEvidencias.getSelectionModel().getSelectedItem();
        if(seleccion != null) {
            boolean exito = EvidenciaDAO.eliminarEvidencia(seleccion.getIdEvidencia());
            if(exito) {
                Utilidades.mostrarAlertaSimple("Eliminado", "Evidencia eliminada correctamente.", Alert.AlertType.INFORMATION);
                cargarTablaEvidencias();
            } else {
                Utilidades.mostrarAlertaSimple("Error", "No se pudo eliminar.", Alert.AlertType.ERROR);
            }
        } else {
            Utilidades.mostrarAlertaSimple("Selección requerida", "Selecciona un archivo para eliminar.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void clicRegresar(ActionEvent event) {
        ((Stage) cbPeriodo.getScene().getWindow()).close();
    }
    
    private void abrirFormulario(Evidencia evidenciaEdicion) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sistematutoriasfx/vista/tutor/FXMLFormularioEvidencia.fxml"));
            Parent root = loader.load();
            
            FXMLFormularioEvidenciaController controlador = loader.getController();
            
            if (evidenciaEdicion != null) {
                controlador.inicializarEdicion(evidenciaEdicion);
            } else {
                controlador.inicializarFormulario(this.idReporteActual);
            }
            
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.setTitle(evidenciaEdicion == null ? "Subir Evidencia" : "Modificar Evidencia");
            stage.showAndWait();
            
            // Recargar tabla al cerrar
            cargarTablaEvidencias();
            
        } catch (IOException ex) {
            ex.printStackTrace();
            Utilidades.mostrarAlertaSimple("Error", "No se pudo abrir el formulario.", Alert.AlertType.ERROR);
        }
    }
}*/