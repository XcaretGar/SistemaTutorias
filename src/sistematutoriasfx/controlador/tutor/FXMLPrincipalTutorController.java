/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package sistematutoriasfx.controlador.tutor;

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
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sistematutoriasfx.modelo.dao.AcademicoDAO;
import sistematutoriasfx.modelo.pojo.Academico;
import sistematutoriasfx.modelo.pojo.Usuario;
import utilidad.Utilidades;
/**
 * FXML Controller class
 *
 * @author Ana Georgina
 */
/*public class FXMLPrincipalTutorController implements Initializable {

    @FXML
    private Label lbNombreTutor;
    private Usuario usuarioSesion;
    private Academico academicoSesion;
    
    /**
     * Initializes the controller class.
     *//*
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    // Método para recibir el usuario del Login y buscar su nombre real
    public void configurarVista(Usuario usuario) {
        this.usuarioSesion = usuario;
        this.academicoSesion = AcademicoDAO.obtenerAcademicoPorIdUsuario(usuario.getIdUsuario());
        
        if(this.academicoSesion != null){
            lbNombreTutor.setText("Tutor: " + this.academicoSesion.getNombreCompleto());
        }else{
            lbNombreTutor.setText("Tutor: " + usuario.getUsername());
        }
    }
    
    @FXML
    private void clicHorarioTutoria(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sistematutoriasfx/vista/tutor/FXMLRegistrarHorarioTutor.fxml"));
            Parent root = loader.load();
            
            FXMLRegistrarHorarioTutorController controlador = loader.getController();
            
            controlador.configurarEscena(this.usuarioSesion);
            
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.setTitle("Gestión de Horarios");
            stage.showAndWait();
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void clicEvidenciaTutoria(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sistematutoriasfx/vista/tutor/FXMLGestionarEvidencia.fxml"));
            Parent root = loader.load();

            FXMLGestionarEvidenciaController controlador = loader.getController();
            controlador.configurarEscena(this.usuarioSesion);

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.setTitle("Gestión de Evidencia");
            stage.showAndWait();
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void clicReporteTutoria(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sistematutoriasfx/vista/tutor/FXMLGestionarReporteTutoria.fxml"));
            Parent root = loader.load();
            FXMLGestionarReporteTutoriaController controlador = loader.getController();

            controlador.configurarEscena(this.usuarioSesion);

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL); // Bloquea el menú de atrás
            stage.setScene(scene);
            stage.setTitle("Gestión de Reportes de Tutoría");
            stage.showAndWait();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void clicAsistenciaTutoria(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sistematutoriasfx/vista/tutor/FXMLAsistenciaTutorados.fxml"));
            Parent root = loader.load();
            FXMLAsistenciaTutoradosController controlador = loader.getController();

            controlador.configurarEscena(this.usuarioSesion);

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL); // Bloquea el menú de atrás
            stage.setScene(scene);
            stage.setTitle("Gestión de Lista de Asistencia");
            stage.showAndWait();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void clicCerrarSesion(ActionEvent event) {
             try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sistematutoriasfx/vista/FXMLInicioSesion.fxml"));
            Parent root = loader.load();
            Stage escenarioBase = new Stage();
            escenarioBase.setScene(new Scene(root));
            escenarioBase.setTitle("Inicio de Sesión");
            escenarioBase.show();
            
            Stage escenarioActual = (Stage) lbNombreTutor.getScene().getWindow();
            escenarioActual.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void clicProblematica(ActionEvent event) {
        //AQUI SE AGREGA EL FXML
    }
}*/