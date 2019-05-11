package com.csc445.frontend.Actors;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.ArrayList;

public class JoinedPlayers extends Actor {

    private final Vector2 canvasPos;
    private final int size;
    private Texture t;
    private Pixmap pixmap;

    public JoinedPlayers(Vector2 canvasPos, int size) {
        this.canvasPos = canvasPos;
        this.size = size;
    }

    @Override    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(t, canvasPos.x, canvasPos.y);
    }
}
