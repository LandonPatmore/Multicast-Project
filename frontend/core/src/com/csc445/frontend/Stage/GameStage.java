package com.csc445.frontend.Stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
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

    private final int textHeight = 500;

    private final int textWidth = 225;

    private String name;

    private String pass;

    private final Skin whiteSkin = new Skin(Gdx.files.internal("skins/whitefont/uiskin.json"));
    private final Skin altSkin = new Skin(Gdx.files.internal("skins/altfont/uiskin.json"));

    private final Button addJoinButton = new TextButton("JOIN", whiteSkin);

    private final TextField passwordTextField = new TextField("Password", whiteSkin);
    private final TextField nameTextField = new TextField("Name", whiteSkin);

    public final TextArea textArea = new TextArea("Welcome to PixelArt!\n"
            // Adding long text for soft line breaks
            + "This game was inspired by r/Place. Credits to Landon Patmore, Ye Bhone Myat, Robert Kilmer, and Benjamin Caro ", altSkin) {
        public float getPrefHeight () {
            float prefHeight = getLines() * getStyle().font.getLineHeight();
//                float prefHeight = (getLines() + 1) * getStyle().font.getLineHeight(); // Work around
            TextFieldStyle style = getStyle();
            if (style.background != null) {
                prefHeight = Math.max(prefHeight + style.background.getBottomHeight() + style.background.getTopHeight(), style.background.getMinHeight());
            }
            return prefHeight;
        }
    };

    private int lineCounter;

    /**
     * @param viewport ScreenViewport
     */
    public GameStage(Viewport viewport) {
        super(viewport);

        off_H = getHeight() / 3;

        Gdx.gl.glClearColor(192/255f, 192/255f, 192/255f, 1);
        generatePixels();
        addPalletColors();
        addText();
        addJoin();
    }

    private void addText(){
        int xPos = 513;
        int yPos = 200;
        Gdx.input.setInputProcessor(this);
        Table container = new Table();
        this.addActor(container);
        container.setColor(Color.BLUE);
        container.setSize(textWidth, textHeight);
        container.setPosition(xPos, yPos);
        container.row().width(textWidth);
        container.row().height(textHeight);
        container.pad(10).defaults().expandX().fillX().space(4);

        final OpenScrollPane scrollPane = new OpenScrollPane(null, altSkin);
        scrollPane.setSize(textWidth, textHeight);
        scrollPane.setPosition(xPos, yPos);
//        scrollPane.setColor(Color.BLUE);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setFlickScroll(false);
        scrollPane.setScrollingDisabled(true, false);

        textArea.setSize(textWidth, textHeight);
        textArea.setDisabled(true);

        scrollPane.setWidget(textArea);

        Button addLineButton = new TextButton("Add new line", whiteSkin);
        addLineButton.setPosition(525, 710);
        addLineButton.setSize(200, 30);
        addLineButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                textArea.appendText("\nLine " + lineCounter++);
                scrollPane.invalidate();
                scrollPane.scheduleScrollToBottom(); // See OpenScrollPane below
            }
        });
        this.addActor(addLineButton);

        container.add(scrollPane);
        container.debugAll();
    }

    private void addJoin(){
        int buttonPassWidth = 200;
        int buttonPassHeight = 30;
        int positionX = 525;
        int positionY = 100;
        nameTextField.setPosition(positionX, positionY+buttonPassHeight+5);
        nameTextField.setSize(buttonPassWidth, buttonPassHeight);
        passwordTextField.setPosition(positionX,positionY);
        passwordTextField.setSize(buttonPassWidth, buttonPassHeight);
        addJoinButton.setSize(buttonPassWidth, buttonPassHeight);
        addJoinButton.setPosition(positionX, positionY-(buttonPassHeight+5));
        addJoinButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                name = nameTextField.getText();
                pass = passwordTextField.getText();
                System.out.println("Name: "+name+"\nPassword: "+pass);
                nameTextField.invalidate();
                passwordTextField.invalidate();
                addJoinButton.invalidate();
            }
        });
        this.addActor(this.nameTextField);
        this.addActor(this.passwordTextField);
        this.addActor(this.addJoinButton);
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


    public class OpenScrollPane extends ScrollPane {

        private boolean scrollToBottom;

        public OpenScrollPane(Actor widget, Skin skin) {
            super(widget, skin);
        }

        public void scheduleScrollToBottom() {
            scrollToBottom = true;
        }

        @Override
        public void layout() {
            super.layout();
            if (scrollToBottom) {
                setScrollY(getMaxY());
            }
        }

    }
}
