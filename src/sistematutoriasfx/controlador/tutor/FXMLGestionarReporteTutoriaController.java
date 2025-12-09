package sistematutoriasfx.controlador.tutor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sistematutoriasfx.modelo.dao.AcademicoDAO;
import sistematutoriasfx.modelo.dao.EstudianteDAO;
import sistematutoriasfx.modelo.dao.FechasTutoriaDAO;
import sistematutoriasfx.modelo.dao.ListaAsistenciaDAO;
import sistematutoriasfx.modelo.dao.PeriodoEscolarDAO;
import sistematutoriasfx.modelo.dao.ReporteTutoriaDAO;
import sistematutoriasfx.modelo.pojo.Academico;
import sistematutoriasfx.modelo.pojo.Estudiante;
import sistematutoriasfx.modelo.pojo.FechasTutoria;
import sistematutoriasfx.modelo.pojo.ListaAsistencia;
import sistematutoriasfx.modelo.pojo.PeriodoEscolar;
import sistematutoriasfx.modelo.pojo.ReporteTutoria;
import sistematutoriasfx.modelo.pojo.Usuario;
import utilidad.Utilidades;

public class FXMLGestionarReporteTutoriaController implements Initializable {

    @FXML
    private ComboBox<PeriodoEscolar> cbPeriodo;
    @FXML
    private ComboBox<FechasTutoria> cbFechaSesion;
    @FXML private Label lbFechaMostrada; 
    @FXML private TableView<ReporteTutoria> tvReportes;
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

    private Usuario usuarioSesion;
    private Academico academicoSesion;
    private ObservableList<ReporteTutoria> listaOriginalReportes;

