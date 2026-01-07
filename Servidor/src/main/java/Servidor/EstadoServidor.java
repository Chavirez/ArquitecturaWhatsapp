package Servidor;

import Objetos.Chat;
import Objetos.Mensaje;
import Objetos.Usuario;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EstadoServidor {
    
    private static EstadoServidor instancia;
    private List<Chat> chats;
    private List<Usuario> usuarios;

    private EstadoServidor() {
        this.chats = Collections.synchronizedList(new ArrayList<>());
        this.usuarios = Collections.synchronizedList(new ArrayList<>());
        
        generarMocks();
    }

    public static synchronized EstadoServidor getInstancia() {
        if (instancia == null) {
            instancia = new EstadoServidor();
        }
        return instancia;
    }

    private void generarMocks() {
        Usuario u1 = new Usuario(1, "Santiago", "1234");
        Usuario u2 = new Usuario(2, "Alejandra", "1234");
        Usuario u3 = new Usuario(3, "Gabriel", "1234");
        Usuario u4 = new Usuario(4, "Luis", "1234");
        Usuario u5 = new Usuario(5, "Romina", "1234");

        this.usuarios.add(u1);
        this.usuarios.add(u2);
        this.usuarios.add(u3);
        this.usuarios.add(u4);
        this.usuarios.add(u5);
        
        crearChatMock(1, u1, u2);
        crearChatMock(2, u1, u3);
        crearChatMock(3, u2, u3);
        crearChatMock(4, u1, u4);
        crearChatMock(5, u2, u4);
        crearChatMock(6, u3, u4);
        crearChatMock(7, u1, u5);
        crearChatMock(8, u2, u5);

        System.out.println("EstadoServidor: Mocks generados. Usuarios: " + usuarios.size() + ", Chats: " + chats.size());
    }

    private void crearChatMock(int idChat, Usuario u1, Usuario u2) {
        List<Usuario> participantes = new ArrayList<>();
        participantes.add(u1);
        participantes.add(u2);
        
        List<Mensaje> mensajesVacios = new ArrayList<>();
        
        Chat chat = new Chat(idChat, mensajesVacios, participantes);
        this.chats.add(chat);
    }

    public List<Chat> getChats() {
        return chats;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void agregarChat(Chat chat) {
        this.chats.add(chat);
    }

    public void agregarUsuario(Usuario usuario) {
        this.usuarios.add(usuario);
    }
}