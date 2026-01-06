package itson.negocio;

import DTOs.CrearChatNuevoDTO;
import DTOs.MensajeEnChatDTO;
import DTOs.UsuarioDTO;
import Eventos.*;
import Interfaz.IBusDeEventos;
import Objetos.Chat;
import Objetos.Mensaje;
import Objetos.Usuario;
import interfaz.INegocioListener;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Negocio {
    
    private IBusDeEventos bus;
    private List<INegocioListener> listeners;
    
    private List<Chat> memoriaChats; 
    private List<Usuario> memoriaUsuarios; 

    public Negocio(IBusDeEventos bus) {
        this.bus = bus;
        this.listeners = new ArrayList<>();
        this.memoriaChats = new ArrayList<>();
        this.memoriaUsuarios = new ArrayList<>();
        configurarSuscripcionesDelBus();
    }

    private void configurarSuscripcionesDelBus() {
        bus.suscribir(evento -> {
            if (evento instanceof EventoMensajeEnChat eventoMensajeEnChat) {
                MensajeEnChatDTO dto = eventoMensajeEnChat.getMensaje();
                
                for(Chat chat : memoriaChats){
                
                    if(chat.getId() == dto.getIdChat()){
                    
                        Mensaje mensajeN = new Mensaje(dto.getMensaje(), dto.getFechaMensaje(), dto.getIdUsuario());
                        chat.getMensajes().add(mensajeN);
                        
                    }
                    
                }
                
                notificarMensaje();
            }
            else if (evento instanceof EventoCrearChatNuevo eventoCrearChatNuevo) {
                CrearChatNuevoDTO dto = eventoCrearChatNuevo.getMensaje();
                
                List<Mensaje> mVacio = new ArrayList<>();
                List<Usuario> usuarios = new ArrayList<>();
                
                for(Usuario u : memoriaUsuarios){
                
                    if(u.getId() == dto.getIdUsuario1() || u.getId() == dto.getIdUsuario2())
                        usuarios.add(u);
                    
                }
                
                Chat chatNuevo = new Chat(memoriaChats.size()+1, mVacio, usuarios);
                
                this.memoriaChats.add(chatNuevo);
                
                notificarMensaje();
            }
            else if (evento instanceof EventoEnviarUsuarios eventoEnviarUsuarios) { 
                 List<UsuarioDTO> usuarios = eventoEnviarUsuarios.getUsuarios();
                 
                 List<Usuario> usuariosAG = new ArrayList<>();
                 
                 for(UsuarioDTO u : usuarios){
                 
                     Usuario uN = new Usuario(u.getId(), u.getNombre(), u.getPassword());
                     usuariosAG.add(uN);
                     
                 }
                 
                 this.memoriaUsuarios = usuariosAG;
                 
                 notificarUsuarios();
            }
        });
    }

    
    public void enviarMensaje(MensajeEnChatDTO dto) {

        bus.publicar(new EventoMensajeEnChat(dto));
    }
    
    public void crearChat(CrearChatNuevoDTO dto) {

        bus.publicar(new EventoCrearChatNuevo(dto));
    }
    
    public void agregarListener(INegocioListener listener) {
        this.listeners.add(listener);
    }

    private void notificarMensaje() {
        for (INegocioListener listener : listeners) {
            listener.recibirChat(memoriaChats);
        }
    }
    
    private void notificarUsuarios() {
        for (INegocioListener listener : listeners) {
            listener.recibirUsuarios(memoriaUsuarios);
        }
    }
    

}