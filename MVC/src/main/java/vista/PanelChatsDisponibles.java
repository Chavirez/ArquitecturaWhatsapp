/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package vista;

import Objetos.Chat;

/**
 *
 * @author santi
 */
public class PanelChatsDisponibles extends javax.swing.JPanel {

    private Chat chat;
    
    public PanelChatsDisponibles(Chat chat) {
        this.chat = chat;
        initComponents();
        configurarTexto();
        
    }

    public void configurarTexto(){
    
        String mensaje = chat.getMensajes().getLast().getMensaje();
        
        if (mensaje == null) {
            mensaje = "Manda el primer mensaje!";
        }
        if (mensaje.length() > 15) {
            mensaje = mensaje.substring(0, 15) + "...";
        }
        lblUltimoMensaje.setText(mensaje);
        
        
        lblNombreUsuario.setText("a");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblUltimoMensaje = new javax.swing.JLabel();
        lblNombreUsuario = new javax.swing.JLabel();
        lblIcono = new javax.swing.JLabel();

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblUltimoMensaje.setText("MensajeDefault");
        add(lblUltimoMensaje, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 60, -1, -1));

        lblNombreUsuario.setText("Usuario Default");
        add(lblNombreUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 20, -1, -1));

        lblIcono.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/IconoDefault.png"))); // NOI18N
        add(lblIcono, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, -1, -1));
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lblIcono;
    private javax.swing.JLabel lblNombreUsuario;
    private javax.swing.JLabel lblUltimoMensaje;
    // End of variables declaration//GEN-END:variables
}
