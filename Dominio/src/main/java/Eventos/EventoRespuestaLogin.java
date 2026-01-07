/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Eventos;

import DTOs.LoginRespuestaDTO;

/**
 *
 * @author santi
 */
public class EventoRespuestaLogin {
    
    private LoginRespuestaDTO loginRespuestaDTO;

    public EventoRespuestaLogin() {
    }

    public EventoRespuestaLogin(LoginRespuestaDTO evento) {
        this.loginRespuestaDTO = evento;
    }

    public LoginRespuestaDTO getEvento() {
        return loginRespuestaDTO;
    }

    public void setEvento(LoginRespuestaDTO evento) {
        this.loginRespuestaDTO = evento;
    }
    
    
    
}
