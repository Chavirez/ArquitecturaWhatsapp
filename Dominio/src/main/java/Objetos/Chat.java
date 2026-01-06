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
    
    private int id;
    private List<Mensaje> mensajes;
    private List<Usuario> usuarios;

    public Chat() {
    }

    public Chat(int id, List<Mensaje> mensajes, List<Usuario> usuarios) {
        this.id = id;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Chat{" + "id=" + id + ", mensajes=" + mensajes + ", usuarios=" + usuarios + '}';
    }


    
    
    
    
}
