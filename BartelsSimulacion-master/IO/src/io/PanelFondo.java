
package io;

/**
 * Clase que sobreescribe el objeto JPanel de javax.swing y permite el uso de imagenes en el panel 
 */
public class PanelFondo extends javax.swing.JPanel {

    java.awt.Image image;

    /**
     * Constructor de la clase, el cual carga la imagen, ajusta el panel a este tamaño y luego lo pinta en pantalla.
     * @param nom parametro que representa el nombre de la imagen que será cargada en el panel
     */
    public PanelFondo(String nom) {
        try {
            image = javax.imageio.ImageIO.read(new java.net.URL(getClass().getResource("PNGS/"+nom), nom)); //carga la imagen de nombre nom
            java.awt.Dimension size = new java.awt.Dimension(image.getWidth(null), image.getHeight(null)); //toma sus dimensiones
            setPreferredSize(size); 
            setMinimumSize(size);
            setMaximumSize(size);
            setSize(size);//ajusta el panel a las dimensiones
            setLayout(null);
        } catch (Exception e) {}
    }

    @Override
    protected void paintComponent(java.awt.Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), this);
        }
    }
}
