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
import com.csc445.frontend.Utils.Players;

import java.util.ArrayList;

public class JoinedPlayers extends Actor {

    private final Vector2 canvasPos;
    private final int size;
    private Texture t;
    private Pixmap pixmap;
    private ArrayList<Players> players = new ArrayList<Players>();

    public JoinedPlayers(Vector2 canvasPos, int size) {
        this.canvasPos = canvasPos;
        this.size = size;
    }

    public void addPlayer(Players p){
        players.add(p);
    }

    public void removePlayer(Players p){
        players.remove(p);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(t, canvasPos.x, canvasPos.y);
    }
}
