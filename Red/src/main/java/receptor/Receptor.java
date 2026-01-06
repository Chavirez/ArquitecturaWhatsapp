package receptor;

import Eventos.EventoCrearChatNuevo;
import Eventos.EventoMensajeEnChat;
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
        // Configuramos GSON con el adaptador de fecha
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
                        EventoMensajeEnChat evento = gson.fromJson(jsonObject, EventoMensajeEnChat.class);
                        bus.publicar(evento);
                        System.out.println("[RECEPTOR] Mensaje recibido y publicado.");

                    } else if (jsonObject.has("chatNuevo")) {
                        EventoCrearChatNuevo evento = gson.fromJson(jsonObject, EventoCrearChatNuevo.class);
                        bus.publicar(evento);
                        System.out.println("[RECEPTOR] Solicitud de nuevo chat recibida.");
                    } else {
                        System.err.println("[RECEPTOR] JSON desconocido: " + cadenaJson);
                    }

                } catch (Exception e) {
                    System.err.println("[RECEPTOR] Error procesando JSON: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            System.err.println("[RECEPTOR] Desconexión: " + e.getMessage());
        }
    }
}