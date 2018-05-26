package io.github.efkabe.launcher;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

import fr.theshark34.swinger.Swinger;

public class LauncherPanel extends JPanel {
	
	private Image background = Swinger.getResource("background.png");

	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.drawImage(background, 0, 0, this.getWidth(), this.getHeight(), this);
	}

}
