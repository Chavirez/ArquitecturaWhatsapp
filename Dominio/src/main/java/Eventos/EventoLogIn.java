/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Eventos;

import DTOs.LoginPedidoDTO;

/**
 *
 * @author santi
 */
public class EventoLogIn {
    
    private LoginPedidoDTO evento;

    public EventoLogIn() {
    }

    public EventoLogIn(LoginPedidoDTO evento) {
        this.evento = evento;
    }

    public LoginPedidoDTO getEvento() {
        return evento;
    }

    public void setEvento(LoginPedidoDTO evento) {
        this.evento = evento;
    }
    
    
    
}
