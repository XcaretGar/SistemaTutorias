/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package sistematutoriasfx.controlador.administrador;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import sistematutoriasfx.modelo.pojo.Academico;

/**
 * FXML Controller class
 *
 * @author JOANA XCARET
 */
public class FXMLGestionarUsuarioController implements Initializable {

    @FXML
    private TableView<Academico> tvUsuarios;
    @FXML
    private TableColumn<?, ?> colNombre;
    @FXML
    private TableColumn<?, ?> colApPaterno;
    @FXML
    private TableColumn<?, ?> colApMaterno;
    @FXML
    private TableColumn<?, ?> colTipoUsuario;
    @FXML
    private TableColumn<?, ?> colCorreo;
    @FXML
    private TextField tfBuscar;
    
    private ObservableList<Academico> academicos;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        /*configurarTabla();
        cargarInformacion();
        configurarBusqueda();*/
    }    

    /*private void configurarTabla() {
        colNombre.setCellValueFactory(new PropertyValueFactory("nombreEstudiante"));
        colApPaterno.setCellValueFactory(new PropertyValueFactory("apellidoPaterno"));
        colApMaterno.setCellValueFactory(new PropertyValueFactory("apellidoMaterno"));
        colMatricula.setCellValueFactory(new PropertyValueFactory("matricula"));
        colProgramaEducativo.setCellValueFactory(new PropertyValueFactory("programaEducativo"));
        colEstatus.setCellValueFactory(new PropertyValueFactory("estatus"));
        colCorreoInstitucional.setCellValueFactory(new PropertyValueFactory("correoInstitucional"));
    }
    
    private void cargarInformacion() {
        HashMap<String, Object> respuesta = EstudianteImp.obtenerEstudiantes();
        //Casteo con paréntesis
        boolean error = (boolean) respuesta.get("error");
        if (!error) {
            ArrayList<Estudiante> estudiantesBD = (ArrayList<Estudiante>) respuesta.get("estudiantes");
            estudiantes = FXCollections.observableArrayList();
            estudiantes.addAll(estudiantesBD);
            tvTutorados.setItems(estudiantes);
        } else {
            Utilidades.mostrarAlertaSimple("error", 
                    (String) respuesta.get("mensaje"), Alert.AlertType.ERROR);
        }
    }*/
    
    @FXML
    private void clicActualizarUsuario(ActionEvent event) {
    }

    @FXML
    private void clicAsignarUsuario(ActionEvent event) {
    }
    
}
