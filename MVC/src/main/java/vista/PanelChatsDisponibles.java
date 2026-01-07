package vista;

import Objetos.Chat;
import Objetos.Usuario;
import controlador.Controlador;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;


public class PanelChatsDisponibles extends javax.swing.JPanel {

    private Chat chat;
    private Controlador controlador;
    private Consumer<Chat> alHacerClic; 
    private Usuario usuario;
    private Boolean seleccionado;

    public PanelChatsDisponibles(Chat chat, Controlador controlador, Consumer<Chat> alHacerClic, Usuario uLogueado) {
        initComponents();
        this.chat = chat;
        this.controlador = controlador;
        this.alHacerClic = alHacerClic; 
        this.usuario = uLogueado;
        
        setBackground(new Color(255, 255, 255));
        configurarTexto();
        configurarEventos(); 
    }
    
public void setSeleccionado(boolean seleccionado) {
        this.seleccionado = seleccionado;
        actualizarColorFondo();
    }

    private void actualizarColorFondo() {
        if (this.seleccionado) {
            setBackground(new Color(230, 230, 230)); // Color Oscuro (Seleccionado)
        } else {
            setBackground(new Color(255, 255, 255)); // Color Blanco (Normal)
        }
    }

    private void configurarEventos() {
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // YA NO cambiamos el color aquí directamente.
                // Dejamos que el Modelo nos avise a través del método actualizar() del Frame.
                
                if (alHacerClic != null) {
                    alHacerClic.accept(chat);
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                // Opcional: Efecto hover suave si NO está seleccionado
                if (!seleccionado) {
                    setBackground(new Color(245, 245, 245));
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // Regresamos al color que corresponda según el estado real
                actualizarColorFondo();
            }
        });
    }

    public void configurarTexto(){
    String nombreMostrar = "Chat " + chat.getId();

            if (chat.getUsuarios() != null) {
                for (Usuario u : chat.getUsuarios()) {
                    if (u.getId() != this.usuario.getId()) {
                        nombreMostrar = u.getUsuario(); 
                        break;
                    }
                }
            }

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
