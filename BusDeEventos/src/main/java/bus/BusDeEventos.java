package bus;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList; // 1. Import vital para concurrencia
import java.util.function.Consumer;
import Interfaz.IBusDeEventos;

public class BusDeEventos implements IBusDeEventos {
    
    // 2. Usamos una lista Thread-Safe (Segura para hilos)
    // Esto permite que alguien se suscriba mientras se están enviando mensajes sin que explote.
    private final List<Consumer<Object>> suscriptores = new CopyOnWriteArrayList<>();

    public BusDeEventos() {
        // Constructor vacío. 
        // Eliminamos la lógica de 'instancia' estática para favorecer la Inyección de Dependencias.
    }

    // NOTA: Si tu interfaz IBusDeEventos te obliga a tener este método,
    // te recomiendo eliminarlo de la interfaz también. 
    // Si no puedes eliminarlo de la interfaz, usa esta implementación temporal:
    @Override
    public BusDeEventos getInstancia() {
        return this; 
    }

    @Override
    public void suscribir(Consumer<Object> suscriptor) {
        if (suscriptor != null) {
            suscriptores.add(suscriptor);
        }
    }

    @Override
    public void publicar(Object evento) {
        // 3. Protección contra fallos
        for (Consumer<Object> s : suscriptores) {
            try {
                s.accept(evento);
            } catch (Exception e) {
                // Si un suscriptor falla, lo reportamos pero NO rompemos el bucle.
                // Así los demás clientes/componentes siguen recibiendo información.
                System.err.println("[BusDeEventos] Error en un suscriptor: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}