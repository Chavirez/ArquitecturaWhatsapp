/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package vista;

import java.awt.Color;
import java.awt.Cursor;
import javax.swing.JOptionPane;
import controlador.Controlador;
import modelo.Modelo;
import observadores.ObservadorLogin;

/**
 *
 * @author santi
 */
public class FrameLogIn extends javax.swing.JFrame implements ObservadorLogin{

    private Controlador controlador;
    
    public FrameLogIn(Controlador controlador) {
        
        this.controlador = controlador;
        
        initComponents();
        lblBtnLog.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        fldUsuario.setOpaque(false);
        fldUsuario.setBackground(new Color(0,0,0,0));
        fldUsuario.setBorder(null);
        fldUsuario.setCaretColor(Color.BLACK);       
        
        fldContrasenia.setOpaque(false);
        fldContrasenia.setBackground(new Color(0,0,0,0));
        fldContrasenia.setBorder(null);
        fldContrasenia.setCaretColor(Color.BLACK);       

        this.revalidate();
        this.repaint();
    }
    
    @Override
    public void actualizar(){
    
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        fldUsuario = new javax.swing.JTextField();
        fldContrasenia = new javax.swing.JTextField();
        lblUsuario = new javax.swing.JLabel();
        lblPass = new javax.swing.JLabel();
        lblTitulo = new javax.swing.JLabel();
        lblBtnLog = new javax.swing.JLabel();
        lblFondo = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        fldUsuario.setFont(new java.awt.Font("Nirmala Text", 0, 18)); // NOI18N
        fldUsuario.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        getContentPane().add(fldUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 370, 380, 50));

        fldContrasenia.setFont(new java.awt.Font("Nirmala Text", 0, 18)); // NOI18N
        fldContrasenia.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        getContentPane().add(fldContrasenia, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 460, 380, 50));

        lblUsuario.setFont(new java.awt.Font("Nirmala Text", 1, 18)); // NOI18N
        lblUsuario.setForeground(new java.awt.Color(0, 0, 0));
        lblUsuario.setText("Usuario");
        getContentPane().add(lblUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 340, -1, -1));

        lblPass.setFont(new java.awt.Font("Nirmala Text", 1, 18)); // NOI18N
        lblPass.setForeground(new java.awt.Color(0, 0, 0));
        lblPass.setText("Contraseña");
        getContentPane().add(lblPass, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 420, -1, 50));

        lblTitulo.setFont(new java.awt.Font("Nirmala Text", 1, 36)); // NOI18N
        lblTitulo.setForeground(new java.awt.Color(0, 0, 0));
        lblTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitulo.setText("Mensajeria");
        getContentPane().add(lblTitulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(102, 240, 590, -1));

        lblBtnLog.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblBtnLogMouseClicked(evt);
            }
        });
        getContentPane().add(lblBtnLog, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 520, 60, 70));

        lblFondo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Login.png"))); // NOI18N
        getContentPane().add(lblFondo, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void lblBtnLogMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblBtnLogMouseClicked
        
        String usuario = fldUsuario.getText().trim();
        String pass = fldContrasenia.getText().trim();
        
        if (usuario.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                    "Por favor, ingresa tu usuario y contraseña.", 
                    "Campos Incompletos", 
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (usuario.length() < 3) {
            JOptionPane.showMessageDialog(this, 
                    "El nombre de usuario es muy corto.", 
                    "Validación", 
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        controlador.intentarLogin(usuario, pass);
    }//GEN-LAST:event_lblBtnLogMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField fldContrasenia;
    private javax.swing.JTextField fldUsuario;
    private javax.swing.JLabel lblBtnLog;
    private javax.swing.JLabel lblFondo;
    private javax.swing.JLabel lblPass;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JLabel lblUsuario;
    // End of variables declaration//GEN-END:variables
}
