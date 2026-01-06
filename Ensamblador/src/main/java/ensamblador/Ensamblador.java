package ensamblador;

import DTOs.MensajeEnChatDTO;
import Eventos.EventoMensajeEnChat;
import bus.BusDeEventos;
import emisor.Emisor;
import receptor.Receptor;
import servidor.Servidor;
import javax.swing.JOptionPane;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;

public class Ensamblador {

    public static void main(String[] args) {
        try {
            // 1. Preguntar el ROL de esta computadora
            String[] opciones = {"Crear Servidor (Host)", "Unirme como Cliente"};
            int seleccion = JOptionPane.showOptionDialog(null, 
                    "¿Qué rol tendrá esta computadora?", 
                    "Configuración de Red", 
                    JOptionPane.DEFAULT_OPTION, 
                    JOptionPane.INFORMATION_MESSAGE, 
                    null, opciones, opciones[0]);

            BusDeEventos bus = new BusDeEventos();
            String ipDestino = "localhost"; // Por defecto
            int puerto = 4444;

            if (seleccion == 0) {
                // --- MODO SERVIDOR ---
                // Iniciamos el servidor en segundo plano
                Servidor servidor = new Servidor(puerto, bus);
                new Thread(servidor).start();
                
                JOptionPane.showMessageDialog(null, 
                        "Servidor iniciado en el puerto " + puerto + ".\n" +
                        "Los demás deben conectarse a TU dirección IP local.");
                
                // El propio servidor también se conecta como cliente a sí mismo
                ipDestino = "localhost"; 
                
            } else {
                // --- MODO CLIENTE ---
                // Pedimos la IP de la computadora servidor
                ipDestino = JOptionPane.showInputDialog(null, 
                        "Ingresa la IP del Servidor:", 
                        "192.168.1.X");
                
                if (ipDestino == null || ipDestino.isEmpty()) return; // Cancelado
            }

            // 2. CONEXIÓN DEL CLIENTE (Común para ambos)
            System.out.println("Conectando a " + ipDestino + ":" + puerto + "...");
            Socket socket = new Socket(ipDestino, puerto);
            
            // Inicializar flujos
            PrintWriter escritor = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader lector = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Inicializar Componentes de tu Arquitectura
            Emisor emisor = new Emisor(escritor, bus);
            Receptor receptor = new Receptor(lector, bus);

            new Thread(emisor).start();
            new Thread(receptor).start();
            
            System.out.println("¡Conexión establecida con éxito!");

            // 3. PRUEBA DE ENVÍO AUTOMÁTICO
            // Enviamos un mensaje de saludo para confirmar que funcionó
            String nombreUsuario = (seleccion == 0) ? "Servidor" : "Cliente Remoto";
            
            MensajeEnChatDTO mensajeDTO = new MensajeEnChatDTO(
                "Hola, soy " + nombreUsuario + " desde " + socket.getLocalAddress(), 
                LocalDateTime.now(), 
                1, 101
            );
            
            // Damos un segundo para que los hilos arranquen
            Thread.sleep(1000); 
            bus.publicar(new EventoMensajeEnChat(mensajeDTO));

            // Aquí normalmente arrancarías tu FramePrincipal
            // new vista.FramePrincipal().setVisible(true);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error de conexión: " + e.getMessage());
        }
    }
}