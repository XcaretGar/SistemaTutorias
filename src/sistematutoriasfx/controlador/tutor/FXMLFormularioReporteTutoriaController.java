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
import sistematutoriasfx.modelo.dao.ReporteTutoriaDAO;
import sistematutoriasfx.modelo.dao.SesionTutoriaDAO;
import sistematutoriasfx.modelo.pojo.FechasTutoria;
import sistematutoriasfx.modelo.pojo.PeriodoEscolar;
import sistematutoriasfx.modelo.pojo.ReporteTutoria;
import sistematutoriasfx.modelo.pojo.SesionTutoria;
import utilidad.Utilidades;

public class FXMLFormularioReporteTutoriaController implements Initializable {

    @FXML private ComboBox<PeriodoEscolar> cbPeriodoReporte;
    @FXML private ComboBox<FechasTutoria> cbFechaSesionReporte;
    @FXML private Label lbEstatusReporte;
    @FXML private TextField tfTotalRiesgo;
    @FXML private TextField tfTotalAsistentes;
    @FXML private TextArea taComentarios;

    private int idAcademico;
    private boolean esEdicion = false;
    private ReporteTutoria reporteEdicion;

    // Recibe el ID del tutor desde la ventana anterior
    public void inicializarTutor(int idAcademico) {
        this.idAcademico = idAcademico;
    }

    // Configura la ventana si es una EDICIÓN
    public void inicializarEdicion(ReporteTutoria reporte) {
        this.esEdicion = true;
        this.reporteEdicion = reporte;
        
        // Llenar campos de texto
        taComentarios.setText(reporte.getComentariosGenerales());
        tfTotalAsistentes.setText(String.valueOf(reporte.getTotalAsistentes()));
        tfTotalRiesgo.setText(String.valueOf(reporte.getTotalEnRiesgo()));
        lbEstatusReporte.setText("Estatus: " + reporte.getEstatus());
        
        // Seleccionar el periodo correcto en el combo
        // Nota: Esto disparará el listener que carga las fechas
        for (PeriodoEscolar p : cbPeriodoReporte.getItems()) {
            if (p.getIdPeriodo() == reporte.getIdPeriodo()) {
                cbPeriodoReporte.setValue(p);
                break;
            }
        }
        
        // IMPORTANTE: Forzamos la carga de fechas y seleccionamos la correcta
        // (Porque el listener a veces no es instantáneo al setear valor por código)
        cargarFechasDelPeriodo(reporte.getIdPeriodo());
        
        // Buscamos la fecha que coincida con la sesión del reporte
        // Necesitamos saber el idFechaTutoria. Lo podemos sacar indirectamente 
        // o asumiendo que el objeto reporte ya trae esa info si hicimos el JOIN correcto.
        // Por simplicidad visual, seleccionamos por texto o id si lo tenemos disponible.
        // Aquí asumiremos que el usuario selecciona la fecha si no se carga auto, 
        // o idealmente haríamos un método extra para buscar la fecha por idSesion.
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarCombos();
        configurarListeners();
    }
    
    private void cargarCombos() {
        cbPeriodoReporte.setItems(FXCollections.observableArrayList(PeriodoEscolarDAO.obtenerPeriodos()));
    }
    
    private void configurarListeners() {
        cbPeriodoReporte.valueProperty().addListener((obs, oldVal, newVal) -> {
            if(newVal != null) {
                cargarFechasDelPeriodo(newVal.getIdPeriodo());
            }
        });
        
        // Al seleccionar fecha, podríamos buscar si ya hay datos previos (opcional)
    }
    
    private void cargarFechasDelPeriodo(int idPeriodo) {
        cbFechaSesionReporte.setItems(FXCollections.observableArrayList(
            FechasTutoriaDAO.obtenerFechasPorPeriodo(idPeriodo)
        ));
    }

