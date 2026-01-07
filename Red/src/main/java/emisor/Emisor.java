package emisor;

import Interfaz.IBusDeEventos;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.PrintWriter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import interfaz.IEmisor;
import java.time.LocalDateTime;
import utilidades.LocalDateTimeAdapter;
// Imports de los eventos a filtrar
import Eventos.EventoSincronizacion;
import Eventos.EventoEnviarUsuarios;
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
        this.esCliente = esCliente;
        
        Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                    .create();
        
        this.bus.suscribir(evento -> {
            if (evento != null) {
                // --- FILTRO PARA ROMPER EL BUCLE ---
                if (this.esCliente) {
                    // El cliente NUNCA debe enviar estos eventos al servidor,
                    // solo debe recibirlos. Si los ve en el bus, es porque llegaron
                    // del Receptor y no debemos hacer eco.
                    if (evento instanceof EventoSincronizacion || 
                        evento instanceof EventoEnviarUsuarios || 
                        evento instanceof EventoRespuestaLogin) {
                        return; // Ignoramos el evento, no lo enviamos
                    }
                }
                // -----------------------------------

                String datoSerializado = gson.toJson(evento);
                try {
                    this.enviar(datoSerializado); 
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