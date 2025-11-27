package com.kloudiz.keyscape;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class SettingsScreen implements Screen {

    private Main game;

    private OrthographicCamera uiCamera;
    private BitmapFont font;
    private GlyphLayout layout;
    private Texture uiPixel;
    private Texture backgroundTexture;

    private Rectangle sliderBarBounds;
    private Rectangle sliderHitBounds;
    private boolean hoverSlider = false;
    private boolean draggingSlider = false;

    private Rectangle btnThemeBounds;
    private boolean hoverTheme = false;

    private Rectangle btnMuteBounds;
    private boolean hoverMute = false;
    private boolean isMuted = false;
    private float lastVolumeBeforeMute = 0.5f;

    private Rectangle btnBackBounds;
    private boolean hoverBack = false;

    public SettingsScreen(Main game) {
        this.game = game;
    }

    @Override
    public void show() {
        uiCamera = new OrthographicCamera();
        uiCamera.setToOrtho(false, 800, 600);

        font = new BitmapFont();
        font.getData().setScale(1.3f);
        layout = new GlyphLayout();

        Pixmap pm = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pm.setColor(Color.WHITE);
        pm.fill();
        uiPixel = new Texture(pm);
        pm.dispose();

        try {
            backgroundTexture = new Texture(Gdx.files.internal("ui/background.png"));
        } catch (Exception e) {
            backgroundTexture = ButtonStyleFactory.createColorTexture(
                1, 1, new Color(0.1f, 0.1f, 0.15f, 1)
            );
        }

        initUi();
    }

    private void initUi() {
        float panelWidth = 420;
        float panelHeight = 260;
        float panelX = (800 - panelWidth) / 2f;
        float panelY = (600 - panelHeight) / 2f;

        float sliderWidth = 260;
        float sliderHeight = 16;
        float sliderX = panelX + (panelWidth - sliderWidth) / 2f;
        float sliderY = panelY + panelHeight - 70;

        sliderBarBounds = new Rectangle(sliderX, sliderY, sliderWidth, sliderHeight);

        float hitPad = 10f;
        sliderHitBounds = new Rectangle(
            sliderX,
            sliderY - hitPad,
            sliderWidth,
            sliderHeight + hitPad * 2
        );

        float muteWidth = 260;
        float muteHeight = 50;
        float muteX = panelX + (panelWidth - muteWidth) / 2f;
        float muteY = sliderBarBounds.y - 70;

        btnMuteBounds = new Rectangle(muteX, muteY, muteWidth, muteHeight);

        float themeWidth = 260;
        float themeHeight = 45;
        float themeX = panelX + (panelWidth - themeWidth) / 2f;
        float themeY = muteY - (themeHeight + 10); // SOUS le bouton Mute

        btnThemeBounds = new Rectangle(themeX, themeY, themeWidth, themeHeight);

        float backWidth = 200;
        float backHeight = 45;
        float backX = panelX + (panelWidth - backWidth) / 2f;
        float backY = themeY - (backHeight + 10);

        btnBackBounds = new Rectangle(backX, backY, backWidth, backHeight);

        float vol = MusicManager.getInstance().getVolume();
        isMuted = vol == 0f;
        if (!isMuted) {
            lastVolumeBeforeMute = vol;
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.05f, 0.05f, 0.08f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        uiCamera.update();
        game.batch.setProjectionMatrix(uiCamera.combined);

        game.batch.begin();
        game.batch.draw(backgroundTexture, 0, 0, 800, 600);

        drawPanel();

        String title = "Param\u00e8tres";
        layout.setText(font, title);
        float tx = (800 - layout.width) / 2f;
        float ty = sliderBarBounds.y + 70;
        font.setColor(Color.WHITE);
        font.draw(game.batch, layout, tx, ty);

        String label = "Volume";
        layout.setText(font, label);
        float lx = sliderBarBounds.x + (sliderBarBounds.width - layout.width) / 2f;
        float ly = sliderBarBounds.y + 40;
        font.draw(game.batch, layout, lx, ly);

        drawGradientBar(sliderBarBounds, (hoverSlider || draggingSlider));

        float vol = MusicManager.getInstance().getVolume();
        float handleX = sliderBarBounds.x + sliderBarBounds.width * vol - 6;
        float handleY = sliderBarBounds.y - 4;
        float handleW = 12;
        float handleH = sliderBarBounds.height + 8;

        game.batch.setColor(Color.WHITE);
        game.batch.draw(uiPixel, handleX, handleY, handleW, handleH);

        String muteText = isMuted ? "Activer le son" : "Couper le son";
        drawGradientButton(btnMuteBounds, muteText, hoverMute);

        drawGradientButton(btnThemeBounds, "Changer couleurs", hoverTheme);

        drawGradientButton(btnBackBounds, "Retour", hoverBack);

        game.batch.end();

        handleInput();
    }

    private void drawPanel() {
        float panelWidth = 420;
        float panelHeight = 260;
        float panelX = (800 - panelWidth) / 2f;
        float panelY = (600 - panelHeight) / 2f;

        game.batch.setColor(0, 0, 0, 0.5f);
        game.batch.draw(uiPixel, panelX, panelY, panelWidth, panelHeight);
        game.batch.setColor(Color.WHITE);
    }

    private void handleInput() {
        Vector3 pos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        uiCamera.unproject(pos);

        boolean justClicked = Gdx.input.justTouched();
        boolean isPressed = Gdx.input.isTouched();

        hoverSlider = sliderHitBounds.contains(pos.x, pos.y);
        hoverTheme  = btnThemeBounds.contains(pos.x, pos.y);
        hoverMute   = btnMuteBounds.contains(pos.x, pos.y);
        hoverBack   = btnBackBounds.contains(pos.x, pos.y);

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            backToHome();
            return;
        }

        if (justClicked && hoverSlider) {
            draggingSlider = true;
            setVolumeFromMouse(pos.x);
        } else if (draggingSlider && isPressed) {
            setVolumeFromMouse(pos.x);
        }
        if (!isPressed) {
            draggingSlider = false;
        }

        if (justClicked && hoverTheme) {
            UIThemeManager.getInstance().nextTheme();
        }

        if (justClicked && hoverMute) {
            toggleMute();
        }

        if (justClicked && hoverBack) {
            backToHome();
        }
    }

    private void setVolumeFromMouse(float mouseX) {
        float x = sliderBarBounds.x;
        float w = sliderBarBounds.width;

        float t = (mouseX - x) / w;
        if (t < 0f) t = 0f;
        if (t > 1f) t = 1f;

        MusicManager mm = MusicManager.getInstance();
        mm.setVolume(t);

        if (t > 0f) {
            isMuted = false;
            lastVolumeBeforeMute = t;
        } else {
            isMuted = true;
        }
    }

    private void toggleMute() {
        MusicManager mm = MusicManager.getInstance();
        if (!isMuted) {
            lastVolumeBeforeMute = mm.getVolume();
            if (lastVolumeBeforeMute < 0.05f) lastVolumeBeforeMute = 0.5f;
            mm.setVolume(0f);
            isMuted = true;
        } else {
            mm.setVolume(lastVolumeBeforeMute);
            isMuted = false;
        }
    }

    private void backToHome() {
        game.setScreen(new HomeScreen(game));
        dispose();
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

    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        if (font != null) font.dispose();
        if (uiPixel != null) uiPixel.dispose();
        if (backgroundTexture != null) backgroundTexture.dispose();
    }
}
