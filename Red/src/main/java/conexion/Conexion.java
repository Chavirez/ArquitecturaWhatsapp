package conexion;

import Interfaz.IBusDeEventos;
import bus.BusDeEventos;
import emisor.Emisor;
import receptor.Receptor;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Conexion {
    
    private Socket socket;
    private IBusDeEventos bus;
    
    private Emisor emisor; 
    private Receptor receptor;

    public Conexion(String ip, int puerto) throws IOException {
        System.out.println("Iniciando conexión con " + ip + ":" + puerto);
        
        this.bus = new BusDeEventos();
        
        this.socket = new Socket(ip, puerto);
        
        PrintWriter escritor = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader lector = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        this.emisor = new Emisor(escritor, bus);
        this.receptor = new Receptor(lector, bus);

        new Thread(emisor).start();
        new Thread(receptor).start();
        
        System.out.println("Conexión establecida y hilos de red iniciados.");
    }

    public IBusDeEventos getBus() {
        return bus;
    }
    
    public void cerrar() {
        try {
            if(socket != null && !socket.isClosed()) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}