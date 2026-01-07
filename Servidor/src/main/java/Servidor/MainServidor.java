/* Archivo: Servidor/src/main/java/itson/servidor/MainServidor.java */
package Servidor;

import DTOs.LoginPedidoDTO;
import DTOs.LoginRespuestaDTO;
import DTOs.UsuarioDTO;
import Eventos.*;
import Objetos.Chat;
import Objetos.Mensaje;
import Objetos.Usuario;
import Servidor.Servidor;
import bus.BusDeEventos;
import java.util.ArrayList;

public class MainServidor {

    public static void main(String[] args) {
        try {
            int puerto = 4444;
            BusDeEventos bus = new BusDeEventos();
            EstadoServidor estado = EstadoServidor.getInstancia();
            
            bus.suscribir(evento -> {
                
                if (evento instanceof EventoLogIn e) {
                    procesarLogin(e, bus, estado);
                }
                
                else if (evento instanceof EventoCerrarSesion e) {
                    estado.desconectar(e.getIdUsuarioACerrar());
                }

                else if (evento instanceof EventoMensajeEnChat e) {
                    for (Chat c : estado.getChats()) {
                        if (c.getId() == e.getMensaje().getChat().getId()) {
                             c.getMensajes().add(new Mensaje(
                                 e.getMensaje().getMensaje(), 
                                 e.getMensaje().getFechaMensaje(), 
                                 e.getMensaje().getUsuario()
                             ));
                             break;
                        }
                    }
                    System.out.println("SERVIDOR: Mensaje guardado.");
                } 
                
                else if (evento instanceof EventoCrearChatNuevo e) {
                    Chat nuevoChat = new Chat(estado.getChats().size() + 1, new ArrayList<>(), new ArrayList<>()); 
                    estado.agregarChat(nuevoChat);
                    System.out.println("SERVIDOR: Chat nuevo creado.");
                }
            });

            Servidor servidor = new Servidor(puerto, bus);
            new Thread(servidor).start();

            System.out.println("--- SERVIDOR DE CHAT INICIADO EN PUERTO " + puerto + " ---");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void procesarLogin(EventoLogIn evento, BusDeEventos bus, EstadoServidor estado) {
        LoginPedidoDTO pedido = evento.getEvento();
        Usuario usuarioEncontrado = null;

        for (Usuario u : estado.getUsuarios()) {
            if (u.getUsuario().equals(pedido.getUsuario()) && 
                u.getContrasenia().equals(pedido.getPassword())) {
                usuarioEncontrado = u;
                break;
            }
        }

        if (usuarioEncontrado == null) {
            UsuarioDTO dummy = new UsuarioDTO(0, pedido.getUsuario(), "");
            bus.publicar(new EventoRespuestaLogin(new LoginRespuestaDTO(false, "Credenciales incorrectas", dummy)));
            return;
        }

        boolean pudoConectarse = estado.intentarConectar(usuarioEncontrado.getId());
        
        UsuarioDTO uDto = new UsuarioDTO(usuarioEncontrado.getId(), usuarioEncontrado.getUsuario(), "");
        
        if (pudoConectarse) {
            System.out.println("SERVIDOR: Usuario " + uDto.getNombre() + " se ha conectado.");
            bus.publicar(new EventoRespuestaLogin(new LoginRespuestaDTO(true, "Login exitoso", uDto)));
        } else {
            System.out.println("SERVIDOR: Login rechazado para " + uDto.getNombre() + " (Ya en línea).");
            bus.publicar(new EventoRespuestaLogin(new LoginRespuestaDTO(false, "El usuario ya tiene una sesión activa.", uDto)));
        }
    }
}