/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Eventos;

import Objetos.Chat;
import java.util.List;

/**
 *
 * @author santi
 */
public class EventoSincronizacion {
    
    List<Chat> chats;

    public EventoSincronizacion() {
    }

    public EventoSincronizacion(List<Chat> chats) {
        this.chats = chats;
    }

    public List<Chat> getChats() {
        return chats;
    }

    public void setChats(List<Chat> chats) {
        this.chats = chats;
    }
    
    
    
}
