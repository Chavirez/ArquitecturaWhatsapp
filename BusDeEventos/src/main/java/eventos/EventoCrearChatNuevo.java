/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package eventos;

import DTOs.CrearChatNuevoDTO;

/**
 *
 * @author santi
 */
public class EventoCrearChatNuevo {
    private final CrearChatNuevoDTO chat;

    public EventoCrearChatNuevo(CrearChatNuevoDTO chat) {
        this.chat = chat;
    }

    public CrearChatNuevoDTO getMensaje() { 
        return chat; 
    }
}
