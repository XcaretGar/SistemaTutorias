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
import sistematutoriasfx.modelo.dao.ListaAsistenciaDAO;
import sistematutoriasfx.modelo.dao.PeriodoEscolarDAO;
import sistematutoriasfx.modelo.dao.ReporteTutoriaDAO;
import sistematutoriasfx.modelo.dao.SesionTutoriaDAO;
import sistematutoriasfx.modelo.pojo.FechasTutoria;
import sistematutoriasfx.modelo.pojo.ListaAsistencia;
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

    public void inicializarTutor(int idAcademico) {
        this.idAcademico = idAcademico;
    }

    public void inicializarEdicion(ReporteTutoria reporte) {
        this.esEdicion = true;
        this.reporteEdicion = reporte;
        
        taComentarios.setText(reporte.getComentariosGenerales());
        tfTotalAsistentes.setText(String.valueOf(reporte.getTotalAsistentes()));
        tfTotalRiesgo.setText(String.valueOf(reporte.getTotalEnRiesgo()));
        lbEstatusReporte.setText("Estatus: " + reporte.getEstatus());
        
        // Seleccionar periodo
        for (PeriodoEscolar p : cbPeriodoReporte.getItems()) {
            if (p.getIdPeriodo() == reporte.getIdPeriodo()) {
                cbPeriodoReporte.setValue(p);
                break;
            }
        }
        
        cargarFechasDelPeriodo(reporte.getIdPeriodo());
        
        // Seleccionar fecha correspondiente
        int idFechaDelReporte = obtenerIdFechaPorSesion(reporte.getIdSesion());
        for (FechasTutoria f : cbFechaSesionReporte.getItems()) {
            if (f.getIdFechaTutoria() == idFechaDelReporte) {
                cbFechaSesionReporte.setValue(f);
                break;
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarCombos();
        configurarListeners();
        
        tfTotalAsistentes.setEditable(false);
        tfTotalRiesgo.setEditable(false);
    }
    
    private void cargarCombos() {
        cbPeriodoReporte.setItems(FXCollections.observableArrayList(PeriodoEscolarDAO.obtenerPeriodos()));
    }
    
    private void configurarListeners() {
        cbPeriodoReporte.valueProperty().addListener((obs, oldVal, newVal) -> {
            if(newVal != null) {
                cargarFechasDelPeriodo(newVal.getIdPeriodo());
                // Limpiar campos al cambiar periodo
                tfTotalAsistentes.setText("0");
                tfTotalRiesgo.setText("0");
                lbEstatusReporte.setText("Estatus: ---");
            }
        });
        
        cbFechaSesionReporte.valueProperty().addListener((obs, oldVal, newVal) -> {
            if(newVal != null) {
                cargarTotalesDeAsistencia();
            }
        });
    }
    
    private void cargarFechasDelPeriodo(int idPeriodo) {
        cbFechaSesionReporte.setItems(FXCollections.observableArrayList(
            FechasTutoriaDAO.obtenerFechasPorPeriodo(idPeriodo)
        ));
    }

    private void cargarTotalesDeAsistencia() {
        if(cbFechaSesionReporte.getValue() == null) {
            return;
        }
        
        int idSesionReal = obtenerIdSesion(cbFechaSesionReporte.getValue().getIdFechaTutoria());
        
        if(idSesionReal == 0) {
            tfTotalAsistentes.setText("0");
            tfTotalRiesgo.setText("0");
            lbEstatusReporte.setText("Estatus: Sin horario");
            return;
        }
        
        // Obtener la lista de asistencia
        java.util.ArrayList<ListaAsistencia> asistencias = 
            ListaAsistenciaDAO.obtenerAsistenciaPorSesion(idSesionReal);
        
        if(asistencias == null || asistencias.isEmpty()) {
            tfTotalAsistentes.setText("0");
            tfTotalRiesgo.setText("0");
            lbEstatusReporte.setText("Estatus: Sin asistencia");
            return;
        }
        
        int totalAsistieron = 0;
        int totalEnRiesgo = 0;
        
        for(ListaAsistencia asist : asistencias) {
            if(asist.isAsistio()) {
                totalAsistieron++;
            }
            if(asist.isRiesgoDetectado()) {
                totalEnRiesgo++;
            }
        }
        
        tfTotalAsistentes.setText(String.valueOf(totalAsistieron));
        tfTotalRiesgo.setText(String.valueOf(totalEnRiesgo));
        
        // Verificar si ya existe un reporte
        ReporteTutoria reporteExistente = ReporteTutoriaDAO.obtenerReportePorSesion(idAcademico, 
            cbFechaSesionReporte.getValue().getIdFechaTutoria());
        
        if(reporteExistente != null) {
            lbEstatusReporte.setText("Estatus: Ya existe reporte");
        } else {
            lbEstatusReporte.setText("Estatus: Listo para crear");
        }
    }

    @FXML
    private void clicGuardar(ActionEvent event) {
        if (cbPeriodoReporte.getValue() == null || cbFechaSesionReporte.getValue() == null) {
            Utilidades.mostrarAlertaSimple("Faltan datos", 
                "Por favor selecciona el periodo y la fecha de sesión.", 
                Alert.AlertType.WARNING);
            return;
        }
        
        int asistentes = Integer.parseInt(tfTotalAsistentes.getText());
        if(asistentes == 0 && !esEdicion) {
            Utilidades.mostrarAlertaSimple("Sin asistencia", 
                "No puedes crear un reporte sin haber registrado asistencia primero.\n" +
                "Ve a 'Gestionar Asistencia' y registra la lista primero.", 
                Alert.AlertType.WARNING);
            return;
        }
        
        try {
            ReporteTutoria reporte = new ReporteTutoria();
            reporte.setComentariosGenerales(taComentarios.getText());
            
            reporte.setTotalAsistentes(Integer.parseInt(tfTotalAsistentes.getText()));
            reporte.setTotalEnRiesgo(Integer.parseInt(tfTotalRiesgo.getText()));
            
            boolean exito;
            
            if(esEdicion) {
                // ACTUALIZAR
                reporte.setIdReporte(reporteEdicion.getIdReporte());
                exito = ReporteTutoriaDAO.actualizarReporte(reporte);
            } else {
                // REGISTRAR NUEVO
                reporte.setIdAcademico(idAcademico);
                reporte.setIdPeriodo(cbPeriodoReporte.getValue().getIdPeriodo());
                
                int idSesionReal = obtenerIdSesion(cbFechaSesionReporte.getValue().getIdFechaTutoria());
                
                if(idSesionReal == 0) {
                    Utilidades.mostrarAlertaSimple("Error de Lógica", 
                        "No has registrado un horario de tutoría para esta fecha.\n" +
                        "Primero ve a 'Horarios de Tutoría' y registra tu sesión.", 
                        Alert.AlertType.ERROR);
                    return;
                }
                reporte.setIdSesion(idSesionReal);
                
                // Validar duplicados
                if(ReporteTutoriaDAO.obtenerReportePorSesion(idAcademico, 
                    cbFechaSesionReporte.getValue().getIdFechaTutoria()) != null) {
                    Utilidades.mostrarAlertaSimple("Duplicado", 
                        "Ya existe un reporte para esta sesión. Úsalo en modo edición.", 
                        Alert.AlertType.WARNING);
                    return;
                }

                exito = ReporteTutoriaDAO.registrarReporte(reporte);
            }
            
            if(exito) {
                Utilidades.mostrarAlertaSimple("Éxito", 
                    "El reporte se guardó correctamente.", 
                    Alert.AlertType.INFORMATION);
                cerrarVentana();
            } else {
                Utilidades.mostrarAlertaSimple("Error", 
                    "No se pudo guardar en la base de datos.", 
                    Alert.AlertType.ERROR);
            }
            
        } catch (NumberFormatException e) {
            Utilidades.mostrarAlertaSimple("Error de formato", 
                "Los totales deben ser números enteros.", 
                Alert.AlertType.WARNING);
        } catch (Exception e) {
            e.printStackTrace();
            Utilidades.mostrarAlertaSimple("Error", 
                "Ocurrió un error inesperado.", 
                Alert.AlertType.ERROR);
        }
    }
    
    private int obtenerIdSesion(int idFechaTutoria) {
        java.util.ArrayList<SesionTutoria> sesiones = 
            SesionTutoriaDAO.obtenerSesionesPorTutor(idAcademico);
        for (SesionTutoria s : sesiones) {
            if (s.getIdFechaTutoria() == idFechaTutoria) {
                return s.getIdSesion();
            }
        }
        return 0;
    }
    
    private int obtenerIdFechaPorSesion(int idSesion) {
        java.util.ArrayList<SesionTutoria> sesiones = 
            SesionTutoriaDAO.obtenerSesionesPorTutor(idAcademico);
        for (SesionTutoria s : sesiones) {
            if (s.getIdSesion() == idSesion) {
                return s.getIdFechaTutoria();
            }
        }
        return 0;
    }

    @FXML
    private void clicCancelar(ActionEvent event) {
        cerrarVentana();
    }
    
    private void cerrarVentana() {
        ((Stage) cbPeriodoReporte.getScene().getWindow()).close();
    }
}