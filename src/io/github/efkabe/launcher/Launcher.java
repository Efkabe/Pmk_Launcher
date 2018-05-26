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

public class Launcher {
	
	public static final GameVersion PMK_VERSION = new GameVersion("1.12.2", GameType.V1_8_HIGHER);
	public static final GameInfos PMK_INFOS = new GameInfos("PumMyKins", PMK_VERSION, new GameTweak[]{GameTweak.FORGE});
	public static final File PMK_DIR = PMK_INFOS.getGameDir();

	private static AuthInfos authInfos;
	
	static void auth(String username, String password) throws AuthenticationException {
		Authenticator authenticator = new Authenticator(Authenticator.MOJANG_AUTH_URL, AuthPoints.NORMAL_AUTH_POINTS);
		AuthResponse response = authenticator.authenticate(AuthAgent.MINECRAFT, username, password, "");
		authInfos = new AuthInfos(response.getSelectedProfile().getName(), response.getAccessToken(), response.getSelectedProfile().getId());
	}
}
