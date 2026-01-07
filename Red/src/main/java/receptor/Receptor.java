package receptor;

import DTOs.CrearChatNuevoDTO;
import Eventos.*;
import Interfaz.IBusDeEventos;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDateTime;
import utilidades.LocalDateTimeAdapter;

public class Receptor implements Runnable {

    private final BufferedReader lector;
    private final IBusDeEventos bus;
    private final Gson gson;

    public Receptor(BufferedReader lector, IBusDeEventos bus) {
        this.lector = lector;
        this.bus = bus;
        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
    }

    @Override
    public void run() {
        try {
            String cadenaJson;
            while ((cadenaJson = lector.readLine()) != null) {
                try {
                    JsonObject jsonObject = JsonParser.parseString(cadenaJson).getAsJsonObject();

                    if (jsonObject.has("mensajeAEnviar")) {
                    EventoCrearChatNuevo eventoOriginal = gson.fromJson(jsonObject, EventoCrearChatNuevo.class);

                    bus.publicar(new EventoChatRecibidoLocal(eventoOriginal.getMensaje()));
                        
                    } else if (jsonObject.has("chatNuevo")) {
                        EventoCrearChatNuevo evento = gson.fromJson(jsonObject, EventoCrearChatNuevo.class);
                        bus.publicar(evento);
                        
                    } else if (jsonObject.has("usuarios")) {
                        EventoEnviarUsuarios evento = gson.fromJson(jsonObject, EventoEnviarUsuarios.class);
                        bus.publicar(evento);
                        
                    } else if (jsonObject.has("chats")) { 
                        EventoSincronizacion evento = gson.fromJson(jsonObject, EventoSincronizacion.class);
                        bus.publicar(evento);
                        
                    } else if (jsonObject.has("loginPedidoDTO")) {
                        EventoLogIn evento = gson.fromJson(jsonObject, EventoLogIn.class);
                        bus.publicar(evento);
                        
                    } else if (jsonObject.has("loginRespuestaDTO")) {
                        EventoRespuestaLogin evento = gson.fromJson(jsonObject, EventoRespuestaLogin.class);
                        bus.publicar(evento);
                        
                    } else if (jsonObject.has("idUsuario")) {
                        EventoCerrarSesion evento = gson.fromJson(jsonObject, EventoCerrarSesion.class);
                        bus.publicar(evento);
                    } 

                } catch (Exception e) {
                    System.err.println("[RECEPTOR] Error: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("[RECEPTOR] Desconexión.");
        }
    }
    
    public static class EventoChatRecibidoLocal {
        private CrearChatNuevoDTO dto;
        public EventoChatRecibidoLocal(CrearChatNuevoDTO dto) { this.dto = dto; }
        public CrearChatNuevoDTO getDto() { return dto; }
    }
}