    @FXML
    private void clicGuardar(ActionEvent event) {
        // 1. Validaciones
        if (cbPeriodoReporte.getValue() == null || cbFechaSesionReporte.getValue() == null) {
            Utilidades.mostrarAlertaSimple("Faltan datos", "Por favor selecciona el periodo y la fecha de sesión.", Alert.AlertType.WARNING);
            return;
        }
        
        try {
            // 2. Preparar objeto
            ReporteTutoria reporte = new ReporteTutoria();
            reporte.setComentariosGenerales(taComentarios.getText());
            
            // Validar que sean números
            int asistentes = tfTotalAsistentes.getText().isEmpty() ? 0 : Integer.parseInt(tfTotalAsistentes.getText());
            int riesgo = tfTotalRiesgo.getText().isEmpty() ? 0 : Integer.parseInt(tfTotalRiesgo.getText());
            
            reporte.setTotalAsistentes(asistentes);
            reporte.setTotalEnRiesgo(riesgo);
            
            boolean exito;
            
            if(esEdicion) {
                // --- ACTUALIZAR ---
                reporte.setIdReporte(reporteEdicion.getIdReporte());
                exito = ReporteTutoriaDAO.actualizarReporte(reporte);
            } else {
                // --- REGISTRAR NUEVO ---
                reporte.setIdAcademico(idAcademico);
                reporte.setIdPeriodo(cbPeriodoReporte.getValue().getIdPeriodo());
                
                // Buscar el idSesion real basado en la fecha seleccionada
                int idSesionReal = obtenerIdSesion(cbFechaSesionReporte.getValue().getIdFechaTutoria());
                
                if(idSesionReal == 0) {
                    Utilidades.mostrarAlertaSimple("Error de Lógica", 
                        "No has registrado un horario de tutoría para esta fecha.\n"
                        + "Primero ve a 'Horarios de Tutoría' y registra tu sesión.", 
                        Alert.AlertType.ERROR);
                    return;
                }
                reporte.setIdSesion(idSesionReal);
                
                // Validar que no exista ya un reporte para esa sesión (para no duplicar)
                if(ReporteTutoriaDAO.obtenerReportePorSesion(idAcademico, cbFechaSesionReporte.getValue().getIdFechaTutoria()) != null){
                     Utilidades.mostrarAlertaSimple("Duplicado", "Ya existe un reporte para esta sesión. Úsalo en modo edición.", Alert.AlertType.WARNING);
                     return;
                }

                exito = ReporteTutoriaDAO.registrarReporte(reporte);
            }
            
            if(exito) {
                Utilidades.mostrarAlertaSimple("Éxito", "El reporte se guardó correctamente.", Alert.AlertType.INFORMATION);
                cerrarVentana();
            } else {
                Utilidades.mostrarAlertaSimple("Error", "No se pudo guardar en la base de datos.", Alert.AlertType.ERROR);
            }
            
        } catch (NumberFormatException e) {
             Utilidades.mostrarAlertaSimple("Error de formato", "Los totales deben ser números enteros.", Alert.AlertType.WARNING);
        } catch (Exception e) {
             e.printStackTrace();
             Utilidades.mostrarAlertaSimple("Error", "Ocurrió un error inesperado.", Alert.AlertType.ERROR);
        }
    }
    
    // Método auxiliar para obtener el ID de la Sesión (Cita) basado en la Fecha Oficial
    private int obtenerIdSesion(int idFechaTutoria) {
         java.util.ArrayList<SesionTutoria> sesiones = 
                SesionTutoriaDAO.obtenerSesionesPorTutor(idAcademico);
        for (SesionTutoria s : sesiones) {
            if (s.getIdFechaTutoria() == idFechaTutoria) {
                return s.getIdSesion();
            }
        }
        return 0; // 0 significa que el tutor NO agendó cita para esa fecha
    }

    @FXML
    private void clicCancelar(ActionEvent event) {
        cerrarVentana();
    }
    
    private void cerrarVentana() {
        ((Stage) cbPeriodoReporte.getScene().getWindow()).close();
    }
}