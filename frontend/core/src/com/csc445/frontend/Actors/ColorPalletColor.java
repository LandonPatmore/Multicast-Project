package com.csc445.frontend.Actors;

import com.csc445.frontend.Utils.Colors;
import com.csc445.frontend.Utils.Helper;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

public class ColorPalletColor extends Actor {

    private Colors color;
    private final Vector2 canvasPos;
    private final int size;
    private Texture t;
    private Color actualColor;


    public ColorPalletColor(Colors color, Vector2 canvasPos, int size) {
        this.color = color;
        this.canvasPos = canvasPos;
        this.size = size;

        createPixmap();
        setBounds(canvasPos.x, canvasPos.y, size, size);
        setListener();
    }

    /**
     * Sets the listener for this particular pixel
     */
    private void setListener() {
        addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Helper.setSelectedColor(actualColor);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println(color);
            }
        });
    }

    /**
     * Creates a pixmap to be piped into a texture to create a filled rectangle
     */
    private void createPixmap() {
        switch (color) {
            case RED:
                actualColor = Color.RED;
                break;
            case BLUE:
                actualColor = Color.BLUE;
                break;
            case BLACK:
                actualColor = Color.BLACK;
                break;
            case GREEN:
                actualColor = Color.GREEN;
                break;
            case WHITE:
                actualColor = Color.WHITE;
                break;
            case ORANGE:
                actualColor = Color.ORANGE;
                break;
            case PURPLE:
                actualColor = Color.PURPLE;
                break;
            case YELLOW:
                actualColor = Color.YELLOW;
                break;
            case PINK:
                actualColor = Color.PINK;
                break;
            case TEAL:
                actualColor = Color.TEAL;
                break;
        }


        final Pixmap pixmap = new Pixmap(size, size, Pixmap.Format.RGBA8888);
        pixmap.setColor(actualColor);
        pixmap.fillRectangle(0, 0, size, size);
        t = new Texture(pixmap);
        pixmap.dispose();
    }

    /**
     * @param batch       batch
     * @param parentAlpha parentAlpha
     */
    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(t, canvasPos.x, canvasPos.y);
    }
}
