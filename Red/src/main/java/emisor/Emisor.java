package emisor;

import interfaz.IEmisor;
import java.io.PrintWriter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Emisor implements IEmisor, Runnable {

    private BlockingQueue<String> cola;
    private PrintWriter escritor; 
    
    public Emisor(PrintWriter escritor) {
        this.cola = new LinkedBlockingQueue<>();
        this.escritor = escritor;
    }

    @Override
    public void enviar(String dato) throws InterruptedException {
        this.cola.put(dato);
    }

    @Override
    public void run() {
        try {
            System.out.println("Emisor listo.");
            
            while (!Thread.currentThread().isInterrupted()) {
                String mensaje = this.cola.take();
                
                this.escritor.println(mensaje);
                
                System.out.println("Enviando: " + mensaje);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Emisor interrumpido.");
        } catch (Exception e) {
            System.err.println("Emisor error al escribir: " + e.getMessage());
        } finally {
             System.out.println("Hilo de emisión terminado.");
        }
    }
}