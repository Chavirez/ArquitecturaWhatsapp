package Eventos;

public class EventoChatRecibido {
    private final EventoCrearChatNuevo eventoOriginal;

    public EventoChatRecibido(EventoCrearChatNuevo evento) {
        this.eventoOriginal = evento;
    }

    public EventoCrearChatNuevo getEventoOriginal() {
        return eventoOriginal;
    }
}