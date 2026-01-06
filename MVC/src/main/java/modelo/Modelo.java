package modelo;

import DTOs.MensajeEnChatDTO;
import DTOs.UsuarioDTO;
import Objetos.Chat;
import Objetos.Usuario;
import interfaz.INegocioListener;
import itson.negocio.Negocio;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Modelo implements INegocioListener {

    private Negocio negocio; 
    
    private Usuario usuarioLocal;
    private Chat chatActual;
    private List<MensajeEnChatDTO> historialMensajes;
    
    private Consumer<MensajeEnChatDTO> notificarVistaMensaje;

    public Modelo(Negocio negocio) {
        this.negocio = negocio;
        this.historialMensajes = new ArrayList<>();
        
    }

    public void enviarMensaje(String texto) {
        if (usuarioLocal == null) return;

        MensajeEnChatDTO dto = new MensajeEnChatDTO(
                texto, 
                LocalDateTime.now(), 
                usuarioLocal.getId(), 
                1 
        );
        
        negocio.enviarMensaje(dto);
    }


    @Override
    public void recibirUsuarios(List<Usuario> usuarios) {
        System.out.println("[MODELO] Lista de usuarios actualizada: " + usuarios.size());
    }
    
    @Override
    public void recibirChat(List<Chat> chats) {
        System.out.println("[MODELO] Lista de chats actualizada: " + chats.size());
    }

    public void setUsuarioLocal(Usuario usuario) {
        this.usuarioLocal = usuario;
    }

    public Usuario getUsuarioLocal() {
        return usuarioLocal;
    }

    public Chat getChatActual() {
        return chatActual;
    }

    public void setChatActual(Chat chatActual) {
        this.chatActual = chatActual;
    }
    
    public void suscribirVista(Consumer<MensajeEnChatDTO> metodoVista) {
        this.notificarVistaMensaje = metodoVista;
    }
}