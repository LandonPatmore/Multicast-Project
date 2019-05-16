package com.csc445.frontend.Actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextTooltip;
import com.csc445.frontend.Utils.Helper;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import org.w3c.dom.Text;

import javax.swing.*;

public class Pixel extends Actor {

    private Pixmap pixmap;
    private final Vector2 gridPos;
    private final Vector2 canvasPos;
    private final int size;
    private TextTooltip toolTip;
    private Texture t;
    private String id;
    private final Skin skin = new Skin(Gdx.files.internal("skins/whitefont/uiskin.json"));


    /**
     * @param gridPos   the pixel's actual grid position
     * @param canvasPos the pixel's position on the canvas
     * @param size      the size of the pixel
     */
    public Pixel(Vector2 gridPos, Vector2 canvasPos, int size) {
        this.gridPos = gridPos;
        this.canvasPos = canvasPos;
        this.size = size;
        id = gridPos.x + " : " + gridPos.y;
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
                Helper.getSocket().emit("pixelChanged", Helper.getSelectedColor());
                pixmap = new Pixmap(size, size, Pixmap.Format.RGBA8888);
                pixmap.setColor(Helper.getSelectedColor());
                pixmap.fillRectangle(0, 0, size, size);
                t = new Texture(pixmap);
                pixmap.dispose();
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println(id);
            }

            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                System.out.println(id);
                toolTip = new TextTooltip(id, skin);
                event.getTarget().addListener(toolTip);
                toolTip.setInstant(true);

                toolTip.handle(event);
            }

            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                toolTip.hide();
            }
        });
    }

    /**
     * Creates a pixmap to be piped into a texture to create a filled rectangle
     */
    private void createPixmap() {
        pixmap = new Pixmap(size, size, Pixmap.Format.RGBA8888);
        setColor();
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

    /**
     * Generates a random color for the pixel
     */
    private void setColor() {
        pixmap.setColor(1, 1, 1, 1);
    }
}
