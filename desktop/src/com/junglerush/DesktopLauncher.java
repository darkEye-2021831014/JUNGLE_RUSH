package com.junglerush;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.junglerush.JungleRush;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setTitle("JUNGLE_RUSH");
		config.setWindowedMode(1080,720);
		config.useVsync(true);
		config.setForegroundFPS(60);
		new Lwjgl3Application(new JungleRush(), config);
	}
}
