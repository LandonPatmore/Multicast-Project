package com.csc445.frontend.Stage;

import com.csc445.frontend.Actors.PalletColor;
import com.csc445.frontend.Actors.Pixel;
import com.csc445.frontend.Utils.Colors;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameStage extends Stage {

    // Array of pixels to be displayed on the screen
    private Pixel[][] pixels = new Pixel[50][65];

    // Array of Pallet Colors
    private PalletColor[] palletColors = new PalletColor[Colors.values().length];

    // Pixel size, could be a local variable but like it out here @Landon
    private final int P_SIZE = 10;

    // Pallet Color size
    private final int CP_SIZE = 50;

    private final float off_H;

    /**
     * @param viewport ScreenViewport
     */
    public GameStage(Viewport viewport) {
        super(viewport);

        off_H = getHeight() / 3;

        generatePixels();
        addPalletColors();
    }

    /**
     * Generates the pixels that will be rendered on the screen
     */
    private void generatePixels() {
        for (int i = 0; i < pixels.length; i++) {
            for (int j = 0; j < pixels[i].length; j++) {
                pixels[i][j] = new Pixel(new Vector2(i, j), new Vector2(i * P_SIZE, 100 + j * P_SIZE), P_SIZE);
                addActor(pixels[i][j]);
            }
        }
    }

    private void addPalletColors() {
        for (int i = 0; i < palletColors.length; i++) {
            palletColors[i] = new PalletColor(Colors.values()[i], new Vector2(i * CP_SIZE, 25), CP_SIZE);
            addActor(palletColors[i]);
        }
    }
}
