package com.kloudiz.keyscape;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.Game;
import com.kloudiz.keyscape.managers.ProgressManager;
import com.kloudiz.keyscape.world.GameWorld;

public class CharacterSelectionScreenEnhanced implements Screen {
    private static final String BACKGROUND_PATH = "ui/background.png";
    private static final String TITLE_PATH = "ui/character_selection_title.png";
    private static final String LEFT_IMAGE_PATH = "ui/right_side_image.png";
    private static final String ARROW_LEFT_PATH = "ui/arrow_left.png";
    private static final String ARROW_RIGHT_PATH = "ui/arrow_right.png";
    private static final String PLAY_BUTTON_PATH = "ui/play_button.png";

    private static final String[] CHARACTER_NAMES = {
            "Cassidy",
            "Lou",
            "Louise"
    };

    private static final String[] CHARACTER_LEVELS = {
            "levels/cassidy/assets-cassidy.tmx",
            "levels/lou/untitled.tmx",
            "levels/louise/assets-louise.tmx"
    };

    private static final String[] CHARACTER_BACKGROUNDS = {
            "levels/cassidy/fond-cassidy.png",
            "levels/lou/fond-lou.png",
            "levels/louise/fond-louise.png"
    };

    private static final String[] CHARACTER_SPRITES = {
            "characters/cassidy/cassidy-spritesheet.png",
            "characters/lou/lou-spritesheet.png",
            "characters/louise/louise-spritesheet.png"
    };

    private static final float SCREEN_WIDTH = 800f;
    private static final float SCREEN_HEIGHT = 600f;
    private static final float LEFT_IMAGE_WIDTH = 300f;
    private static final float LEFT_IMAGE_HEIGHT = 300f;
    private static final float CHARACTER_WIDTH = 300f;
    private static final float CHARACTER_HEIGHT = 350f;
    private static final float ARROW_WIDTH = 40f;
    private static final float ARROW_HEIGHT = 50f;
    private static final float PLAY_BUTTON_WIDTH = 120f;
    private static final float PLAY_BUTTON_HEIGHT = 45f;
    private static final float TITLE_WIDTH = 300f;
    private static final float TITLE_HEIGHT = 120f;

    private Game game;
    private Stage stage;
    private SpriteBatch batch;

    private Texture titleTexture;
    private Texture leftImageTexture;
    private Texture[] characterTextures;
    private Texture arrowLeftTexture;
    private Texture arrowRightTexture;
    private Texture backgroundTexture;
    private Texture playButtonTexture;

    private Image currentCharacterImage;
    private Label characterNameLabel;
    private Label levelStatusLabel;
    private int currentCharacterIndex = 0;

    public CharacterSelectionScreenEnhanced(Game game) {
        this.game = game;
        this.batch = new SpriteBatch();
        this.stage = new Stage(new FitViewport(SCREEN_WIDTH, SCREEN_HEIGHT));
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        loadTextures();
        buildUI();
    }

