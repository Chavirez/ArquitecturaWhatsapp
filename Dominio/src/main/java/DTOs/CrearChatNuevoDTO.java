/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DTOs;

import Objetos.Chat;

/**
 *
 * @author santi
 */
public class CrearChatNuevoDTO {
    
    private int idUsuario1;
    private int idUsuario2;

    public CrearChatNuevoDTO() {
    }

    public CrearChatNuevoDTO(int idUsuario1, int idUsuario2) {
        this.idUsuario1 = idUsuario1;
        this.idUsuario2 = idUsuario2;
    }

    public int getIdUsuario1() {
        return idUsuario1;
    }

    public void setIdUsuario1(int idUsuario1) {
        this.idUsuario1 = idUsuario1;
    }

    public int getIdUsuario2() {
        return idUsuario2;
    }

    public void setIdUsuario2(int idUsuario2) {
        this.idUsuario2 = idUsuario2;
    }

    @Override
    public String toString() {
        return "CrearChatNuevoDTO{" + "idUsuario1=" + idUsuario1 + ", idUsuario2=" + idUsuario2 + '}';
    }
    
    
    
    
}
