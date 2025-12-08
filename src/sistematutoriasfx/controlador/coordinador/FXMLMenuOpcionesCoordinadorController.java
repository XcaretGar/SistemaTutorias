/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package sistematutoriasfx.controlador.coordinador;

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
public class FXMLMenuOpcionesCoordinadorController implements Initializable {
    
    @FXML
    private Label lbNombreCoordinador;
    @FXML
    private Usuario usuarioSesion;
    @FXML
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
        
        if(this.academicoSesion != null){
            lbNombreCoordinador.setText("Coordinador: " + this.academicoSesion.getNombreCompleto());
        }else{
            lbNombreCoordinador.setText("Coordinador: " + usuario.getUsername());
        }
    }

    @FXML
    private void clicAsignarTutorado(ActionEvent event) {
        try {
            Parent vista = FXMLLoader.load(
                    SistemaTutoriasFx.class.getResource("/sistematutoriasfx/vista/coordinador/FXMLAsignarTutorado.fxml")); 
            Scene escenaGestionarTutorado = new Scene(vista);
            Stage stAdmin = new Stage();
            stAdmin.setScene(escenaGestionarTutorado);
            stAdmin.setTitle("Asignar Tutorado");
            stAdmin.initModality(Modality.APPLICATION_MODAL);
            stAdmin.showAndWait();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void clicRegistrarFechaTutoria(ActionEvent event) {
         try {
            Parent vista = FXMLLoader.load(
                    SistemaTutoriasFx.class.getResource("/sistematutoriasfx/vista/coordinador/FXMLRegistrarFechaTutoria.fxml")); 
            Scene escenaGestionarTutorado = new Scene(vista);
            Stage stAdmin = new Stage();
            stAdmin.setScene(escenaGestionarTutorado);
            stAdmin.setTitle("Asignar Tutorado");
            stAdmin.initModality(Modality.APPLICATION_MODAL);
            stAdmin.showAndWait();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void clicRevisarReporteTutoria(ActionEvent event) {
         try {
            Parent vista = FXMLLoader.load(
                    SistemaTutoriasFx.class.getResource("/sistematutoriasfx/vista/coordinador/FXMLRevisarReporteTutoria.fxml")); 
            Scene escenaGestionarTutorado = new Scene(vista);
            Stage stAdmin = new Stage();
            stAdmin.setScene(escenaGestionarTutorado);
            stAdmin.setTitle("Asignar Tutorado");
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
            Stage stPrincipal = (Stage) lbNombreCoordinador.getScene().getWindow();
            stPrincipal.setScene(escena);
            stPrincipal.setTitle("Iniciar Sesión");
            stPrincipal.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

