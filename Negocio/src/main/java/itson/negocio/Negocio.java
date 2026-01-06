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
import java.time.LocalDateTime;
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
            else if (evento instanceof EventoLogIn eventoLogIn) {
                procesarLogin(eventoLogIn);
            }
            else if (evento instanceof EventoCerrarSesion eventoCerrarSesion) {
                procesarCierreSesion(eventoCerrarSesion);
            }
        });
    }

    private void procesarLogin(EventoLogIn evento) {
        LoginPedidoDTO request = evento.getEvento();
        Usuario usuarioEncontrado = null;

        for (Usuario u : memoriaUsuarios) {
            if (u.getUsuario().equals(request.getUsuario()) && 
                u.getContrasenia().equals(request.getPassword())) {
                usuarioEncontrado = u;
                break;
            }
        }

        if (usuarioEncontrado == null) {
            bus.publicar(new EventoRespuestaLogin(new LoginRespuestaDTO(false, "Usuario o contraseña incorrectos", null)));
            return;
        }

        if (usuariosActivos.contains(usuarioEncontrado.getId())) {
            bus.publicar(new EventoRespuestaLogin(new LoginRespuestaDTO(false, "El usuario ya tiene una sesión activa.", null)));
        } else {
            usuariosActivos.add(usuarioEncontrado.getId());
            
            UsuarioDTO uDto = new UsuarioDTO(usuarioEncontrado.getId(), usuarioEncontrado.getUsuario(), "");
            bus.publicar(new EventoRespuestaLogin(new LoginRespuestaDTO(true, "Login exitoso", uDto)));
            
            System.out.println("Negocio: Usuario " + uDto.getNombre() + " ha iniciado sesión.");
        }
    }

    private void procesarCierreSesion(EventoCerrarSesion evento) {
        int idUsuario = evento.getIdUsuarioACerrar();
        if (usuariosActivos.contains(idUsuario)) {
            usuariosActivos.remove(idUsuario);
            System.out.println("Negocio: Usuario ID " + idUsuario + " cerró sesión. Disponible para login.");
        }
    }
    public void enviarMensaje(MensajeEnChatDTO dto) {

        bus.publicar(new EventoMensajeEnChat(dto));
    }
    
    public void crearChat(CrearChatNuevoDTO dto) {

        bus.publicar(new EventoCrearChatNuevo(dto));
    }
    
    public void validarLogin(LoginPedidoDTO dto){
    
        bus.publicar(new EventoLogIn(dto));
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