
package eventos;

import DTOs.MensajeEnChatDTO;

public class EventoMensajeEnChat {
    private final MensajeEnChatDTO mensajeAEnviar;

    public EventoMensajeEnChat(MensajeEnChatDTO mensajeAEnviar) {
        this.mensajeAEnviar = mensajeAEnviar;
    }

    public MensajeEnChatDTO getMensaje() { 
        return mensajeAEnviar; 
    }
}