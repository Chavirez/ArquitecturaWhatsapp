/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package vista;

import Objetos.Chat;
import Objetos.Usuario;
import controlador.Controlador;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;

/**
 *
 * @author santi
 */
public class PanelChatsDisponibles extends javax.swing.JPanel {

    private Chat chat;
    private Controlador controlador;
    private Consumer<Chat> alHacerClic; // Variable para guardar la acción
    private Usuario usuario;

    // Modificamos el constructor para recibir la acción 'alHacerClic'
    public PanelChatsDisponibles(Chat chat, Controlador controlador, Consumer<Chat> alHacerClic, Usuario uLogueado) {
        initComponents();
        this.chat = chat;
        this.controlador = controlador;
        this.alHacerClic = alHacerClic; // Guardamos la acción
        this.usuario = uLogueado;
        
        configurarTexto();
        configurarEventos(); // Método nuevo
    }
    
    private void configurarEventos() {
        // Agregamos el evento de clic a todo el panel
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Cambiar color para efecto visual de selección
                setBackground(new Color(230, 230, 230)); 
                
                // Ejecutar la acción que nos pasó el FramePrincipal
                if (alHacerClic != null) {
                    alHacerClic.accept(chat);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                 // Regresar al color original al salir (opcional)
                 setBackground(new Color(255, 255, 255));
            }
        });
    }

    public void configurarTexto(){
    String nombreMostrar = "Chat " + chat.getId(); // Valor por defecto

            if (chat.getUsuarios() != null) {
                for (Usuario u : chat.getUsuarios()) {
                    // Si el usuario analizado NO soy yo, entonces es mi contacto
                    if (u.getId() != this.usuario.getId()) {
                        nombreMostrar = u.getUsuario(); // Obtenemos su nombre real
                        break; // En chats de 2 personas, con encontrar uno basta
                    }
                }
            }

            // Asignamos el nombre al label (Funciona tenga mensajes o no)
            lblNombreUsuario.setText(nombreMostrar);
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
