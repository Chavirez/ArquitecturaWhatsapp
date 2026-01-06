package ensamblador;

import DTOs.CrearChatNuevoDTO;
import DTOs.UsuarioDTO;
import Eventos.*;
import bus.BusDeEventos;
import emisor.Emisor;
import itson.negocio.Negocio;
import receptor.Receptor;

import javax.swing.JOptionPane;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class MainCliente {

    public static void main(String[] args) {
        try {
            
            String ip = "192.168.100.2"; 
            int puerto = 4444;

            System.out.println("Iniciando Cliente conectando a: " + ip + ":" + puerto);

            BusDeEventos bus = new BusDeEventos();
            Negocio negocio = new Negocio(bus);

            Socket socket = new Socket(ip, puerto);
            
            PrintWriter escritor = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader lector = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            Emisor emisor = new Emisor(escritor, bus);
            Receptor receptor = new Receptor(lector, bus);

            new Thread(emisor).start();
            new Thread(receptor).start();

            
            generarMocks(bus);
       
            System.out.println(negocio.memoriaChats.toString());

            System.out.println("Cliente iniciado exitosamente.");

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "No se pudo conectar al servidor.\n" +
                    "Abriste el servidor?");
        }
    }

    private static void generarMocks(BusDeEventos bus) {
        List<UsuarioDTO> listaMocks = new ArrayList<>();
        
        listaMocks.add(new UsuarioDTO(1, "Alice", "1234"));
        listaMocks.add(new UsuarioDTO(2, "Bob", "1234"));
        listaMocks.add(new UsuarioDTO(3, "Charlie", "1234"));
        listaMocks.add(new UsuarioDTO(4, "Dave", "1234"));
        listaMocks.add(new UsuarioDTO(5, "Eve", "1234"));
        
        bus.publicar(new EventoEnviarUsuarios(listaMocks));
        
        bus.publicar(new EventoCrearChatNuevo(new CrearChatNuevoDTO(1,2)));
        bus.publicar(new EventoCrearChatNuevo(new CrearChatNuevoDTO(1,3)));
        bus.publicar(new EventoCrearChatNuevo(new CrearChatNuevoDTO(2,3)));
        bus.publicar(new EventoCrearChatNuevo(new CrearChatNuevoDTO(1,4)));
        bus.publicar(new EventoCrearChatNuevo(new CrearChatNuevoDTO(2,4)));
        bus.publicar(new EventoCrearChatNuevo(new CrearChatNuevoDTO(3,4)));
        bus.publicar(new EventoCrearChatNuevo(new CrearChatNuevoDTO(1,5)));
        bus.publicar(new EventoCrearChatNuevo(new CrearChatNuevoDTO(2,5)));

        
    }
}