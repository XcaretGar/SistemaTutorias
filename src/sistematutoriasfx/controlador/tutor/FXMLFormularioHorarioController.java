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

public class FXMLFormularioHorarioController implements Initializable {

    @FXML
    private Label lbNombreTutor;
    @FXML
    private ComboBox<PeriodoEscolar> cbPeriodo;
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
    private boolean esEdicion = false; // Bandera para saber si editamos
    private SesionTutoria sesionEdicion; // Objeto original

    // Recibe el ID del tutor desde la tabla
    public void inicializarTutor(int idAcademicoRecibido){
        this.idAcademico = idAcademicoRecibido;
    }

    // NUEVO: Recibe la sesión a editar desde la tabla
    public void inicializarSesionEdicion(SesionTutoria sesion) {
        this.esEdicion = true;
        this.sesionEdicion = sesion;
        
        // Llenar campos simples
        tfLugar.setText(sesion.getLugar());
        taComentarios.setText(sesion.getComentarios());
        
        // Seleccionar Periodo (Buscando por ID)
        for (PeriodoEscolar p : cbPeriodo.getItems()) {
            if (p.getIdPeriodo() == sesion.getIdPeriodo()) {
                cbPeriodo.setValue(p);
                break;
            }
        }
        
        // Forzar carga de fechas y seleccionar la correcta
        cargarFechasOficiales(sesion.getIdPeriodo());
        for (FechasTutoria f : cbFechaBase.getItems()) {
            if (f.getIdFechaTutoria() == sesion.getIdFechaTutoria()) {
                cbFechaBase.setValue(f);
                break;
            }
        }
        
        // Desglosar la hora (Viene "14:30:00")
        try {
            String[] partes = sesion.getHora().split(":");
            int h = Integer.parseInt(partes[0]);
            String m = partes[1];
            
            if (h >= 12) {
                cbAmPm.setValue("PM");
                if (h > 12) h -= 12;
            } else {
                cbAmPm.setValue("AM");
                if (h == 0) h = 12;
            }
            cbHora.setValue(String.format("%02d", h));
            cbMinutos.setValue(m);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarDatosIniciales();
        configurarListeners();
    }    
    
    private void cargarDatosIniciales() {
        cbPeriodo.setItems(FXCollections.observableArrayList(PeriodoEscolarDAO.obtenerPeriodos()));
        
        ObservableList<String> horas = FXCollections.observableArrayList();
        for(int i=1; i<=12; i++) horas.add(i < 10 ? "0"+i : String.valueOf(i));
        cbHora.setItems(horas);
        
        ObservableList<String> minutos = FXCollections.observableArrayList();
        for(int i=0; i<60; i+=10) minutos.add(String.format("%02d", i));
        cbMinutos.setItems(minutos);
        
        cbAmPm.setItems(FXCollections.observableArrayList("AM", "PM"));
    }
    
    private void configurarListeners() {
        cbPeriodo.valueProperty().addListener((obs, oldVal, newVal) -> {
            if(newVal != null) {
                cargarFechasOficiales(newVal.getIdPeriodo());
                lbFechaMostrada.setText("");
            }
        });
        
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
        
        SesionTutoria sesion = new SesionTutoria();
        sesion.setIdAcademico(this.idAcademico);
        sesion.setIdPeriodo(cbPeriodo.getValue().getIdPeriodo());
        sesion.setIdFechaTutoria(cbFechaBase.getValue().getIdFechaTutoria());
        sesion.setHora(horaInicio);
        sesion.setLugar(tfLugar.getText());
        sesion.setComentarios(taComentarios.getText());
        
        boolean exito;
        
        if (esEdicion) {
            // Caso ACTUALIZAR
            sesion.setIdSesion(this.sesionEdicion.getIdSesion());
            // TODO: Asegúrate de tener el método 'actualizarSesion' en tu DAO
            // exito = SesionTutoriaDAO.actualizarSesion(sesion); 
            Utilidades.mostrarAlertaSimple("Aviso", "Falta implementar el UPDATE en el DAO", Alert.AlertType.INFORMATION);
            return; // Quitamos esto cuando implementes el DAO update
        } else {
            // Caso REGISTRAR NUEVO
            exito = SesionTutoriaDAO.registrarSesion(sesion);
        }
        
        if(exito){
            Utilidades.mostrarAlertaSimple("Éxito", "La información se guardó correctamente.", Alert.AlertType.INFORMATION);
            cerrarVentana();
        } else {
            Utilidades.mostrarAlertaSimple("Error", "No se pudo guardar en la base de datos.", Alert.AlertType.ERROR);
        }
    }

    @FXML private void clicCancelar(ActionEvent event) { cerrarVentana(); }
    
    private void cerrarVentana() {
        ((Stage) cbPeriodo.getScene().getWindow()).close();
    }
}