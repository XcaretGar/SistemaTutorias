/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author JOANA XCARET
 */
package sistematutoriasfx.modeloo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {
    
    // Constantes de conexión
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String BD = "sistema_tutorias"; 
    private static final String HOST = "localhost";
    private static final String PUERTO = "3306";
    private static final String USUARIO = "root"; // Tu usuario de MySQL (usualmente root)
    private static final String PASSWORD = "REOA040715"; // CONTRASEÑA DE MYSQL
    private static final String URL = "jdbc:mysql://" + HOST + ":" + PUERTO + "/" + BD + "?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=America/Mexico_City";
    public static Connection abrirConexion() throws SQLException {
        try {
            Class.forName(DRIVER);
            return DriverManager.getConnection(URL, USUARIO, PASSWORD);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new SQLException("No se encontró el driver de MySQL.");
        }
    }
}
