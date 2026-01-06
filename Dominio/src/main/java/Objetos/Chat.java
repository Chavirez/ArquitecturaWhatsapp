/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Objetos;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author santi
 */
public class Chat implements Serializable {
    
    private List<Mensaje> mensajes;
    private List<Usuario> usuarios;

    public Chat() {
    }

    public Chat(List<Mensaje> mensajes, List<Usuario> usuarios) {
        this.mensajes = mensajes;
        this.usuarios = usuarios;
    }

    public List<Mensaje> getMensajes() {
        return mensajes;
    }

    public void setMensajes(List<Mensaje> mensajes) {
        this.mensajes = mensajes;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    @Override
    public String toString() {
        return "Chat{" + "mensajes=" + mensajes + ", usuarios=" + usuarios + '}';
    }
    
    
    
    
}
