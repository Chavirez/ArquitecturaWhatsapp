/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DTOs;

import Objetos.Chat;
import Objetos.Usuario;

/**
 *
 * @author santi
 */
public class CrearChatNuevoDTO {
    
    private Usuario usuario1;
    private Usuario usuario2;

    public CrearChatNuevoDTO() {
    }

    public CrearChatNuevoDTO(Usuario usuario1, Usuario usuario2) {
        this.usuario1 = usuario1;
        this.usuario2 = usuario2;
    }

    public Usuario getUsuario1() {
        return usuario1;
    }

    public void setUsuario1(Usuario usuario1) {
        this.usuario1 = usuario1;
    }

    public Usuario getUsuario2() {
        return usuario2;
    }

    public void setUsuario2(Usuario usuario2) {
        this.usuario2 = usuario2;
    }


    
    
    
}
