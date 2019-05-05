package com.csc445.frontend.Game;

import com.csc445.frontend.Screens.GameScreen;
import com.badlogic.gdx.Game;

public class PixelArt extends Game{

    /**
     * Sets the screen to the GameScreen on Create
     */
    @Override
    public void create() {
        setScreen(new GameScreen());
    }
}
