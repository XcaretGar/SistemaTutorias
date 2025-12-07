/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package sistematutoriasfx.controlador.administrador;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sistematutoriasfx.dominio.CatalogoImp;
import sistematutoriasfx.dominio.EstudianteImp;
import sistematutoriasfx.interfaces.IObservador;
import sistematutoriasfx.modelo.pojo.Estudiante;
import sistematutoriasfx.modelo.pojo.ProgramaEducativo;
import sistematutoriasfx.modelo.pojo.Respuesta;
import utilidad.Utilidades;

/**
 * FXML Controller class
 *
 * @author JOANA XCARET
 */
public class FXMLFormularioTutoradoController implements Initializable {

    @FXML
    private TextField tfNombre;
    @FXML
    private TextField tfApellidoPaterno;
    @FXML
    private TextField tfApellidoMaterno;
    @FXML
    private TextField tfCorreo;
    @FXML
    private TextField tfMatricula;
    @FXML
    private ComboBox<ProgramaEducativo> cbProgramaEducativo;
    @FXML
    private ComboBox<Estudiante.Estatus> cbEstadoAcademico;
    
    private IObservador observador;
    private Estudiante estudianteEdicion;
    private ObservableList<ProgramaEducativo> programasEducativos;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarInformacionProgramasEducativos();
        cbEstadoAcademico.setItems(
                FXCollections.observableArrayList(Estudiante.Estatus.values()));
    }   
    
    public void inicializarDatos(IObservador observador, Estudiante estudiante) {
        this.observador = observador;
        this.estudianteEdicion = estudiante;
        try {
            if (estudiante != null) {
                tfNombre.setText(estudiante.getNombreEstudiante());
                tfApellidoPaterno.setText(estudiante.getApellidoPaterno());
                tfApellidoMaterno.setText(estudiante.getApellidoMaterno());
                tfCorreo.setText(estudiante.getCorreoInstitucional());
                tfMatricula.setText(estudiante.getMatricula());
                tfMatricula.setEditable(false);
                cbEstadoAcademico.setValue(estudiante.getEstatus());
                cbProgramaEducativo.setValue(new ProgramaEducativo(
                    estudiante.getIdProgramaEducativo(),
                    estudiante.getProgramaEducativo()
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void clicGuardar(ActionEvent event) {
        if (sonCamposValidos()) {
            if (estudianteEdicion == null) {
                registrarEstudiante();
            } else {
                actualizarEstudiante();
            }
        }
    }

    @FXML
    private void clicCancelar(ActionEvent event) {
        cerrarVentana();
    }
    
    private boolean sonCamposValidos() {
        boolean valido = true;
        String mensajeError = "Se encontraron los siguientes errores:\n";
        
        if (tfNombre.getText().isEmpty()) {
            valido = false;
            mensajeError += "- Nombre del tutorado requerido \n";
        }
        
        if (tfApellidoPaterno.getText().isEmpty()) {
            valido = false;
            mensajeError += "- Apellido Paterno del tutorado requerido \n";
        }
        
        if (tfApellidoMaterno.getText().isEmpty()) {
            valido = false;
            mensajeError += "- Apellido Materno del tutorado requerido \n";
        }
        
        if (tfCorreo.getText().isEmpty()) {
            valido = false;
            mensajeError += "- Correo electrónico requerido \n";
        }
        
        if (tfMatricula.getText().isEmpty()) {
            valido = false;
            mensajeError += "- Matrícula del tutorado requerida \n";
        }
        
        if (cbProgramaEducativo.getSelectionModel().getSelectedItem() == null) {
            valido = false;
            mensajeError += "- Programa Educativo requerido \n";
        }

        if (cbEstadoAcademico.getSelectionModel().getSelectedItem() == null) {
            valido = false;
            mensajeError += "- Estado Académico requerido \n";
        }
        
        if (!valido) {
            Utilidades.mostrarAlertaSimple("Campos vacíos", 
                    mensajeError, Alert.AlertType.WARNING);
        }
        return valido;
    }
    
    private void registrarEstudiante() {
        Estudiante estudiante = obtenerEstudiante();
        Respuesta respuesta = EstudianteImp.registrar(estudiante);
        if (!respuesta.isError()) {
            Utilidades.mostrarAlertaSimple("Tutorado registrado exitosamente",
                respuesta.getMensaje(), Alert.AlertType.INFORMATION);
            if (observador != null) {
                observador.notificarOperacionExitosa("registrar", estudiante.getNombreCompleto());
            }
            cerrarVentana();
        } else {
            Utilidades.mostrarAlertaSimple("Error al registrar",
                respuesta.getMensaje(), Alert.AlertType.ERROR);
        }
    }
    
    private void actualizarEstudiante() {
        Estudiante estudianteEdicion = obtenerEstudiante();
        estudianteEdicion.setIdEstudiante(this.estudianteEdicion.getIdEstudiante());
        HashMap<String, Object> resultado = EstudianteImp.actualizar(estudianteEdicion);
        if (!(boolean) resultado.get("error")) {
            Utilidades.mostrarAlertaSimple("Tutorado actualizado exitosamente", 
                    resultado.get("mensaje").toString(), Alert.AlertType.INFORMATION);
            observador.notificarOperacionExitosa("actualizar", estudianteEdicion.getNombreCompleto());
            cerrarVentana();
        } else {
            Utilidades.mostrarAlertaSimple("Error al actualizar",
                    resultado.get("mensaje").toString(), Alert.AlertType.ERROR);
        }
    }
    
    private void cerrarVentana() {
        ((Stage) tfNombre.getScene().getWindow()).close();
    }
    
    private Estudiante obtenerEstudiante() {
        Estudiante estudiante = new Estudiante();
        estudiante.setNombreEstudiante(tfNombre.getText());
        estudiante.setApellidoPaterno(tfApellidoPaterno.getText());
        estudiante.setApellidoMaterno(tfApellidoMaterno.getText());
        estudiante.setMatricula(tfMatricula.getText());
        estudiante.setCorreoInstitucional(tfCorreo.getText());
        ProgramaEducativo programaEducativoSeleccionado = cbProgramaEducativo.getSelectionModel().getSelectedItem();
        estudiante.setIdProgramaEducativo(programaEducativoSeleccionado.getIdPrograma());
        Estudiante.Estatus estadoAcademicoSeleccionado = cbEstadoAcademico.getSelectionModel().getSelectedItem();
        estudiante.setEstatus(estadoAcademicoSeleccionado);
        return estudiante;
    }
    
    private void cargarInformacionProgramasEducativos() {
        HashMap<String, Object> respuesta = CatalogoImp.obtenerProgramasEducativos();
        if (!(boolean) respuesta.get("error")) {
           List<ProgramaEducativo> programasEducativosBD = (List<ProgramaEducativo>) respuesta.get("programas");
            cbProgramaEducativo.setItems(FXCollections.observableArrayList(programasEducativosBD));
        } else {
            Utilidades.mostrarAlertaSimple("Error al cargar los programas educativos",
                    respuesta.get("mensaje").toString(), Alert.AlertType.ERROR);
        }    
    }
}
