/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package sistematutoriasfx.controlador;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import sistematutoriasfx.modelo.dao.UsuarioDAO; // Asegúrate de que este import sea correcto
import sistematutoriasfx.modelo.pojo.Usuario; // Asegúrate de que este import sea correcto
import utilidad.Utilidades; // Asegúrate de que este import sea correcto

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
    
    // Método temporal para preparar la navegación
    private void irPantallaPrincipal(Usuario usuarioLogin) {
        // Aquí pondremos el código para cerrar esta ventana y abrir la otra
        System.out.println("El usuario " + usuarioLogin.getUsername() + " ha entrado.");
    }
}