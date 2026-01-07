package Servidor;

import Interfaz.IBusDeEventos;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import emisor.Emisor;
import receptor.Receptor;
import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import Servidor.EstadoServidor;
import utilidades.LocalDateTimeAdapter;
import DTOs.UsuarioDTO;
import Eventos.EventoEnviarUsuarios;
import Eventos.EventoSincronizacion;
import Objetos.Chat;
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

            // 1. SINCRONIZAR USUARIOS (Esto ya lo tienes y funciona)
            List<Usuario> usuariosEnMemoria = estado.getUsuarios();
            List<UsuarioDTO> usuariosParaEnviar = new ArrayList<>();
            synchronized (usuariosEnMemoria) {
                for (Usuario u : usuariosEnMemoria) {
                    usuariosParaEnviar.add(new UsuarioDTO(u.getId(), u.getUsuario(), u.getContrasenia()));
                }
            }
            EventoEnviarUsuarios eventoUsuarios = new EventoEnviarUsuarios(usuariosParaEnviar);
            this.escritor.println(gson.toJson(eventoUsuarios));

            // 2. SINCRONIZAR CHATS (Esto es lo que falta)
            // Obtenemos la lista real de chats del servidor
            List<Chat> chatsDelServidor = estado.getChats();

            // Empaquetamos la lista en el evento que creaste
            EventoSincronizacion eventoSync = new EventoSincronizacion(chatsDelServidor);

            // Lo enviamos al cliente
            String jsonChats = gson.toJson(eventoSync);
            this.escritor.println(jsonChats);

            System.out.println(" > Sincronizados " + chatsDelServidor.size() + " chats al cliente.");

        } catch (Exception e) {
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