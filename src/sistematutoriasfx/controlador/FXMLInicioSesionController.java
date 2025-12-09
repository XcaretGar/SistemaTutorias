/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package sistematutoriasfx.controlador;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sistematutoriasfx.controlador.administrador.FXMLMenuOpcionesAdministradorController;
import sistematutoriasfx.controlador.coordinador.FXMLMenuOpcionesCoordinadorController;
import sistematutoriasfx.controlador.tutor.FXMLPrincipalTutorController;
import sistematutoriasfx.modelo.dao.UsuarioDAO; 
import sistematutoriasfx.modelo.pojo.Usuario; 
//import sistematutoriasfx.controlador.tutor.FXMLPrincipalTutorController;
import sistematutoriasfx.modelo.pojo.Rol;
import utilidad.Utilidades; 

/**
 * FXML Controller class
 *
 * @author Ana Georgina
 */

public class FXMLInicioSesionController implements Initializable {

    @FXML
    private PasswordField pfContraseña;
    @FXML
    private TextField tfUsuario;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    

    @FXML
    private void clicIniciar(ActionEvent event) {
        String usuario = tfUsuario.getText();
        String password = pfContraseña.getText();
        
        if(usuario.isEmpty() || password.isEmpty()){
            Utilidades.mostrarAlertaSimple("Campos requeridos", 
                    "Por favor, ingresa tu usuario y contraseña.", 
                    Alert.AlertType.WARNING);
            return;
        }
        
        validarCredenciales(usuario, password);
    }
    
    private void validarCredenciales(String usuario, String password) {
        // 1. Paso 1: ¿Existe el usuario?
        Usuario usuarioSesion = UsuarioDAO.verificarSesion(usuario, password);
        
        if (usuarioSesion != null) {
            // 2. Paso 2: ¿Qué roles tiene?
            ArrayList<Rol> roles = UsuarioDAO.obtenerRolesDeUsuario(usuarioSesion.getIdUsuario());
            
            if (roles.isEmpty()) {
                Utilidades.mostrarAlertaSimple("Acceso Denegado", 
                        "El usuario no tiene roles asignados en el sistema.", 
                        Alert.AlertType.WARNING);
            } 
            else if (roles.size() == 1) {
                // CASO A: Solo tiene un rol, entra directo
                Rol rolUnico = roles.get(0);
                ingresarAlSistema(usuarioSesion, rolUnico);
            } 
            else {
                // CASO B: Tiene múltiples roles, debe elegir
                seleccionarRol(usuarioSesion, roles);
            }
            
        } else {
            Utilidades.mostrarAlertaSimple("Credenciales incorrectas", 
                    "El usuario o contraseña no son correctos.", 
                    Alert.AlertType.ERROR);
        }
    }
    
    private void seleccionarRol(Usuario usuario, ArrayList<Rol> roles) {
        ChoiceDialog<Rol> dialogo = new ChoiceDialog<>(roles.get(0), roles);
        dialogo.setTitle("Selección de Rol");
        dialogo.setHeaderText("Múltiples roles detectados");
        dialogo.setContentText("Por favor selecciona con qué perfil deseas ingresar:");
        
        dialogo.showAndWait().ifPresent(rolSeleccionado -> {
            ingresarAlSistema(usuario, rolSeleccionado);
        });
    }
    
    private void ingresarAlSistema(Usuario usuario, Rol rol) {
        usuario.setIdRol(rol.getIdRol());
        usuario.setUsername(rol.getNombre());
        Utilidades.mostrarAlertaSimple("Bienvenido", "Ingresando como: " + rol.getNombre(), Alert.AlertType.INFORMATION);
        irPantallaPrincipal(usuario);
    }
    
    private void irPantallaPrincipal(Usuario usuarioLogin) {
        Stage escenarioActual = (Stage) tfUsuario.getScene().getWindow();
        try {
            String ruta = "";
            String titulo = "";
            
            // Redirección según el Rol seleccionado
            switch (usuarioLogin.getIdRol()) {
                case 1: // ADMIN
                    ruta = "/sistematutoriasfx/vista/administrador/FXMLMenuOpcionesAdministrador.fxml";
                    titulo = "Menú Principal - Administrador";
                    break; 
                case 2: // COORDINADOR
                    ruta = "/sistematutoriasfx/vista/coordinador/FXMLMenuOpcionesCoordinador.fxml";
                    titulo = "Menú Principal - Coordinador";
                    break;
                case 3: // TUTOR
                    ruta = "/sistematutoriasfx/vista/tutor/FXMLPrincipalTutor.fxml";
                    titulo = "Menú Principal - Tutor";
                    break;
                default:
                    Utilidades.mostrarAlertaSimple("Error inesperado", "Verifique, por favor.", Alert.AlertType.ERROR);
                    return;
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource(ruta));
            Parent root = loader.load();
            
            if (usuarioLogin.getIdRol() == 1) {
                FXMLMenuOpcionesAdministradorController controlador = loader.getController();
                controlador.configurarVista(usuarioLogin);
            } else if (usuarioLogin.getIdRol() == 2) {
                FXMLMenuOpcionesCoordinadorController controlador = loader.getController();
                controlador.configurarVista(usuarioLogin);
            } else if (usuarioLogin.getIdRol() == 3) {
                FXMLPrincipalTutorController controlador = loader.getController();
                controlador.configurarVista(usuarioLogin);
            }
            
            Scene escena = new Scene(root);
            Stage nuevoEscenario = new Stage();
            nuevoEscenario.setScene(escena);
            nuevoEscenario.setTitle(titulo);
            nuevoEscenario.show();
            escenarioActual.close();
            
        } catch (IOException ex) {
            ex.printStackTrace();
            Utilidades.mostrarAlertaSimple("Error", "No se pudo cargar la ventana: " + ex.getMessage(), Alert.AlertType.ERROR);
        }
    }
}