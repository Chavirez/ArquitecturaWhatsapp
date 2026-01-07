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
                            // Desempaquetamos el evento real
                            EventoMensajeEnChat eventoReal = eventoLocal.getEventoOriginal();
                            MensajeEnChatDTO dto = eventoReal.getMensaje();

                            // Tu lógica existente para guardar el mensaje en memoria...
                            for (Chat c : memoriaChats) {
                                if (c.getId() == dto.getChat().getId()) {
                                     // Convertir DTO a Mensaje y guardar
                                     // ...
                                     break;
                                }
                            }
                            notificarMensaje(); // Actualizar vista
                        }
            else if (evento instanceof EventoCrearChatNuevo eventoCrearChatNuevo) {
                CrearChatNuevoDTO dto = eventoCrearChatNuevo.getMensaje();
                
                if (this.usuarioActual != null && dto.getUsuario1().getId() == this.usuarioActual.getId()) {
                        System.out.println("Negocio: Ignorando eco de chat creado por mí.");
                        return; 
                    }
                if (this.usuarioActual != null && dto.getUsuario2().getId() == this.usuarioActual.getId()) {

                        List<Usuario> participantes = new ArrayList<>();
                        participantes.add(dto.getUsuario1());
                        participantes.add(dto.getUsuario2());

                        int nuevoId = this.memoriaChats.size() + 1; 

                        Chat chatNuevo = new Chat(nuevoId, new ArrayList<>(), participantes);
                        this.memoriaChats.add(chatNuevo);

                        System.out.println("Negocio: Chat nuevo recibido y agregado.");
                        notificarMensaje(); 
                }
            }
            else if (evento instanceof EventoSincronizacion eventoSync) {
                this.memoriaChats = eventoSync.getChats();
                System.out.println("Negocio: Chats sincronizados (" + memoriaChats.size() + ")");
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
            else if (evento instanceof EventoRespuestaLogin eventoRespuesta) {
                procesarRespuestaLogin(eventoRespuesta);
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