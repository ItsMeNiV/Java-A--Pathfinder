package CH.niv.astarmain;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.Buffer;

public class ColorPanel extends JPanel{
    BufferedImage img;
    public ColorPanel(BufferedImage image){
        img = image;
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(img, 0, 0, Mainframe.WINDOW_WIDTH, Mainframe.WINDOW_HEIGHT, Color.white, null);
    }
}