    public void configurarEscena(Usuario usuario) {
        this.usuarioSesion = usuario;
        this.academicoSesion = AcademicoDAO.obtenerAcademicoPorIdUsuario(usuario.getIdUsuario());
        if(this.academicoSesion != null){
            cargarTabla(); 
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarColumnas();
        cargarCombos();
        configurarListeners();
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
    
    private void cargarCombos() {
        cbPeriodo.setItems(FXCollections.observableArrayList(PeriodoEscolarDAO.obtenerPeriodos()));
    }
    
    private void configurarListeners() {
        cbPeriodo.valueProperty().addListener((obs, oldVal, newVal) -> {
            if(newVal != null) {
                cbFechaSesion.setItems(FXCollections.observableArrayList(
                    FechasTutoriaDAO.obtenerFechasPorPeriodo(newVal.getIdPeriodo())
                ));
                filtrarTabla();
            } else {
                cbFechaSesion.setItems(FXCollections.observableArrayList());
                lbFechaMostrada.setText("");
                filtrarTabla();
            }
        });
        
        cbFechaSesion.valueProperty().addListener((obs, oldVal, newVal) -> {
            if(newVal != null) {
                lbFechaMostrada.setText(newVal.getFechaSesion().toString());
            } else {
                lbFechaMostrada.setText("");
            }
            filtrarTabla();
        });
    }
    
    private void cargarTabla() {
        if(academicoSesion != null) {
            ArrayList<ReporteTutoria> lista = ReporteTutoriaDAO.obtenerReportesPorTutor(academicoSesion.getIdAcademico());
            listaOriginalReportes = FXCollections.observableArrayList(lista);

            tvReportes.setItems(listaOriginalReportes);
        }
    }

    private void filtrarTabla() {
        if(listaOriginalReportes == null) return;

        ObservableList<ReporteTutoria> filtro = FXCollections.observableArrayList();
        PeriodoEscolar pSel = cbPeriodo.getValue();
        FechasTutoria fSel = cbFechaSesion.getValue();

        if(pSel == null && fSel == null) {
            tvReportes.setItems(listaOriginalReportes);
            return;
        }

        for(ReporteTutoria r : listaOriginalReportes) {
            boolean coincidePeriodo = (pSel == null) || (r.getIdPeriodo() == pSel.getIdPeriodo());
            boolean coincideFecha = (fSel == null) || (r.getFechaSesion().equals(fSel.getFechaSesion()));

            if(coincidePeriodo && coincideFecha) {
                filtro.add(r);
            }
        }

        tvReportes.setItems(filtro);
    }
    
   
    @FXML
    private void clicRegistrar(ActionEvent event) {
        abrirFormulario(null);
    }

    @FXML
    private void clicModificar(ActionEvent event) {
        ReporteTutoria seleccion = tvReportes.getSelectionModel().getSelectedItem();
        if(seleccion != null) {
            abrirFormulario(seleccion);
        } else {
            Utilidades.mostrarAlertaSimple("Selección requerida", 
                "Selecciona un reporte para modificar.", 
                Alert.AlertType.WARNING);
        }
    }
    
    private void abrirFormulario(ReporteTutoria reporteEdicion) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sistematutoriasfx/vista/tutor/FXMLFormularioReporteTutoria.fxml"));
            Parent root = loader.load();
            
            FXMLFormularioReporteTutoriaController controlador = loader.getController();
            controlador.inicializarTutor(academicoSesion.getIdAcademico());
            
            if(reporteEdicion != null) {
                controlador.inicializarEdicion(reporteEdicion);
            }
            
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.setTitle(reporteEdicion == null ? "Registrar Reporte" : "Modificar Reporte");
            stage.showAndWait();
            
            cargarTabla();
            filtrarTabla(); 
            
        } catch (IOException ex) {
            ex.printStackTrace();
            Utilidades.mostrarAlertaSimple("Error", 
                "No se pudo abrir la ventana del formulario.", 
                Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void clicRegresar(ActionEvent event) {
        ((Stage) cbPeriodo.getScene().getWindow()).close();
    }
    
    @FXML 
    private void clicEliminar(ActionEvent event) {
        ReporteTutoria seleccion = tvReportes.getSelectionModel().getSelectedItem();
        
        if(seleccion == null) {
            Utilidades.mostrarAlertaSimple("Selección requerida", 
                "Por favor selecciona un reporte de la tabla para eliminar.", 
                Alert.AlertType.WARNING);
            return;
        }
        
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar eliminación");
        confirmacion.setHeaderText("¿Eliminar este reporte?");
        confirmacion.setContentText(
            "Período: " + seleccion.getPeriodoNombre() + "\n" +
            "Fecha: " + seleccion.getFechaSesion() + "\n" +
            "Sesión: " + seleccion.getNumSesion() + "\n\n" +
            "Esta acción NO se puede deshacer."
        );
        
        java.util.Optional<javafx.scene.control.ButtonType> resultado = confirmacion.showAndWait();
        
        if(resultado.isPresent() && resultado.get() == javafx.scene.control.ButtonType.OK) {
            boolean exito = ReporteTutoriaDAO.eliminarReporte(seleccion.getIdReporte());
            
            if(exito) {
                Utilidades.mostrarAlertaSimple("Éxito", 
                    "El reporte ha sido eliminado correctamente.", 
                    Alert.AlertType.INFORMATION);
                cargarTabla();
                filtrarTabla();
            } else {
                Utilidades.mostrarAlertaSimple("Error", 
                    "No se pudo eliminar el reporte.", 
                    Alert.AlertType.ERROR);
            }
        }
    }
    
    @FXML
    private void clicExportar(ActionEvent event) {
        ReporteTutoria seleccion = tvReportes.getSelectionModel().getSelectedItem();
        
        if(seleccion == null) {
            Utilidades.mostrarAlertaSimple("Selección requerida", 
                "Por favor selecciona un reporte de la tabla para exportar.", 
                Alert.AlertType.WARNING);
            return;
        }
        
        exportarReporteATXT(seleccion);
    }
    
    private void exportarReporteATXT(ReporteTutoria reporte) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar Reporte");
        fileChooser.setInitialFileName("Reporte_Tutoria_" + reporte.getFechaSesion() + ".txt");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Archivos de texto", "*.txt")
        );
        
        File archivo = fileChooser.showSaveDialog(cbPeriodo.getScene().getWindow());
        
        if(archivo != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivo))) {
                // ===== ENCABEZADO =====
                writer.write("========================================\n");
                writer.write("    REPORTE DE TUTORÍA\n");
                writer.write("========================================\n\n");
                
                // ===== INFORMACIÓN GENERAL =====
                writer.write("INFORMACIÓN GENERAL:\n");
                writer.write("----------------------------------------\n");
                writer.write("Período:          " + reporte.getPeriodoNombre() + "\n");
                writer.write("Fecha de Sesión:  " + reporte.getFechaSesion() + "\n");
                writer.write("Número de Sesión: " + reporte.getNumSesion() + "\n");
                writer.write("Estatus:          " + reporte.getEstatus() + "\n");
                writer.write("Fecha de Entrega: " + reporte.getFechaEntrega() + "\n\n");
                
                // ===== ESTADÍSTICAS =====
                writer.write("ESTADÍSTICAS DE ASISTENCIA:\n");
                writer.write("----------------------------------------\n");
                writer.write("Total de Asistentes:  " + reporte.getTotalAsistentes() + "\n");
                writer.write("Alumnos en Riesgo:    " + reporte.getTotalEnRiesgo() + "\n\n");
                
                // ===== LISTA DE ALUMNOS =====
                writer.write("LISTA DE ALUMNOS:\n");
                writer.write("----------------------------------------\n");
                
                // Obtener la lista de asistencia
                ArrayList<ListaAsistencia> asistencias = 
                    ListaAsistenciaDAO.obtenerAsistenciaPorSesion(reporte.getIdSesion());
                
                if(asistencias != null && !asistencias.isEmpty()) {
                    // Obtener datos de estudiantes
                    ArrayList<Estudiante> estudiantes = 
                        EstudianteDAO.obtenerEstudiantesPorTutor(academicoSesion.getIdAcademico(), reporte.getIdPeriodo());
                    
                    for(ListaAsistencia asist : asistencias) {
                        // Buscar el estudiante correspondiente
                        Estudiante est = null;
                        for(Estudiante e : estudiantes) {
                            if(e.getIdEstudiante() == asist.getIdEstudiante()) {
                                est = e;
                                break;
                            }
                        }
                        
                        if(est != null) {
                            writer.write(String.format("%-12s %-40s %-10s %-10s\n",  
                                est.getMatricula(),
                                est.getNombreCompleto(),
                                asist.isAsistio() ? "Asistió" : "Falta",
                                asist.isRiesgoDetectado() ? "En Riesgo" : "Normal"
                            ));
                        }
                    }
                } else {
                    writer.write("Sin registros de asistencia.\n");
                }
                
                writer.write("\n");
                
                // ===== COMENTARIOS =====
                writer.write("COMENTARIOS GENERALES:\n");
                writer.write("----------------------------------------\n");
                String comentarios = reporte.getComentariosGenerales();
                if(comentarios != null && !comentarios.isEmpty()) {
                    writer.write(comentarios + "\n");
                } else {
                    writer.write("Sin comentarios.\n");
                }
                
                writer.write("\n");
                writer.write("========================================\n");
                writer.write("Generado: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) + "\n");
                writer.write("========================================\n");
                
                Utilidades.mostrarAlertaSimple("Éxito", 
                    "El reporte se exportó correctamente a:\n" + archivo.getAbsolutePath(), 
                    Alert.AlertType.INFORMATION);
                
            } catch (IOException e) {
                e.printStackTrace();
                Utilidades.mostrarAlertaSimple("Error", 
                    "No se pudo guardar el archivo:\n" + e.getMessage(), 
                    Alert.AlertType.ERROR);
            }
        }
    }

}