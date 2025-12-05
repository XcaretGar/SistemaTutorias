/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package sistematutoriasfx.controlador;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sistematutoriasfx.modelo.dao.UsuarioDAO; 
import sistematutoriasfx.modelo.pojo.Usuario; 
import sistematutoriasfx.controlador.tutor.FXMLPrincipalTutorController;
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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }    

    @FXML
    private void clicIniciar(ActionEvent event) {
        //Obtener lo que escribió el usuario
        String usuario = tfUsuario.getText();
        String password = pfContraseña.getText();
        
        //Validar que no estén vacíos
        if(usuario.isEmpty() || password.isEmpty()){
            Utilidades.mostrarAlertaSimple("Campos requeridos", 
                    "Por favor, ingresa tu usuario y contraseña.", 
                    Alert.AlertType.WARNING);
            return; // Detenemos la ejecución aquí si faltan datos
        }
        //Llamar al método auxiliar para verificar en la BD
        validarCredenciales(usuario, password);
    }
    
    private void validarCredenciales(String usuario, String password) {
        // Llamamos al DAO (quien hace la consulta SQL)
        Usuario usuarioSesion = UsuarioDAO.verificarSesion(usuario, password);
        
        if (usuarioSesion != null) {
            Utilidades.mostrarAlertaSimple("Bienvenido", 
                    "Bienvenido al sistema: " + usuarioSesion.getUsername(), 
                    Alert.AlertType.INFORMATION);
            
            // TODO: AQUÍ CODIFICAREMOS EL CAMBIO DE PANTALLA (MENÚ PRINCIPAL) EN EL SIGUIENTE PASO
            irPantallaPrincipal(usuarioSesion);            
        } else {
            Utilidades.mostrarAlertaSimple("Credenciales incorrectas", 
                    "El usuario o contraseña no son correctos, favor de verificar.", 
                    Alert.AlertType.ERROR);
        }
    }
    
    private void irPantallaPrincipal(Usuario usuarioLogin) {
        Stage escenarioActual = (Stage) tfUsuario.getScene().getWindow();
        
        try {
            String ruta = "";
            String titulo = "";
            
            // Validamos el rol para saber qué pantalla abrir
            // 1: Admin, 2: Coordinador, 3: Tutor
            switch (usuarioLogin.getIdRol()) {
                case 1:
                    // ruta = "/sistematutoriasfx/vista/FXMLPrincipalAdmin.fxml";
                    Utilidades.mostrarAlertaSimple("Pendiente", "La vista de Administrador aún no está creada.", Alert.AlertType.INFORMATION);
                    return; 
                    
                case 2:
                    // ruta = "/sistematutoriasfx/vista/FXMLPrincipalCoordinador.fxml";
                    Utilidades.mostrarAlertaSimple("Pendiente", "La vista de Coordinador aún no está creada.", Alert.AlertType.INFORMATION);
                    return; 
                    
                case 3: // TUTOR
                    ruta = "/sistematutoriasfx/vista/FXMLPrincipalTutor.fxml";
                    titulo = "Menú Principal - Tutor";
                    break;
                    
                default:
                    Utilidades.mostrarAlertaSimple("Error", "Rol no reconocido en el sistema.", Alert.AlertType.ERROR);
                    return;
            }

            // 1. Cargamos el archivo FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource(ruta));
            Parent root = loader.load();
            
            // 2. Configuración específica por Rol
            // Si es Tutor, obtenemos su controlador y le pasamos el usuario para que busque el nombre
            if (usuarioLogin.getIdRol() == 3) {
                FXMLPrincipalTutorController controlador = loader.getController();
                controlador.configurarVista(usuarioLogin);
            }
            
            // 3. Mostramos la nueva ventana
            Scene escena = new Scene(root);
            Stage nuevoEscenario = new Stage();
            nuevoEscenario.setScene(escena);
            nuevoEscenario.setTitle(titulo);
            nuevoEscenario.show();
            
            // 4. Cerramos la ventana de Login
            escenarioActual.close();
            
        } catch (IOException ex) {
            ex.printStackTrace();
            Utilidades.mostrarAlertaSimple("Error de navegación", 
                    "No se pudo cargar la ventana principal: " + ex.getMessage(), 
                    Alert.AlertType.ERROR);
        }
    }
}