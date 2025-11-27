/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMain.java to edit this template
 */
package sistematutoriasfx;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
    public void start(Stage primaryStage) {
        Button btn = new Button();
        btn.setText("Say 'Hello World'");
        btn.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Hello World!");
            }
        });
        
        StackPane root = new StackPane();
        root.getChildren().add(btn);
        
        Scene scene = new Scene(root, 300, 250);
        
        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

       //PRUEBA
       System.out.println("--- Probando conexión y Login ---");
        
       // Intenta poner el usuario y contraseña que creaste en MySQL
       String usuarioPrueba = "admin";
       String passwordPrueba = "1234"; 

       Usuario usuarioSesion = UsuarioDAO.verificarSesion(usuarioPrueba, passwordPrueba);
       if (usuarioSesion != null) {
           System.out.println("¡ÉXITO! Bienvenido: " + usuarioSesion.getUsername());
           System.out.println("Tu rol es: " + usuarioSesion.getNombreRol());
       } else {
           System.out.println("ERROR: Usuario o contraseña incorrectos, o falla en la BD.");
        }
    }
}
        
        
    
    

