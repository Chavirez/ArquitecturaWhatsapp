/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Objetos;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 *
 * @author santi
 */
public class Mensaje implements Serializable {
    
    private String mensaje;
    private LocalDateTime fechaEnviado;
    private Usuario usuario;

    public Mensaje() {
    }

    public Mensaje(String mensaje, LocalDateTime fechaEnviado, Usuario usuario) {
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

    public LocalDateTime getFechaEnviado() {
        return fechaEnviado;
    }

    public void setFechaEnviado(LocalDateTime fechaEnviado) {
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
