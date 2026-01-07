package receptor;

import Interfaz.IBusDeEventos;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDateTime;
import utilidades.LocalDateTimeAdapter;

// Imports de Eventos
import Eventos.EventoMensajeEnChat;
import Eventos.EventoCrearChatNuevo;
import Eventos.EventoLogIn;
import Eventos.EventoRespuestaLogin;
import Eventos.EventoSincronizacion;
import Eventos.EventoEnviarUsuarios;
import Eventos.EventoCerrarSesion;
import Eventos.EventoMensajeRecibido; // Import de Dominio
import Eventos.EventoChatRecibido;    // Import de Dominio

public class Receptor implements Runnable {

    private final BufferedReader lector;
    private final IBusDeEventos bus;
    private final Gson gson;
    private final boolean esCliente; // Bandera vital

    public Receptor(BufferedReader lector, IBusDeEventos bus, boolean esCliente) {
        this.lector = lector;
        this.bus = bus;
        this.esCliente = esCliente;
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

                    // 1. MENSAJES DE CHAT
                    if (jsonObject.has("mensajeAEnviar")) {
                        EventoMensajeEnChat evento = gson.fromJson(jsonObject, EventoMensajeEnChat.class);
                        // Lógica de "Disfraz" solo para Cliente
                        if (esCliente) bus.publicar(new EventoMensajeRecibido(evento));
                        else bus.publicar(evento);
                        
                    } 
                    // 2. CHAT NUEVO
                    else if (jsonObject.has("chatNuevo")) {
                        EventoCrearChatNuevo evento = gson.fromJson(jsonObject, EventoCrearChatNuevo.class);
                        // Lógica de "Disfraz" solo para Cliente
                        if (esCliente) bus.publicar(new EventoChatRecibido(evento));
                        else bus.publicar(evento);
                        
                    } 
                    // 3. SINCRONIZACIÓN DE CHATS (Mocks y guardados) -> ¡ESTO FALTABA!
                    else if (jsonObject.has("chats")) { 
                        EventoSincronizacion evento = gson.fromJson(jsonObject, EventoSincronizacion.class);
                        bus.publicar(evento);
                        
                    } 
                    // 4. SINCRONIZACIÓN DE USUARIOS -> ¡ESTO FALTABA!
                    else if (jsonObject.has("usuarios")) {
                        EventoEnviarUsuarios evento = gson.fromJson(jsonObject, EventoEnviarUsuarios.class);
                        bus.publicar(evento);
                        
                    } 
                    // 5. LOGIN (Pedido)
                    else if (jsonObject.has("loginPedidoDTO")) {
                        EventoLogIn evento = gson.fromJson(jsonObject, EventoLogIn.class);
                        bus.publicar(evento);
                        
                    } 
                    // 6. LOGIN (Respuesta)
                    else if (jsonObject.has("loginRespuestaDTO")) {
                        EventoRespuestaLogin evento = gson.fromJson(jsonObject, EventoRespuestaLogin.class);
                        bus.publicar(evento);
                        
                    } 
                    // 7. CERRAR SESIÓN
                    else if (jsonObject.has("idUsuario")) {
                        EventoCerrarSesion evento = gson.fromJson(jsonObject, EventoCerrarSesion.class);
                        bus.publicar(evento);
                    } 

                } catch (Exception e) {
                    System.err.println("[Receptor] Error procesando JSON: " + e.getMessage());
                    // e.printStackTrace(); // Descomenta para depurar si hace falta
                }
            }
        } catch (IOException e) {
            System.err.println("[Receptor] Conexión cerrada.");
        }
    }
}