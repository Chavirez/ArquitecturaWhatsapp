package emisor;

import Eventos.EventoChatRecibido;
import Interfaz.IBusDeEventos;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.PrintWriter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import interfaz.IEmisor;
import java.time.LocalDateTime;
import utilidades.LocalDateTimeAdapter;
// Imports de eventos para filtrar
import Eventos.EventoSincronizacion;
import Eventos.EventoEnviarUsuarios;
import Eventos.EventoMensajeRecibido;
import Eventos.EventoRespuestaLogin;

public class Emisor implements IEmisor, Runnable {

    private BlockingQueue<String> cola;
    private PrintWriter escritor; 
    private IBusDeEventos bus;
    private boolean esCliente; // <--- NUEVA BANDERA
    
    // Constructor modificado: Agregamos 'boolean esCliente'
    public Emisor(PrintWriter escritor, IBusDeEventos bus, boolean esCliente) {
        this.cola = new LinkedBlockingQueue<>();
        this.escritor = escritor;
        this.bus = bus;
        this.esCliente = esCliente; // Guardamos quién soy
        
        Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                    .create();
        
        // Usamos 'this.bus' directamente (sin getInstancia)
        this.bus.suscribir(evento -> {
            if (evento != null) {
                
                if (evento instanceof EventoMensajeRecibido || 
                    evento instanceof EventoChatRecibido) {
                return; 
                                }
                // --- FILTRO ANTI-ECO ---
                if (this.esCliente) {
                    // Si soy Cliente, IGNORO los eventos que envía el servidor.
                    // Solo debo enviar lo que genera mi Interfaz Gráfica.
                    if (evento instanceof EventoSincronizacion || 
                        evento instanceof EventoEnviarUsuarios || 
                        evento instanceof EventoRespuestaLogin) {
                        return; // NO ENVIAR
                    }
                }
                // -----------------------

                String datoSerializado = gson.toJson(evento);
                try {
                    this.enviar(datoSerializado); 
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }
    
    // ... (El resto de los métodos enviar y run quedan igual) ...
    @Override
    public void enviar(String dato) throws InterruptedException {
        this.cola.put(dato);
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                String mensaje = this.cola.take();
                this.escritor.println(mensaje);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            System.err.println("Emisor error: " + e.getMessage());
        }
    }
}