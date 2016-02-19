// Created by: Raymond Clark

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class DisplayFrame {
	
	public static void main(String[] args) {
		createAndShowGUI();
	}
	
	static void createAndShowGUI() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				final int WIDTH = 1280;
				final int HEIGHT = 700;
				
				JFrame frame = new FlockFrame(WIDTH,HEIGHT);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setResizable(false);
				frame.setVisible(true);
			}
		});
	}
	
}
