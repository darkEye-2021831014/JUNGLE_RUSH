package com.junglerush;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.junglerush.JungleRush;

import java.util.Scanner;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher implements Platform{

	public static String playerName;
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setTitle("JUNGLE_RUSH");
		config.setWindowedMode(1280, 800);
		config.useVsync(true);
		config.setForegroundFPS(60);

		//take player name from console
		Scanner in = new Scanner(System.in);
		playerName = " ";
		while (playerName.isEmpty() || (playerName.contains(":") || Character.toUpperCase(playerName.charAt(0)) < 'A' || Character.toUpperCase(playerName.charAt(0)) > 'Z')){
			System.out.println("Please Enter Your Name(max 15 character): ");
			playerName = in.nextLine();
			if(playerName.length() > 15) playerName = "";
		}
		JungleRush jungleRush = new JungleRush();
		jungleRush.setPlatform(new DesktopLauncher());
		new Lwjgl3Application(jungleRush, config);
	}

	@Override
	public String getPlayerName() {
		return playerName;
	}
}
