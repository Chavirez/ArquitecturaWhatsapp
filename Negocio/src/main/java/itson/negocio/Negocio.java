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
            
            // 1. RECIBIR MENSAJE (Viene "disfrazado" del Receptor Cliente)
            if (evento instanceof EventoMensajeRecibido eventoLocal) {
                EventoMensajeEnChat eventoReal = eventoLocal.getEventoOriginal();
                MensajeEnChatDTO dto = eventoReal.getMensaje();
                
                // Buscar chat y agregar mensaje
                for (Chat c : memoriaChats) {
                    if (c.getId() == dto.getChat().getId()) {
                         c.getMensajes().add(new Mensaje(dto.getMensaje(), dto.getFechaMensaje(), dto.getUsuario()));
                         break;
                    }
                }
                notificarMensaje();
            }

            // 2. RECIBIR CHAT NUEVO (Viene "disfrazado" del Receptor Cliente)
            else if (evento instanceof EventoChatRecibido eventoLocal) {
                EventoCrearChatNuevo eventoReal = eventoLocal.getEventoOriginal();
                CrearChatNuevoDTO dto = eventoReal.getMensaje();
                
                // Reconstruir chat y agregar a memoria
                // (Tu lógica de agregar chat va aquí...)
                List<Usuario> parts = new ArrayList<>();
                parts.add(new Usuario(dto.getUsuario1().getId(), dto.getUsuario1().getUsuario(), ""));
                parts.add(new Usuario(dto.getUsuario2().getId(), dto.getUsuario2().getUsuario(), ""));
                
                // Usamos ID temporal o el que venga
                Chat nuevo = new Chat(memoriaChats.size() + 1, new ArrayList<>(), parts);
                memoriaChats.add(nuevo);
                
                notificarMensaje();
            }

            // 3. RESPUESTA LOGIN
            else if (evento instanceof EventoRespuestaLogin eventoResp) {
                if(eventoResp.getEvento().isExito()){
                    UsuarioDTO uDto = eventoResp.getEvento().getUsuarioLogueado();
                    this.usuarioActual = new Usuario(uDto.getId(), uDto.getNombre(), "");
                }
                notificarLogin(eventoResp.getEvento());
            }

            // 4. SINCRONIZACIÓN DE CHATS (¡RECUPERADO!)
            else if (evento instanceof EventoSincronizacion eventoSync) {
                if (eventoSync.getChats() != null) {
                    this.memoriaChats = eventoSync.getChats(); // Actualiza la lista local
                    System.out.println("Negocio: Chats sincronizados (" + memoriaChats.size() + ")");
                    notificarMensaje(); // Actualiza la vista
                }
            }

            // 5. SINCRONIZACIÓN DE USUARIOS (¡RECUPERADO!)
            else if (evento instanceof EventoEnviarUsuarios eventoUsers) {
                if (eventoUsers.getUsuarios() != null) {
                    
                    List<Usuario> usuariosM = new ArrayList<>();
                    
                    for(UsuarioDTO u : eventoUsers.getUsuarios()){
                    
                        usuariosM.add(new Usuario(u.getId(), u.getNombre(), u.getPassword()));
                    
                    }
                    
                    this.memoriaUsuarios = usuariosM;
                    System.out.println("Negocio: Usuarios sincronizados (" + memoriaUsuarios.size() + ")");
                    // Si tienes una vista de "Agregar Contacto", notifícala aquí
                }
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
            UsuarioDTO usuarioDummy = new UsuarioDTO(0, request.getUsuario(), "");
            bus.publicar(new EventoRespuestaLogin(new LoginRespuestaDTO(false, "Usuario o contraseña incorrectos", usuarioDummy)));
            return;
        }

        if (usuariosActivos.contains(usuarioEncontrado.getId())) {
            UsuarioDTO usuarioDummy = new UsuarioDTO(usuarioEncontrado.getId(), usuarioEncontrado.getUsuario(), "");
            bus.publicar(new EventoRespuestaLogin(new LoginRespuestaDTO(false, "El usuario ya tiene una sesión activa.", usuarioDummy)));
        } else {
            usuariosActivos.add(usuarioEncontrado.getId());
            
            UsuarioDTO uDto = new UsuarioDTO(usuarioEncontrado.getId(), usuarioEncontrado.getUsuario(), "");
            bus.publicar(new EventoRespuestaLogin(new LoginRespuestaDTO(true, "Login exitoso", uDto)));
            
            System.out.println("Negocio: Usuario " + uDto.getNombre() + " ha iniciado sesión.");
        }
    }

    private void procesarRespuestaLogin(EventoRespuestaLogin evento) {
        LoginRespuestaDTO respuesta = evento.getEvento();
        
        if (this.usuarioEsperandoRespuesta != null && respuesta.getUsuarioLogueado() != null) {
            
            String nombreUsuarioRespuesta = respuesta.getUsuarioLogueado().getNombre();
            
            if (nombreUsuarioRespuesta.equals(this.usuarioEsperandoRespuesta)) {
                
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