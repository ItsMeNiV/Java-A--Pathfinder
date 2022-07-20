package CH.niv.astarmain;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class ColorPanel extends JPanel {

	private BufferedImage img;
	private int windowWidth;
	private int windowHeight;

	public ColorPanel(BufferedImage image, int windowWidth, int windowHeight) {
		img = image;
		this.windowWidth = windowWidth;
		this.windowHeight = windowHeight;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(img, 0, 0, windowWidth - 15, windowHeight - 25, Color.white, null);
	}
}
