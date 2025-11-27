/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMain.java to edit this template
 */
package sistematutoriasfx;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import sistematutoriasfx.modelo.dao.UsuarioDAO;
import sistematutoriasfx.modelo.pojo.Usuario;

/**
 *
 * @author JOANA XCARET
 */
public class SistemaTutoriasFx extends Application {
    
@Override
    public void start(Stage stage) throws Exception {
        // Asegúrate que la ruta al FXML sea correcta. 
        // Si tu FXML está en el paquete 'vista', la ruta es así:
        Parent root = FXMLLoader.load(getClass().getResource("vista/FXMLInicioSesion.fxml"));
        
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
      launch(args);
    }
}
        
        
    
    

