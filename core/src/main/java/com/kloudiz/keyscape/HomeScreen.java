package com.kloudiz.keyscape;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.Game;

public class HomeScreen implements Screen {

    private static final String BACKGROUND_PATH = "ui/background.png";
    private static final String LOGO_PATH = "ui/logo.png";
    private static final String PLAY_BUTTON_PATH = "ui/play_button.png";
    private static final String SETTINGS_BUTTON_PATH = "bouton/settings.png";

    private static final float SCREEN_WIDTH = 800f;
    private static final float SCREEN_HEIGHT = 600f;
    private static final float LOGO_WIDTH = 400f;
    private static final float LOGO_HEIGHT = 400f;
    private static final float PLAY_BUTTON_WIDTH = 150f;
    private static final float PLAY_BUTTON_HEIGHT = 55f;

    private Game game;
    private Stage stage;
    private SpriteBatch batch;

    private Texture backgroundTexture;
    private Texture logoTexture;
    private Texture playButtonTexture;
    private Texture settingsTexture;

    public HomeScreen(Game game) {
        this.game = game;
        this.batch = new SpriteBatch();
        this.stage = new Stage(new FitViewport(SCREEN_WIDTH, SCREEN_HEIGHT));
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        loadTextures();
        buildUI();

        MusicManager.getInstance().loadAndPlay("music/menu_music.mp3");
    }

    private void loadTextures() {
        try {
            backgroundTexture = new Texture(Gdx.files.internal(BACKGROUND_PATH));
            System.out.println("Fond chargé");
        } catch (Exception e) {
            backgroundTexture = ButtonStyleFactory.createColorTexture(1, 1, new Color(0.1f, 0.1f, 0.15f, 1));
        }

        try {
            logoTexture = new Texture(Gdx.files.internal(LOGO_PATH));
            System.out.println("Logo chargé");
        } catch (Exception e) {
            logoTexture = ButtonStyleFactory.createColorTexture(400, 400, new Color(0.8f, 0.2f, 0.2f, 1));
        }

        try {
            playButtonTexture = new Texture(Gdx.files.internal(PLAY_BUTTON_PATH));
            System.out.println("Bouton Play chargé");
        } catch (Exception e) {
            playButtonTexture = ButtonStyleFactory.createColorTexture(150, 55, new Color(0.2f, 0.8f, 0.2f, 1));
        }

        try {
            settingsTexture = new Texture(Gdx.files.internal(SETTINGS_BUTTON_PATH));
            System.out.println("Bouton Settings chargé");
        } catch (Exception e) {
            settingsTexture = ButtonStyleFactory.createColorTexture(40, 40, new Color(0.5f, 0.5f, 0.8f, 1));
        }
    }

    private void buildUI() {
        Table mainTable = new Table();
        mainTable.setFillParent(true);

        TextureRegion bgRegion = new TextureRegion(backgroundTexture);
        TextureRegionDrawable bgDrawable = new TextureRegionDrawable(bgRegion);
        bgDrawable.setMinWidth(SCREEN_WIDTH);
        bgDrawable.setMinHeight(SCREEN_HEIGHT);
        mainTable.setBackground(bgDrawable);

        Image logoImage = new Image(logoTexture);
        logoImage.addAction(Actions.sequence(
            Actions.alpha(0),
            Actions.fadeIn(1f)
        ));

        ImageButton playButton = createPlayButton();

        mainTable.add(logoImage).size(LOGO_WIDTH, LOGO_HEIGHT).center().row();
        mainTable.add(playButton).size(PLAY_BUTTON_WIDTH, PLAY_BUTTON_HEIGHT).padTop(30).center();

        stage.addActor(mainTable);

        TextureRegionDrawable settingsDrawable = new TextureRegionDrawable(new TextureRegion(settingsTexture));
        ImageButton settingsButton = new ImageButton(settingsDrawable);
        settingsButton.setSize(40f, 40f);

        settingsButton.setPosition(SCREEN_WIDTH - 50f, SCREEN_HEIGHT - 60f);

        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Ouverture de l'écran des paramètres");
                game.setScreen(new SettingsScreen((Main) game));
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, com.badlogic.gdx.scenes.scene2d.Actor fromActor) {
                settingsButton.addAction(Actions.scaleTo(1.1f, 1.1f, 0.1f));
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, com.badlogic.gdx.scenes.scene2d.Actor toActor) {
                settingsButton.addAction(Actions.scaleTo(1.0f, 1.0f, 0.1f));
            }
        });

        stage.addActor(settingsButton);
    }

    private ImageButton createPlayButton() {
        TextureRegionDrawable drawable = new TextureRegionDrawable(new TextureRegion(playButtonTexture));
        ImageButton button = new ImageButton(drawable);

        button.addAction(Actions.sequence(
            Actions.alpha(0),
            Actions.delay(0.5f),
            Actions.fadeIn(0.5f)
        ));

        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                startGame();
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, com.badlogic.gdx.scenes.scene2d.Actor fromActor) {
                button.addAction(Actions.scaleTo(1.15f, 1.15f, 0.1f));
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, com.badlogic.gdx.scenes.scene2d.Actor toActor) {
                button.addAction(Actions.scaleTo(1.0f, 1.0f, 0.1f));
            }
        });

        return button;
    }

    private void startGame() {
        System.out.println("Démarrage → Écran des règles");

        stage.addAction(Actions.sequence(
            Actions.fadeOut(0.5f),
            Actions.run(() -> {
                game.setScreen(new RulesScreen(game));
            })
        ));
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.15f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override public void pause() {}
    @Override public void resume() {}

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        stage.dispose();
        batch.dispose();

        if (backgroundTexture != null) backgroundTexture.dispose();
        if (logoTexture != null) logoTexture.dispose();
        if (playButtonTexture != null) playButtonTexture.dispose();
        if (settingsTexture != null) settingsTexture.dispose();
    }
}
