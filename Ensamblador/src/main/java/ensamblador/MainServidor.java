package ensamblador;

import Eventos.EventoCrearChatNuevo;
import Eventos.EventoMensajeEnChat;
import Objetos.Chat;
import Objetos.Mensaje;
import bus.BusDeEventos;
import java.util.ArrayList;
import java.util.List;
import Servidor.EstadoServidor;
import Servidor.Servidor;

public class MainServidor {

public static void main(String[] args) {
        try {
            int puerto = 4444;

            EstadoServidor estado = EstadoServidor.getInstancia();
            
            bus.suscribir(evento -> {
                if (evento instanceof EventoMensajeEnChat e) {
                    for (Chat c : estado.getChats()) {
                        if (c == e.getMensaje().getChat()) {
                             c.getMensajes().add(new Mensaje(
                                 e.getMensaje().getMensaje(), 
                                 e.getMensaje().getFechaMensaje(), 
                                 e.getMensaje().getUsuario()
                             ));
                             break;
                        }
                    }
                    System.out.println("SERVIDOR: Mensaje guardado en memoria.");
                } 
                else if (evento instanceof EventoCrearChatNuevo e) {
 
                    List<Mensaje> vacio = new ArrayList<>();
                    // (Lógica simplificada para recuperar objetos Usuario reales basada en IDs)
                    Chat nuevoChat = new Chat(estado.getChats().size() + 1, vacio, new ArrayList<>()); 
                    estado.agregarChat(nuevoChat);
                    
                    System.out.println("SERVIDOR: Chat nuevo guardado en memoria.");
                }
            });
            // --------------------------------------------------------------

            Servidor servidor = new Servidor(puerto, bus);
            new Thread(servidor).start();

            System.out.println("--------------------------------------------------");
            System.out.println(" SERVIDOR INICIADO EN EL PUERTO: " + puerto);
            System.out.println(" Esperando clientes...");
            System.out.println("--------------------------------------------------");

        } catch (Exception e) {
            System.err.println("Error al iniciar el servidor: " + e.getMessage());
            e.printStackTrace();
        }
    }
}