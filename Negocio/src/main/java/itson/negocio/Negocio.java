/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package itson.negocio;

import DTOs.CrearChatNuevoDTO;
import DTOs.MensajeEnChatDTO;
import Eventos.EventoCrearChatNuevo;
import Eventos.EventoMensajeEnChat;
import Interfaz.IBusDeEventos;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author santi
 */
public class Negocio {
    private Map<Integer, List<MensajeEnChatDTO>> memoriaMensajes;
    private IBusDeEventos bus;

    public Negocio(IBusDeEventos bus) {
        this.bus = bus;
        this.memoriaMensajes = new HashMap<>();
        configurarSuscripciones();
    }

    private void configurarSuscripciones() {
        // Suscripción al bus para recibir mensajes en tiempo real [cite: 1, 11]
        bus.getInstancia().suscribir(evento -> {
            if (evento instanceof EventoMensajeEnChat) {
                procesarMensajeRecibido((EventoMensajeEnChat) evento);
            } else if (evento instanceof EventoCrearChatNuevo) {
                procesarNuevoChat((EventoCrearChatNuevo) evento);
            }
        });
    }

    private void procesarMensajeRecibido(EventoMensajeEnChat evento) {
        MensajeEnChatDTO dto = evento.getMensaje();

        // 1. Validaciones Lógicas (Requisito del examen) 
        if (dto.getMensaje() == null || dto.getMensaje().trim().isEmpty()) {
            System.err.println("Negocio: Intento de guardar un mensaje vacío ignorado.");
            return;
        }

        // 2. Almacenamiento en memoria segmentado por Chat ID 
        memoriaMensajes.computeIfAbsent(dto.getIdChat(), k -> new ArrayList<>()).add(dto);
        
        System.out.println("Negocio: Mensaje validado y guardado en memoria para el chat " + dto.getIdChat());
        
        // Aquí se podría publicar un evento de "REFRESCO_UI" si fuera necesario
    }

    private void procesarNuevoChat(EventoCrearChatNuevo evento) {
        CrearChatNuevoDTO dto = evento.getMensaje();
        // Lógica para inicializar una nueva lista de mensajes en memoria
        memoriaMensajes.putIfAbsent(dto.hashCode(), new ArrayList<>());
        System.out.println("Negocio: Nuevo chat registrado entre usuarios " + dto.getIdUsuario1() + " y " + dto.getIdUsuario2());
    }

    // Método para que la UI recupere los mensajes (Usabilidad) 
    public List<MensajeEnChatDTO> obtenerMensajesPorChat(int idChat) {
        return memoriaMensajes.getOrDefault(idChat, new ArrayList<>());
    }
}
