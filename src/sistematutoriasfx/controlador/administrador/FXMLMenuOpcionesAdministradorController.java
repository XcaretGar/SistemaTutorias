/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package sistematutoriasfx.controlador.administrador;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sistematutoriasfx.SistemaTutoriasFx;
import sistematutoriasfx.modelo.dao.AcademicoDAO;
import sistematutoriasfx.modelo.pojo.Academico;
import sistematutoriasfx.modelo.pojo.Usuario;

/**
 * FXML Controller class
 *
 * @author JOANA XCARET
 */
public class FXMLMenuOpcionesAdministradorController implements Initializable {
    
    @FXML
    private Label lbNombreAdministrador;
    private Usuario usuarioSesion;
    private Academico academicoSesion;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }   
    
    public void configurarVista(Usuario usuario) {
        this.usuarioSesion = usuario;

        // Buscamos los datos del Académico en la BD
        this.academicoSesion = AcademicoDAO.obtenerAcademicoPorIdUsuario(usuario.getIdUsuario());

        if (this.academicoSesion != null) {
            lbNombreAdministrador.setText("Administrador: " + this.academicoSesion.getNombreCompleto());
        } else {
            lbNombreAdministrador.setText("Administrador: " + usuario.getUsername());
        }
    }

    @FXML
    private void clicGestionarTutorado(ActionEvent event) {
        try {
            Parent vista = FXMLLoader.load(
                    SistemaTutoriasFx.class.getResource("/sistematutoriasfx/vista/administrador/FXMLGestionarTutorado.fxml")); 
            Scene escenaGestionarTutorado = new Scene(vista);
            Stage stAdmin = new Stage();
            stAdmin.setScene(escenaGestionarTutorado);
            stAdmin.setTitle("Gestionar Tutorado");
            stAdmin.initModality(Modality.APPLICATION_MODAL);
            stAdmin.showAndWait();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void clicGestionarUsuario(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sistematutoriasfx/vista/administrador/FXMLGestionarUsuario.fxml"));
            Parent vista = loader.load();
            FXMLGestionarUsuarioController controlador = loader.getController();
            controlador.configurarVista(this.usuarioSesion);
            Scene escenaGestionarUsuario = new Scene(vista);
            Stage stAdmin = new Stage();
            stAdmin.setScene(escenaGestionarUsuario);
            stAdmin.setTitle("Gestionar Usuario");
            stAdmin.initModality(Modality.APPLICATION_MODAL);
            stAdmin.showAndWait();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    } 
    
    @FXML
    private void clicCerrarSesion(ActionEvent event) {
        try {
            Parent vista = FXMLLoader.load(
                 SistemaTutoriasFx.class.getResource("vista/FXMLInicioSesion.fxml")); 
            Scene escena = new Scene(vista);
            Stage stPrincipal = (Stage) lbNombreAdministrador.getScene().getWindow();
            stPrincipal.setScene(escena);
            stPrincipal.setTitle("Iniciar Sesión");
            stPrincipal.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
