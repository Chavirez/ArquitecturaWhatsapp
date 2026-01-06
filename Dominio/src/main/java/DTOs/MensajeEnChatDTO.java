
package DTOs;

import Objetos.Mensaje;
import java.util.Date;

public class MensajeEnChatDTO {
    
    private String mensaje;
    private Date fechaMensaje;
    private int idUsuario;
    private int idChat;

    public MensajeEnChatDTO() {
    }

    public MensajeEnChatDTO(String mensaje, Date fechaMensaje, int idUsuario, int idChat) {
        this.mensaje = mensaje;
        this.fechaMensaje = fechaMensaje;
        this.idUsuario = idUsuario;
        this.idChat = idChat;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Date getFechaMensaje() {
        return fechaMensaje;
    }

    public void setFechaMensaje(Date fechaMensaje) {
        this.fechaMensaje = fechaMensaje;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdChat() {
        return idChat;
    }

    public void setIdChat(int idChat) {
        this.idChat = idChat;
    }

    @Override
    public String toString() {
        return "MensajeEnChatDTO{" + "mensaje=" + mensaje + ", fechaMensaje=" + fechaMensaje + ", idUsuario=" + idUsuario + ", idChat=" + idChat + '}';
    }
    
    
}
