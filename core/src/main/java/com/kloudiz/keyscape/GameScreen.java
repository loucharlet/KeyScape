package com.kloudiz.keyscape;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.kloudiz.keyscape.entities.Player;
import com.kloudiz.keyscape.managers.InputManager;
import com.kloudiz.keyscape.managers.ProgressManager;
import com.kloudiz.keyscape.world.GameWorld;

public class GameScreen implements Screen {

    private Main game;
    private OrthographicCamera camera;
    private OrthographicCamera uiCamera;
    private GameWorld world;

    private BitmapFont font;
    private GlyphLayout layout;

    private String mapPath;
    private String backgroundPath;
    private String characterPath;

    private static final String[] CHARACTER_MUSICS = {
            "music/cassidy_music.mp3",
            "music/lou_music.mp3",
            "music/louise_music.mp3"
    };

    private Music levelMusic;
    private int characterIndex = 1;
    private int levelIndex = -1;

    private boolean isPaused = false;
    private Texture menuIconTexture;
    private Rectangle menuIconBounds;
    private Texture uiPixel;

    private Rectangle btnContinueBounds;
    private Rectangle btnRestartBounds;
    private Rectangle btnVolumeBounds;
    private Rectangle btnMainMenuBounds;
    private Rectangle btnStyleBounds;

    private boolean hoverContinue = false;
    private boolean hoverRestart = false;
    private boolean hoverVolume = false;
    private boolean hoverMainMenu = false;
    private boolean hoverStyle = false;

    private boolean showVolumeOverlay = false;
    private Rectangle volPanelBounds;
    private Rectangle volSliderBounds;
    private Rectangle volSliderHitBounds;
    private Rectangle volMuteBounds;
    private Rectangle volBackBounds;

    private boolean hoverVolSlider = false;
    private boolean hoverVolMute = false;
    private boolean hoverVolBack = false;
    private boolean draggingVolSlider = false;

    private boolean levelMuted = false;
    private float lastLevelVolumeBeforeMute = 0.6f;

    public GameScreen(Main game, String mapPath, String backgroundPath, String characterPath, int levelIndex) {
        this.game = game;
        this.mapPath = mapPath;
        this.backgroundPath = backgroundPath;
        this.characterPath = characterPath;
        this.levelIndex = levelIndex;
    }

    public GameScreen(Main game) {
        this(game, "maplou/untilted.tmx", "maplou/fond-lou.png", "characters/lou-spritesheet.png", 1);
    }

    @Override
    public void show() {
        MusicManager.getInstance().dispose();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 600);

        uiCamera = new OrthographicCamera();
        uiCamera.setToOrtho(false, 800, 600);

        font = new BitmapFont();
        layout = new GlyphLayout();
        font.getData().setScale(1.35f);

        menuIconTexture = new Texture(Gdx.files.internal("bouton/3points.png"));

        Pixmap pm = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pm.setColor(Color.WHITE);
        pm.fill();
        uiPixel = new Texture(pm);
        pm.dispose();

        world = new GameWorld(mapPath, backgroundPath, characterPath, levelIndex);

        initLevelMusic();

        Gdx.input.setInputProcessor(new InputManager(world.getPlayer()));

