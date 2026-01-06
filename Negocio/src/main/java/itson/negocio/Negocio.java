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

    // VARIABLE NUEVA: Para saber a quién estamos intentando loguear en este cliente
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
            if (evento instanceof EventoMensajeEnChat eventoMensajeEnChat) {
                // ... lógica existente de mensajes ...
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
                // ... lógica existente de crear chat ...
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
                 // ... lógica existente de usuarios ...
                 List<UsuarioDTO> usuarios = eventoEnviarUsuarios.getUsuarios();
                 List<Usuario> usuariosAG = new ArrayList<>();
                 for(UsuarioDTO u : usuarios){
                     Usuario uN = new Usuario(u.getId(), u.getNombre(), u.getPassword());
                     usuariosAG.add(uN);
                 }
                 this.memoriaUsuarios = usuariosAG;
                 notificarUsuarios();
            }
            // --- EVENTOS DE LOGIN ---
            else if (evento instanceof EventoLogIn eventoLogIn) {
                // Esto ocurre en el LADO SERVIDOR (o quien procesa la lógica)
                procesarLogin(eventoLogIn);
            }
            else if (evento instanceof EventoRespuestaLogin eventoRespuesta) {
                // Esto ocurre en el LADO CLIENTE (recibir la respuesta)
                procesarRespuestaLogin(eventoRespuesta);
            }
            else if (evento instanceof EventoCerrarSesion eventoCerrarSesion) {
                procesarCierreSesion(eventoCerrarSesion);
            }
        });
    }

    // --- LÓGICA DEL SERVIDOR (Procesar la petición) ---
    private void procesarLogin(EventoLogIn evento) {
        LoginPedidoDTO request = evento.getEvento(); // Asegúrate que sea getLoginPedidoDTO() o getEvento() según tu clase
        Usuario usuarioEncontrado = null;

        for (Usuario u : memoriaUsuarios) {
            if (u.getUsuario().equals(request.getUsuario()) && 
                u.getContrasenia().equals(request.getPassword())) {
                usuarioEncontrado = u;
                break;
            }
        }

        if (usuarioEncontrado == null) {
            // CAMBIO IMPORTANTE: Incluso si falla, enviamos un UsuarioDTO dummy con el nombre
            // para que el cliente sepa que SU intento falló.
            UsuarioDTO usuarioDummy = new UsuarioDTO(0, request.getUsuario(), "");
            bus.publicar(new EventoRespuestaLogin(new LoginRespuestaDTO(false, "Usuario o contraseña incorrectos", usuarioDummy)));
            return;
        }

        if (usuariosActivos.contains(usuarioEncontrado.getId())) {
            // CAMBIO IMPORTANTE: Enviamos el usuario para identificación
            UsuarioDTO usuarioDummy = new UsuarioDTO(usuarioEncontrado.getId(), usuarioEncontrado.getUsuario(), "");
            bus.publicar(new EventoRespuestaLogin(new LoginRespuestaDTO(false, "El usuario ya tiene una sesión activa.", usuarioDummy)));
        } else {
            usuariosActivos.add(usuarioEncontrado.getId());
            
            UsuarioDTO uDto = new UsuarioDTO(usuarioEncontrado.getId(), usuarioEncontrado.getUsuario(), "");
            bus.publicar(new EventoRespuestaLogin(new LoginRespuestaDTO(true, "Login exitoso", uDto)));
            
            System.out.println("Negocio: Usuario " + uDto.getNombre() + " ha iniciado sesión.");
        }
    }

    // --- LÓGICA DEL CLIENTE (Procesar la respuesta) ---
    private void procesarRespuestaLogin(EventoRespuestaLogin evento) {
        LoginRespuestaDTO respuesta = evento.getEvento();
        
        if (this.usuarioEsperandoRespuesta != null && respuesta.getUsuarioLogueado() != null) {
            
            String nombreUsuarioRespuesta = respuesta.getUsuarioLogueado().getNombre();
            
            if (nombreUsuarioRespuesta.equals(this.usuarioEsperandoRespuesta)) {
                // ¡Es para mí!
                
                if (respuesta.isExito()) {
                    // Actualizo mi usuario actual
                    UsuarioDTO uDTO = respuesta.getUsuarioLogueado();
                    this.usuarioActual = new Usuario(uDTO.getId(), uDTO.getNombre(), uDTO.getPassword());
                    
                    // Ya no espero más respuestas
                    this.usuarioEsperandoRespuesta = null; 
                } else {
                    // Falló, pero era para mí. 
                    // No limpio usuarioEsperandoRespuesta inmediatamente si quiero permitir reintentos 
                    // o lo limpio y obligo a dar click de nuevo. Lo limpiamos por seguridad:
                    this.usuarioEsperandoRespuesta = null;
                }

                // Notificar a la Vista (Controlador/Modelo)
                notificarLogin(respuesta);
            } 
            // Si no coinciden los nombres, ignoramos el evento (era para otro cliente)
        }
    }

    private void procesarCierreSesion(EventoCerrarSesion evento) {
        int idUsuario = evento.getIdUsuarioACerrar();
        if (usuariosActivos.contains(idUsuario)) {
            usuariosActivos.remove(idUsuario);
            System.out.println("Negocio: Usuario ID " + idUsuario + " cerró sesión. Disponible para login.");
        }
    }

    // --- MÉTODOS PÚBLICOS ---

    public void enviarMensaje(MensajeEnChatDTO dto) {
        bus.publicar(new EventoMensajeEnChat(dto));
    }
    
    public void crearChat(CrearChatNuevoDTO dto) {
        bus.publicar(new EventoCrearChatNuevo(dto));
    }
    
    public void validarLogin(LoginPedidoDTO dto){
        // CAMBIO: Antes de enviar, registramos a quién estamos esperando
        this.usuarioEsperandoRespuesta = dto.getUsuario();
        bus.publicar(new EventoLogIn(dto));
    }

    public void agregarListener(INegocioListener listener) {
        this.listeners.add(listener);
    }

    // --- NOTIFICADORES ---

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
    
    private void notificarLogin(LoginRespuestaDTO dto) {
        for (INegocioListener listener : listeners) {
            listener.recibirLogin(dto);
        }
    }
}