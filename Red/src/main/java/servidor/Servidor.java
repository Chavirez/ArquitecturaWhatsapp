package servidor;

import Interfaz.IBusDeEventos;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor implements Runnable {
    private int puerto;
    private IBusDeEventos bus;
    private boolean ejecutando = true;

    public Servidor(int puerto, IBusDeEventos bus) {
        this.puerto = puerto;
        this.bus = bus;
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(puerto)) {
            System.out.println("Servidor de Chat iniciado en el puerto " + puerto);

            while (ejecutando) {
                Socket socketCliente = serverSocket.accept();
                
                ManejadorCliente manejador = new ManejadorCliente(socketCliente, bus);
                new Thread(manejador).start();
            }
        } catch (IOException e) {
            System.err.println("Error en el servidor: " + e.getMessage());
        }
    }
    
    public void detener() {
        this.ejecutando = false;
    }
}