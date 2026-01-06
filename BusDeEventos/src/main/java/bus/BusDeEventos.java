package bus;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import Interfaz.IBusDeEventos;

public class BusDeEventos implements IBusDeEventos{
    private static BusDeEventos instancia;
    private final List<Consumer<Object>> suscriptores = new ArrayList<>();

    public BusDeEventos() {}

    @Override
    public BusDeEventos getInstancia() {
        if (instancia == null) instancia = new BusDeEventos();
        return instancia;
    }

    @Override
    public void suscribir(Consumer<Object> suscriptor) {
        suscriptores.add(suscriptor);
    }

    @Override
    public void publicar(Object evento) {
        // Notificación en tiempo real 
        for (Consumer s : suscriptores) {
            s.accept(evento);
        }
    }
}