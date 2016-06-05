
package io;

public class BackgroundPanel extends javax.swing.JPanel {

    java.awt.Image image;

    public BackgroundPanel(String nom) {
        try {
            image = javax.imageio.ImageIO.read(new java.net.URL(getClass().getResource("PNGS/"+nom), nom));
            java.awt.Dimension size = new java.awt.Dimension(image.getWidth(null), image.getHeight(null));
            setPreferredSize(size);
            setMinimumSize(size);
            setMaximumSize(size);
            setSize(size);
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
