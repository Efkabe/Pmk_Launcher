package io.github.efkabe.launcher;

import java.io.File;

import fr.litarvan.openauth.AuthPoints;
import fr.litarvan.openauth.AuthenticationException;
import fr.litarvan.openauth.Authenticator;
import fr.litarvan.openauth.model.AuthAgent;
import fr.litarvan.openauth.model.response.AuthResponse;
import fr.theshark34.openlauncherlib.minecraft.AuthInfos;
import fr.theshark34.openlauncherlib.minecraft.GameInfos;
import fr.theshark34.openlauncherlib.minecraft.GameTweak;
import fr.theshark34.openlauncherlib.minecraft.GameType;
import fr.theshark34.openlauncherlib.minecraft.GameVersion;
import fr.theshark34.supdate.BarAPI;
import fr.theshark34.supdate.SUpdate;
import fr.theshark34.swinger.Swinger;

public class Launcher {
	
	public static final GameVersion PMK_VERSION = new GameVersion("1.12.2", GameType.V1_8_HIGHER);
	public static final GameInfos PMK_INFOS = new GameInfos("PumMyKins", PMK_VERSION, new GameTweak[]{GameTweak.FORGE});
	public static final File PMK_DIR = PMK_INFOS.getGameDir();

	private static AuthInfos authInfos;
	private static Thread threadUpdate;
	
	static void auth(String username, String password) throws AuthenticationException {
		Authenticator authenticator = new Authenticator(Authenticator.MOJANG_AUTH_URL, AuthPoints.NORMAL_AUTH_POINTS);
		AuthResponse response = authenticator.authenticate(AuthAgent.MINECRAFT, username, password, "");
		authInfos = new AuthInfos(response.getSelectedProfile().getName(), response.getAccessToken(), response.getSelectedProfile().getId());
	}
	
	public static void update() throws Exception {
		SUpdate su = new SUpdate("http://launcher-pmk.000webhostapp.com", PMK_DIR);
		threadUpdate = new Thread(){
			private int val;
			private int max;
			
			@Override
			public void run() {
				while(!this.isInterrupted()) {
					val = (int) (BarAPI.getNumberOfTotalDownloadedBytes() / 1000);
					max = (int) (BarAPI.getNumberOfTotalBytesToDownload() / 1000);
					
					
					LauncherFrame.getInstance().getPanel().getProgressBar().setMaximum(max);
					LauncherFrame.getInstance().getPanel().getProgressBar().setValue(val);
					
					
					LauncherFrame.getInstance().getPanel().setInfoText("Telechargement des fichiers : "+ 
						BarAPI.getNumberOfDownloadedFiles()+"/"+BarAPI.getNumberOfFileToDownload() +
						" "+ Swinger.percentage(val, max));
					}
			}
		};
		threadUpdate.start();
		
		su.start();
		threadUpdate.interrupt();
		
	}
	
	public static void interruptThread() {
		threadUpdate.interrupt();
	}
}
