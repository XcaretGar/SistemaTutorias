/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package sistematutoriasfx.controlador.coordinador;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * FXML Controller class
 *
 * @author JOANA XCARET
 */
public class FXMLRegistrarFechaTutoriaController implements Initializable {

    @FXML
    private TableView<?> tvFechasTutoria;
    @FXML
    private TableColumn<?, ?> colSesion;
    @FXML
    private TableColumn<?, ?> colPeriodo;
    @FXML
    private TableColumn<?, ?> colFecha;
    @FXML
    private TableColumn<?, ?> colDescripcion;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void clicEstablecerNuevaFecha(ActionEvent event) {
    }

    @FXML
    private void clicActualizarFecha(ActionEvent event) {
    }
    
}
