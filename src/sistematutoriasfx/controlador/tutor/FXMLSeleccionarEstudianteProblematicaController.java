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
    @FXML private TableColumn<Estudiante, String> colMatricula;
    @FXML private TableColumn<Estudiante, String> colNombre;

    private int idAcademico;
    private FXMLFormularioProblematicaController controladorPadre;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colMatricula.setCellValueFactory(new PropertyValueFactory<>("matricula"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombreCompleto")); 
        
        tvEstudiantes.setOnMouseClicked(event -> {
            if(event.getClickCount() == 2 && tvEstudiantes.getSelectionModel().getSelectedItem() != null){
                clicSeleccionar(null);
            }
        });
    }

    public void inicializar(int idAcademico, FXMLFormularioProblematicaController padre) {
        this.idAcademico = idAcademico;
        this.controladorPadre = padre;
        cargarAlumnos();
    }

    private void cargarAlumnos() {
        int idPeriodoActual = 2; 
        
        System.out.println("Cargando alumnos para Tutor ID: " + idAcademico + " Periodo: " + idPeriodoActual);
        
        tvEstudiantes.setItems(FXCollections.observableArrayList(
            EstudianteDAO.obtenerEstudiantesPorTutor(idAcademico, idPeriodoActual)
        ));
    }

    @FXML
    private void clicSeleccionar(ActionEvent event) {
        Estudiante seleccionado = tvEstudiantes.getSelectionModel().getSelectedItem();
        if(seleccionado != null){
            controladorPadre.setEstudianteSeleccionado(seleccionado);
            cerrarVentana();
        } else {
            Utilidades.mostrarAlertaSimple("Selección", "Elige un estudiante.", Alert.AlertType.WARNING);
        }
    }

    @FXML private void clicCancelar(ActionEvent event) { cerrarVentana(); }
    
    private void cerrarVentana() {
        ((Stage) tvEstudiantes.getScene().getWindow()).close();
    }
}