
import javax.swing.*;
import java.awt.Dimension;
import javax.swing.event.*;

import java.awt.*;
import java.awt.event.*;

import javax.swing.BoxLayout;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.ArrayList;

// Created by: Raymond Clark

public class FlockFrame extends JFrame  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	JPanel buttonBar = new JPanel();
	
	private Insets insets = new Insets(0, 10, 4, 10);
	private FlockPanel flockPanel;
	private Graphics2D g2d;
	private BufferedImage flockBufferedImage;
	private ImageIcon icon;
	private Timer timer;
	
	Color backgroundColor;
	
	
	private final int WIDTH = 900;
	private final int HEIGHT = 700;
	
	private final int MILLISECONDS_BETWEEN_FRAMES = 17;
	
	private ArrayList<Bird> birdieArray;
	
	int r, g, b;
	int fr, fg, fb;
	
	
	JSlider rbSlider;
	JSlider gbSlider;
	JSlider bbSlider;
	
	JSlider rFSlider;
	JSlider gFSlider;
	JSlider bFSlider;
	
	JSlider separationSlider;
	JSlider alignmentSlider;
	JSlider cohesionSlider;
	
	JSlider speedSlider;
	JSlider sizeSlider;
	
	
	JButton start;
	JButton stop;
	JButton reset;
	
	
	int birdCount;
	
	float separationInten, alignInten, coheInten;
	
	int speed, size;
	
	
	public FlockFrame(int width, int height) {
		this.setTitle("CAP 3027 2014-Term Project-Raymond Clark");
		this.setSize(width,height);
		
		flockBufferedImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
						
		birdieArray = new ArrayList<Bird>();
		
		r = g = b = 0;
		fr = fb = 0;
		fg = 255;
		separationInten = 1.5f;
		alignInten = coheInten = 1.0f;
		speed = 2;
		size = 4;
		
		g2d = (Graphics2D)flockBufferedImage.createGraphics();
		
		backgroundColor = new Color(r,g,b);
		
		birdCount = 200;
		
		addMenu();
		addControls();
		setupPanel();
		addBirds();
	}
	
	void resetControls() {
		this.r = this.g = this.b = 0;
		this.fr = this.fb = 0;
		this.fg = 255;
		this.separationInten = 1.5f;
		this.alignInten = this.coheInten = 1.0f;
		this.speed = 2;
		this.size = 4;
	}
	
	void resetStuff() {
		resetControls();
		
		rbSlider.setValue(0);
		gbSlider.setValue(0);
		bbSlider.setValue(0);
		
		rFSlider.setValue(0);
		gFSlider.setValue(255);
		bFSlider.setValue(0);
		
		separationSlider.setValue((int)1.5f);
		alignmentSlider.setValue((int)1.0f);
		cohesionSlider.setValue((int)1.0f);
		
		speedSlider.setValue(2);
		sizeSlider.setValue(4);
	}
	
	
	void updateBirdCount(int count) {
		this.birdCount = count;
	}
	
	void addBirds() {
		for(int i = 0; i < birdCount; i++) {
			birdieArray.add(new Bird(flockBufferedImage.getWidth()/2, flockBufferedImage.getHeight()/2));
		}
	}
	
	void drawBirds() {
		for(Bird bird: birdieArray) {
			bird.simulate(birdieArray);
					
			bird.drawBird(g2d);
		}
		icon.setImage(flockBufferedImage);
		repaint();
	}
	
	void setupPanel() {
		g2d.setColor((backgroundColor));
		g2d.fillRect(0, 0, flockBufferedImage.getWidth(), flockBufferedImage.getHeight());
		
		flockPanel = new FlockPanel(flockBufferedImage);
		
		flockPanel.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if(SwingUtilities.isLeftMouseButton(e)) {
					//System.out.println("Left Mouse Pressed!");
					Point mousePoint = MouseInfo.getPointerInfo().getLocation();
					birdieArray.add(new Bird(mousePoint.x, mousePoint.y - 70));
				}
				else if(SwingUtilities.isRightMouseButton(e)) {
					//System.out.println("Right Mouse Pressed!");
					birdieArray.remove(birdieArray.size() - 1);
				}
			}
		});
		
		icon = new ImageIcon(flockBufferedImage);
		this.getContentPane().add(flockPanel, BorderLayout.CENTER);
		
		timer = new Timer(MILLISECONDS_BETWEEN_FRAMES, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						timer.stop();
						drawBirds();
						g2d.setColor(backgroundColor);
						g2d.fillRect(0, 0, flockBufferedImage.getWidth(), flockBufferedImage.getHeight());
						drawBirds();
						timer.restart();
					}
				
				});
			}
		});
	}
	
	void addComponent(Component a, Container b, int xPos, int yPos, int width, int height, Insets inset, int center, int span) {
		GridBagConstraints g = new GridBagConstraints(xPos, yPos, width, height, 1.0, 1.0, center, span, insets, 0, 0);
		b.add(a, g);
	}
	
	
	void addControls() {
		
		TitledBorder backgroundBorder;
		backgroundBorder = BorderFactory.createTitledBorder("Background Color");
		
		TitledBorder foregroundBorder;
		foregroundBorder = BorderFactory.createTitledBorder("Bird Color");
		
		TitledBorder flockRulesBorder;
		flockRulesBorder = BorderFactory.createTitledBorder("Flocking Rules");
		
		TitledBorder optionsBorder;
		optionsBorder = BorderFactory.createTitledBorder("Additional Options");
		
		
		JPanel gridPanel = new JPanel(new GridLayout(1, 2, 4, 0));
		
		JPanel backgroundPanel = new JPanel();
		backgroundPanel.setLayout(new GridBagLayout());
		backgroundPanel.setBorder(backgroundBorder);
		
		JPanel foregroundPanel = new JPanel();
		foregroundPanel.setLayout(new GridBagLayout());
		foregroundPanel.setBorder(foregroundBorder);
		
		JPanel rulesPanel = new JPanel();
		rulesPanel.setLayout(new GridBagLayout());
		rulesPanel.setBorder(flockRulesBorder);
		
		JPanel optionsPanel = new JPanel();
		optionsPanel.setLayout(new GridBagLayout());
		optionsPanel.setBorder(optionsBorder);
		
		buttonBar.setLayout(new GridBagLayout());
				
		int width = 0;
		
		JLabel rLabel = new JLabel("Red");
		rLabel.setHorizontalAlignment(SwingConstants.LEFT);
		JLabel gLabel = new JLabel("Green");
		gLabel.setHorizontalAlignment(SwingConstants.LEFT);
		JLabel bLabel = new JLabel("Blue");
		bLabel.setHorizontalAlignment(SwingConstants.LEFT);
		
		JLabel rFLabel = new JLabel("Red");
		rLabel.setHorizontalAlignment(SwingConstants.LEFT);
		JLabel gFLabel = new JLabel("Green");
		gLabel.setHorizontalAlignment(SwingConstants.LEFT);
		JLabel bFLabel = new JLabel("Blue");
		bLabel.setHorizontalAlignment(SwingConstants.LEFT);
		
		JLabel separation = new JLabel("Separation");
		separation.setHorizontalAlignment(SwingConstants.LEFT);
		JLabel alignment = new JLabel("Alignment");
		alignment.setHorizontalAlignment(SwingConstants.LEFT);
		JLabel cohesion = new JLabel("Cohesion");
		cohesion.setHorizontalAlignment(SwingConstants.LEFT);
		
		JLabel speedLabel = new JLabel("Speed");
		speedLabel.setHorizontalAlignment(SwingConstants.LEFT);
		JLabel sizeLabel = new JLabel("Size");
		sizeLabel.setHorizontalAlignment(SwingConstants.LEFT);
		
		
		rbSlider = new JSlider(JSlider.HORIZONTAL, 0, 255, 0);
		gbSlider = new JSlider(JSlider.HORIZONTAL, 0, 255, 0);
		bbSlider = new JSlider(JSlider.HORIZONTAL, 0, 255, 0);
		
		rbSlider.setPaintLabels(true);
		rbSlider.setMajorTickSpacing(255 / 3);
		gbSlider.setPaintLabels(true);
		gbSlider.setMajorTickSpacing(255 / 3);
		bbSlider.setPaintLabels(true);
		bbSlider.setMajorTickSpacing(255 / 3);
		
		rbSlider.addChangeListener(new BackgroundColorListener());
		gbSlider.addChangeListener(new BackgroundColorListener());
		bbSlider.addChangeListener(new BackgroundColorListener());
		
		addComponent(rLabel, backgroundPanel, 0, width, 2, 1, insets, GridBagConstraints.LINE_START, GridBagConstraints.HORIZONTAL);
		addComponent(rbSlider, backgroundPanel, 2, width++, 2, 1, insets, GridBagConstraints.LINE_START, GridBagConstraints.HORIZONTAL);
		addComponent(gLabel, backgroundPanel, 0, width, 2, 1, insets, GridBagConstraints.LINE_START, GridBagConstraints.HORIZONTAL);
		addComponent(gbSlider, backgroundPanel, 2, width++, 2, 1, insets, GridBagConstraints.LINE_START, GridBagConstraints.HORIZONTAL);
		addComponent(bLabel, backgroundPanel, 0, width, 2, 1, insets, GridBagConstraints.LINE_START, GridBagConstraints.HORIZONTAL);
		addComponent(bbSlider, backgroundPanel, 2, width++, 2, 1, insets, GridBagConstraints.LINE_START, GridBagConstraints.HORIZONTAL);	
		
		addComponent(backgroundPanel, buttonBar, 0, width++, 2, 1, insets, GridBagConstraints.LINE_START, GridBagConstraints.HORIZONTAL);	

		
		rFSlider = new JSlider(JSlider.HORIZONTAL, 0, 255, 0);
		gFSlider = new JSlider(JSlider.HORIZONTAL, 0, 255, 255);
		bFSlider = new JSlider(JSlider.HORIZONTAL, 0, 255, 0);
		
		rFSlider.setPaintLabels(true);
		rFSlider.setMajorTickSpacing(255 / 3);
		gFSlider.setPaintLabels(true);
		gFSlider.setMajorTickSpacing(255 / 3);
		bFSlider.setPaintLabels(true);
		bFSlider.setMajorTickSpacing(255 / 3);
		
		rFSlider.addChangeListener(new BackgroundColorListener());
		gFSlider.addChangeListener(new BackgroundColorListener());
		bFSlider.addChangeListener(new BackgroundColorListener());
		
		addComponent(rFLabel, foregroundPanel, 0, width, 2, 1, insets, GridBagConstraints.LINE_START, GridBagConstraints.HORIZONTAL);
		addComponent(rFSlider, foregroundPanel, 2, width++, 2, 1, insets, GridBagConstraints.LINE_START, GridBagConstraints.HORIZONTAL);
		addComponent(gFLabel, foregroundPanel, 0, width, 2, 1, insets, GridBagConstraints.LINE_START, GridBagConstraints.HORIZONTAL);
		addComponent(gFSlider, foregroundPanel, 2, width++, 2, 1, insets, GridBagConstraints.LINE_START, GridBagConstraints.HORIZONTAL);
		addComponent(bFLabel, foregroundPanel, 0, width, 2, 1, insets, GridBagConstraints.LINE_START, GridBagConstraints.HORIZONTAL);
		addComponent(bFSlider, foregroundPanel, 2, width++, 2, 1, insets, GridBagConstraints.LINE_START, GridBagConstraints.HORIZONTAL);
		
		addComponent(foregroundPanel, buttonBar, 0, width++, 2, 1, insets, GridBagConstraints.LINE_START, GridBagConstraints.HORIZONTAL);
		
		separationSlider = new JSlider(JSlider.HORIZONTAL, 0, 10, (int)1.5f);
		alignmentSlider = new JSlider(JSlider.HORIZONTAL, 0, 10, (int)1.0f);
		cohesionSlider = new JSlider(JSlider.HORIZONTAL, 0, 10, (int)1.0f);
			
		separationSlider.setPaintLabels(true);
		separationSlider.setMajorTickSpacing(5);
		alignmentSlider.setPaintLabels(true);
		alignmentSlider.setMajorTickSpacing(5);
		cohesionSlider.setPaintLabels(true);	
		cohesionSlider.setMajorTickSpacing(5);
		
		separationSlider.addChangeListener(new RuleChangeListener());
		alignmentSlider.addChangeListener(new RuleChangeListener());
		cohesionSlider.addChangeListener(new RuleChangeListener());
		
		addComponent(separation, rulesPanel, 0, width, 2, 1, insets, GridBagConstraints.LINE_START, GridBagConstraints.HORIZONTAL);
		addComponent(separationSlider, rulesPanel, 2, width++, 2, 1, insets, GridBagConstraints.LINE_START, GridBagConstraints.HORIZONTAL);
		addComponent(alignment, rulesPanel, 0, width, 2, 1, insets, GridBagConstraints.LINE_START, GridBagConstraints.HORIZONTAL);
		addComponent(alignmentSlider, rulesPanel, 2, width++, 2, 1, insets, GridBagConstraints.LINE_START, GridBagConstraints.HORIZONTAL);
		addComponent(cohesion, rulesPanel, 0, width, 2, 1, insets, GridBagConstraints.LINE_START, GridBagConstraints.HORIZONTAL);
		addComponent(cohesionSlider, rulesPanel, 2, width++, 2, 1, insets, GridBagConstraints.LINE_START, GridBagConstraints.HORIZONTAL);	
		
		addComponent(rulesPanel, buttonBar, 0, width++, 2, 1, insets, GridBagConstraints.LINE_START, GridBagConstraints.HORIZONTAL);	

		
		speedSlider = new JSlider(JSlider.HORIZONTAL, 1, 10, 2);
		sizeSlider = new JSlider(JSlider.HORIZONTAL, 1, 10, 4);
		
		speedSlider.setPaintLabels(true);
		speedSlider.setMajorTickSpacing(9 / 3);
		sizeSlider.setPaintLabels(true);
		sizeSlider.setMajorTickSpacing(9 / 3);
		
		speedSlider.addChangeListener(new ChangeSpeedListener());		
		sizeSlider.addChangeListener(new ChangeSizeListener());
		
		addComponent(speedLabel, optionsPanel, 0, width, 2, 1, insets, GridBagConstraints.LINE_START, GridBagConstraints.HORIZONTAL);
		addComponent(speedSlider, optionsPanel, 2, width++, 2, 1, insets, GridBagConstraints.LINE_START, GridBagConstraints.HORIZONTAL);
		addComponent(sizeLabel, optionsPanel, 0, width, 2, 1, insets, GridBagConstraints.LINE_START, GridBagConstraints.HORIZONTAL);
		addComponent(sizeSlider, optionsPanel, 2, width++, 2, 1, insets, GridBagConstraints.LINE_START, GridBagConstraints.HORIZONTAL);		

		addComponent(optionsPanel, buttonBar, 0, width++, 2, 1, insets, GridBagConstraints.LINE_START, GridBagConstraints.HORIZONTAL);		

		
		start = new JButton("Start");
		start.setHorizontalAlignment(SwingConstants.CENTER);
		start.addActionListener(new StartListener());
		gridPanel.add(start);
		
		stop = new JButton("Stop");
		stop.setHorizontalAlignment(SwingConstants.CENTER);
		stop.addActionListener(new StopListener());
		gridPanel.add(stop);

		reset = new JButton("Reset");
		reset.setHorizontalAlignment(SwingConstants.CENTER);		
		reset.addActionListener(new ResetListener());
		gridPanel.add(reset);
		addComponent(gridPanel, buttonBar, 0, width++, 4, 1, insets, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL);		
		
		buttonBar.setPreferredSize(new Dimension(380, 650));
		this.getContentPane().add(buttonBar, BorderLayout.EAST);
	}

	void changeBackground() {		
		backgroundColor = new Color(r,g,b);		
	}
	
	void addMenu() {
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		
		
		JMenuItem exitItem = new JMenuItem("Exit");
		exitItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		
		fileMenu.add(exitItem);
		
		menuBar.add(fileMenu);
		
		this.setJMenuBar(menuBar);
		
	}
	
	public class ResetListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			birdieArray.clear();
			timer.restart();
			addBirds();
			resetStuff();
		}
	}
	public class StartListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			timer.start();
		}
	}
	
	public class StopListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			timer.stop();
		}
	}
	
	public class ChangeSizeListener implements ChangeListener {
		public void stateChanged(ChangeEvent e) {
			for(Bird b: birdieArray) {
				float size = 4f;
				
				size = sizeSlider.getValue();
				
				b.updateSize(size);
			}
		}
	}
	
	public class ChangeSpeedListener implements ChangeListener {
		public void stateChanged(ChangeEvent e) {
			for(Bird b: birdieArray) {
				float speed = 2f;
				
				speed = speedSlider.getValue();
				
				b.updateSpeed(speed);
			}
		}
	}
	
	public class RuleChangeListener implements ChangeListener {
		public void stateChanged(ChangeEvent e) {
			
			for(Bird b: birdieArray) {
				float sep = 1.5f;
				float ali = 1.0f;
				float coh = 1.0f;
			
				sep = separationSlider.getValue();
				ali = alignmentSlider.getValue();
				coh = cohesionSlider.getValue();
			
				b.updateRuleIntensity(sep, ali, coh);
			}
		}
	}
	public class BackgroundColorListener implements ChangeListener {
		public void stateChanged(ChangeEvent e) {
			
			r = rbSlider.getValue();
			g = gbSlider.getValue();
			b = bbSlider.getValue();
			
			for(Bird b: birdieArray) {				
				b.r = rFSlider.getValue();
				b.g = gFSlider.getValue();
				b.b = bFSlider.getValue();
							
				b.changeColor();
			}
			
			changeBackground();
		}
	}

}