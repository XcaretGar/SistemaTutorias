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
import sistematutoriasfx.modelo.pojo.Academico;
import sistematutoriasfx.modelo.pojo.Evidencia;
import sistematutoriasfx.modelo.pojo.FechasTutoria;
import sistematutoriasfx.modelo.pojo.PeriodoEscolar;
import sistematutoriasfx.modelo.pojo.Usuario;
import utilidad.Utilidades;

public class FXMLGestionarEvidenciaController implements Initializable {

    @FXML private ComboBox<PeriodoEscolar> cbPeriodo;
    @FXML private ComboBox<FechasTutoria> cbFechaSesion;
    @FXML private TableView<Evidencia> tvEvidencias;
    @FXML private TableColumn colFechaSubida;
    @FXML private TableColumn colTamano; // Tipo de archivo
    @FXML private TableColumn colNombreArchivo;

    private Usuario usuarioSesion;
    private Academico academicoSesion;
    private int idReporteActual = 0; 

    // Método llamado desde el menú principal
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
        // Asegúrate de tener getTipoArchivo() en el POJO Evidencia
        colTamano.setCellValueFactory(new PropertyValueFactory("tipoArchivo")); 
    }
    
    private void cargarPeriodos() {
        cbPeriodo.setItems(FXCollections.observableArrayList(PeriodoEscolarDAO.obtenerPeriodos()));
    }
    
    private void configurarListeners() {
        // Al cambiar periodo, cargar fechas
        cbPeriodo.valueProperty().addListener((obs, oldVal, newVal) -> {
            if(newVal != null) {
                cbFechaSesion.setItems(FXCollections.observableArrayList(
                    FechasTutoriaDAO.obtenerFechasPorPeriodo(newVal.getIdPeriodo())
                ));
                tvEvidencias.getItems().clear();
                idReporteActual = 0;
            }
        });
        
        // Al cambiar fecha, buscar el reporte y cargar evidencias
        cbFechaSesion.valueProperty().addListener((obs, oldVal, newVal) -> {
            if(newVal != null && academicoSesion != null) {
                buscarReporte(newVal.getIdFechaTutoria());
            }
        });
    }
    
    private void buscarReporte(int idFechaTutoria) {
        // TODO: Aquí va la consulta real para obtener el idReporte
        // Por ahora simulamos ID 1 para que funcione la prueba
        this.idReporteActual = 1; 
        
        cargarTablaEvidencias();
    }
    
    private void cargarTablaEvidencias() {
        if(idReporteActual > 0) {
            ObservableList<Evidencia> lista = FXCollections.observableArrayList(
                EvidenciaDAO.obtenerEvidenciasPorReporte(idReporteActual)
            );
            tvEvidencias.setItems(lista);
        }
    }

    // --- ACCIONES DE BOTONES ---

    @FXML
    private void clicSubirArchivo(ActionEvent event) {
        if (idReporteActual > 0) {
            // Abrir formulario en modo REGISTRO (null)
            //abrirFormulario(null); 
        } else {
            Utilidades.mostrarAlertaSimple("Atención", "Selecciona una fecha válida o genera el reporte primero.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void clicModificarArchivo(ActionEvent event) {
        // 1. Obtener selección de la tabla
        Evidencia seleccion = tvEvidencias.getSelectionModel().getSelectedItem();
        
        if (seleccion != null) {
            // 2. Abrir formulario en modo EDICIÓN (pasando el objeto)
            //abrirFormulario(seleccion); 
        } else {
            Utilidades.mostrarAlertaSimple("Selección requerida", "Selecciona un archivo de la tabla para actualizar.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void clicEliminarArchivo(ActionEvent event) {
        Evidencia seleccion = tvEvidencias.getSelectionModel().getSelectedItem();
        if(seleccion != null) {
            // Confirmación simple (puedes usar AlertType.CONFIRMATION si quieres)
            boolean exito = EvidenciaDAO.eliminarEvidencia(seleccion.getIdEvidencia());
            if(exito) {
                Utilidades.mostrarAlertaSimple("Eliminado", "Evidencia eliminada correctamente.", Alert.AlertType.INFORMATION);
                cargarTablaEvidencias();
            } else {
                Utilidades.mostrarAlertaSimple("Error", "No se pudo eliminar de la base de datos.", Alert.AlertType.ERROR);
            }
        } else {
            Utilidades.mostrarAlertaSimple("Selección requerida", "Selecciona un archivo para eliminar.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void clicRegresar(ActionEvent event) {
        ((Stage) cbPeriodo.getScene().getWindow()).close();
    }
    
    /*
    // Método auxiliar para abrir la ventana modal
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
            
            // Recargar al cerrar
            cargarTablaEvidencias();
            
        } catch (IOException ex) {
            ex.printStackTrace();
            Utilidades.mostrarAlertaSimple("Error", "No se pudo abrir el formulario.", Alert.AlertType.ERROR);
        }
    }
*/
}