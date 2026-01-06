package emisor;

import DTOs.CrearChatNuevoDTO;
import DTOs.MensajeEnChatDTO;
import Interfaz.IBusDeEventos;
import com.google.gson.Gson;
import eventos.EventoCrearChatNuevo;
import eventos.EventoMensajeEnChat;
import java.io.PrintWriter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import interfaz.IEmisor;

public class Emisor implements IEmisor, Runnable {

    private BlockingQueue<String> cola;
    private PrintWriter escritor; 
    private IBusDeEventos bus;
    
    public Emisor(PrintWriter escritor, IBusDeEventos bus) {
        this.cola = new LinkedBlockingQueue<>();
        this.escritor = escritor;
        this.bus = bus;
        
        Gson gson = new Gson();
        
        bus.getInstancia().suscribir(evento -> {
                    if (evento instanceof EventoMensajeEnChat) {
                        EventoMensajeEnChat e = (EventoMensajeEnChat) evento;
                        
                        String datoSerializado = gson.toJson(e);
                        try {
                            this.enviar(datoSerializado); // Lo mete a la cola interna
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    }
                    if (evento instanceof EventoCrearChatNuevo) {
                        EventoCrearChatNuevo e = (EventoCrearChatNuevo) evento;
                        
                        String datoSerializado = gson.toJson(e);
                        try {
                            this.enviar(datoSerializado); // Lo mete a la cola interna
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
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