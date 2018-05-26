package io.github.efkabe.launcher;

import javax.swing.JFrame;

import fr.theshark34.swinger.Swinger;
import fr.theshark34.swinger.util.WindowMover;

@SuppressWarnings("serial")
public class LauncherFrame extends JFrame {
	
	private static LauncherFrame instance;
	private LauncherPanel launcherPanel;
	
	public LauncherFrame() {
		this.setTitle("PumpMyKins Launcher");
		this.setSize(1280, 720);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setUndecorated(true);
		this.setIconImage(Swinger.getResource("Logo x64.png"));
		this.setContentPane(launcherPanel = new LauncherPanel());
		
		this.setVisible(true);
		
		WindowMover mover = new WindowMover(this);
		this.addMouseListener(mover);
		this.addMouseMotionListener(mover);
	}

	public static void main(String[] args) {
		
		Swinger.setSystemLookNFeel();
		Swinger.setResourcePath("/io/github/efkabe/launcher/resources/");
		
		instance = new LauncherFrame();

	}
	
	public static LauncherFrame getInstance(){
		return instance;
	}
	
	public LauncherPanel getPanel() {
		return this.launcherPanel;
	}

}
