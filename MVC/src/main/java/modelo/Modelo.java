package modelo;

import DTOs.CrearChatNuevoDTO;
import DTOs.LoginPedidoDTO;
import DTOs.LoginRespuestaDTO;
import DTOs.MensajeEnChatDTO;
import DTOs.UsuarioDTO;
import Objetos.Chat;
import Objetos.Usuario;
import interfaz.INegocioListener;
import itson.negocio.Negocio;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import observadores.ObservadoChat;
import observadores.ObservadoLogin;
import observadores.ObservadorChat;
import observadores.ObservadorLogin;

public class Modelo implements INegocioListener, ObservadoLogin, ObservadoChat {

    private Negocio negocio; 
    
    private Usuario usuarioLocal;
    private Chat chatSeleccionado;
    private List<Chat> chats;
    private List<Usuario> usuarios;
    
    private List<ObservadorChat> chat = new ArrayList<>();
    private List<ObservadorLogin> log = new ArrayList<>();
    private Consumer<List<Chat>> notificarVistaChats; 
    
    public Modelo(Negocio negocio) {
        this.negocio = negocio;
        
        this.negocio.agregarListener(this);
        
    }
    
    public void agregarListenerChat(ObservadorChat listener) {
        this.chat.add(listener);
    }
    
    public void agregarListenerLogin(ObservadorLogin listener) {
        this.log.add(listener);
    }

    public void suscribirListaChats(Consumer<List<Chat>> metodoVista) {
            this.notificarVistaChats = metodoVista;
        }


    public void intentarLogin(String usuario, String pass) {
            LoginPedidoDTO loginDto = new LoginPedidoDTO(usuario, pass);
 
            negocio.validarLogin(loginDto); 
        }
    
    @Override
        public void notificarLogin(LoginRespuestaDTO respuesta) {

            for (ObservadorLogin obs : log) {
                obs.actualizar(respuesta);
            }
        }

        
    
    
    @Override
    public void notificarChat(){
        
        System.out.println(negocio.memoriaChats.toString());

        for (ObservadorChat obs : chat) {
            obs.actualizar(this);
        }
    }
    
    @Override
    public void recibirLogin(LoginRespuestaDTO dto) {
        if (dto.getUsuarioLogueado() != null) {
                setUsuarioLocal(new Usuario(
                    dto.getUsuarioLogueado().getId(), 
                    dto.getUsuarioLogueado().getNombre(), 
                    dto.getUsuarioLogueado().getPassword()
                ));
            }

            
            // 2. IMPORTANTE: Notificar a la Vista (FrameLogIn) para que reaccione
            notificarLogin(dto);
    }
    @Override
    public void recibirUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }
    
    @Override
    public void recibirChat(List<Chat> chats) {
        this.chats = chats; 

        notificarChat();
    }
    
    public void crearChat(CrearChatNuevoDTO dto) {
            negocio.crearChat(dto);
        }
    
    public void enviarMensaje(MensajeEnChatDTO dto){
        negocio.enviarMensaje(dto);
    }
    
    public List<Usuario> getUsuariosDisponiblesParaChat() {
        List<Usuario> disponibles = new ArrayList<>();



        System.out.println("aqui");
        Set<Integer> idsOcupados = new HashSet<>();
        
        for (Chat c : chats) {
            boolean estoyEnEsteChat = false;
            int idDelOtro = -1;
            
            for (Usuario participante : c.getUsuarios()) {
                if (participante.getId() == usuarioLocal.getId()) {
                    estoyEnEsteChat = true;
                } else {
                    idDelOtro = participante.getId();
                }
            }
            
            if (estoyEnEsteChat && idDelOtro != -1) {
                idsOcupados.add(idDelOtro);
            }
        }

        for (Usuario u : usuarios) {
            if (u.getId() != usuarioLocal.getId() && !idsOcupados.contains(u.getId())) {
                disponibles.add(u);
            }
        }
        
        return disponibles;
    }
    
    public void setUsuarioLocal(Usuario usuario) {
        this.usuarioLocal = usuario;
    }

    public Usuario getUsuarioLocal() {
        return usuarioLocal;
    }

    public List<Chat> getChat() {
                System.out.println("a");

        return chats;
    }

    public void setChat(List<Chat> chat) {
        this.chats = chat;
    }

    public Chat getChatSeleccionado() {
        return chatSeleccionado;
    }

    public void setChatSeleccionado(Chat chatSeleccionado) {
        this.chatSeleccionado = chatSeleccionado;
    }
    
}