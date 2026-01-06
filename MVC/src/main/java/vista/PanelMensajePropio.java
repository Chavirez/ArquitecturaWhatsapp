package vista;

import Objetos.Mensaje;
import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;

public class PanelMensajePropio extends JPanel {
    private JTextArea txtMensaje;
    private JScrollPane scroll;
    private JLabel lblFecha;
    private Mensaje mensaje;

    public PanelMensajePropio(Mensaje mensaje) {
        this.mensaje = mensaje;
        this.setBorder(new javax.swing.border.EmptyBorder(15, 20, 25, 20));
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(5, 5));
        setOpaque(false); 
        
        txtMensaje = new JTextArea("Ejemplo de Texto muy largo...Ejemplo de Texto muy largo...Ejemplo de Texto muy largo...Ejemplo de Texto muy largo...Ejemplo de Texto muy largo...Ejemplo de Texto muy largo...Ejemplo de Texto muy largo...Ejemplo de Texto muy largo...Ejemplo de Texto muy largo...Ejemplo de Texto muy largo...Ejemplo de Texto muy largo...Ejemplo de Texto muy largo...Ejemplo de Texto muy largo...");
        txtMensaje = new JTextArea(mensaje.getMensaje());
        txtMensaje.setLineWrap(true);    
        txtMensaje.setWrapStyleWord(true);
        txtMensaje.setEditable(false);    
        txtMensaje.setOpaque(false);    
        txtMensaje.setBackground(new Color(0,0,0,0));     
        txtMensaje.setForeground(Color.WHITE);
        txtMensaje.setFont(new Font("Nirmala Text", Font.PLAIN, 14));

        scroll = new JScrollPane(txtMensaje);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d - M - yyyy h:mm a");
        lblFecha = new JLabel(mensaje.getFechaEnviado().format(formatter));
        lblFecha.setForeground(new Color(200, 200, 200));
        lblFecha.setFont(new Font("Nirmala Text", Font.BOLD, 10));
        lblFecha.setHorizontalAlignment(SwingConstants.RIGHT);

        add(scroll, BorderLayout.CENTER);
        add(lblFecha, BorderLayout.SOUTH);
        
        
    }
    
    @Override
    public Dimension getMaximumSize() {
        return new Dimension(350, super.getPreferredSize().height);
    }
    
    @Override
    protected void paintComponent(java.awt.Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        ImageIcon icono = new ImageIcon(getClass().getResource("/Imagenes/MensajePropio.png"));
        g2.drawImage(icono.getImage(), 0, 0, getWidth(), getHeight(), this);
    }
}