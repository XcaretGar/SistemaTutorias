/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package sistematutoriasfx.controlador.tutor;

/**
 * FXML Controller class
 *
 * @author Ana Georgina
 */

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sistematutoriasfx.modelo.dao.PeriodoEscolarDAO;
import sistematutoriasfx.modelo.dao.SesionTutoriaDAO;
import sistematutoriasfx.modelo.pojo.PeriodoEscolar;
import sistematutoriasfx.modelo.pojo.SesionTutoria;
import utilidad.Utilidades;

public class FXMLRegistrarHorarioTutorController implements Initializable {

    @FXML
    private Label lbNombreTutor;
    @FXML
    private ComboBox<Integer> cbNoSesion;
    @FXML
    private ComboBox<PeriodoEscolar> cbPeriodo;
    @FXML
    private DatePicker dpFecha;
    @FXML
    private ComboBox<String> cbHora;
    @FXML
    private ComboBox<String> cbMinutos;
    @FXML
    private ComboBox<String> cbAmPm;
    @FXML
    private TextField tfLugar;
    @FXML
    private TextArea taComentarios;
    
    private int idAcademico = 0; 

    public void inicializarTutor(int idAcademicoRecibido){
        this.idAcademico = idAcademicoRecibido;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarDatos();
    }    
    
    private void cargarDatos() {
        cbPeriodo.setItems(FXCollections.observableArrayList(PeriodoEscolarDAO.obtenerPeriodos()));
        cbNoSesion.setItems(FXCollections.observableArrayList(1, 2, 3));
        
        // Horas (01 - 12)
        ObservableList<String> horas = FXCollections.observableArrayList();
        for(int i=1; i<=12; i++) {
            horas.add(i < 10 ? "0"+i : String.valueOf(i));
        }
        cbHora.setItems(horas);
        
        // --- CAMBIO: MINUTOS DE 10 EN 10 ---
        ObservableList<String> minutos = FXCollections.observableArrayList();
        for(int i=0; i<60; i+=10){
            minutos.add(String.format("%02d", i)); // 00, 10, 20, 30, 40, 50
        }
        cbMinutos.setItems(minutos);
        
        cbAmPm.setItems(FXCollections.observableArrayList("AM", "PM"));
    }

    @FXML
    private void clicGuardar(ActionEvent event) {
        if(cbNoSesion.getValue() == null || cbPeriodo.getValue() == null || dpFecha.getValue() == null ||
           cbHora.getValue() == null || cbMinutos.getValue() == null || cbAmPm.getValue() == null || 
           tfLugar.getText().isEmpty()){
            Utilidades.mostrarAlertaSimple("Faltan datos", "Por favor llena todos los campos.", Alert.AlertType.WARNING);
            return;
        }
        
        // Conversión AM/PM a 24 Horas
        String horaSel = cbHora.getValue();
        String minutoSel = cbMinutos.getValue();
        String ampmSel = cbAmPm.getValue();
        int horaInt = Integer.parseInt(horaSel);
        
        if(ampmSel.equals("PM") && horaInt != 12){
            horaInt += 12; 
        } else if (ampmSel.equals("AM") && horaInt == 12){
            horaInt = 0;
        }
        
        // Formato para MySQL (HH:mm:ss)
        String hora = String.format("%02d:%s:00", horaInt, minutoSel);
        
        // Crear objeto 
        SesionTutoria sesion = new SesionTutoria();
        sesion.setIdPeriodo(cbPeriodo.getValue().getIdPeriodo());
        sesion.setNumSesion(cbNoSesion.getValue());
        sesion.setFecha(dpFecha.getValue().toString());
        sesion.setHora(hora);
        sesion.setLugar(tfLugar.getText());
        sesion.setComentarios(taComentarios.getText());
        sesion.setIdAcademico(this.idAcademico); // Usamos el ID real recibido
        
        boolean exito = SesionTutoriaDAO.registrarSesion(sesion);
        
        if(exito){
            Utilidades.mostrarAlertaSimple("Registro Exitoso", "La sesión de tutoría ha sido agendada.", Alert.AlertType.INFORMATION);
            cerrarVentana();
        } else {
            Utilidades.mostrarAlertaSimple("Error", "No se pudo guardar la sesión en la base de datos.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void clicSalir(ActionEvent event) {
        cerrarVentana();
    }
    
    @FXML
    private void clicCancelar(ActionEvent event) {
        cerrarVentana(); 
    }
    
    private void cerrarVentana() {
        Stage escenario = (Stage) cbNoSesion.getScene().getWindow();
        escenario.close();
    }
}