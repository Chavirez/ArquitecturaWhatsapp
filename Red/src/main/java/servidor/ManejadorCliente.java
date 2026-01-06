package servidor;

import Interfaz.IBusDeEventos;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import emisor.Emisor;
import receptor.Receptor;
import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import servidor.EstadoServidor;
import utilidades.LocalDateTimeAdapter;
import DTOs.UsuarioDTO;
import Eventos.EventoEnviarUsuarios;
import Objetos.Usuario;
import java.util.ArrayList;
import java.util.List;

public class ManejadorCliente implements Runnable {
    private Socket socket;
    private IBusDeEventos bus;
    private PrintWriter escritor;
    private BufferedReader lector;

    public ManejadorCliente(Socket socket, IBusDeEventos bus) {
        this.socket = socket;
        this.bus = bus;
    }

    @Override
    public void run() {
        try {
            this.escritor = new PrintWriter(socket.getOutputStream(), true);
            this.lector = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            sincronizarEstadoActual();
            
            Emisor emisor = new Emisor(escritor, bus);
            Receptor receptor = new Receptor(lector, bus);

            Thread hiloEmisor = new Thread(emisor);
            Thread hiloReceptor = new Thread(receptor);
            
            hiloEmisor.start();
            hiloReceptor.start();

            System.out.println("Nuevo cliente conectado desde: " + socket.getInetAddress());

            hiloReceptor.join(); 

        } catch (IOException | InterruptedException e) {
            System.err.println("Error manejando cliente: " + e.getMessage());
        } finally {
            cerrarConexion();
        }
    }
    
    private void sincronizarEstadoActual() {
            try {
                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                        .create();

                EstadoServidor estado = EstadoServidor.getInstancia();

                System.out.println("Sincronizando cliente...");

                // -----------------------------------------------------------
                // 1. SINCRONIZAR USUARIOS
                // -----------------------------------------------------------
                List<Usuario> usuariosEnMemoria = estado.getUsuarios();
                List<UsuarioDTO> usuariosParaEnviar = new ArrayList<>();

                // Convertimos de Entidad (Dominio) a DTO (Transporte)
                synchronized (usuariosEnMemoria) { // Sincronizamos para evitar errores si alguien se registra justo ahora
                    for (Usuario u : usuariosEnMemoria) {
                        usuariosParaEnviar.add(new UsuarioDTO(u.getId(), u.getUsuario(), u.getContrasenia()));
                    }
                }

                // Creamos el evento y lo enviamos (JSON) directamente a ESTE cliente
                EventoEnviarUsuarios eventoUsuarios = new EventoEnviarUsuarios(usuariosParaEnviar);
                String jsonUsuarios = gson.toJson(eventoUsuarios);
                this.escritor.println(jsonUsuarios);

                System.out.println(" > Enviados " + usuariosParaEnviar.size() + " usuarios.");

                // -----------------------------------------------------------
                // 2. SINCRONIZAR CHATS (Simulación con EventoCrearChatNuevo)
                // -----------------------------------------------------------
                // Nota: Como vimos antes, esto solo envía la existencia del chat.
                // Idealmente deberías crear un EventoChatCompleto para enviar mensajes viejos.
                /* for (Chat chat : estado.getChats()) {
                     // Tu lógica de envío de chats aquí...
                }
                */

            } catch (Exception e) {
                System.err.println("Error al sincronizar cliente: " + e.getMessage());
                e.printStackTrace();
            }
        }
        
    private void cerrarConexion() {
        try {
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}