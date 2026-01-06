package emisor;

import Interfaz.IBusDeEventos;
import com.google.gson.Gson;
import Eventos.EventoCrearChatNuevo;
import Eventos.EventoMensajeEnChat;
import com.google.gson.GsonBuilder;
import java.io.PrintWriter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import interfaz.IEmisor;
import java.time.LocalDateTime;
import utilidades.LocalDateTimeAdapter;

public class Emisor implements IEmisor, Runnable {

    private BlockingQueue<String> cola;
    private PrintWriter escritor; 
    private IBusDeEventos bus;
    
    public Emisor(PrintWriter escritor, IBusDeEventos bus) {
        this.cola = new LinkedBlockingQueue<>();
        this.escritor = escritor;
        this.bus = bus;
        
        Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                    .create();
        
        bus.getInstancia().suscribir(evento -> {


                        String datoSerializado = gson.toJson(evento);
                        try {
                            this.enviar(datoSerializado); // Lo mete a la cola interna
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    
                });
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