/* Archivo: Negocio/src/main/java/itson/negocio/Negocio.java */
package itson.negocio;

import DTOs.*;
import Eventos.*;
import Interfaz.IBusDeEventos;
import Objetos.*;
import interfaz.INegocioListener;
import java.util.ArrayList;
import java.util.List;

public class Negocio {
    
    private IBusDeEventos bus;
    private List<INegocioListener> listeners;
    public List<Chat> memoriaChats; 
    private Usuario usuarioActual;
    
    private String usuarioEsperandoRespuesta = null;

    public Negocio(IBusDeEventos bus) {
        this.bus = bus;
        this.listeners = new ArrayList<>();
        this.memoriaChats = new ArrayList<>();
        configurarSuscripcionesDelBus();
    }

    private void configurarSuscripcionesDelBus() {
        bus.suscribir(evento -> {
            if (evento instanceof EventoRespuestaLogin eventoRespuesta) {
                procesarRespuestaLogin(eventoRespuesta);
            }
            else if (evento instanceof EventoSincronizacion eventoSync) {
                this.memoriaChats = eventoSync.getChats();
                notificarMensaje(); 
            }
            else if (evento instanceof EventoMensajeEnChat eventoMensaje) {
                MensajeEnChatDTO dto = eventoMensaje.getMensaje();
                for(Chat chat : memoriaChats){
                    if(chat.getId() == dto.getChat().getId()){
                        chat.getMensajes().add(new Mensaje(dto.getMensaje(), dto.getFechaMensaje(), dto.getUsuario()));
                    }
                }
                notificarMensaje();
            }
            else if (evento instanceof EventoEnviarUsuarios eventoUsuarios) {
                List<Usuario> listaConvertida = new ArrayList<>();
                for(UsuarioDTO dto : eventoUsuarios.getUsuarios()){
                    listaConvertida.add(new Usuario(dto.getId(), dto.getNombre(), ""));
                }
                notificarUsuarios(listaConvertida);
            }
        });
    }


    public void validarLogin(LoginPedidoDTO dto){
        this.usuarioEsperandoRespuesta = dto.getUsuario();
        bus.publicar(new EventoLogIn(dto));
    }
    
    public void cerrarSesion() {
        if (usuarioActual != null) {
            bus.publicar(new EventoCerrarSesion(usuarioActual.getId()));
            this.usuarioActual = null;
        }
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


    private void procesarRespuestaLogin(EventoRespuestaLogin evento) {
        LoginRespuestaDTO respuesta = evento.getEvento();
        
        if (this.usuarioEsperandoRespuesta != null && 
            respuesta.getUsuarioLogueado() != null &&
            respuesta.getUsuarioLogueado().getNombre().equals(this.usuarioEsperandoRespuesta)) {
            
            if (respuesta.isExito()) {
                UsuarioDTO uDTO = respuesta.getUsuarioLogueado();
                this.usuarioActual = new Usuario(uDTO.getId(), uDTO.getNombre(), uDTO.getPassword());
                this.usuarioEsperandoRespuesta = null; 
            } else {
                this.usuarioEsperandoRespuesta = null;
            }
            notificarLogin(respuesta);
        }
    }

    private void notificarMensaje() {
        for (INegocioListener l : listeners) l.recibirChat(memoriaChats);
    }
    private void notificarUsuarios(List<Usuario> usuarios) {
        for (INegocioListener l : listeners) l.recibirUsuarios(usuarios);
    }
    private void notificarLogin(LoginRespuestaDTO dto) {
        for (INegocioListener l : listeners) l.recibirLogin(dto);
    }
}