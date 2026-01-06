package ensamblador;

import DTOs.CrearChatNuevoDTO;
import DTOs.UsuarioDTO;
import Eventos.*;
import bus.BusDeEventos;
import controlador.Controlador;
import emisor.Emisor;
import itson.negocio.Negocio;
import receptor.Receptor;

import javax.swing.JOptionPane;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import modelo.Modelo;

public class MainCliente {

    public static void main(String[] args) {
        try {
            
            String ip = "192.168.100.2"; 
            int puerto = 4444;

            System.out.println("Iniciando Cliente conectando a: " + ip + ":" + puerto);

            BusDeEventos bus = new BusDeEventos();
            Negocio negocio = new Negocio(bus);

            Socket socket = new Socket(ip, puerto);
            
            PrintWriter escritor = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader lector = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            Emisor emisor = new Emisor(escritor, bus);
            Receptor receptor = new Receptor(lector, bus);

            new Thread(emisor).start();
            new Thread(receptor).start();

           
            Modelo modelo = new Modelo(negocio);
            Controlador control = new Controlador(modelo);
            control.abrirFrameLogin();

            System.out.println("Cliente iniciado exitosamente.");

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "No se pudo conectar al servidor.\n" +
                    "Abriste el servidor?");
        }
    }


}