package io.github.efkabe.launcher;

import java.io.File;
import java.util.Arrays;

import fr.litarvan.openauth.AuthPoints;
import fr.litarvan.openauth.AuthenticationException;
import fr.litarvan.openauth.Authenticator;
import fr.litarvan.openauth.model.AuthAgent;
import fr.litarvan.openauth.model.response.AuthResponse;
import fr.theshark34.openlauncherlib.LaunchException;
import fr.theshark34.openlauncherlib.external.ExternalLaunchProfile;
import fr.theshark34.openlauncherlib.external.ExternalLauncher;
import fr.theshark34.openlauncherlib.minecraft.AuthInfos;
import fr.theshark34.openlauncherlib.minecraft.GameFolder;
import fr.theshark34.openlauncherlib.minecraft.GameInfos;
import fr.theshark34.openlauncherlib.minecraft.GameTweak;
import fr.theshark34.openlauncherlib.minecraft.GameType;
import fr.theshark34.openlauncherlib.minecraft.GameVersion;
import fr.theshark34.openlauncherlib.minecraft.MinecraftLauncher;
import fr.theshark34.openlauncherlib.util.ramselector.RamSelector;
import fr.theshark34.supdate.BarAPI;
import fr.theshark34.supdate.SUpdate;
import fr.theshark34.supdate.application.integrated.FileDeleter;
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
		SUpdate su = new SUpdate("http://launcher.pumpmykins.eu/", PMK_DIR);
		su.getServerRequester().setRewriteEnabled(true);
		su.addApplication(new FileDeleter());
		
		threadUpdate = new Thread(){
			private int val;
			private int max;
			
			@Override
			public void run() {
				while(!this.isInterrupted()) {
					if(BarAPI.getNumberOfFileToDownload()==0) {
						LauncherFrame.getInstance().getPanel().setInfoText("verification des fichiers");
						continue;
					}
					val = (int) (BarAPI.getNumberOfTotalDownloadedBytes() / 1000);
					max = (int) (BarAPI.getNumberOfTotalBytesToDownload() / 1000);
					
					
					LauncherFrame.getInstance().getPanel().getProgressBar().setMaximum(max);
					LauncherFrame.getInstance().getPanel().getProgressBar().setValue(val);
					
					
					LauncherFrame.getInstance().getPanel().setInfoText("Telechargement des fichiers : "+ 
						BarAPI.getNumberOfDownloadedFiles()+"/"+BarAPI.getNumberOfFileToDownload() +
						" "+ Swinger.percentage(val, max)+ "%");
					}
			}
		};
		threadUpdate.start();
		
		su.start();
		threadUpdate.interrupt();
		
	}
	public static void launch() throws LaunchException{
		ExternalLaunchProfile profile = MinecraftLauncher.createExternalProfile(PMK_INFOS, GameFolder.BASIC, authInfos);
		profile.getVmArgs().addAll(Arrays.asList(LauncherFrame.getInstance().getPanel().getRamSelector().getRamArguments()));
		LauncherFrame.getInstance().getPanel().getRamSelector().save();
		
		ExternalLauncher launcher = new ExternalLauncher(profile);
		
		Process p = launcher.launch();
		
		try {
			Thread.sleep(5000L);
			LauncherFrame.getInstance().setVisible(false);	
			p.waitFor();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.exit(0);
	}
	
	public static void interruptThread() {
		threadUpdate.interrupt();
	}
}
