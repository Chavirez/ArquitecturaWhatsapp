package modelo;

import DTOs.LoginPedidoDTO;
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
import observadores.ObservadoChat;
import observadores.ObservadoLogin;
import observadores.ObservadorChat;
import observadores.ObservadorLogin;

public class Modelo implements INegocioListener, ObservadoLogin, ObservadoChat {

    private Negocio negocio; 
    
    private Usuario usuarioLocal;
    private Chat chatActual;
    private List<MensajeEnChatDTO> historialMensajes;
    
    private List<ObservadorChat> chat = new ArrayList<>();
    private List<ObservadorLogin> log = new ArrayList<>();
    
    public Modelo(Negocio negocio) {
        this.negocio = negocio;
        this.historialMensajes = new ArrayList<>();
        
    }
    
    public void agregarListenerChat(ObservadorChat listener) {
        this.chat.add(listener);
    }
    
    public void agregarListenerLogin(ObservadorLogin listener) {
        this.log.add(listener);
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

    public void intentarLogin(String usuario, String pass) {
            LoginPedidoDTO loginDto = new LoginPedidoDTO(usuario, pass);
 
            negocio.validarLogin(loginDto); 
        }



//        public void cerrarSesion() {
//            if (usuarioLocal != null) {
//                // Avisar al servidor que libere el ID
//                negocio.cerrarSesion(usuarioLocal.getId());
//            }
//        }
//    }
    
    
    @Override
    public void notificarLog(){
        
        
        
    }

        
    
    
    @Override
    public void notificarChat(){
    }
    
    @Override
    public void recibirLogin(Usuario usuario) {
        setUsuarioLocal(usuario);
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
    
}