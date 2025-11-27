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

public class RulesScreen implements Screen {

    private static final String BACKGROUND_PATH = "ui/background.png";
    private static final String TITLE_PATH = "ui/rules_title.png";
    private static final String RIGHT_IMAGE_PATH = "ui/right_side_image.png";
    private static final String RULES_IMAGE_PATH = "ui/rules.png";
    private static final String ARROW_RIGHT_PATH = "ui/arrow_right.png";

    private static final float SCREEN_WIDTH = 800f;
    private static final float SCREEN_HEIGHT = 600f;
    private static final float RIGHT_IMAGE_WIDTH = 300f;
    private static final float RIGHT_IMAGE_HEIGHT = 300f;
    private static final float RULES_WIDTH = 300f;
    private static final float RULES_HEIGHT = 350f;
    private static final float ARROW_WIDTH = 60f;
    private static final float ARROW_HEIGHT = 70f;
    private static final float TITLE_WIDTH = 300f;
    private static final float TITLE_HEIGHT = 120f;

    private Game game;
    private Stage stage;
    private SpriteBatch batch;

    private Texture titleTexture;
    private Texture rightImageTexture;
    private Texture rulesTexture;
    private Texture arrowRightTexture;
    private Texture backgroundTexture;

    public RulesScreen(Game game) {
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
            System.out.println("Titre des règles chargé");
        } catch (Exception e) {
            titleTexture = ButtonStyleFactory.createColorTexture(300, 120, new Color(0.8f, 0.2f, 0.2f, 1));
        }

        try {
            rightImageTexture = new Texture(Gdx.files.internal(RIGHT_IMAGE_PATH));
            System.out.println("Image droite chargée");
        } catch (Exception e) {
            rightImageTexture = ButtonStyleFactory.createColorTexture(300, 300, new Color(0.9f, 0.9f, 0.9f, 1));
        }

        try {
            rulesTexture = new Texture(Gdx.files.internal(RULES_IMAGE_PATH));
            System.out.println("Image des règles chargée");
        } catch (Exception e) {
            rulesTexture = ButtonStyleFactory.createColorTexture(300, 350, new Color(0.2f, 0.8f, 0.8f, 1));
        }

        try {
            arrowRightTexture = new Texture(Gdx.files.internal(ARROW_RIGHT_PATH));
            System.out.println("Flèche droite chargée");
        } catch (Exception e) {
            arrowRightTexture = ButtonStyleFactory.createColorTexture(60, 70, new Color(0.9f, 0.9f, 0.9f, 1));
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

        Table rightContainer = new Table();
        Image rightImage = new Image(rightImageTexture);
        rightImage.addAction(Actions.sequence(Actions.alpha(0), Actions.fadeIn(0.7f)));
        rightContainer.add(rightImage).width(RIGHT_IMAGE_WIDTH).height(RIGHT_IMAGE_HEIGHT).bottom().expandY();

        Table centerTable = buildCenterTable();

        mainTable.add(rightContainer).bottom().padLeft(10).padBottom(0);
        mainTable.add(centerTable).expand().padRight(10).top().padTop(5);

        stage.addActor(mainTable);
    }

    private Table buildCenterTable() {
        Table centerTable = new Table();

        Image rulesImage = new Image(rulesTexture);
        rulesImage.addAction(Actions.sequence(Actions.alpha(0), Actions.fadeIn(0.5f)));
        centerTable.add(rulesImage).size(RULES_WIDTH, RULES_HEIGHT).padTop(-50).row();  // ← Ajoute .padTop(-50)

        ImageButton arrowButton = createArrowButton();
        centerTable.add(arrowButton).size(ARROW_WIDTH, ARROW_HEIGHT).padTop(15);

        return centerTable;
    }

    private ImageButton createArrowButton() {
        TextureRegionDrawable drawable = new TextureRegionDrawable(new TextureRegion(arrowRightTexture));
        ImageButton button = new ImageButton(drawable);

        button.addAction(Actions.sequence(
                Actions.alpha(0),
                Actions.delay(0.3f),
                Actions.fadeIn(0.3f)
        ));

        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                goToCharacterSelection();
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

    private void goToCharacterSelection() {
        System.out.println("Passage à la sélection de personnages");

        stage.addAction(Actions.sequence(
                Actions.fadeOut(0.5f),
                Actions.run(() -> {
                    game.setScreen(new CharacterSelectionScreenEnhanced(game));
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

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        stage.dispose();
        batch.dispose();

        if (titleTexture != null) titleTexture.dispose();
        if (rightImageTexture != null) rightImageTexture.dispose();
        if (rulesTexture != null) rulesTexture.dispose();
        if (arrowRightTexture != null) arrowRightTexture.dispose();
        if (backgroundTexture != null) backgroundTexture.dispose();
    }
}
