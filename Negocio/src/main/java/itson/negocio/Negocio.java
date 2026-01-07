package itson.negocio;

import DTOs.CrearChatNuevoDTO;
import DTOs.LoginPedidoDTO;
import DTOs.LoginRespuestaDTO;
import DTOs.MensajeEnChatDTO;
import DTOs.UsuarioDTO;
import Eventos.*;
import Interfaz.IBusDeEventos;
import Objetos.Chat;
import Objetos.Mensaje;
import Objetos.Usuario;
import interfaz.INegocioListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Negocio {
    
    private IBusDeEventos bus;
    private List<INegocioListener> listeners;
    
    public List<Chat> memoriaChats; 
    private List<Usuario> memoriaUsuarios;  
    private Set<Integer> usuariosActivos = new HashSet<>();
    private Usuario usuarioActual;

    private String usuarioEsperandoRespuesta = null;

    public Negocio(IBusDeEventos bus) {
        this.bus = bus;
        this.listeners = new ArrayList<>();
        this.memoriaChats = new ArrayList<>();
        this.memoriaUsuarios = new ArrayList<>();
        configurarSuscripcionesDelBus();
    }

    private void configurarSuscripcionesDelBus() {
            bus.suscribir(evento -> {
            
            if (evento instanceof EventoMensajeRecibido eventoLocal) {
                EventoMensajeEnChat eventoReal = eventoLocal.getEventoOriginal();
                MensajeEnChatDTO dto = eventoReal.getMensaje();
                
                for (Chat c : memoriaChats) {
                    if (c.getId() == dto.getChat().getId()) {
                         c.getMensajes().add(new Mensaje(dto.getMensaje(), dto.getFechaMensaje(), dto.getUsuario()));
                         break;
                    }
                }
                notificarMensaje();
            }

            else if (evento instanceof EventoChatRecibido eventoLocal) {
                EventoCrearChatNuevo eventoReal = eventoLocal.getEventoOriginal();
                CrearChatNuevoDTO dto = eventoReal.getMensaje();
                
                List<Usuario> parts = new ArrayList<>();
                parts.add(new Usuario(dto.getUsuario1().getId(), dto.getUsuario1().getUsuario(), ""));
                parts.add(new Usuario(dto.getUsuario2().getId(), dto.getUsuario2().getUsuario(), ""));
                
                Chat nuevo = new Chat(memoriaChats.size() + 1, new ArrayList<>(), parts);
                memoriaChats.add(nuevo);
                
                notificarMensaje();
            }

            else if (evento instanceof EventoRespuestaLogin eventoResp) {
                if(eventoResp.getEvento().isExito()){
                    UsuarioDTO uDto = eventoResp.getEvento().getUsuarioLogueado();
                    this.usuarioActual = new Usuario(uDto.getId(), uDto.getNombre(), "");
                }
                notificarLogin(eventoResp.getEvento());
            }

            else if (evento instanceof EventoSincronizacion eventoSync) {
                if (eventoSync.getChats() != null) {
                    this.memoriaChats = eventoSync.getChats(); // Actualiza la lista local
                    System.out.println("Negocio: Chats sincronizados (" + memoriaChats.size() + ")");
                    notificarMensaje(); // Actualiza la vista
                }
            }

            else if (evento instanceof EventoEnviarUsuarios eventoUsers) {
                if (eventoUsers.getUsuarios() != null) {
                    
                    List<Usuario> usuariosM = new ArrayList<>();
                    
                    for(UsuarioDTO u : eventoUsers.getUsuarios()){
                    
                        usuariosM.add(new Usuario(u.getId(), u.getNombre(), u.getPassword()));
                    
                    }
                    
                    this.memoriaUsuarios = usuariosM;
                    notificarUsuarios();
                    System.out.println("Negocio: Usuarios sincronizados (" + memoriaUsuarios.size() + ")");
                    // Si tienes una vista de "Agregar Contacto", notifícala aquí
                }
            }
        });
    }





    public void enviarMensaje(MensajeEnChatDTO dto) {
        System.out.println("negoc");
        bus.publicar(new EventoMensajeEnChat(dto));
    }
    
    public void crearChat(CrearChatNuevoDTO dto) {
        bus.publicar(new EventoCrearChatNuevo(dto));
    }
    
    public void validarLogin(LoginPedidoDTO dto){
        this.usuarioEsperandoRespuesta = dto.getUsuario();
        bus.publicar(new EventoLogIn(dto));
    }

    public void agregarListener(INegocioListener listener) {
        this.listeners.add(listener);
    }


    private void notificarMensaje() {
        for (INegocioListener listener : listeners) {
            System.out.println(memoriaChats);
            listener.recibirChat(memoriaChats);
        }
    }
    
    private void notificarUsuarios() {
        for (INegocioListener listener : listeners) {
            listener.recibirUsuarios(memoriaUsuarios);
        }
    }
    
    private void notificarLogin(LoginRespuestaDTO dto) {
        for (INegocioListener listener : listeners) {
            listener.recibirLogin(dto);
        }
    }
    
    
}