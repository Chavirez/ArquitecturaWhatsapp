package Eventos;

import DTOs.LoginRespuestaDTO;

public class EventoRespuestaLogin {
    
    private LoginRespuestaDTO loginRespuestaDTO;
    private String idSolicitante; // <--- NUEVO CAMPO

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

    public String getIdSolicitante() {
        return idSolicitante;
    }

    public void setIdSolicitante(String idSolicitante) {
        this.idSolicitante = idSolicitante;
    }
}