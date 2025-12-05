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
public class FXMLPrincipalTutorController implements Initializable {

    @FXML
    private Label lbNombreTutor;
    private Usuario usuarioSesion;
    private Academico academicoSesion;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    // Método para recibir el usuario del Login y buscar su nombre real
    public void configurarVista(Usuario usuario) {
        this.usuarioSesion = usuario;
        
        // Buscamos los datos del Académico en la BD
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
            // 1. Cargar la vista de Registrar Horario
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sistematutoriasfx/vista/FXMLRegistrarHorarioTutor.fxml"));
            Parent root = loader.load();
            
            // 2. Obtener el controlador de la ventana NUEVA
            FXMLRegistrarHorarioTutorController controladorH = loader.getController();
            
            // 3. PASAR EL ID DEL TUTOR (Esta es la parte clave que faltaba)
            if (this.academicoSesion != null) {
                controladorH.inicializarTutor(this.academicoSesion.getIdAcademico());
            } else {
                // Por seguridad, si por alguna razón falló la carga del académico, usamos el del usuario o mandamos error
               Utilidades.mostrarAlertaSimple("Error", "No se identificó al académico.", Alert.AlertType.ERROR);
               return;
            }
            
            // 4. Crear el escenario (ventana)
            Scene escena = new Scene(root);
            Stage escenarioHorario = new Stage();
            escenarioHorario.setScene(escena);
            escenarioHorario.setTitle("Registrar Horario de Tutoría");
            
            // 5. Configurar como MODAL (Bloquea la ventana de atrás)
            escenarioHorario.initModality(Modality.APPLICATION_MODAL);
            
            // 6. Mostrar
            escenarioHorario.showAndWait();
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void clicEvidenciaTutoria(ActionEvent event) {
    }

    @FXML
    private void clicReporteTutoria(ActionEvent event) {
    }

    @FXML
    private void clicAsistenciaTutoria(ActionEvent event) {
    }

    @FXML
    private void clicSalir(ActionEvent event) {
        try {
            // Regresar al Login
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sistematutoriasfx/vista/FXMLInicioSesion.fxml"));
            Parent root = loader.load();
            Stage escenarioBase = new Stage();
            escenarioBase.setScene(new Scene(root));
            escenarioBase.setTitle("Inicio de Sesión");
            escenarioBase.show();
            
            // Cerrar ventana actual
            Stage escenarioActual = (Stage) lbNombreTutor.getScene().getWindow();
            escenarioActual.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