        initUiButtons();
    }

    private void initLevelMusic() {
        if (levelMusic != null) {
            levelMusic.stop();
            levelMusic.dispose();
            levelMusic = null;
        }

        String lower = characterPath.toLowerCase();
        if (lower.contains("cassidy")) characterIndex = 0;
        else if (lower.contains("louise")) characterIndex = 2;
        else if (lower.contains("lou")) characterIndex = 1;

        try {
            String musicPath = CHARACTER_MUSICS[characterIndex];
            levelMusic = Gdx.audio.newMusic(Gdx.files.internal(musicPath));
            levelMusic.setLooping(true);
            levelMusic.setVolume(0.6f);
            lastLevelVolumeBeforeMute = 0.6f;
            levelMusic.play();
        } catch (Exception ignored) {}
    }

    private void initUiButtons() {
        menuIconBounds = new Rectangle(10, 540, 48, 48);

        float menuWidth = 400;
        float menuHeight = 320;
        float menuX = (800 - menuWidth) / 2f;
        float menuY = (600 - menuHeight) / 2f;

        float buttonWidth = 300;
        float buttonHeight = 50;
        float buttonX = menuX + (menuWidth - buttonWidth) / 2f;
        float spacing = 12;
        float firstY = menuY + menuHeight - buttonHeight - 30;

        btnContinueBounds = new Rectangle(buttonX, firstY, buttonWidth, buttonHeight);
        btnRestartBounds  = new Rectangle(buttonX, firstY - (buttonHeight + spacing), buttonWidth, buttonHeight);
        btnVolumeBounds   = new Rectangle(buttonX, firstY - 2 * (buttonHeight + spacing), buttonWidth, buttonHeight);
        btnMainMenuBounds = new Rectangle(buttonX, firstY - 3 * (buttonHeight + spacing), buttonWidth, buttonHeight);
        btnStyleBounds    = new Rectangle(buttonX, firstY - 4 * (buttonHeight + spacing), buttonWidth, buttonHeight);

        float panelWidth = 420;
        float panelHeight = 260;
        float panelX = (800 - panelWidth) / 2f;
        float panelY = (600 - panelHeight) / 2f;
        volPanelBounds = new Rectangle(panelX, panelY, panelWidth, panelHeight);

        float sliderWidth = 260;
        float sliderHeight = 16;
        float sliderX = panelX + (panelWidth - sliderWidth) / 2f;
        float sliderY = panelY + panelHeight - 70;

        volSliderBounds = new Rectangle(sliderX, sliderY, sliderWidth, sliderHeight);

        float hitPad = 10f;
        volSliderHitBounds = new Rectangle(
                sliderX,
                sliderY - hitPad,
                sliderWidth,
                sliderHeight + hitPad * 2
        );

        float muteWidth = 260;
        float muteHeight = 50;
        float muteX = panelX + (panelWidth - muteWidth) / 2f;
        float muteY = volSliderBounds.y - 70;

        volMuteBounds = new Rectangle(muteX, muteY, muteWidth, muteHeight);

        float backWidth = 200;
        float backHeight = 45;
        float backX = panelX + (panelWidth - backWidth) / 2f;
        float backY = muteY - (backHeight + 10);

        volBackBounds = new Rectangle(backX, backY, backWidth, backHeight);

        if (levelMusic != null) {
            float v = levelMusic.getVolume();
            levelMuted = (v == 0f);
            if (!levelMuted) {
                lastLevelVolumeBeforeMute = v;
            }
        }
    }

    private void handleKeyCollected() {
        ProgressManager.getInstance().markLevelCompleted(levelIndex);

        if (ProgressManager.getInstance().areAllLevelsCompleted()) {
            System.out.println("Tous les niveaux complétés ! Lancement de la cinématique de fin...");
            game.setScreen(new CinematicScreen(game, CinematicScreen.CinematicType.OUTRO));
        } else {
            System.out.println("Retour à la sélection des personnages");
            game.setScreen(new CharacterSelectionScreenEnhanced(game));
        }

        dispose();
    }

    @Override
    public void render(float delta) {
        handleGlobalInput();

        if (!isPaused && world != null) {
            world.update(delta);
            updateCamera();

            if (world.isKeyCollected()) {
                handleKeyCollected();
                return;
            }
        }

        Gdx.gl.glClearColor(0.2f, 0.2f, 0.3f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        world.render(game.batch, camera);

        uiCamera.update();
        game.batch.setProjectionMatrix(uiCamera.combined);
        game.batch.begin();

        game.batch.draw(menuIconTexture, menuIconBounds.x, menuIconBounds.y, 48, 48);

        if (isPaused) {
            drawPauseOverlay();
            if (showVolumeOverlay) {
                drawVolumeOverlay();
            }
        }

        game.batch.end();

        handleClickInput();
    }

    private void handleGlobalInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            // ESC ferme tout (volume + pause) ou ouvre la pause
            if (isPaused && showVolumeOverlay) {
                showVolumeOverlay = false;
            } else {
                isPaused = !isPaused;
                if (!isPaused) showVolumeOverlay = false;
            }
        }
    }

    private void handleClickInput() {
        Vector3 pos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        uiCamera.unproject(pos);

        boolean clicked = Gdx.input.justTouched();

        if (isPaused && showVolumeOverlay) {
            handleVolumeOverlayInput(pos, clicked);
            return;
        }

        hoverContinue   = btnContinueBounds.contains(pos.x, pos.y);
        hoverRestart    = btnRestartBounds.contains(pos.x, pos.y);
        hoverVolume     = btnVolumeBounds.contains(pos.x, pos.y);
        hoverMainMenu   = btnMainMenuBounds.contains(pos.x, pos.y);
        hoverStyle      = btnStyleBounds.contains(pos.x, pos.y);

        if (!isPaused) {
            if (clicked && menuIconBounds.contains(pos.x, pos.y)) {
                isPaused = true;
            }
            return;
        }

        if (clicked && hoverContinue) {
            isPaused = false;
            showVolumeOverlay = false;
        } else if (clicked && hoverRestart) {
            restartLevel();
        } else if (clicked && hoverVolume) {
            showVolumeOverlay = true;
        } else if (clicked && hoverMainMenu) {
            goBackToCharacterMenu();
        } else if (clicked && hoverStyle) {
            UIThemeManager.getInstance().nextTheme();
        }
    }


    private void handleVolumeOverlayInput(Vector3 pos, boolean justClicked) {
        boolean isPressed = Gdx.input.isTouched();

        hoverVolSlider = volSliderHitBounds.contains(pos.x, pos.y);
        hoverVolMute   = volMuteBounds.contains(pos.x, pos.y);
        hoverVolBack   = volBackBounds.contains(pos.x, pos.y);

        if (justClicked && hoverVolSlider) {
            draggingVolSlider = true;
            setLevelVolumeFromMouse(pos.x);
        } else if (draggingVolSlider && isPressed) {
            setLevelVolumeFromMouse(pos.x);
        }

        if (!isPressed) {
            draggingVolSlider = false;
        }

        if (justClicked && hoverVolMute) {
            toggleLevelMute();
        }

        if (justClicked && hoverVolBack) {
            showVolumeOverlay = false;
        }
    }

    private void setLevelVolumeFromMouse(float mouseX) {
        if (levelMusic == null) return;

        float x = volSliderBounds.x;
        float w = volSliderBounds.width;

        float t = (mouseX - x) / w;
        if (t < 0f) t = 0f;
        if (t > 1f) t = 1f;

        levelMusic.setVolume(t);

        if (t > 0f) {
            levelMuted = false;
            lastLevelVolumeBeforeMute = t;
        } else {
            levelMuted = true;
        }
    }

    private void toggleLevelMute() {
        if (levelMusic == null) return;

        if (!levelMuted) {
            lastLevelVolumeBeforeMute = levelMusic.getVolume();
            if (lastLevelVolumeBeforeMute < 0.05f) lastLevelVolumeBeforeMute = 0.5f;
            levelMusic.setVolume(0f);
            levelMuted = true;
        } else {
            levelMusic.setVolume(lastLevelVolumeBeforeMute);
            levelMuted = false;
        }
    }

    private void restartLevel() {
        if (world != null) world.dispose();
        world = new GameWorld(mapPath, backgroundPath, characterPath, levelIndex);
        Gdx.input.setInputProcessor(new InputManager(world.getPlayer()));
        isPaused = false;
        showVolumeOverlay = false;

        initLevelMusic();
    }

    private void goBackToCharacterMenu() {
        isPaused = false;
        showVolumeOverlay = false;
        if (levelMusic != null) {
            levelMusic.stop();
        }
        game.setScreen(new CharacterSelectionScreenEnhanced(game));
        dispose();
    }

    private void drawPauseOverlay() {
        game.batch.setColor(0, 0, 0, 0.45f);
        game.batch.draw(uiPixel, 0, 0, 800, 600);

        drawGradientButton(btnContinueBounds, "Continuer", hoverContinue);
        drawGradientButton(btnRestartBounds,  "Recommencer", hoverRestart);
        drawGradientButton(btnVolumeBounds,   "Volume", hoverVolume);
        drawGradientButton(btnMainMenuBounds, "Retour menu", hoverMainMenu);
        drawGradientButton(btnStyleBounds,    "Couleurs", hoverStyle);

        game.batch.setColor(Color.WHITE);
    }

    private void drawVolumeOverlay() {
        float panelX = volPanelBounds.x;
        float panelY = volPanelBounds.y;
        float panelW = volPanelBounds.width;
        float panelH = volPanelBounds.height;

        game.batch.setColor(0, 0, 0, 0.7f);
        game.batch.draw(uiPixel, panelX, panelY, panelW, panelH);
        game.batch.setColor(Color.WHITE);

        String title = "Volume";
        layout.setText(font, title);
        float tx = panelX + (panelW - layout.width) / 2f;
        float ty = volSliderBounds.y + 80;
        font.setColor(Color.WHITE);
        font.draw(game.batch, layout, tx, ty);

        String label = "Volume musique";
        layout.setText(font, label);
        float lx = volSliderBounds.x + (volSliderBounds.width - layout.width) / 2f;
        float ly = volSliderBounds.y + 40;
        font.draw(game.batch, layout, lx, ly);

        drawGradientBar(volSliderBounds, (hoverVolSlider || draggingVolSlider));

        float vol = (levelMusic != null) ? levelMusic.getVolume() : 0f;
        float handleX = volSliderBounds.x + volSliderBounds.width * vol - 6;
        float handleY = volSliderBounds.y - 4;
        float handleW = 12;
        float handleH = volSliderBounds.height + 8;

        game.batch.setColor(Color.WHITE);
        game.batch.draw(uiPixel, handleX, handleY, handleW, handleH);

        String muteText = levelMuted ? "Activer le son" : "Couper le son";
        drawGradientButton(volMuteBounds, muteText, hoverVolMute);

        drawGradientButton(volBackBounds, "Retour", hoverVolBack);
    }

    private void drawGradientBar(Rectangle r, boolean hover) {
        Color start = UIThemeManager.getInstance().getStartColor(hover);
        Color end   = UIThemeManager.getInstance().getEndColor(hover);

        int steps = (int) r.width;
        if (steps < 1) steps = 1;

        for (int i = 0; i < steps; i++) {
            float t = (float) i / (steps - 1);
            Color c = new Color(
                    start.r + (end.r - start.r) * t,
                    start.g + (end.g - start.g) * t,
                    start.b + (end.b - start.b) * t,
                    1f
            );
            game.batch.setColor(c);
            game.batch.draw(uiPixel, r.x + i, r.y, 1, r.height);
        }

        game.batch.setColor(Color.WHITE);
    }

    private void drawGradientButton(Rectangle r, String text, boolean hover) {
        Color start = UIThemeManager.getInstance().getStartColor(hover);
        Color end   = UIThemeManager.getInstance().getEndColor(hover);

        int border = 3;

        float ix = r.x + border;
        float iy = r.y + border;
        float iw = r.width - border * 2;
        float ih = r.height - border * 2;

        int steps = (int) iw;
        if (steps < 1) steps = 1;

        for (int i = 0; i < steps; i++) {
            float t = (float) i / (steps - 1);

            Color c = new Color(
                    start.r + (end.r - start.r) * t,
                    start.g + (end.g - start.g) * t,
                    start.b + (end.b - start.b) * t,
                    1f
            );

            game.batch.setColor(c);
            game.batch.draw(uiPixel, ix + i, iy, 1, ih);
        }

        game.batch.setColor(0.28f, 0.12f, 0.45f, 1f);
        game.batch.draw(uiPixel, r.x, r.y + r.height - border, r.width, border);
        game.batch.draw(uiPixel, r.x, r.y, r.width, border);
        game.batch.draw(uiPixel, r.x, r.y, border, r.height);
        game.batch.draw(uiPixel, r.x + r.width - border, r.y, border, r.height);

        layout.setText(font, text);
        float tx = r.x + (r.width - layout.width) / 2f;
        float ty = r.y + (r.height + layout.height) / 2f - 4;

        font.setColor(1, 1, 1, 0.5f);
        font.draw(game.batch, layout, tx + 1, ty + 1);

        font.setColor(Color.BLACK);
        font.draw(game.batch, layout, tx, ty);

        font.setColor(Color.WHITE);
        game.batch.setColor(Color.WHITE);
    }

    private void updateCamera() {
        if (world == null) return;
        Player p = world.getPlayer();
        float mapWidth = world.getMapWidth();

        camera.position.x = Math.max(400, Math.min(p.getX() + 32, mapWidth - 400));
        camera.position.y = 300;
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
        if (levelMusic != null) levelMusic.stop();
    }

    @Override
    public void dispose() {
        if (world != null) world.dispose();
        if (font != null) font.dispose();
        if (uiPixel != null) uiPixel.dispose();
        if (menuIconTexture != null) menuIconTexture.dispose();
        if (levelMusic != null) {
            levelMusic.stop();
            levelMusic.dispose();
        }
    }
}
