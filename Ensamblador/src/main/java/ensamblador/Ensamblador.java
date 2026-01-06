/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package ensamblador;

import DTOs.MensajeEnChatDTO;
import bus.BusDeEventos;
import emisor.Emisor;
import eventos.EventoMensajeEnChat;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintWriter;
import java.util.Date;
import receptor.Receptor;

/**
 *
 * @author santi
 */
public class Ensamblador {

    public static void main(String[] args) throws IOException, InterruptedException {
        
        BusDeEventos bus = new BusDeEventos();

        // 2. Simular un socket con Pipes para la prueba
        PipedOutputStream output = new PipedOutputStream();
        PipedInputStream input = new PipedInputStream(output);
        
        PrintWriter escritor = new PrintWriter(output, true);
        BufferedReader lector = new BufferedReader(new InputStreamReader(input));

        // 3. Inicializar Componentes de Red 
        Emisor emisor = new Emisor(escritor, bus);
        Receptor receptor = new Receptor(lector, bus);

        // Iniciar hilos de red 
        Thread hiloEmisor = new Thread(emisor);
        Thread hiloReceptor = new Thread(receptor);
        hiloEmisor.start();
        hiloReceptor.start();

        // 4. Suscribir un "Mock de Negocio" al bus para ver qué llega al final 
        bus.suscribir(evento -> {
            if (evento instanceof EventoMensajeEnChat) {
                EventoMensajeEnChat e = (EventoMensajeEnChat) evento;
                System.out.println("\n[PRUEBA] El Bus notificó un nuevo mensaje:");
                System.out.println("Contenido: " + e.getMensaje().getMensaje());
                System.out.println("Chat ID: " + e.getMensaje().getIdChat());
            }
        });

        // 5. EJECUCIÓN DE LA PRUEBA: Enviar un mensaje
        System.out.println("[PRUEBA] Creando DTO de mensaje...");
        MensajeEnChatDTO mensajeDTO = new MensajeEnChatDTO(
                "Hola desde la prueba de arquitectura!", 
                new Date(), 
                1, // idUsuario
                101 // idChat
        );

        System.out.println("[PRUEBA] Publicando evento en el bus...");
        // Al publicar esto, el Emisor lo captura, lo serializa a JSON y lo envía 
        bus.publicar(new EventoMensajeEnChat(mensajeDTO));

        // Dar un momento para que los hilos procesen
        Thread.sleep(1000);

        System.out.println("\n[PRUEBA] Fin de la simulación.");
        hiloEmisor.interrupt();
        hiloReceptor.interrupt();
    }
    
}
