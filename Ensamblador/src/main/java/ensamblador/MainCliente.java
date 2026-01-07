package ensamblador;

import conexion.Conexion;
import controlador.Controlador;
import itson.negocio.Negocio;
import javax.swing.JOptionPane;
import modelo.Modelo;

public class MainCliente {

    public static void main(String[] args) {
        try {
            String ip = "192.168.100.2"; 
            int puerto = 4444;

            Conexion conexion = new Conexion(ip, puerto);

            Negocio negocio = new Negocio(conexion.getBus());

            Modelo modelo = new Modelo(negocio);
            Controlador control = new Controlador(modelo);
            control.abrirFrameLogin();

            System.out.println("Cliente iniciado exitosamente.");

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "No se pudo conectar al servidor.\n" +
                    "Verifica la IP o que el servidor esté encendido.");
        }
    }
}