package io.github.efkabe.launcher;

import java.io.File;

import fr.theshark34.openlauncherlib.minecraft.GameInfos;
import fr.theshark34.openlauncherlib.minecraft.GameTweak;
import fr.theshark34.openlauncherlib.minecraft.GameType;
import fr.theshark34.openlauncherlib.minecraft.GameVersion;

public class Launcher {
	
	public static final GameVersion PMK_VERSION = new GameVersion("1.12.2", GameType.V1_8_HIGHER);
	public static final GameInfos PMK_INFOS = new GameInfos("PumMyKins", PMK_VERSION, new GameTweak[]{GameTweak.FORGE});
	public static final File PMK_DIR = PMK_INFOS.getGameDir();

}
