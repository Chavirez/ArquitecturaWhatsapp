package bus;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList; // 1. Import vital para concurrencia
import java.util.function.Consumer;
import Interfaz.IBusDeEventos;

public class BusDeEventos implements IBusDeEventos {
    

    private final List<Consumer<Object>> suscriptores = new CopyOnWriteArrayList<>();

    public BusDeEventos() {

    }

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

        for (Consumer<Object> s : suscriptores) {
            try {
                s.accept(evento);
            } catch (Exception e) {

                System.err.println("[BusDeEventos] Error en un suscriptor: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}