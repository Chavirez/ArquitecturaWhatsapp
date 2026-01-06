package receptor;

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
                    // 1. Convertir a JsonObject genérico
                    JsonObject jsonObject = JsonParser.parseString(cadenaJson).getAsJsonObject();

                    // 2. Identificar el tipo de evento por su atributo único
                    if (jsonObject.has("mensajeAEnviar")) {
                        // Mensaje de Chat
                        EventoMensajeEnChat evento = gson.fromJson(jsonObject, EventoMensajeEnChat.class);
                        bus.publicar(evento);
                        
                    } else if (jsonObject.has("chatNuevo")) {
                        // Nuevo Chat creado
                        EventoCrearChatNuevo evento = gson.fromJson(jsonObject, EventoCrearChatNuevo.class);
                        bus.publicar(evento);
                        
                    } else if (jsonObject.has("usuarios")) {
                        // Lista de usuarios (para contactos)
                        EventoEnviarUsuarios evento = gson.fromJson(jsonObject, EventoEnviarUsuarios.class);
                        bus.publicar(evento);
                        
                    } 
                    // --- NUEVOS EVENTOS DE LOGIN ---
                    else if (jsonObject.has("loginPedidoDTO")) {
                        // Solicitud de Login (Llega al Servidor)
                        EventoLogIn evento = gson.fromJson(jsonObject, EventoLogIn.class);
                        bus.publicar(evento);
                        
                    } else if (jsonObject.has("loginRespuestaDTO")) {
                        // Respuesta de Login (Llega al Cliente)
                        EventoRespuestaLogin evento = gson.fromJson(jsonObject, EventoRespuestaLogin.class);
                        bus.publicar(evento);
                        
                    } else if (jsonObject.has("idUsuario")) {
                        // Cierre de sesión (Llega al Servidor)
                        // 'idUsuario' es el único campo de EventoCerrarSesion
                        EventoCerrarSesion evento = gson.fromJson(jsonObject, EventoCerrarSesion.class);
                        bus.publicar(evento);
                        
                    } else {
                        System.err.println("[RECEPTOR] JSON desconocido o no manejado: " + cadenaJson);
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
