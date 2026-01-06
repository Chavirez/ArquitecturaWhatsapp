/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package itson.negocio;

import DTOs.CrearChatNuevoDTO;
import DTOs.MensajeEnChatDTO;
import Eventos.EventoCrearChatNuevo;
import Eventos.EventoMensajeEnChat;
import Interfaz.IBusDeEventos;
import Objetos.Chat;
import Objetos.Usuario;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author santi
 */
public class Negocio {
    private List<Chat> memoriaChats;
    private List<Usuario> memoriaUsuarios;
    private IBusDeEventos bus;

    public Negocio(IBusDeEventos bus) {
        this.bus = bus;
        this.memoriaChats = new ArrayList<>();
        this.memoriaUsuarios = new ArrayList<>();
        configurarSuscripciones();
    }

    private void configurarSuscripciones() {
        
        bus.getInstancia().suscribir(evento -> {
            if (evento instanceof EventoMensajeEnChat eventoMensajeEnChat) {
                procesarMensajeRecibido(eventoMensajeEnChat);
            } else if (evento instanceof EventoCrearChatNuevo eventoCrearChatNuevo) {
                procesarNuevoChat(eventoCrearChatNuevo);
            }
        });
    }

    private void procesarMensajeRecibido(EventoMensajeEnChat evento) {
        MensajeEnChatDTO dto = evento.getMensaje();

        // 2. Almacenamiento en memoria segmentado por Chat ID 
//        memoriaMensajes.add
        
        System.out.println("Negocio: Mensaje validado y guardado en memoria para el chat " + dto.getIdChat());
        
        // Aquí se podría publicar un evento de "REFRESCO_UI" si fuera necesario
    }

    private void procesarNuevoChat(EventoCrearChatNuevo evento) {
        CrearChatNuevoDTO dto = evento.getMensaje();
        // Lógica para inicializar una nueva lista de mensajes en memoria
        List<Usuario> usuarios = new ArrayList<>();
//        usuarios.add(dto.getIdUsuario1());
//        usuarios.add(dto.getIdUsuario2());
//        
//        Chat nuevoChat = new Chat(mensajes, usuarios)
//        memoriaMensajes.putIfAbsent(dto.hashCode(), new ArrayList<>());
        System.out.println("Negocio: Nuevo chat registrado entre usuarios " + dto.getIdUsuario1() + " y " + dto.getIdUsuario2());
    }

 
}
