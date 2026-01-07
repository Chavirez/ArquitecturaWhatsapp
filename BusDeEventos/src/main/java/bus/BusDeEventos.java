package bus;

import Interfaz.IBusDeEventos;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList; // <--- IMPORTANTE
import java.util.function.Consumer;

public class BusDeEventos implements IBusDeEventos {
    
    // Usamos CopyOnWriteArrayList para que sea seguro usarlo con hilos (Thread-Safe)
    private List<Consumer<Object>> suscriptores;

    public BusDeEventos() {
        this.suscriptores = new CopyOnWriteArrayList<>(); // <--- CAMBIO AQUÍ
    }

    @Override
    public void suscribir(Consumer<Object> subscriber) {
        this.suscriptores.add(subscriber);
    }

    @Override
    public void publicar(Object evento) {
        for (Consumer<Object> subscriber : suscriptores) {
            try {
                subscriber.accept(evento);
            } catch (Exception e) {
                // Si un suscriptor falla, lo reportamos pero NO rompemos el bucle
                System.err.println("Error en un suscriptor del Bus: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
    