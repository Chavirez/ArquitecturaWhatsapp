/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import java.util.ArrayList;
import java.util.List;
import modelo.Modelo;
import observadores.ObservadorLogin;
import vista.FrameLogIn;
import vista.FramePrincipal;

/**
 *
 * @author santi
 */
public class Controlador {
    
    private Modelo modelo;

    public Controlador(Modelo modelo) {
        this.modelo = modelo;
    }
    
    public void abrirFrameLogin(){
    
        FrameLogIn frm = new FrameLogIn(this);
        
        modelo.agregarListenerLogin(frm);
        
        frm.setVisible(true);
        
    }
    
    public void abrirFramePrincipal(){
    
        FramePrincipal frm = new FramePrincipal(this);
        
        modelo.agregarListenerChat(frm);
        
        frm.setVisible(true);
        
        modelo.notificarChat();

    }
 
    
    public void intentarLogin(String usuario, String pass) {

        modelo.intentarLogin(usuario, pass);
        
    }
    
}
