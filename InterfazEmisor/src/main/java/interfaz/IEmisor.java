/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package interfaz;

/**
 *
 * @author santi
 */
public interface IEmisor {
    
    public void enviar(String dato) throws InterruptedException;
    
    public void run(); 
    
}
