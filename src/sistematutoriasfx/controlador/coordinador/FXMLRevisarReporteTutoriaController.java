/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package sistematutoriasfx.controlador.coordinador;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import sistematutoriasfx.modelo.dao.ReporteTutoriaDAO;
import sistematutoriasfx.modelo.pojo.ReporteTutoria;

/**
 * FXML Controller class
 *
 * @author JOANA XCARET
 */
public class FXMLRevisarReporteTutoriaController implements Initializable {

    @FXML
    private TextField tfBuscar;
    @FXML
    private TableView<ReporteTutoria> tvRevisarReportes;
    @FXML
    private TableColumn colPeriodo;
    @FXML
    private TableColumn colFechaSesion;
    @FXML
    private TableColumn colSesion;
    @FXML
    private TableColumn colEstatus;
    @FXML
    private TableColumn colAsistentes;
    @FXML
    private TableColumn colEnRiesgo;
    @FXML
    private TableColumn colComentarios;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarColumnas();
        cargarReportes();
    }

    private void configurarColumnas() {
        colPeriodo.setCellValueFactory(new PropertyValueFactory("periodoNombre"));
        colFechaSesion.setCellValueFactory(new PropertyValueFactory("fechaSesion"));
        colSesion.setCellValueFactory(new PropertyValueFactory("numSesion"));
        colEstatus.setCellValueFactory(new PropertyValueFactory("estatus"));
        colAsistentes.setCellValueFactory(new PropertyValueFactory("totalAsistentes"));
        colEnRiesgo.setCellValueFactory(new PropertyValueFactory("totalEnRiesgo"));
        colComentarios.setCellValueFactory(new PropertyValueFactory("comentariosGenerales"));
    }

    private void cargarReportes() {
        tvRevisarReportes.setItems(FXCollections.observableArrayList(
            ReporteTutoriaDAO.obtenerTodosLosReportes()
        ));
    }
    
    @FXML
    private void clicVerReporte(ActionEvent event) {
        ReporteTutoria seleccionado = tvRevisarReportes.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta("Selecciona un reporte para ver.", Alert.AlertType.WARNING);
            return;
        }

        if (seleccionado.getEstatus().equals("Revisado")) {
            mostrarAlerta("Este reporte ya ha sido revisado.", Alert.AlertType.INFORMATION);
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("========================================\n");
        sb.append("    REPORTE DE TUTORÍA\n");
        sb.append("========================================\n\n");

        sb.append("INFORMACIÓN GENERAL:\n");
        sb.append("----------------------------------------\n");
        sb.append("Período:          ").append(seleccionado.getPeriodoNombre()).append("\n");
        sb.append("Fecha de Sesión:  ").append(seleccionado.getFechaSesion()).append("\n");
        sb.append("Número de Sesión: ").append(seleccionado.getNumSesion()).append("\n");
        sb.append("Estatus:          ").append(seleccionado.getEstatus()).append("\n");
        sb.append("Fecha de Entrega: ").append(seleccionado.getFechaEntrega()).append("\n\n");

        sb.append("ESTADÍSTICAS DE ASISTENCIA:\n");
        sb.append("----------------------------------------\n");
        sb.append("Total de Asistentes:  ").append(seleccionado.getTotalAsistentes()).append("\n");
        sb.append("Alumnos en Riesgo:    ").append(seleccionado.getTotalEnRiesgo()).append("\n\n");

        sb.append("COMENTARIOS GENERALES:\n");
        sb.append("----------------------------------------\n");
        String comentarios = seleccionado.getComentariosGenerales();
        sb.append((comentarios != null && !comentarios.isEmpty()) ? comentarios : "Sin comentarios").append("\n");

        sb.append("\n========================================\n");
        sb.append("Generado: ").append(java.time.LocalDateTime.now()
                .format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))).append("\n");
        sb.append("========================================\n");

        // Mostrar el contenido en un Alert con TextArea
        TextArea textArea = new TextArea(sb.toString());
        textArea.setEditable(false);
        textArea.setWrapText(true);

        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Reporte de Tutoría");
        alerta.setHeaderText("Contenido del reporte #" + seleccionado.getIdReporte());
        alerta.getDialogPane().setContent(textArea);
        alerta.showAndWait();

        boolean actualizado = ReporteTutoriaDAO.marcarComoRevisado(seleccionado.getIdReporte());
        if (actualizado) {
            cargarReportes(); 
        }
    }

    private void mostrarAlerta(String mensaje, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle("Reporte de Tutoría");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
