/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import DTOs.CrearChatNuevoDTO;
import DTOs.MensajeEnChatDTO;
import Objetos.Chat;
import Objetos.Usuario;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
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
    private FrameLogIn frm;

    public Controlador(Modelo modelo) {
        this.modelo = modelo;
    }
    
    public void abrirFrameLogin(){
    
        FrameLogIn frm = new FrameLogIn(this);
        
        this.frm = frm;
        modelo.agregarListenerLogin(frm);
        
        frm.setVisible(true);
        
    }
    
    public void abrirFramePrincipal(){
    
        
        
        FramePrincipal frm = new FramePrincipal(this);
        
        modelo.agregarListenerChat(frm);
        
        frm.setVisible(true);
        this.frm.dispose();
        modelo.notificarChat();

    }
    
    public List<Usuario> getUsuariosDisponiblesParaChat(){
    
        return modelo.getUsuariosDisponiblesParaChat();
        
    }
    
    public void crearChatCon(Usuario usuarioDestino) {
        if (usuarioDestino == null) return;

        CrearChatNuevoDTO dto = new CrearChatNuevoDTO(modelo.getUsuarioLocal(), usuarioDestino);

        modelo.crearChat(dto);

    }
    
    public void intentarLogin(String usuario, String pass) {

        modelo.intentarLogin(usuario, pass);
        
    }
    
    public void seleccionarChat(Chat chat){
        
        modelo.setChatSeleccionado(chat);
        
    }
    
    public void enviarMensaje(String mensaje, LocalDateTime fecha){
    

        MensajeEnChatDTO dto = new MensajeEnChatDTO(mensaje, fecha, modelo.getUsuarioLocal(), modelo.getChatSeleccionado());
        modelo.enviarMensaje(dto);
    }
    
}
