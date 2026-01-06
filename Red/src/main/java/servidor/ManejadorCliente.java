package servidor;

import Interfaz.IBusDeEventos;
import emisor.Emisor;
import receptor.Receptor;
import java.io.*;
import java.net.Socket;

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

    private void cerrarConexion() {
        try {
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}