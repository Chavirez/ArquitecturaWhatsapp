/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package vista;

import Objetos.Chat;
import Objetos.Mensaje;
import Objetos.Usuario;
import controlador.Controlador;
import java.awt.Component;
import java.awt.Dimension;
import java.time.LocalDateTime;
import java.util.List;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import modelo.Modelo;
import observadores.ObservadorChat;

/**
 *
 * @author santi
 */
public class FramePrincipal extends javax.swing.JFrame implements ObservadorChat{
    
    private Controlador controlador;
    private Usuario usuarioLogueado;
    private Chat chatSeleccionadoActual; 
    private Modelo modelo;
    
    private javax.swing.JPanel contenedorMensajes;
    private javax.swing.JPanel contenedorContactos;
    
    /**
     * Creates new form FramePrincipal
     */
    public FramePrincipal(Controlador controlador) {
        this.controlador = controlador;
        initComponents();
        configurarChat();
        configurarContactos();
        

    }
    
    @Override
    public void actualizar(Modelo modelo){
        
        this.modelo = modelo;
        this.usuarioLogueado = modelo.getUsuarioLocal();
        actualizarListaChats(modelo.getChat());
        this.revalidate();
        this.repaint();
        
    }
    
    private void cargarMensajesDelChat(Chat chat) {
        this.chatSeleccionadoActual = chat;

        // 1. Limpiamos el CONTENEDOR INTERNO, no el ScrollPane
        contenedorMensajes.removeAll();

        // El layout ya se definió en configurarChat(), pero no hace daño asegurarlo
        // contenedorMensajes.setLayout(new BoxLayout(contenedorMensajes, BoxLayout.Y_AXIS));

        int idMiUsuario = usuarioLogueado.getId();

        // 2. Iteramos los mensajes
        for (Mensaje m : chat.getMensajes()) {
            if (m.getUsuario().getId() == idMiUsuario) { // Nota: Revisa si es m.getIdUsuario() o m.getUsuario().getId()
                PanelMensajePropio panelMio = new PanelMensajePropio(m);

                // Alineación a la derecha para mensajes propios
                panelMio.setAlignmentX(Component.RIGHT_ALIGNMENT); 

                contenedorMensajes.add(panelMio);
                contenedorMensajes.add(Box.createRigidArea(new Dimension(0, 5))); // Espacio entre mensajes

            } else {
                PanelMensajeAjeno panelAjeno = new PanelMensajeAjeno(m);

                // Alineación a la izquierda para mensajes ajenos
                panelAjeno.setAlignmentX(Component.LEFT_ALIGNMENT);

                contenedorMensajes.add(panelAjeno);
                contenedorMensajes.add(Box.createRigidArea(new Dimension(0, 5)));
            }
        }

        // 3. Importante: Agregar "Pegamento" al final si quieres que empiecen desde arriba
        // Opcional: contenedorMensajes.add(Box.createVerticalGlue());

        // 4. Refrescar visualmente
        contenedorMensajes.revalidate();
        contenedorMensajes.repaint();

        // 5. Bajar el scroll automáticamente al último mensaje
        javax.swing.SwingUtilities.invokeLater(() -> {
            pnlChat.getVerticalScrollBar().setValue(pnlChat.getVerticalScrollBar().getMaximum());
    });}
    private void configurarContactos() {
            contenedorContactos = new JPanel();
            contenedorContactos.setLayout(new BoxLayout(contenedorContactos, BoxLayout.Y_AXIS));
            contenedorContactos.setOpaque(false); 


            pnlContactos.setViewportView(contenedorContactos);
            pnlContactos.getViewport().setOpaque(false);
            pnlContactos.setOpaque(false);
            pnlContactos.setBorder(null);

            pnlContactos.getVerticalScrollBar().setUI(new EstiloScrollBar());
            pnlContactos.getVerticalScrollBar().setPreferredSize(new Dimension(8, 0));
            pnlContactos.getVerticalScrollBar().setUnitIncrement(16);

            pnlContactos.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        }
    

    
    public void actualizarListaChats(List<Chat> listaChats) {
        contenedorContactos.removeAll(); 

        if(listaChats == null || listaChats.isEmpty()){
            System.out.println("No hay chats para mostrar");
            // Refrescamos para que se limpie la pantalla si no hay chats
            contenedorContactos.revalidate();
            contenedorContactos.repaint();
            return;
        }

        // Obtenemos el ID del usuario local una sola vez para optimizar
        int miId = (usuarioLogueado != null) ? usuarioLogueado.getId() : -1;

        for (Chat chat : listaChats) {
            // Validación: Solo mostrar chats donde estoy yo
            boolean soyParteDelChat = false;
            for (Usuario u : chat.getUsuarios()) {
                if (u.getId() == miId) {
                    soyParteDelChat = true;
                    break;
                }
            }

            if (!soyParteDelChat) continue; 

            // --- CAMBIO CLAVE AQUÍ ---
            // Pasamos el chat, el usuario, Y LA ACCIÓN (this::cargarMensajesDelChat)
            // Nota: Asegúrate que PanelChatsDisponibles tenga este constructor
            PanelChatsDisponibles itemChat = new PanelChatsDisponibles(chat, controlador, this::cargarMensajesDelChat, usuarioLogueado);

            itemChat.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70)); 
            itemChat.setAlignmentX(Component.LEFT_ALIGNMENT);

            contenedorContactos.add(itemChat);
            contenedorContactos.add(Box.createRigidArea(new Dimension(0, 5)));
        }

        contenedorContactos.revalidate();
        contenedorContactos.repaint();
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

        pnlContactos = new javax.swing.JScrollPane();
        pnlChat = new javax.swing.JScrollPane();
        lblAgregar = new javax.swing.JLabel();
        panelEnviarMensaje = new vista.panelEnviarMensaje();
        lblFondo = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(800, 800));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        getContentPane().add(pnlContactos, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 260, 670));
        getContentPane().add(pnlChat, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 22, 470, 640));

        lblAgregar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/BotonAgregar.png"))); // NOI18N
        lblAgregar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblAgregarMouseClicked(evt);
            }
        });
        getContentPane().add(lblAgregar, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 700, -1, -1));
        getContentPane().add(panelEnviarMensaje, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 680, -1, -1));

        lblFondo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/FondoPrincipal.png"))); // NOI18N
        getContentPane().add(lblFondo, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 800, 800));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void lblAgregarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblAgregarMouseClicked
    List<Usuario> usuarios = controlador.getUsuariosDisponiblesParaChat();
    
    if (usuarios.isEmpty()) {
        JOptionPane.showMessageDialog(this, "No hay más usuarios");
        return;
    }
    
    Usuario[] opciones = new Usuario[usuarios.size()];
    usuarios.toArray(opciones);
    
    Usuario seleccionado = (Usuario) JOptionPane.showInputDialog(
            this,
            "Selecciona con quien hablar!",
            "Nuevo Chat",
            JOptionPane.QUESTION_MESSAGE,
            null,
            opciones,
            opciones[0]);

    if (seleccionado != null) {
        controlador.crearChatCon(seleccionado);
    }
    }//GEN-LAST:event_lblAgregarMouseClicked

 
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lblAgregar;
    private javax.swing.JLabel lblFondo;
    private vista.panelEnviarMensaje panelEnviarMensaje;
    private javax.swing.JScrollPane pnlChat;
    private javax.swing.JScrollPane pnlContactos;
    // End of variables declaration//GEN-END:variables
}
