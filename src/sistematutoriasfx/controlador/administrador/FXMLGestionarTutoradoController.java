/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistematutoriasfx.controlador.administrador;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sistematutoriasfx.dominio.EstudianteImp;
import sistematutoriasfx.interfaces.IObservador;
import sistematutoriasfx.modelo.pojo.Estudiante;
import utilidad.Utilidades;

/**
 * FXML Controller class
 * 
 * @author JOANA XCARET
 */
public class FXMLGestionarTutoradoController implements Initializable, IObservador {

    @FXML
    private TableView<Estudiante> tvTutorados;
    @FXML
    private TableColumn colNombre;
    @FXML
    private TableColumn colApPaterno;
    @FXML
    private TableColumn colApMaterno;
    @FXML
    private TableColumn colMatricula;
    @FXML
    private TableColumn colProgramaEducativo;
    @FXML
    private TableColumn colEstatus;
    @FXML
    private TableColumn colCorreoInstitucional;
    @FXML
    private TextField tfBuscar;
    
    private ObservableList<Estudiante> estudiantes;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
        cargarEstudiantes();
        configurarBusqueda();
    }    

    private void configurarTabla() {
        colNombre.setCellValueFactory(new PropertyValueFactory("nombreEstudiante"));
        colApPaterno.setCellValueFactory(new PropertyValueFactory("apellidoPaterno"));
        colApMaterno.setCellValueFactory(new PropertyValueFactory("apellidoMaterno"));
        colMatricula.setCellValueFactory(new PropertyValueFactory("matricula"));
        colProgramaEducativo.setCellValueFactory(new PropertyValueFactory("programaEducativo"));
        colEstatus.setCellValueFactory(new PropertyValueFactory("estatus"));
        colCorreoInstitucional.setCellValueFactory(new PropertyValueFactory("correoInstitucional"));
    }
    
    private void cargarEstudiantes() {
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
    }
    
    @FXML
    private void clicActualizarTutorado(ActionEvent event) {
        Estudiante estudianteSeleccion = tvTutorados.getSelectionModel().getSelectedItem();
        if (estudianteSeleccion != null) {
            irFormulario(estudianteSeleccion);
        } else {
            Utilidades.mostrarAlertaSimple("", "Selecciona un tutorado, para actualizar la información"
                    + " de un tutorado primero debes seleccionarlo", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void clicAsignarTutorado(ActionEvent event) {
        irFormulario(null);
    }
    
    private void irFormulario(Estudiante estudiante) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sistematutoriasfx/vista/administrador/FXMLFormularioTutorado.fxml"));  
            Parent vista = loader.load();
            FXMLFormularioTutoradoController controlador = loader.getController();
            controlador.inicializarDatos(this, estudiante);
            Scene scene = new Scene(vista);
            Stage stage = new Stage();
            stage.setTitle("Formulario Tutorado");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException ex) {
            ex.printStackTrace();
        }       
    }
    
    @FXML
    private void clicDarBaja(ActionEvent event) {
        Estudiante estudianteSeleccion = tvTutorados.getSelectionModel().getSelectedItem();
        if (estudianteSeleccion != null) {
            if (estudianteSeleccion.getMotivoBaja() != null) {
                Utilidades.mostrarAlertaSimple("Imposible dar de Baja", "No se puede dar de baja a un tutorado que ya está dado de baja", Alert.AlertType.WARNING);
                return;
            }
            irDarBaja(estudianteSeleccion);
        } else {
            Utilidades.mostrarAlertaSimple("Aviso", 
                "Selecciona un tutorado para darlo de baja.", 
                Alert.AlertType.WARNING);
        }
    }
    
    private void irDarBaja(Estudiante estudiante) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sistematutoriasfx/vista/administrador/FXMLDarBajaTutorado.fxml"));           
            Parent vista = loader.load();
            FXMLDarBajaTutoradoController controlador = loader.getController();
            controlador.inicializarDatos(this, estudiante);
            Scene scene = new Scene(vista);
            Stage stage = new Stage();
            stage.setTitle("Dar de Baja Tutorado");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException ex) {
            ex.printStackTrace();
        }  
    }
    
    @Override
    public void notificarOperacionExitosa(String tipoOperacion, String nombre) {
        System.out.println("Operación: " + tipoOperacion);
        System.out.println("Nombre tutorado: " + nombre);
        tfBuscar.setText("");
        cargarEstudiantes();
        configurarBusqueda();
    }
    
    private void configurarBusqueda() {
        if (estudiantes != null && estudiantes.size() > 0) {
            FilteredList<Estudiante> filtradoEstudiantes =
                    //a representa cada alumno que hay en el list y si lo cumple(true) va en la lista filtrada
                    new FilteredList<>(estudiantes, a -> true);
            tfBuscar.textProperty().addListener(new ChangeListener<String>() {
                
                @Override
                public void changed(ObservableValue<? extends String> observable, 
                        String oldValue, String newValue) {
                    filtradoEstudiantes.setPredicate(estudiante -> {
                        //CASO DEFAULT VACÍO DEVOLVER TODOS
                        if (newValue == null || newValue.isEmpty()) {
                            return true;
                        }
                        String lowerNewValue = newValue.toLowerCase();
                        
                        //Criterio 1 de búsqueda por nombre
                        if (estudiante.getNombreEstudiante().toLowerCase().contains(lowerNewValue)) {
                            return true;
                        }
                        
                        //Criterio 2 de búsqueda por matrícula
                        if (estudiante.getMatricula().toLowerCase().contains(lowerNewValue)) {
                            return true;
                        }
                        
                        return false;
                    });
                }
            });
            SortedList<Estudiante> sortedEstudiante = new SortedList<>(filtradoEstudiantes);
            sortedEstudiante.comparatorProperty().bind(tvTutorados.comparatorProperty());
            tvTutorados.setItems(sortedEstudiante);
        }
    }
    
    @FXML
    private void clicRegresar(ActionEvent event) {
        Stage stage = (Stage) tvTutorados.getScene().getWindow();
        stage.close();
    }
}
