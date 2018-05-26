package io.github.efkabe.launcher;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import fr.litarvan.openauth.AuthenticationException;
import fr.theshark34.openlauncherlib.LaunchException;
import fr.theshark34.openlauncherlib.util.Saver;
import fr.theshark34.swinger.Swinger;
import fr.theshark34.swinger.colored.SColoredBar;
import fr.theshark34.swinger.event.SwingerEvent;
import fr.theshark34.swinger.event.SwingerEventListener;
import fr.theshark34.swinger.textured.STexturedButton;

public class LauncherPanel extends JPanel implements SwingerEventListener {

	private Image background = Swinger.getResource("launcher_bg.png");

	private Saver saver = new Saver(new File(Launcher.PMK_DIR, "launcher.properties"));

	private JTextField usernameField = new JTextField("");
	private JPasswordField passwordField = new JPasswordField("");
	private STexturedButton quitButton = new STexturedButton(Swinger.getResource("launcher_quit.png"));
	private STexturedButton hideButton = new STexturedButton(Swinger.getResource("launcher_minimize.png"));
	private STexturedButton playButton = new STexturedButton(Swinger.getResource("launcher_play.png"));
	private SColoredBar progressBar = new SColoredBar(new Color(255, 255, 255, 100), Color.GREEN);
	private JLabel infoLabel = new JLabel("Clique sur Jouer !", SwingConstants.CENTER);

	public LauncherPanel() {
		this.setLayout(null);

		usernameField.setForeground(Color.WHITE);
		usernameField.setOpaque(false);
		usernameField.setFont(usernameField.getFont().deriveFont(30F));
		usernameField.setCaretColor(Color.WHITE);
		usernameField.setBorder(null);
		usernameField.setBounds(833, 134, 377, 83);
		this.add(usernameField);

		passwordField.setForeground(Color.WHITE);
		passwordField.setOpaque(false);
		passwordField.setFont(usernameField.getFont());
		passwordField.setCaretColor(Color.WHITE);
		passwordField.setBorder(null);
		passwordField.setBounds(833, 297, 377, 83);
		this.add(passwordField);
		
		playButton.setBounds(829, 600);
		playButton.addEventListener(this);
		this.add(playButton);

		quitButton.setBounds(0, 0);
		quitButton.addEventListener(this);
		this.add(quitButton);

		hideButton.setBounds(64, 0);
		hideButton.addEventListener(this);
		this.add(hideButton);
		
		infoLabel.setForeground(Color.BLACK);
		infoLabel.setBounds(0,674,825,46);
		this.add(infoLabel);
		
		progressBar.setBounds(0,674,825,46);
		this.add(progressBar);

	}

	@Override
	public void onEvent(SwingerEvent e) {
		if (e.getSource() == playButton) {
			setFieldEnabled(false);
			if(usernameField.getText().replaceAll(" ", "").length() == 0 || passwordField.getText().length() == 0) {
				JOptionPane.showMessageDialog(this, "Aucun nom d'utilisateur ou mot de passe n'a été renseigné", "", JOptionPane.ERROR_MESSAGE);
				setFieldEnabled(true);
				return;
			}
			
			Thread t = new Thread() {
				@Override
				public void run() {
					try {
						Launcher.auth(usernameField.getText(), passwordField.getText());
					} catch(AuthenticationException e){
						JOptionPane.showMessageDialog(LauncherPanel.this, "Impossible de se connecter" + e.getErrorModel().getErrorMessage() , "Erreur", JOptionPane.ERROR_MESSAGE);
						setFieldEnabled(true);
						return;
					}
					
					try {
						Launcher.update();
					} catch(Exception e){
						JOptionPane.showMessageDialog(LauncherPanel.this, "Impossible de mettre le jeu à jour" + e , "Erreur", JOptionPane.ERROR_MESSAGE);
						Launcher.interruptThread();
						setFieldEnabled(true);
						return;
					}
					
					try {
						Launcher.launch();
					} catch(LaunchException e){
						JOptionPane.showMessageDialog(LauncherPanel.this, "impossible de lancer le jeu" + e, "", JOptionPane.ERROR_MESSAGE);
						setFieldEnabled(true);
					}
				}
			};
			t.start();
		}
		else if(e.getSource() == quitButton) {
			System.exit(0);
		}
		else if(e.getSource() == hideButton) {
			LauncherFrame.getInstance().setState(JFrame.ICONIFIED);
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		g.drawImage(background, 0, 0, this.getWidth(), this.getHeight(), this);
	}
	
	private void setFieldEnabled (boolean enabled) {
		usernameField.setEnabled(enabled);
		passwordField.setEnabled(enabled);
		playButton.setEnabled(enabled);
	}
	
	public SColoredBar getProgressBar() {
		return progressBar;
	}
	
	public void setInfoText(String text) {
		infoLabel.setText(text);
	}

}
