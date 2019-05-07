package com.csc445.frontend;

import com.csc445.frontend.Game.PixelArt;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 500;
		config.height = 750;
		new LwjglApplication(new PixelArt(), config);
	}
}
