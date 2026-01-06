
package DTOs;

import Objetos.Chat;
import Objetos.Mensaje;
import Objetos.Usuario;
import java.time.LocalDateTime;

public class MensajeEnChatDTO {
    
    private String mensaje;
    private LocalDateTime fechaMensaje;
    private Usuario usuario;
    private Chat chat;

    public MensajeEnChatDTO() {
    }

    public MensajeEnChatDTO(String mensaje, LocalDateTime fechaMensaje, Usuario usuario, Chat chat) {
        this.mensaje = mensaje;
        this.fechaMensaje = fechaMensaje;
        this.usuario = usuario;
        this.chat = chat;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public LocalDateTime getFechaMensaje() {
        return fechaMensaje;
    }

    public void setFechaMensaje(LocalDateTime fechaMensaje) {
        this.fechaMensaje = fechaMensaje;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    @Override
    public String toString() {
        return "MensajeEnChatDTO{" + "mensaje=" + mensaje + ", fechaMensaje=" + fechaMensaje + ", usuario=" + usuario + ", chat=" + chat + '}';
    }


    
}
