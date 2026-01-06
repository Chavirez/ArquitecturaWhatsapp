/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Objetos;

import java.util.Date;

/**
 *
 * @author santi
 */
public class Mensaje {
    
    private String mensaje;
    private Date fechaEnviado;
    private Usuario usuario;

    public Mensaje() {
    }

    public Mensaje(String mensaje, Date fechaEnviado, Usuario usuario) {
        this.mensaje = mensaje;
        this.fechaEnviado = fechaEnviado;
        this.usuario = usuario;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Date getFechaEnviado() {
        return fechaEnviado;
    }

    public void setFechaEnviado(Date fechaEnviado) {
        this.fechaEnviado = fechaEnviado;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public String toString() {
        return "Mensaje{" + "mensaje=" + mensaje + ", fechaEnviado=" + fechaEnviado + ", usuario=" + usuario + '}';
    }
    
    
    
}
