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
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sistematutoriasfx.modelo.dao.FechasTutoriaDAO;
import sistematutoriasfx.modelo.dao.PeriodoEscolarDAO;
import sistematutoriasfx.modelo.dao.SesionTutoriaDAO;
import sistematutoriasfx.modelo.pojo.FechasTutoria;
import sistematutoriasfx.modelo.pojo.PeriodoEscolar;
import sistematutoriasfx.modelo.pojo.SesionTutoria;
import utilidad.Utilidades;

public class FXMLRegistrarHorarioTutorController implements Initializable {

    @FXML
    private Label lbNombreTutor;
    @FXML
    private ComboBox<PeriodoEscolar> cbPeriodo;
    
    // CAMBIO: Este es el combo importante ahora
    @FXML
    private ComboBox<FechasTutoria> cbFechaBase;
    @FXML
    private Label lbFechaMostrada; 
    
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
        cargarDatosIniciales();
        configurarListeners();
    }    
    
    private void cargarDatosIniciales() {
        // 1. Periodos
        cbPeriodo.setItems(FXCollections.observableArrayList(PeriodoEscolarDAO.obtenerPeriodos()));
        
        // 2. Horas (01 - 12)
        ObservableList<String> horas = FXCollections.observableArrayList();
        for(int i=1; i<=12; i++) horas.add(i < 10 ? "0"+i : String.valueOf(i));
        cbHora.setItems(horas);
        
        // 3. Minutos (Intervalos de 10)
        ObservableList<String> minutos = FXCollections.observableArrayList();
        for(int i=0; i<60; i+=10) minutos.add(String.format("%02d", i));
        cbMinutos.setItems(minutos);
        
        cbAmPm.setItems(FXCollections.observableArrayList("AM", "PM"));
    }
    
    private void configurarListeners() {
        // Cuando cambia el periodo -> Cargar las fechas oficiales de ese periodo
        cbPeriodo.valueProperty().addListener((obs, oldVal, newVal) -> {
            if(newVal != null) {
                cargarFechasOficiales(newVal.getIdPeriodo());
                lbFechaMostrada.setText("");
            }
        });
        
        // Cuando selecciona una sesión -> Mostrar la fecha en el label azul
        cbFechaBase.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                lbFechaMostrada.setText(newVal.getFechaSesion());
            }
        });
    }
    
    private void cargarFechasOficiales(int idPeriodo) {
        ObservableList<FechasTutoria> fechas = FXCollections.observableArrayList(
                FechasTutoriaDAO.obtenerFechasPorPeriodo(idPeriodo)
        );
        cbFechaBase.setItems(fechas);
    }

    @FXML
    private void clicGuardar(ActionEvent event) {
        // Validar vacíos (Ahora validamos cbFechaBase, no dpFecha)
        if(cbPeriodo.getValue() == null || cbFechaBase.getValue() == null ||
           cbHora.getValue() == null || cbMinutos.getValue() == null || cbAmPm.getValue() == null || 
           tfLugar.getText().isEmpty()){
            Utilidades.mostrarAlertaSimple("Faltan datos", "Por favor llena todos los campos.", Alert.AlertType.WARNING);
            return;
        }
        
        // Conversión Hora
        String horaSel = cbHora.getValue();
        int horaInt = Integer.parseInt(horaSel);
        if(cbAmPm.getValue().equals("PM") && horaInt != 12) horaInt += 12;
        else if (cbAmPm.getValue().equals("AM") && horaInt == 12) horaInt = 0;
        
        String horaInicio = String.format("%02d:%s:00", horaInt, cbMinutos.getValue());
        
        // Crear objeto para guardar
        SesionTutoria sesion = new SesionTutoria();
        sesion.setIdAcademico(this.idAcademico);
        sesion.setIdPeriodo(cbPeriodo.getValue().getIdPeriodo());
        
        // AQUÍ EL CAMBIO CLAVE: Guardamos el ID de la fecha oficial
        sesion.setIdFechaTutoria(cbFechaBase.getValue().getIdFechaTutoria());
        
        sesion.setHora(horaInicio);
        sesion.setLugar(tfLugar.getText());
        sesion.setComentarios(taComentarios.getText());
        
        boolean exito = SesionTutoriaDAO.registrarSesion(sesion);
        
        if(exito){
            Utilidades.mostrarAlertaSimple("Registro Exitoso", "Horario agendado correctamente.", Alert.AlertType.INFORMATION);
            cerrarVentana();
        } else {
            Utilidades.mostrarAlertaSimple("Error", "No se pudo guardar.", Alert.AlertType.ERROR);
        }
    }

    @FXML private void clicCancelar(ActionEvent event) { cerrarVentana(); }
    
    private void cerrarVentana() {
        // Usamos cualquier componente visual para obtener la ventana y cerrarla
        ((Stage) cbPeriodo.getScene().getWindow()).close();
    }
}