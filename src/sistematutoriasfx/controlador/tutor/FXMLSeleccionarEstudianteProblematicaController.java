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

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sistematutoriasfx.modelo.dao.EstudianteDAO;
import sistematutoriasfx.modelo.pojo.Estudiante;
import utilidad.Utilidades;

public class FXMLSeleccionarEstudianteProblematicaController implements Initializable {

    @FXML private TableView<Estudiante> tvEstudiantes;
    @FXML private TableColumn colMatricula;
    @FXML private TableColumn colNombre;

    private int idAcademico;
    private FXMLFormularioProblematicaController controladorPadre;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colMatricula.setCellValueFactory(new PropertyValueFactory("matricula"));
        // Asegúrate de tener getNombreCompleto() en tu POJO Estudiante
        colNombre.setCellValueFactory(new PropertyValueFactory("nombreCompleto")); 
        
        // Doble clic para seleccionar rápido
        tvEstudiantes.setOnMouseClicked(event -> {
            if(event.getClickCount() == 2 && tvEstudiantes.getSelectionModel().getSelectedItem() != null){
                clicSeleccionar(null);
            }
        });
    }

    public void inicializar(int idAcademico, FXMLFormularioProblematicaController padre) {
        this.idAcademico = idAcademico;
        this.controladorPadre = padre;
        cargarEstudiantes();
    }

    private void cargarEstudiantes() {
        // Carga los alumnos del tutor del periodo 1 (o pásalo como parámetro)
        ObservableList<Estudiante> lista = FXCollections.observableArrayList(
            EstudianteDAO.obtenerEstudiantesPorTutor(idAcademico, 1)
        );
        tvEstudiantes.setItems(lista);
    }

    @FXML
    private void clicSeleccionar(ActionEvent event) {
        Estudiante seleccion = tvEstudiantes.getSelectionModel().getSelectedItem();
        if (seleccion != null) {
            // ¡AQUÍ ESTÁ LA MAGIA! Le mandamos el alumno a la ventana de atrás
            controladorPadre.setEstudianteSeleccionado(seleccion);
            cerrarVentana();
        } else {
            Utilidades.mostrarAlertaSimple("Selección", "Selecciona un estudiante.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void clicCancelar(ActionEvent event) {
        cerrarVentana();
    }
    
    private void cerrarVentana() {
        ((Stage) tvEstudiantes.getScene().getWindow()).close();
    }
}