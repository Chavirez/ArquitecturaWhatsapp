/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Interfaz;

import bus.BusDeEventos;
import java.util.function.Consumer;

/**
 *
 * @author santi
 */
public interface IBusDeEventos {
    
    public void suscribir(Consumer<Object> suscriptor);
    public void publicar(Object evento);
    public BusDeEventos getInstancia();
}
