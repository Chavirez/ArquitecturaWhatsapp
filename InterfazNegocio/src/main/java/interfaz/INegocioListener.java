package interfaz;

import Objetos.Chat;
import Objetos.Usuario;
import java.util.List;

public interface INegocioListener {
    void recibirUsuarios(List<Usuario> usuarios);
    void recibirChat(List<Chat> chats);
}