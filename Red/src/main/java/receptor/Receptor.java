package receptor;

import Interfaz.IBusDeEventos;
import interfaz.IReceptor;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Receptor implements IReceptor, Runnable {

    private BufferedReader lector;
    private BlockingQueue<String> cola;
    private IBusDeEventos bus;

    public Receptor(BufferedReader lector, IBusDeEventos bus) {
        this.cola = new LinkedBlockingQueue<>();
        this.lector = lector;
        this.bus = bus;
    }

    @Override
    public void run() {
        try {
            System.out.println("Receptor iniciado:");
            while (!Thread.currentThread().isInterrupted()) {
                String datoRecibido = lector.readLine();
                if (datoRecibido != null) {
                    System.out.println("Datos recibidos en Red: " + datoRecibido);
                }
            }
        } catch (IOException e) {
            System.err.println("Error en recepción: " + e.getMessage());
        }
    }
    
    @Override
    public void recibir() {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    String mensaje = cola.take();

                    bus.getInstancia().publicar(mensaje);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
}