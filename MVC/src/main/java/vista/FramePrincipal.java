/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package vista;

import Objetos.Mensaje;
import java.awt.Dimension;
import java.time.LocalDateTime;
import javax.swing.Box;
import javax.swing.JPanel;
import javax.swing.UIManager;

/**
 *
 * @author santi
 */
public class FramePrincipal extends javax.swing.JFrame {
    
    private javax.swing.JPanel contenedorMensajes;
    /**
     * Creates new form FramePrincipal
     */
    public FramePrincipal() {
        initComponents();
        configurarChat();

    }

    private void configurarChat() {
        contenedorMensajes = new javax.swing.JPanel();

        contenedorMensajes.setLayout(new javax.swing.BoxLayout(contenedorMensajes, javax.swing.BoxLayout.Y_AXIS));
        contenedorMensajes.setOpaque(false);
        contenedorMensajes.add(javax.swing.Box.createVerticalGlue());

        pnlChat.setViewportView(contenedorMensajes);
        pnlChat.getViewport().setOpaque(false);
        pnlChat.setOpaque(false);
        pnlChat.setBorder(null);
        pnlChat.getVerticalScrollBar().setUI(new EstiloScrollBar());

        pnlChat.getVerticalScrollBar().setPreferredSize(new Dimension(8, 0));

        pnlChat.getVerticalScrollBar().setUnitIncrement(16); 

        pnlChat.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        
    }
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        pnlChat = new javax.swing.JScrollPane();
        panelEnviarMensaje = new vista.panelEnviarMensaje();
        lblFondo = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(800, 800));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jButton1.setText("jButton1");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 40, -1, -1));
        getContentPane().add(pnlChat, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 122, 470, 540));
        getContentPane().add(panelEnviarMensaje, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 680, -1, -1));

        lblFondo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/FondoPrincipal.png"))); // NOI18N
        getContentPane().add(lblFondo, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 800, 800));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    
 
    
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
     
        
//Mensaje mns = new Mensaje("Pruebaruebaruebaruebaruebaruebaruebaruebaruebaruebaruebaruebaruebaruebaruebaruebaruebaruebaruebaruebaruebaruebaruebaruebaruebaruebaruebarueba", LocalDateTime.now(), null);
Mensaje mns = new Mensaje("Prueba", LocalDateTime.now(), null);

    // PARA MENSAJE PROPIO (Derecha)
    PanelMensajePropio propia = new PanelMensajePropio(mns);
    Box filaPropia = Box.createHorizontalBox();
    filaPropia.setMaximumSize(new Dimension(460, propia.getPreferredSize().height));
    filaPropia.add(Box.createHorizontalGlue()); // Empuja hacia la derecha
    filaPropia.add(propia);
    contenedorMensajes.add(filaPropia);

    // Espaciado

    // PARA MENSAJE AJENO (Izquierda)
    PanelMensajeAjeno ajena = new PanelMensajeAjeno(mns);
    Box filaAjena = Box.createHorizontalBox();
    filaAjena.add(ajena);
    filaAjena.add(Box.createHorizontalGlue()); 
    contenedorMensajes.add(filaAjena);

    // Refrescar y Scroll
    contenedorMensajes.revalidate();
    contenedorMensajes.repaint();
    javax.swing.SwingUtilities.invokeLater(() -> {
        pnlChat.getVerticalScrollBar().setValue(pnlChat.getVerticalScrollBar().getMaximum());
    });
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FramePrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FramePrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FramePrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FramePrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FramePrincipal().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel lblFondo;
    private vista.panelEnviarMensaje panelEnviarMensaje;
    private javax.swing.JScrollPane pnlChat;
    // End of variables declaration//GEN-END:variables
}
