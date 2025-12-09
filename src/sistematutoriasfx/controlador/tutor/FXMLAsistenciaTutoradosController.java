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

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sistematutoriasfx.modelo.dao.AcademicoDAO;
import sistematutoriasfx.modelo.dao.FechasTutoriaDAO;
import sistematutoriasfx.modelo.dao.ListaAsistenciaDAO;
import sistematutoriasfx.modelo.dao.PeriodoEscolarDAO;
import sistematutoriasfx.modelo.dao.SesionTutoriaDAO;
import sistematutoriasfx.modelo.pojo.Academico;
import sistematutoriasfx.modelo.pojo.FechasTutoria;
import sistematutoriasfx.modelo.pojo.PeriodoEscolar;
import sistematutoriasfx.modelo.pojo.SesionTutoria;
import sistematutoriasfx.modelo.pojo.Usuario;
import utilidad.Utilidades;

public class FXMLAsistenciaTutoradosController implements Initializable {

    @FXML private ComboBox<PeriodoEscolar> cbPeriodo;
    @FXML private ComboBox<FechasTutoria> cbFechaSesion;
    
    @FXML private TableView<SesionTutoria> tvListaAsistencia;
    @FXML private TableColumn colFechaSesion;
    @FXML private TableColumn colSesion;
    @FXML private TableColumn colTotalAlumnos;
    @FXML private TableColumn colTotalAsistentes;
    @FXML private TableColumn colEnRiesgo;
    @FXML private TableColumn<SesionTutoria, String> colEstado;

    private Usuario usuarioSesion;
    private Academico academicoSesion;
    
    // Lista auxiliar para guardar todos los datos y poder filtrar
    private ObservableList<SesionTutoria> listaOriginalSesiones;
    @FXML
    private Label lbFechaMostrada;

    public void configurarEscena(Usuario usuario) {
        this.usuarioSesion = usuario;
        this.academicoSesion = AcademicoDAO.obtenerAcademicoPorIdUsuario(usuario.getIdUsuario());
        cargarTabla();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarColumnas();
        cargarPeriodos();
        configurarListeners();
    }
    
    private void configurarColumnas() {
        colFechaSesion.setCellValueFactory(new PropertyValueFactory("fecha"));
        colSesion.setCellValueFactory(new PropertyValueFactory("numSesion"));
        colTotalAsistentes.setCellValueFactory(new PropertyValueFactory("totalAsistentes"));
        colEnRiesgo.setCellValueFactory(new PropertyValueFactory("totalRiesgo"));

        //CAMBIAR ESTA LÍNEA (antes decía "N/A")
        colTotalAlumnos.setCellValueFactory(new PropertyValueFactory("totalAlumnos"));

        colEstado.setCellValueFactory(cellData -> {
            SesionTutoria s = cellData.getValue();
            boolean registrada = s.getTotalAsistentes() > 0 || s.getTotalRiesgo() > 0;
            return new SimpleStringProperty(registrada ? "Registrada" : "Pendiente");
        });
    }
    
    private void cargarPeriodos() {
        cbPeriodo.setItems(FXCollections.observableArrayList(PeriodoEscolarDAO.obtenerPeriodos()));
    }
    
     private void configurarListeners() {
        // ✅ Al cambiar periodo, cargar fechas y filtrar
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
        
        // Al cambiar Fecha -> Filtrar
        cbFechaSesion.valueProperty().addListener((obs, oldVal, newVal) -> {
            filtrarTabla(); // <--- ESTO FALTABA
        });
    }
    
    private void cargarTabla() {
        if(academicoSesion != null) {
            ArrayList<SesionTutoria> lista = SesionTutoriaDAO.obtenerSesionesPorTutor(academicoSesion.getIdAcademico());
            listaOriginalSesiones = FXCollections.observableArrayList(lista);

            // ❌ ANTES: Mostraba todo inmediatamente
            // tvListaAsistencia.setItems(listaOriginalSesiones); 

            // ✅ AHORA: La tabla empieza VACÍA hasta que selecciones filtros
            tvListaAsistencia.setItems(FXCollections.observableArrayList());
    }
}
    
    private void filtrarTabla() {
        if (listaOriginalSesiones == null) return;

        ObservableList<SesionTutoria> filtro = FXCollections.observableArrayList();
        PeriodoEscolar pSel = cbPeriodo.getValue();
        FechasTutoria fSel = cbFechaSesion.getValue();

        // ✅ SI AMBOS COMBOS ESTÁN VACÍOS, NO MOSTRAR NADA
        if (pSel == null && fSel == null) {
            tvListaAsistencia.setItems(filtro); // Tabla vacía
            return;
        }

        for (SesionTutoria s : listaOriginalSesiones) {
            boolean coincidePeriodo = (pSel == null) || (s.getIdPeriodo() == pSel.getIdPeriodo());
            boolean coincideFecha = (fSel == null) || (s.getIdFechaTutoria() == fSel.getIdFechaTutoria());

            if (coincidePeriodo && coincideFecha) {
                filtro.add(s);
            }
        }
        tvListaAsistencia.setItems(filtro);
    }

    // --- ACCIONES (Métodos PUBLIC para evitar problemas de visibilidad) ---

