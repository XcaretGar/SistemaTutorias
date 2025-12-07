package sistematutoriasfx.controlador.tutor;

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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sistematutoriasfx.modelo.dao.AcademicoDAO;
import sistematutoriasfx.modelo.dao.FechasTutoriaDAO;
import sistematutoriasfx.modelo.dao.PeriodoEscolarDAO;
import sistematutoriasfx.modelo.dao.ReporteTutoriaDAO;
import sistematutoriasfx.modelo.pojo.Academico;
import sistematutoriasfx.modelo.pojo.FechasTutoria;
import sistematutoriasfx.modelo.pojo.PeriodoEscolar;
import sistematutoriasfx.modelo.pojo.ReporteTutoria;
import sistematutoriasfx.modelo.pojo.Usuario;
import utilidad.Utilidades;

public class FXMLGestionarReporteTutoriaController implements Initializable {

    @FXML private ComboBox<PeriodoEscolar> cbPeriodo;
    @FXML private ComboBox<FechasTutoria> cbFechaSesion;
    @FXML private TableView<ReporteTutoria> tvReportes;
    @FXML private TableColumn colPeriodo;
    @FXML private TableColumn colFechaSesion; 
    @FXML private TableColumn colSesion;
    @FXML private TableColumn colEstatus;
    @FXML private TableColumn colAsistentes;
    @FXML private TableColumn colEnRiesgo;
    @FXML private TableColumn colComentarios;

    private Usuario usuarioSesion;
    private Academico academicoSesion;

    public void configurarEscena(Usuario usuario) {
        this.usuarioSesion = usuario;
        this.academicoSesion = AcademicoDAO.obtenerAcademicoPorIdUsuario(usuario.getIdUsuario());
        if(this.academicoSesion != null){
            cargarTabla(); 
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarColumnas();
        cargarCombos();
        configurarListeners();
    }
    
    private void configurarColumnas() {
        // Enlazar columnas con atributos del POJO (incluyendo los virtuales)
        colPeriodo.setCellValueFactory(new PropertyValueFactory("periodoNombre")); 
        colFechaSesion.setCellValueFactory(new PropertyValueFactory("fechaSesion"));
        colSesion.setCellValueFactory(new PropertyValueFactory("numSesion")); 
        colEstatus.setCellValueFactory(new PropertyValueFactory("estatus"));
        colAsistentes.setCellValueFactory(new PropertyValueFactory("totalAsistentes"));
        colEnRiesgo.setCellValueFactory(new PropertyValueFactory("totalEnRiesgo"));
        colComentarios.setCellValueFactory(new PropertyValueFactory("comentariosGenerales"));
    }
    
    private void cargarCombos() {
        cbPeriodo.setItems(FXCollections.observableArrayList(PeriodoEscolarDAO.obtenerPeriodos()));
    }
    
    private void configurarListeners() {
        cbPeriodo.valueProperty().addListener((obs, oldVal, newVal) -> {
            if(newVal != null) {
                cbFechaSesion.setItems(FXCollections.observableArrayList(
                    FechasTutoriaDAO.obtenerFechasPorPeriodo(newVal.getIdPeriodo())
                ));
                // Opcional: Filtrar tabla aquí si quisieras ver solo ese periodo
            }
        });
    }
    
    private void cargarTabla() {
        if(academicoSesion != null) {
            tvReportes.setItems(FXCollections.observableArrayList(
                ReporteTutoriaDAO.obtenerReportesPorTutor(academicoSesion.getIdAcademico())
            ));
        }
    }

    @FXML
    private void clicRegistrar(ActionEvent event) {
        abrirFormulario(null); // Nuevo reporte
    }

    @FXML
    private void clicModificar(ActionEvent event) {
        ReporteTutoria seleccion = tvReportes.getSelectionModel().getSelectedItem();
        if(seleccion != null) {
            abrirFormulario(seleccion); // Editar reporte seleccionado
        } else {
            Utilidades.mostrarAlertaSimple("Selección requerida", "Selecciona un reporte para modificar.", Alert.AlertType.WARNING);
        }
    }
    
    private void abrirFormulario(ReporteTutoria reporteEdicion) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sistematutoriasfx/vista/tutor/FXMLFormularioReporteTutoria.fxml"));
            Parent root = loader.load();
            
            FXMLFormularioReporteTutoriaController controlador = loader.getController();
            controlador.inicializarTutor(academicoSesion.getIdAcademico());
            
            if(reporteEdicion != null) {
                controlador.inicializarEdicion(reporteEdicion);
            }
            
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.setTitle(reporteEdicion == null ? "Registrar Reporte" : "Modificar Reporte");
            stage.showAndWait();
            
            cargarTabla(); // Recargar la tabla al cerrar el formulario
            
        } catch (IOException ex) {
            ex.printStackTrace();
            Utilidades.mostrarAlertaSimple("Error", "No se pudo abrir la ventana del formulario.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void clicRegresar(ActionEvent event) {
        ((Stage) cbPeriodo.getScene().getWindow()).close();
    }
    
    @FXML 
    private void clicEliminar(ActionEvent event) {
        Utilidades.mostrarAlertaSimple("Pendiente", "Funcionalidad de eliminar pendiente.", Alert.AlertType.INFORMATION);
    }
    
    @FXML 
    private void clicExportar(ActionEvent event) {
        Utilidades.mostrarAlertaSimple("Pendiente", "Funcionalidad de exportar a PDF pendiente.", Alert.AlertType.INFORMATION);
    }
}