/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DTOs;

/**
 *
 * @author santi
 */
public class LoginRespuestaDTO {
    
    private boolean exito;
    private String mensaje;
    private UsuarioDTO usuarioLogueado;

    public LoginRespuestaDTO() {
    }

    public LoginRespuestaDTO(boolean exito, String mensaje, UsuarioDTO usuarioLogueado) {
        this.exito = exito;
        this.mensaje = mensaje;
        this.usuarioLogueado = usuarioLogueado;
    }

    public boolean isExito() {
        return exito;
    }

    public void setExito(boolean exito) {
        this.exito = exito;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public UsuarioDTO getUsuarioLogueado() {
        return usuarioLogueado;
    }

    public void setUsuarioLogueado(UsuarioDTO usuarioLogueado) {
        this.usuarioLogueado = usuarioLogueado;
    }
    
    
}
