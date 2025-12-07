/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package sistematutoriasfx.controlador.administrador;

import java.net.URL;
import java.util.ArrayList;
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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sistematutoriasfx.dominio.AcademicoImp;
import sistematutoriasfx.dominio.CatalogoImp;
import sistematutoriasfx.dominio.EstudianteImp;
import sistematutoriasfx.interfaces.IObservador;
import sistematutoriasfx.modelo.pojo.Academico;
import sistematutoriasfx.modelo.pojo.Respuesta;
import sistematutoriasfx.modelo.pojo.Rol;
import sistematutoriasfx.modelo.pojo.Usuario;
import utilidad.Utilidades;

/**
 * FXML Controller class
 *
 * @author JOANA XCARET
 */
public class FXMLFormularioUsuarioController implements Initializable {

    @FXML
    private TextField tfCorreo;
    @FXML
    private TextField tfNoPersonal;
    @FXML
    private ComboBox<Rol> cbTipoUsuario;
    @FXML
    private PasswordField pfContraseña;
    @FXML
    private TextField tfNombre;
    @FXML
    private TextField tfApPaterno;
    @FXML
    private TextField tfApMaterno;
    
    private IObservador observador;
    private Academico academicoEdicion;
    private ObservableList<Rol> tipoUsuarios;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarInformacionTipoUsuarios();
    }    
    
    public void inicializarDatos(IObservador observador, Academico academico) {
        this.observador = observador;
        this.academicoEdicion = academico;
        try {
            if (academico != null) {
                tfNombre.setText(academico.getNombre());
                tfApPaterno.setText(academico.getApellidoPaterno());
                tfApMaterno.setText(academico.getApellidoMaterno());
                tfCorreo.setText(academico.getCorreoInstitucional());
                tfNoPersonal.setText(academico.getNoPersonal());
                tfNoPersonal.setEditable(false);
                pfContraseña.setText(academico.getUsuario().getPassword());
                cbTipoUsuario.setValue(academico.getUsuario().getRoles().get(0));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void clicGuardar(ActionEvent event) {
        if (sonCamposValidos()) {
            if (academicoEdicion == null) {
                registrarUsuario();
            } else {
                actualizarUsuario();
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
            mensajeError += "- Nombre del usuario requerido \n";
        }
        
        if (tfApPaterno.getText().isEmpty()) {
            valido = false;
            mensajeError += "- Apellido Paterno del usuario requerido \n";
        }
        
        if (tfApMaterno.getText().isEmpty()) {
            valido = false;
            mensajeError += "- Apellido Materno del usuario requerido \n";
        }
        
        if (tfCorreo.getText().isEmpty()) {
            valido = false;
            mensajeError += "- Correo electrónico requerido \n";
        }
        
        if (tfNoPersonal.getText().isEmpty()) {
            valido = false;
            mensajeError += "- Número de Personal del usuario requerido \n";
        }
        
        if (cbTipoUsuario.getSelectionModel().getSelectedItem() == null) {
            valido = false;
            mensajeError += "- Tipo de Usuario requerido \n";
        }

        if (pfContraseña.getText().isEmpty()) {
            valido = false;
            mensajeError += "- Contraseña requerida \n";
        }
        
        if (!valido) {
            Utilidades.mostrarAlertaSimple("Campos vacíos", 
                    mensajeError, Alert.AlertType.WARNING);
        }
        return valido;
    }
    
    private void registrarUsuario() {
        Academico academico = obtenerAcademico();
        Respuesta respuesta = AcademicoImp.registrar(academico);
        if (!respuesta.isError()) {
            Utilidades.mostrarAlertaSimple("Usuario registrado exitosamente",
                respuesta.getMensaje(), Alert.AlertType.INFORMATION);
            if (observador != null) {
                observador.notificarOperacionExitosa("registrar", academico.getNombreCompleto());
            }
            cerrarVentana();
        } else {
            Utilidades.mostrarAlertaSimple("Error al registrar",
                respuesta.getMensaje(), Alert.AlertType.ERROR);
        }
    }
    
    private void actualizarUsuario() {
        Academico academicoEdicion = obtenerAcademico();
        academicoEdicion.setIdAcademico(this.academicoEdicion.getIdAcademico());
        HashMap<String, Object> resultado = AcademicoImp.actualizar(academicoEdicion);
        if (!(boolean) resultado.get("error")) {
            Utilidades.mostrarAlertaSimple("Tutorado actualizado exitosamente", 
                    resultado.get("mensaje").toString(), Alert.AlertType.INFORMATION);
            observador.notificarOperacionExitosa("actualizar", academicoEdicion.getNombreCompleto());
            cerrarVentana();
        } else {
            Utilidades.mostrarAlertaSimple("Error al actualizar",
                    resultado.get("mensaje").toString(), Alert.AlertType.ERROR);
        }
    }
    
    private void cerrarVentana() {
        ((Stage) tfNombre.getScene().getWindow()).close();
    }
    
    private Academico obtenerAcademico() {
        Academico academico = new Academico();
        academico.setNombre(tfNombre.getText());
        academico.setApellidoPaterno(tfApPaterno.getText());
        academico.setApellidoMaterno(tfApMaterno.getText());
        academico.setNoPersonal(tfNoPersonal.getText());
        academico.setCorreoInstitucional(tfCorreo.getText());
        
        Usuario usuario = new Usuario();
        usuario.setUsername(tfNoPersonal.getText());
        usuario.setPassword(pfContraseña.getText());
        
        Rol rolSeleccionado = cbTipoUsuario.getSelectionModel().getSelectedItem();
        List<Rol> roles = new ArrayList<>();
        roles.add(rolSeleccionado);
        usuario.setRoles(roles);
        academico.setUsuario(usuario);
        return academico;
    }
    
    private void cargarInformacionTipoUsuarios() {
        HashMap<String, Object> respuesta = CatalogoImp.obtenerTiposUsuarios();
        if (!(boolean) respuesta.get("error")) {
           List<Rol> roles = (List<Rol>) respuesta.get("tiposUsuarios");
            cbTipoUsuario.setItems(FXCollections.observableArrayList(roles));
        } else {
            Utilidades.mostrarAlertaSimple("Error al cargar los tipos de usuario",
                    respuesta.get("mensaje").toString(), Alert.AlertType.ERROR);
        }    
    }
}
