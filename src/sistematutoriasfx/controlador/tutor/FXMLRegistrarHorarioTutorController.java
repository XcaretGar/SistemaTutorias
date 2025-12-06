/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package sistematutoriasfx.controlador.tutor;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;

/**
 * FXML Controller class
 *
 * @author Ana Georgina
 */

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

    // Este método lo llama el Menú Principal para pasar los datos
    public void configurarEscena(Usuario usuario) {
        this.usuarioSesion = usuario;
        this.academicoSesion = AcademicoDAO.obtenerAcademicoPorIdUsuario(usuario.getIdUsuario());
        
        // Una vez que tenemos al académico, cargamos SU tabla
        if(academicoSesion != null){
            cargarTabla();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarColumnas();
    }    
    
    private void configurarColumnas() {
        // Enlazar columnas con los atributos del POJO SesionTutoria
        colPeriodo.setCellValueFactory(new PropertyValueFactory("periodo"));
        colFechaTutoria.setCellValueFactory(new PropertyValueFactory("fecha"));
        colSesion.setCellValueFactory(new PropertyValueFactory("numSesion"));
        colHora.setCellValueFactory(new PropertyValueFactory("horaInicio"));
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
        // Enviar NULL significa "Quiero uno NUEVO"
        abrirFormulario(null); 
    }

    @FXML
    private void clicActualizarHorario(ActionEvent event) {
        // 1. Obtenemos la fila que el usuario seleccionó en la tabla
        SesionTutoria sesionSeleccionada = tvHorarios.getSelectionModel().getSelectedItem();
        
        // 2. Verificamos que sí haya seleccionado algo
        if (sesionSeleccionada != null) {
            // Enviar EL OBJETO significa "Quiero EDITAR este"
            abrirFormulario(sesionSeleccionada); 
        } else {
            Utilidades.mostrarAlertaSimple("Selección requerida", 
                "Selecciona un horario de la tabla para actualizar.", 
                Alert.AlertType.WARNING);
        }
    }

    // Este es el método "Cartero" que abre la ventana y entrega el paquete
    private void abrirFormulario(SesionTutoria sesionParaEditar) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sistematutoriasfx/vista/tutor/FXMLFormularioRegistrarHorarioTutor.fxml"));
            Parent root = loader.load();
            
            // Obtenemos el controlador de la OTRA ventana (el Formulario)
            FXMLFormularioRegistrarHorarioTutorController controladorFormulario = loader.getController();
            
            // Le pasamos el ID del tutor (siempre necesario)
            controladorFormulario.inicializarTutor(academicoSesion.getIdAcademico());
            
            // AQUÍ ESTÁ LA CLAVE:
            // Si le mandamos una sesión (Actualizar), llamamos al método especial para llenar campos
            if (sesionParaEditar != null) {
                //controladorFormulario.inicializarSesionEdicion(sesionParaEditar);
            }
            
            // Mostramos la ventana
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.setTitle(sesionParaEditar == null ? "Registrar Horario" : "Editar Horario");
            stage.showAndWait();
            
            // Cuando se cierre, recargamos la tabla para ver cambios
            cargarTabla();
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}