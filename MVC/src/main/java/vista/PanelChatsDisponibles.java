/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package vista;

import Objetos.Chat;
import Objetos.Usuario;

/**
 *
 * @author santi
 */
public class PanelChatsDisponibles extends javax.swing.JPanel {

    private Chat chat;
    private Usuario uLogueado;
    
    public PanelChatsDisponibles(Chat chat, Usuario uLogueado) {
        this.chat = chat;
        this.uLogueado = uLogueado;
        initComponents();
        configurarTexto();
        
    }


    public void configurarTexto(){
    
        
        if(chat.getUsuarios().getFirst().getId() == uLogueado.getId())
            lblNombreUsuario.setText(chat.getUsuarios().get(1).getUsuario());
        if(chat.getUsuarios().getFirst().getId() != uLogueado.getId())
            lblNombreUsuario.setText(chat.getUsuarios().get(0).getUsuario());
        
        
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblNombreUsuario = new javax.swing.JLabel();
        lblIcono = new javax.swing.JLabel();

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblNombreUsuario.setFont(new java.awt.Font("Nirmala Text", 1, 18)); // NOI18N
        lblNombreUsuario.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblNombreUsuario.setText("Usuario Default");
        add(lblNombreUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 16, 170, 70));

        lblIcono.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/IconoDefault.png"))); // NOI18N
        add(lblIcono, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, -1, -1));
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lblIcono;
    private javax.swing.JLabel lblNombreUsuario;
    // End of variables declaration//GEN-END:variables
}