    @FXML
    public void clicPasarLista(ActionEvent event) {
        System.out.println("Intentando abrir formulario...");

        if(cbFechaSesion.getValue() != null) {
            abrirFormulario(cbFechaSesion.getValue(), false); // false = NO es consulta
        } else {
            SesionTutoria seleccion = tvListaAsistencia.getSelectionModel().getSelectedItem();
            if(seleccion != null) {
                FechasTutoria fechaTemp = new FechasTutoria();
                fechaTemp.setIdFechaTutoria(seleccion.getIdFechaTutoria());
                fechaTemp.setFechaSesion(java.time.LocalDate.parse(seleccion.getFecha())); 
                fechaTemp.setIdPeriodo(seleccion.getIdPeriodo());

                abrirFormulario(fechaTemp, false); // false = NO es consulta
            } else {
                Utilidades.mostrarAlertaSimple("Selección requerida", "Selecciona una sesión.", Alert.AlertType.WARNING);
            }
        }
    }

    @FXML 
    public void clicConsultar(ActionEvent event) {
        System.out.println("Modo consulta");

        if(cbFechaSesion.getValue() != null) {
            abrirFormulario(cbFechaSesion.getValue(), true); // ✅ true = SÍ es consulta
        } else {
            SesionTutoria seleccion = tvListaAsistencia.getSelectionModel().getSelectedItem();
            if(seleccion != null) {
                FechasTutoria fechaTemp = new FechasTutoria();
                fechaTemp.setIdFechaTutoria(seleccion.getIdFechaTutoria());
                fechaTemp.setFechaSesion(java.time.LocalDate.parse(seleccion.getFecha())); // ✅ Correcto
                fechaTemp.setIdPeriodo(seleccion.getIdPeriodo());

                abrirFormulario(fechaTemp, true); // ✅ true = SÍ es consulta
            } else {
                Utilidades.mostrarAlertaSimple("Selección requerida", "Selecciona una sesión.", Alert.AlertType.WARNING);
            }
        }
    }

    @FXML public void clicModificar(ActionEvent event) { clicPasarLista(event); }

    @FXML
    public void clicEliminar(ActionEvent event) {
        // 1. Verificar que haya una sesión seleccionada
        SesionTutoria seleccion = tvListaAsistencia.getSelectionModel().getSelectedItem();

        if(seleccion == null) {
            Utilidades.mostrarAlertaSimple("Selección requerida", 
                "Por favor selecciona una sesión de la tabla para eliminar su asistencia.", 
                Alert.AlertType.WARNING);
            return;
        }

        // 2. Verificar que la sesión tenga datos registrados
        if(seleccion.getTotalAsistentes() == 0 && seleccion.getTotalRiesgo() == 0) {
            Utilidades.mostrarAlertaSimple("Sin datos", 
                "Esta sesión no tiene asistencia registrada.", 
                Alert.AlertType.INFORMATION);
            return;
        }

        // 3. Confirmar con el usuario
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar eliminación");
        confirmacion.setHeaderText("¿Eliminar asistencia de esta sesión?");
        confirmacion.setContentText(
            "Fecha: " + seleccion.getFecha() + "\n" +
            "Sesión: " + seleccion.getNumSesion() + "\n" +
            "Asistentes registrados: " + seleccion.getTotalAsistentes() + "\n\n" +
            "Esta acción NO se puede deshacer."
        );

        // 4. Si el usuario confirma, proceder a eliminar
        java.util.Optional<javafx.scene.control.ButtonType> resultado = confirmacion.showAndWait();

        if(resultado.isPresent() && resultado.get() == javafx.scene.control.ButtonType.OK) {
            boolean exito = ListaAsistenciaDAO.eliminarAsistenciaPorSesion(seleccion.getIdSesion());

            if(exito) {
                Utilidades.mostrarAlertaSimple("Éxito", 
                    "La asistencia de esta sesión ha sido eliminada correctamente.", 
                    Alert.AlertType.INFORMATION);

                // 5. Recargar la tabla para actualizar el estado
                cargarTabla();
            } else {
                Utilidades.mostrarAlertaSimple("Error", 
                    "No se pudo eliminar la asistencia. Verifica la conexión con la base de datos.", 
                    Alert.AlertType.ERROR);
            }
        }
    }
    @FXML
    public void clicRegresar(ActionEvent event) {
        ((Stage) cbPeriodo.getScene().getWindow()).close();
    }
    
    private void abrirFormulario(FechasTutoria fechaSeleccionada, boolean soloConsulta) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sistematutoriasfx/vista/tutor/FXMLFormularioAsistencia.fxml"));
            Parent root = loader.load();

            FXMLFormularioAsistenciaController controlador = loader.getController();
            controlador.inicializarAsistencia(academicoSesion.getIdAcademico(), fechaSeleccionada, soloConsulta);

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.setTitle("Lista de Asistencia - " + fechaSeleccionada.getFechaSesion());
            stage.showAndWait();

            cargarTabla();

        } catch (IOException ex) {
            ex.printStackTrace();
            Utilidades.mostrarAlertaSimple("Error", "No se pudo abrir la ventana de asistencia.", Alert.AlertType.ERROR);
        }
    }
}