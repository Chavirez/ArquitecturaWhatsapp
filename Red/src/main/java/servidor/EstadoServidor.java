package servidor;

import Objetos.Chat;
import Objetos.Mensaje;
import Objetos.Usuario;
import java.time.LocalDateTime; // Importante para las fechas
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
        // 1. Creación de Usuarios
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
        
        // 2. Creación de Mensajes para el Chat 1 (Santiago y Alejandra)
        List<Mensaje> msjChat1 = new ArrayList<>();
        // Mensaje de Santiago (hace 2 horas)
        msjChat1.add(new Mensaje("Hola Alejandra, ¿cómo estás?", LocalDateTime.now().minusHours(2), u1));
        // Mensaje de Alejandra (hace 1 hora)
        msjChat1.add(new Mensaje("Hola Santi! Todo bien por aquí, ¿y tú?", LocalDateTime.now().minusHours(1), u2));
        // Mensaje de Santiago (ahora)
        msjChat1.add(new Mensaje("Todo excelente, programando el proyecto.", LocalDateTime.now(), u1));

        // 3. Creación de Mensajes para el Chat 2 (Santiago y Gabriel)
        List<Mensaje> msjChat2 = new ArrayList<>();
        msjChat2.add(new Mensaje("¿Vamos a jugar hoy?", LocalDateTime.now().minusMinutes(30), u3));
        msjChat2.add(new Mensaje("Va, me conecto en 10.", LocalDateTime.now().minusMinutes(5), u1));

        // 4. Creación de Chats
        // Chats con mensajes
        crearChatMock(1, u1, u2, msjChat1);
        crearChatMock(2, u1, u3, msjChat2);
        
        // Chats vacíos (usamos la versión corta del método)
        crearChatMock(3, u2, u3);
        crearChatMock(4, u1, u4);
        crearChatMock(5, u2, u4);
        crearChatMock(6, u3, u4);
        crearChatMock(7, u1, u5);
        crearChatMock(8, u2, u5);

        System.out.println("EstadoServidor: Mocks generados. Usuarios: " + usuarios.size() + ", Chats: " + chats.size());
    }

    // Método auxiliar 1: Crea chat con lista de mensajes específica
    private void crearChatMock(int idChat, Usuario u1, Usuario u2, List<Mensaje> mensajes) {
        List<Usuario> participantes = new ArrayList<>();
        participantes.add(u1);
        participantes.add(u2);
        
        Chat chat = new Chat(idChat, mensajes, participantes);
        this.chats.add(chat);
    }

    // Método auxiliar 2 (Sobrecarga): Crea chat vacío por defecto
    private void crearChatMock(int idChat, Usuario u1, Usuario u2) {
        crearChatMock(idChat, u1, u2, new ArrayList<>());
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