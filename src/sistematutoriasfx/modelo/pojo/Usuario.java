/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author Ana Georgina
 */
package sistematutoriasfx.modelo.pojo;

import java.util.List;

public class Usuario {
    private int idUsuario;
    private String username;
    private String password;
    private int idRol;
    private List<Rol> roles; 

    public Usuario() {
    }

    public Usuario(int idUsuario, String username, String password, int idRol) {
        this.idUsuario = idUsuario;
        this.username = username;
        this.password = password;
        this.idRol = idRol;
    }

    public int getIdUsuario() { 
        return idUsuario; 
    }
    
    public void setIdUsuario(int idUsuario) { 
        this.idUsuario = idUsuario; 
    }

    public String getUsername() { 
        return username; 
    }
    public void setUsername(String username) { 
        this.username = username; 
    }

    public String getPassword() { 
        return password; 
    }
    public void setPassword(String password) {
        this.password = password; 
    }

    public int getIdRol() { 
        return idRol; 
    }
    public void setIdRol(int idRol) { 
        this.idRol = idRol; 
    }

    public List<Rol> getRoles() {
        return roles;
    }

    public void setRoles(List<Rol> roles) {
        this.roles = roles;
    }
}
