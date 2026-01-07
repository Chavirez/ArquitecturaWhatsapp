package Eventos;

public class EventoMensajeRecibido {
    private final EventoMensajeEnChat eventoOriginal;

    public EventoMensajeRecibido(EventoMensajeEnChat evento) {
        this.eventoOriginal = evento;
    }

    public EventoMensajeEnChat getEventoOriginal() {
        return eventoOriginal;
    }
}