package ensamblador;

import bus.BusDeEventos;
import servidor.Servidor;

public class MainServidor {

    public static void main(String[] args) {
        try {
            BusDeEventos bus = new BusDeEventos();
            int puerto = 4444;

            Servidor servidor = new Servidor(puerto, bus);
            new Thread(servidor).start();

            System.out.println("--------------------------------------------------");
            System.out.println(" SERVIDOR INICIADO EN EL PUERTO: " + puerto);
            System.out.println(" Esperando clientes...");
            System.out.println("--------------------------------------------------");

        } catch (Exception e) {
            System.err.println("Error al iniciar el servidor: " + e.getMessage());
            e.printStackTrace();
        }
    }
}