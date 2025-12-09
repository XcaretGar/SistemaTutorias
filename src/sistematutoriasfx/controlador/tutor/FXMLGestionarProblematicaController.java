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
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sistematutoriasfx.modelo.dao.AcademicoDAO;
import sistematutoriasfx.modelo.dao.ProblematicaDAO;
import sistematutoriasfx.modelo.pojo.Academico;
import sistematutoriasfx.modelo.pojo.Problematica;
import sistematutoriasfx.modelo.pojo.Usuario;
import utilidad.Utilidades;

public class FXMLGestionarProblematicaController implements Initializable {

    @FXML private TableView<Problematica> tvReportes; // Se llama tvReportes en tu FXML
    @FXML private TableColumn<Problematica, String> colMatricula;
    @FXML private TableColumn<Problematica, String> colNombreEstudiante;
    @FXML private TableColumn<Problematica, String> colProgramaEducativo;
    @FXML private TableColumn<Problematica, String> colTitulo;
    @FXML private TableColumn<Problematica, String> colDescripcion;
    
    private TextField tfBusquedaNombre;
    @FXML private Label lbEstatusProblematica;

    private Usuario usuarioSesion;
    private Academico academicoSesion;
    private ObservableList<Problematica> listaProblematicas;
    private FilteredList<Problematica> listaFiltrada;
    @FXML
    private Label lbFechaMostrada;

    public void configurarEscena(Usuario usuario) {
        this.usuarioSesion = usuario;
        this.academicoSesion = AcademicoDAO.obtenerAcademicoPorIdUsuario(usuario.getIdUsuario());
        if(this.academicoSesion != null) {
            cargarTabla();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarColumnas();
        configurarFiltro();
        configurarSeleccionTabla();
    }
    
    private void configurarColumnas() {
        colNombreEstudiante.setCellValueFactory(new PropertyValueFactory<>("nombreEstudiante"));
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        
        // Configuración especial para columnas que no están directas en el POJO Problematica
        // Si tu POJO no tiene getMatricula(), ponemos un valor por defecto o tendríamos que hacer un JOIN extra
        colMatricula.setCellValueFactory(cellData -> new SimpleStringProperty("---")); 
        colProgramaEducativo.setCellValueFactory(cellData -> new SimpleStringProperty("---"));
        
        // Nota: Para ver Matrícula y Programa real, necesitas agregar esos campos al POJO Problematica
        // y llenarlos en el DAO con el JOIN a Estudiante y ProgramaEducativo.
    }
    
    private void cargarTabla() {
        if(academicoSesion != null) {
            // Cargar problemáticas. 
            // Nota: Usamos el método obtenerProblematicasPorReporte del DAO. 
            // Idealmente deberías tener obtenerProblematicasPorTutor para ver todas.
            // Aquí simulamos cargar las del reporte 1 para que veas datos.
            ArrayList<Problematica> lista = ProblematicaDAO.obtenerProblematicasPorReporte(1);
            
            listaProblematicas = FXCollections.observableArrayList(lista);
            listaFiltrada = new FilteredList<>(listaProblematicas, p -> true);
            tvReportes.setItems(listaFiltrada);
        }
    }
    
    private void configurarFiltro() {
        tfBusquedaNombre.textProperty().addListener((observable, oldValue, newValue) -> {
            listaFiltrada.setPredicate(problema -> {
                if (newValue == null || newValue.isEmpty()) return true;
                String lower = newValue.toLowerCase();
                return problema.getNombreEstudiante().toLowerCase().contains(lower);
            });
        });
    }
    
    private void configurarSeleccionTabla() {
        tvReportes.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                lbEstatusProblematica.setText("Estatus: " + newVal.getEstatus());
            } else {
                lbEstatusProblematica.setText("Estatus: ---");
            }
        });
    }

    @FXML
    private void clicRegistrar(ActionEvent event) {
        abrirFormulario(null); // Null indica Nuevo Registro
    }

    @FXML
    private void clicModificar(ActionEvent event) {
        Problematica seleccion = tvReportes.getSelectionModel().getSelectedItem();
        if(seleccion != null) {
            abrirFormulario(seleccion); // Pasamos objeto para Editar
        } else {
            Utilidades.mostrarAlertaSimple("Selección requerida", "Selecciona una problemática para modificar.", Alert.AlertType.WARNING);
        }
    }
    
    @FXML private void clicConsultar(ActionEvent event) { 
        // Reutilizamos modificar por ahora, o podrías abrir en modo solo lectura
        clicModificar(event); 
    }

    @FXML
    private void clicEliminar(ActionEvent event) {
        Problematica seleccion = tvReportes.getSelectionModel().getSelectedItem();
        if(seleccion != null) {
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmar eliminación");
            confirmacion.setHeaderText("¿Estás seguro de eliminar esta problemática?");
            confirmacion.setContentText("Título: " + seleccion.getTitulo());
            
            Optional<ButtonType> resultado = confirmacion.showAndWait();
            if(resultado.isPresent() && resultado.get() == ButtonType.OK) {
                boolean exito = ProblematicaDAO.eliminarProblematica(seleccion.getIdProblematica());
                if(exito) {
                    Utilidades.mostrarAlertaSimple("Éxito", "Eliminado correctamente.", Alert.AlertType.INFORMATION);
                    cargarTabla();
                    lbEstatusProblematica.setText("Estatus: ---");
                } else {
                    Utilidades.mostrarAlertaSimple("Error", "No se pudo eliminar.", Alert.AlertType.ERROR);
                }
            }
        } else {
            Utilidades.mostrarAlertaSimple("Selección requerida", "Selecciona una problemática.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void clicRegresar(ActionEvent event) {
        ((Stage) tfBusquedaNombre.getScene().getWindow()).close();
    }
    
    private void abrirFormulario(Problematica problemaEdicion) {
        try {
            // Cargar el FXML del FORMULARIO
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sistematutoriasfx/vista/tutor/FXMLFormularioProblematica.fxml"));
            Parent root = loader.load();
            
            FXMLFormularioProblematicaController controlador = loader.getController();
            
            // Siempre inicializamos el tutor para que pueda buscar alumnos
            controlador.inicializarTutor(academicoSesion.getIdAcademico());
            
            // Si es edición, cargamos los datos
            if(problemaEdicion != null) {
                controlador.inicializarEdicion(problemaEdicion);
            }
            
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.setTitle(problemaEdicion == null ? "Registrar Problemática" : "Modificar Problemática");
            stage.showAndWait();
            
            cargarTabla(); // Recargar tabla al cerrar
            
        } catch (IOException ex) {
            ex.printStackTrace();
            Utilidades.mostrarAlertaSimple("Error", "No se pudo abrir el formulario.", Alert.AlertType.ERROR);
        }
    }
}