package ensamblador;

import DTOs.CrearChatNuevoDTO;
import DTOs.MensajeEnChatDTO;
import Eventos.EventoCrearChatNuevo;
import Eventos.EventoMensajeEnChat;
import Objetos.Chat;
import Objetos.Mensaje;
import Objetos.Usuario;
import bus.BusDeEventos;
import java.util.ArrayList;
import java.util.List;
import servidor.EstadoServidor;
import servidor.Servidor;

public class MainServidor {

    public static void main(String[] args) {
        try {
            BusDeEventos bus = new BusDeEventos();
            int puerto = 4444;
            EstadoServidor estado = EstadoServidor.getInstancia();
            System.out.println("Iniciando servidor...");

            bus.suscribir(evento -> {
                try { 
                    
                    if (evento instanceof EventoMensajeEnChat e) {
                        MensajeEnChatDTO dto = e.getMensaje();
                        for (Chat c : estado.getChats()) {
                            if (c.getId() == dto.getChat().getId()) {
                                 c.getMensajes().add(new Mensaje(dto.getMensaje(), dto.getFechaMensaje(), dto.getUsuario()));
                                 System.out.println("SRV: Mensaje guardado en Chat " + c.getId()); 
                                 break;
                            }
                        }
                    } 
                    else if (evento instanceof EventoCrearChatNuevo e) {
                        CrearChatNuevoDTO dto = e.getMensaje();
                        if(dto.getUsuario1() == null || dto.getUsuario2() == null) {
                            System.err.println("SRV: Error, intentaron crear chat con usuarios nulos");
                            return; 
                        }
                    boolean yaExiste = false;
                        for (Chat c : estado.getChats()) {
                            List<Usuario> parts = c.getUsuarios();
                            if (parts.size() == 2) {
                                int id1 = parts.get(0).getId();
                                int id2 = parts.get(1).getId();

                                boolean caso1 = (id1 == dto.getUsuario1().getId() && id2 == dto.getUsuario2().getId());
                                boolean caso2 = (id1 == dto.getUsuario2().getId() && id2 == dto.getUsuario1().getId());

                                if (caso1 || caso2) {
                                    yaExiste = true;
                                    System.out.println("SRV: Solicitud ignorada. Ya existe chat entre " + id1 + " y " + id2);
                                    break;
                                }
                            }
                        }

                        if (yaExiste) {
                            return; 
                        }
                        Usuario u1 = null;
                        Usuario u2 = null;
                        
                        for(Usuario u : estado.getUsuarios()){
                            if(u.getId() == dto.getUsuario1().getId()) u1 = u;
                            if(u.getId() == dto.getUsuario2().getId()) u2 = u;
                        }
                        
                        if (u1 != null && u2 != null) {
                             // ... crear y guardar chat ...
                             int nuevoId = estado.getChats().size() + 1;
                             Chat nuevoChat = new Chat(nuevoId, new ArrayList<>(), List.of(u1, u2));
                             estado.agregarChat(nuevoChat);
                             System.out.println("SRV: Chat creado " + nuevoId);
                        }
                    }
                    
                } catch (Exception ex) {
                    System.err.println("Error procesando evento en MainServidor: " + ex.getMessage());
                    ex.printStackTrace();
                }
            });

            Servidor servidor = new Servidor(puerto, bus);
            new Thread(servidor).start();
            System.out.println("Servidor escuchando en puerto " + puerto);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}