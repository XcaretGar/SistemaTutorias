package sistematutoriasfx.controlador.tutor;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sistematutoriasfx.modelo.dao.AcademicoDAO;
import sistematutoriasfx.modelo.dao.SesionTutoriaDAO;
import sistematutoriasfx.modelo.pojo.Academico;
import sistematutoriasfx.modelo.pojo.SesionTutoria;
import sistematutoriasfx.modelo.pojo.Usuario;
import utilidad.Utilidades;

public class FXMLRegistrarHorarioTutorController implements Initializable {

    @FXML
    private TableView<SesionTutoria> tvHorarios;
    @FXML
    private TableColumn colPeriodo;
    @FXML
    private TableColumn colFechaTutoria;
    @FXML
    private TableColumn colSesion;
    @FXML
    private TableColumn colHora;
    @FXML
    private TableColumn colLugar;
    @FXML
    private TableColumn colComentarios;
    
    private Usuario usuarioSesion;
    private Academico academicoSesion;

    // Método llamado desde el Menú Principal
    public void configurarEscena(Usuario usuario) {
        this.usuarioSesion = usuario;
        this.academicoSesion = AcademicoDAO.obtenerAcademicoPorIdUsuario(usuario.getIdUsuario());
        
        if(academicoSesion != null){
            cargarTabla();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarColumnas();
    }    
    
    private void configurarColumnas() {
        colPeriodo.setCellValueFactory(new PropertyValueFactory("periodo"));
        colFechaTutoria.setCellValueFactory(new PropertyValueFactory("fecha"));
        colSesion.setCellValueFactory(new PropertyValueFactory("numSesion"));
        colHora.setCellValueFactory(new PropertyValueFactory("hora"));
        colLugar.setCellValueFactory(new PropertyValueFactory("lugar"));
        colComentarios.setCellValueFactory(new PropertyValueFactory("comentarios"));
    }
    
    private void cargarTabla() {
        if(academicoSesion != null){
            ArrayList<SesionTutoria> respuesta = SesionTutoriaDAO.obtenerSesionesPorTutor(academicoSesion.getIdAcademico());
            ObservableList<SesionTutoria> listaSesiones = FXCollections.observableArrayList(respuesta);
            tvHorarios.setItems(listaSesiones);
        }
    }

    @FXML
    private void clicRegistrarHorario(ActionEvent event) {
        abrirFormulario(null);
    }

    @FXML
    private void clicActualizarHorario(ActionEvent event) {
        SesionTutoria sesionSeleccionada = tvHorarios.getSelectionModel().getSelectedItem();
        
        if (sesionSeleccionada != null) {
            abrirFormulario(sesionSeleccionada);
        } else {
            Utilidades.mostrarAlertaSimple("Selección requerida", 
                "Selecciona un horario de la tabla para actualizar.", 
                Alert.AlertType.WARNING);
        }
    }

    private void abrirFormulario(SesionTutoria sesionParaEditar) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sistematutoriasfx/vista/tutor/FXMLFormularioHorario.fxml"));            
            Parent root = loader.load();
            FXMLFormularioHorarioController controladorFormulario = loader.getController();
            
            controladorFormulario.inicializarTutor(academicoSesion.getIdAcademico());
            
            if (sesionParaEditar != null) {
                controladorFormulario.inicializarSesionEdicion(sesionParaEditar);
            }
            
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.setTitle(sesionParaEditar == null ? "Registrar Horario" : "Editar Horario");
            stage.showAndWait();
            
            cargarTabla();
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}