    private void loadTextures() {
        try {
            backgroundTexture = new Texture(Gdx.files.internal(BACKGROUND_PATH));
            System.out.println("Fond chargé");
        } catch (Exception e) {
            backgroundTexture = ButtonStyleFactory.createColorTexture(1, 1, new Color(0.1f, 0.1f, 0.15f, 1));
        }

        try {
            titleTexture = new Texture(Gdx.files.internal(TITLE_PATH));
            System.out.println("Titre chargé");
        } catch (Exception e) {
            titleTexture = ButtonStyleFactory.createColorTexture(300, 120, new Color(0.8f, 0.2f, 0.2f, 1));
        }

        try {
            leftImageTexture = new Texture(Gdx.files.internal(LEFT_IMAGE_PATH));
            System.out.println("Image gauche chargée");
        } catch (Exception e) {
            leftImageTexture = ButtonStyleFactory.createColorTexture(150, 150, new Color(0.9f, 0.9f, 0.9f, 1));
        }

        try {
            arrowLeftTexture = new Texture(Gdx.files.internal(ARROW_LEFT_PATH));
            arrowRightTexture = new Texture(Gdx.files.internal(ARROW_RIGHT_PATH));
            System.out.println("Flèches chargées");
        } catch (Exception e) {
            arrowLeftTexture = ButtonStyleFactory.createColorTexture(40, 50, new Color(0.9f, 0.9f, 0.9f, 1));
            arrowRightTexture = ButtonStyleFactory.createColorTexture(40, 50, new Color(0.9f, 0.9f, 0.9f, 1));
        }

        try {
            playButtonTexture = new Texture(Gdx.files.internal(PLAY_BUTTON_PATH));
            System.out.println("Bouton Jouer chargé");
        } catch (Exception e) {
            playButtonTexture = ButtonStyleFactory.createColorTexture(120, 45, new Color(0.2f, 0.8f, 0.2f, 1));
        }

        characterTextures = new Texture[3];
        for (int i = 0; i < characterTextures.length; i++) {
            try {
                characterTextures[i] = new Texture(Gdx.files.internal("ui/character_" + (i + 1) + ".png"));
                System.out.println("Personnage " + (i + 1) + " chargé");
            } catch (Exception e) {
                Color[] colors = {
                        new Color(0.8f, 0.2f, 0.2f, 1),
                        new Color(0.2f, 0.2f, 0.8f, 1),
                        new Color(0.2f, 0.8f, 0.2f, 1)
                };
                characterTextures[i] = ButtonStyleFactory.createColorTexture(200, 250, colors[i]);
            }
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

        Image titleImage = new Image(titleTexture);
        titleImage.addAction(Actions.sequence(Actions.alpha(0), Actions.fadeIn(0.5f)));
        mainTable.add(titleImage).size(TITLE_WIDTH, TITLE_HEIGHT).colspan(2).left().padLeft(10).padTop(5).padBottom(5);
        mainTable.row();

        Table leftContainer = new Table();
        Image leftImage = new Image(leftImageTexture);
        leftImage.addAction(Actions.sequence(Actions.alpha(0), Actions.fadeIn(0.7f)));
        leftContainer.add(leftImage).width(LEFT_IMAGE_WIDTH).height(LEFT_IMAGE_HEIGHT).bottom().expandY();

        Table centerTable = buildCenterTable();

        mainTable.add(leftContainer).bottom().padLeft(10).padBottom(0);
        mainTable.add(centerTable).expand().padRight(10).top().padTop(5);

        stage.addActor(mainTable);
    }

    private Table buildCenterTable() {
        Table centerTable = new Table();

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = new BitmapFont();
        labelStyle.fontColor = Color.WHITE;

        characterNameLabel = new Label(CHARACTER_NAMES[currentCharacterIndex], labelStyle);
        characterNameLabel.setFontScale(1.5f);
        centerTable.add(characterNameLabel).padBottom(5).row();

        Label.LabelStyle statusStyle = new Label.LabelStyle();
        statusStyle.font = new BitmapFont();
        statusStyle.fontColor = getStatusColor();
        levelStatusLabel = new Label(getStatusText(), statusStyle);
        levelStatusLabel.setFontScale(1.0f);
        centerTable.add(levelStatusLabel).padBottom(10).row();

        Table navigationTable = new Table();

        ImageButton leftArrow = createArrowButton(arrowLeftTexture, -1);
        currentCharacterImage = new Image(characterTextures[currentCharacterIndex]);
        currentCharacterImage.addAction(Actions.sequence(Actions.alpha(0), Actions.fadeIn(0.3f)));
        ImageButton rightArrow = createArrowButton(arrowRightTexture, 1);

        navigationTable.add(leftArrow).size(ARROW_WIDTH, ARROW_HEIGHT).padRight(10);
        navigationTable.add(currentCharacterImage).size(CHARACTER_WIDTH, CHARACTER_HEIGHT);
        navigationTable.add(rightArrow).size(ARROW_WIDTH, ARROW_HEIGHT).padLeft(10);

        centerTable.add(navigationTable).row();

        ImageButton playButton = createPlayButton();
        centerTable.add(playButton).size(PLAY_BUTTON_WIDTH, PLAY_BUTTON_HEIGHT).padTop(5);

        return centerTable;
    }

    private ImageButton createArrowButton(Texture texture, int direction) {
        TextureRegionDrawable drawable = new TextureRegionDrawable(new TextureRegion(texture));
        ImageButton button = new ImageButton(drawable);

        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                changeCharacter(direction);
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, com.badlogic.gdx.scenes.scene2d.Actor fromActor) {
                button.addAction(Actions.scaleTo(1.2f, 1.2f, 0.1f));
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, com.badlogic.gdx.scenes.scene2d.Actor toActor) {
                button.addAction(Actions.scaleTo(1.0f, 1.0f, 0.1f));
            }
        });

        return button;
    }

    private ImageButton createPlayButton() {
        TextureRegionDrawable drawable = new TextureRegionDrawable(new TextureRegion(playButtonTexture));
        ImageButton button = new ImageButton(drawable);

        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                startGame();
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, com.badlogic.gdx.scenes.scene2d.Actor fromActor) {
                button.addAction(Actions.scaleTo(1.1f, 1.1f, 0.1f));
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, com.badlogic.gdx.scenes.scene2d.Actor toActor) {
                button.addAction(Actions.scaleTo(1.0f, 1.0f, 0.1f));
            }
        });

        return button;
    }

    private void changeCharacter(int direction) {
        currentCharacterImage.addAction(Actions.sequence(
                Actions.fadeOut(0.15f),
                Actions.run(() -> {
                    currentCharacterIndex += direction;

                    if (currentCharacterIndex < 0) {
                        currentCharacterIndex = characterTextures.length - 1;
                    } else if (currentCharacterIndex >= characterTextures.length) {
                        currentCharacterIndex = 0;
                    }

                    currentCharacterImage.setDrawable(new TextureRegionDrawable(
                            new TextureRegion(characterTextures[currentCharacterIndex])));
                    characterNameLabel.setText(CHARACTER_NAMES[currentCharacterIndex]);

                    levelStatusLabel.setText(getStatusText());
                    levelStatusLabel.setColor(getStatusColor());
                }),
                Actions.fadeIn(0.15f)
        ));
    }

    private void startGame() {
        if (ProgressManager.getInstance().isLevelCompleted(currentCharacterIndex)) {
            System.out.println(" Niveau déjà complété ! Impossible de rejouer.");
            return;
        }

        System.out.println("========================================");
        System.out.println("=== DÉMARRAGE DU JEU ===");
        System.out.println("========================================");
        System.out.println("Personnage : " + CHARACTER_NAMES[currentCharacterIndex]);
        System.out.println("Index : " + currentCharacterIndex);
        System.out.println("Niveau : " + CHARACTER_LEVELS[currentCharacterIndex]);
        System.out.println("========================================");

        stage.addAction(Actions.sequence(
                Actions.fadeOut(0.5f),
                Actions.run(() -> {
                    game.setScreen(new GameScreen(
                            (Main) game,
                            CHARACTER_LEVELS[currentCharacterIndex],
                            CHARACTER_BACKGROUNDS[currentCharacterIndex],
                            CHARACTER_SPRITES[currentCharacterIndex],
                            currentCharacterIndex
                    ));
                })
        ));
    }

    private String getStatusText() {
        if (ProgressManager.getInstance().isLevelCompleted(currentCharacterIndex)) {
            return "Niveau complété !";
        } else {
            return "Niveau disponible";
        }
    }

    private Color getStatusColor() {
        if (ProgressManager.getInstance().isLevelCompleted(currentCharacterIndex)) {
            return new Color(0.2f, 0.8f, 0.2f, 1); // Vert
        } else {
            return new Color(1f, 0.9f, 0.3f, 1); // Jaune/Or
        }
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

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        stage.dispose();
        batch.dispose();

        if (titleTexture != null) titleTexture.dispose();
        if (leftImageTexture != null) leftImageTexture.dispose();
        if (arrowLeftTexture != null) arrowLeftTexture.dispose();
        if (arrowRightTexture != null) arrowRightTexture.dispose();
        if (backgroundTexture != null) backgroundTexture.dispose();
        if (playButtonTexture != null) playButtonTexture.dispose();

        if (characterTextures != null) {
            for (Texture texture : characterTextures) {
                if (texture != null) texture.dispose();
            }
        }
    }
}
