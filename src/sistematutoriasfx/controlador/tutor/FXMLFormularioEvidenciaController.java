/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package sistematutoriasfx.controlador.tutor;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sistematutoriasfx.modelo.dao.EvidenciaDAO;
import sistematutoriasfx.modelo.pojo.Evidencia;
import utilidad.Utilidades;

/**
 * FXML Controller class
 *
 * @author Ana Georgina
 */
public class FXMLFormularioEvidenciaController implements Initializable {

    @FXML
    private TextField tfNombreEvidencia;
    @FXML
    private TextField tfNombreArchivoOriginal;
    @FXML
    private Label lbError;

    private int idReporteActual;
    private File archivoSeleccionado;
    private boolean esEdicion = false;
    private Evidencia evidenciaEdicion;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lbError.setText("");
    }    

    public void inicializarFormulario(int idReporte) {
        this.idReporteActual = idReporte;
        this.esEdicion = false;
    }

    public void inicializarEdicion(Evidencia evidencia) {
        this.esEdicion = true;
        this.evidenciaEdicion = evidencia;
        this.idReporteActual = evidencia.getIdReporte();
        
        tfNombreEvidencia.setText(evidencia.getNombreArchivo());
        
        // Mostrar el nombre del archivo actual (extraído de la ruta)
        if(evidencia.getRutaArchivo() != null){
            File archivoActual = new File(evidencia.getRutaArchivo());
            tfNombreArchivoOriginal.setText(archivoActual.getName());
        }
    }

    @FXML
    public void clicSeleccionarArchivo(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar archivo de evidencia");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Archivos soportados", "*.pdf", "*.jpg", "*.png", "*.jpeg", "*.docx")
        );
        
        File archivo = fileChooser.showOpenDialog(tfNombreEvidencia.getScene().getWindow());
        
        if (archivo != null) {
            // Validación de tamaño (Ej. Máximo 10 MB)
            if (archivo.length() > 10 * 1024 * 1024) { 
                Utilidades.mostrarAlertaSimple("Archivo muy pesado", "El archivo excede el límite de 10 MB.", Alert.AlertType.WARNING);
                return;
            }
            
            this.archivoSeleccionado = archivo;
            tfNombreArchivoOriginal.setText(archivo.getName());
            
            // Si el usuario no ha escrito un nombre, sugerimos el nombre del archivo
            if(tfNombreEvidencia.getText().isEmpty()){
                tfNombreEvidencia.setText(archivo.getName());
            }
        }
    }

    @FXML
    public void clicGuardar(ActionEvent event) {
        String nombreUsuario = tfNombreEvidencia.getText();
        if (nombreUsuario.isEmpty()) {
            lbError.setText("Por favor asigna un nombre a la evidencia.");
            return;
        }
        
        // Si es nuevo registro, es OBLIGATORIO seleccionar archivo.
        // Si es edición, es opcional (puede que solo quiera cambiar el nombre).
        if (!esEdicion && archivoSeleccionado == null) {
            lbError.setText("Debes seleccionar un archivo adjunto.");
            return;
        }

        try {
            boolean exito = false;
            String rutaFinalBD = ""; 

            if (archivoSeleccionado != null) {
                // Creamos la carpeta si no existe
                String carpetaDestino = "evidencias_subidas/";
                File carpeta = new File(carpetaDestino);
                if (!carpeta.exists()) {
                    carpeta.mkdir();
                }

                // Generamos nombre único: tiempo + nombre original (para no sobrescribir)
                String nombreFisico = System.currentTimeMillis() + "_" + archivoSeleccionado.getName();
                File destino = new File(carpeta, nombreFisico);
                
                // Copiamos el archivo
                Files.copy(archivoSeleccionado.toPath(), destino.toPath(), StandardCopyOption.REPLACE_EXISTING);
                
                rutaFinalBD = destino.getPath();
            } else if (esEdicion) {
                // Si editamos pero no cambiamos el archivo, mantenemos la ruta vieja
                rutaFinalBD = evidenciaEdicion.getRutaArchivo();
            }
            Evidencia evidenciaGuardar = new Evidencia();
            evidenciaGuardar.setNombreArchivo(nombreUsuario);
            evidenciaGuardar.setRutaArchivo(rutaFinalBD);
            evidenciaGuardar.setIdReporte(idReporteActual);

            // Llamar al DAO
            if (esEdicion) {
                evidenciaGuardar.setIdEvidencia(evidenciaEdicion.getIdEvidencia());
                exito = EvidenciaDAO.actualizarEvidencia(evidenciaGuardar);
            } else {
                exito = EvidenciaDAO.registrarEvidencia(evidenciaGuardar);
            }
            if (exito) {
                Utilidades.mostrarAlertaSimple("Operación exitosa", "La evidencia se ha guardado correctamente.", Alert.AlertType.INFORMATION);
                cerrarVentana();
            } else {
                Utilidades.mostrarAlertaSimple("Error", "No se pudo guardar la información en la base de datos.", Alert.AlertType.ERROR);
            }

        } catch (IOException e) {
            e.printStackTrace();
            Utilidades.mostrarAlertaSimple("Error", "Fallo al copiar el archivo físico a la carpeta del servidor.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void clicCancelar(ActionEvent event) {
        cerrarVentana();
    }
    
    private void cerrarVentana() {
        ((Stage) tfNombreEvidencia.getScene().getWindow()).close();
    }
}