package Eventos;

import DTOs.LoginPedidoDTO;

public class EventoLogIn {
    
    private LoginPedidoDTO loginPedidoDTO;
    private String idSolicitante;

    public EventoLogIn() {
    }

    public EventoLogIn(LoginPedidoDTO evento) {
        this.loginPedidoDTO = evento;
    }

    public LoginPedidoDTO getEvento() {
        return loginPedidoDTO;
    }

    public void setEvento(LoginPedidoDTO evento) {
        this.loginPedidoDTO = evento;
    }

    public String getIdSolicitante() {
        return idSolicitante;
    }

    public void setIdSolicitante(String idSolicitante) {
        this.idSolicitante = idSolicitante;
    }
}