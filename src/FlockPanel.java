// Created by: Raymond Clark

import javax.swing.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;


public class FlockPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final int WIDTH;
	private final int HEIGHT;
	private BufferedImage image;
	private Graphics2D g2d;
	
	public FlockPanel(BufferedImage image) {
		this.image = image;
		g2d = image.createGraphics();
		WIDTH = image.getWidth();
		HEIGHT = image.getHeight();
	}
	
	public void setImage(BufferedImage image) {
		g2d.setPaintMode();
		g2d.drawImage(image, 0, 0, WIDTH-1, HEIGHT-1, 0, 0, WIDTH-1, HEIGHT-1, Color.BLACK, null);
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(image, 0, 0, null);
	}
	
}
