/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package sistematutoriasfx.controlador.administrador;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
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
import sistematutoriasfx.dominio.AcademicoImp;
import sistematutoriasfx.interfaces.IObservador;
import sistematutoriasfx.modelo.pojo.Academico;
import sistematutoriasfx.modelo.pojo.Rol;
import sistematutoriasfx.modelo.pojo.Usuario;
import utilidad.Utilidades;

/**
 * FXML Controller class
 *
 * @author JOANA XCARET
 */
public class FXMLGestionarUsuarioController implements Initializable, IObservador  {

    @FXML
    private TableView<Academico> tvUsuarios;
    @FXML
    private TableColumn colNombre;
    @FXML
    private TableColumn colApPaterno;
    @FXML
    private TableColumn colApMaterno;
    @FXML
    private TableColumn<Academico, String> colTipoUsuario;
    @FXML
    private TableColumn colCorreo;
    @FXML
    private TextField tfBuscar;
    
    private ObservableList<Academico> academicos;
    private Usuario usuarioSesion;


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
        cargarAcademicos();
        configurarBusqueda();
    }    

    private void configurarTabla() {
        colNombre.setCellValueFactory(new PropertyValueFactory("nombre"));
        colApPaterno.setCellValueFactory(new PropertyValueFactory("apellidoPaterno"));
        colApMaterno.setCellValueFactory(new PropertyValueFactory("apellidoMaterno"));
        colTipoUsuario.setCellValueFactory(cellData -> {
            Academico academico = cellData.getValue();
            String rolNombre = "Sin rol";

            if (academico != null 
                && academico.getUsuario() != null 
                && academico.getUsuario().getRoles() != null 
                && !academico.getUsuario().getRoles().isEmpty()) {

                rolNombre = academico.getUsuario().getRoles().get(0).getNombre();
            }

            return new SimpleStringProperty(rolNombre);
        });
        colCorreo.setCellValueFactory(new PropertyValueFactory("correoInstitucional"));
    }
    
    private void cargarAcademicos() {
        HashMap<String, Object> respuesta = AcademicoImp.obtenerUsuarios();
        //Casteo con paréntesis
        boolean error = (boolean) respuesta.get("error");
        if (!error) {
            ArrayList<Academico> academicosBD = (ArrayList<Academico>) respuesta.get("academicos");
            academicos = FXCollections.observableArrayList();
            academicos.addAll(academicosBD);
            tvUsuarios.setItems(academicos);
        } else {
            Utilidades.mostrarAlertaSimple("error", 
                    (String) respuesta.get("mensaje"), Alert.AlertType.ERROR);
        }
    }
    
    @FXML
    private void clicActualizarUsuario(ActionEvent event) {
        Academico academicoSeleccion = tvUsuarios.getSelectionModel().getSelectedItem();
        if (academicoSeleccion != null) {
            irFormulario(academicoSeleccion);
        } else {
            Utilidades.mostrarAlertaSimple("", "Selecciona un usuario, para actualizar la información"
                    + " de un usuario primero debes seleccionarlo", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void clicAsignarUsuario(ActionEvent event) {
        irFormulario(null);
    }
    
    private void irFormulario(Academico academico) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sistematutoriasfx/vista/administrador/FXMLFormularioUsuario.fxml"));  
            Parent vista = loader.load();
            FXMLFormularioUsuarioController controlador = loader.getController();
            controlador.inicializarDatos(this, academico);
            Scene scene = new Scene(vista);
            Stage stage = new Stage();
            stage.setTitle("Formulario Usuario");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException ex) {
            ex.printStackTrace();
        }       
    }
    
    @FXML
    private void clicDarBaja(ActionEvent event) {
        Academico academicoSeleccion = tvUsuarios.getSelectionModel().getSelectedItem();
        if (academicoSeleccion != null) {
            irDarBaja(academicoSeleccion);
        } else {
            Utilidades.mostrarAlertaSimple("Aviso", 
                "Selecciona un usuario para darlo de baja.", 
                Alert.AlertType.WARNING);
        }
    }
    
    private void irDarBaja(Academico academico) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sistematutoriasfx/vista/administrador/FXMLDarBajaUsuario.fxml"));           
            Parent vista = loader.load();
            FXMLDarBajaUsuarioController controlador = loader.getController();
            //Se obtiene el rol y también lo pasa
            Rol rol = academico.getUsuario().getRoles().get(0);
            controlador.inicializarDatos(this, academico, rol, usuarioSesion);
            Scene scene = new Scene(vista);
            Stage stage = new Stage();
            stage.setTitle("Dar de Baja Usuario");
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
        System.out.println("Nombre usuario: " + nombre);
        tfBuscar.setText("");
        cargarAcademicos();
        configurarBusqueda();
    }
    
    private void configurarBusqueda() {
        if (academicos != null && academicos.size() > 0) {
            FilteredList<Academico> filtradoAcademicos =
                    //a representa cada alumno que hay en el list y si lo cumple(true) va en la lista filtrada
                    new FilteredList<>(academicos, a -> true);
            tfBuscar.textProperty().addListener(new ChangeListener<String>() {
                
                @Override
                public void changed(ObservableValue<? extends String> observable, 
                        String oldValue, String newValue) {
                    filtradoAcademicos.setPredicate(academico -> {
                        //CASO DEFAULT VACÍO DEVOLVER TODOS
                        if (newValue == null || newValue.isEmpty()) {
                            return true;
                        }
                        String lowerNewValue = newValue.toLowerCase();
                        
                        //Criterio 1 de búsqueda por nombre
                        if (academico.getNombre().toLowerCase().contains(lowerNewValue)) {
                            return true;
                        }
                        
                        //Criterio 2 de búsqueda por matrícula
                        if (academico.getNoPersonal().toLowerCase().contains(lowerNewValue)) {
                            return true;
                        }
                        
                        return false;
                    });
                }
            });
            SortedList<Academico> sortedAcademicos = new SortedList<>(filtradoAcademicos);
            sortedAcademicos.comparatorProperty().bind(tvUsuarios.comparatorProperty());
            tvUsuarios.setItems(sortedAcademicos);
        }
    }
}
