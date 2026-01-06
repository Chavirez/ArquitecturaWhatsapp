/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package observadores;

import DTOs.LoginRespuestaDTO;

/**
 *
 * @author santi
 */
public interface ObservadoLogin {
    
    public void notificarLogin(LoginRespuestaDTO respuesta) ;
    
}
