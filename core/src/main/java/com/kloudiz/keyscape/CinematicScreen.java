package com.kloudiz.keyscape;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class CinematicScreen implements Screen {

    public enum CinematicType {
        INTRO,
        OUTRO
    }

    private Main game;
    private CinematicType type;

    private Array<Texture> frames;
    private int currentFrame;
    private float frameTimer;
    private float frameDuration;

    private Music audio;
    private Texture pixel;
    private BitmapFont font;
    private GlyphLayout layout;

    private boolean finished;

    private Rectangle skipButtonBounds;
    private boolean hoverSkip;

    public CinematicScreen(Main game, CinematicType type) {
        this.game = game;
        this.type = type;
        this.finished = false;
        this.currentFrame = 0;
        this.frameTimer = 0;
        this.frameDuration = 1f / 15f;

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        pixel = new Texture(pixmap);
        pixmap.dispose();

        font = new BitmapFont();
        font.getData().setScale(1.5f);
        layout = new GlyphLayout();

        skipButtonBounds = new Rectangle(650, 20, 130, 50);
    }

    @Override
    public void show() {
        try {
            String framesPath;
            String audioPath;

            if (type == CinematicType.INTRO) {
                framesPath = "cinematic/intro_frames/";
                audioPath = "cinematic/intro_audio.mp3";
            } else {
                framesPath = "cinematic/outro_frames/";
                audioPath = "cinematic/outro_audio.mp3";
            }

            frames = new Array<>();
            int frameNum = 1;
            while (true) {
                String framePath = framesPath + String.format("frame_%04d.png", frameNum);
                if (!Gdx.files.internal(framePath).exists()) {
                    break;
                }
                frames.add(new Texture(Gdx.files.internal(framePath)));
                frameNum++;
            }

            audio = Gdx.audio.newMusic(Gdx.files.internal(audioPath));
            audio.setOnCompletionListener(music -> finished = true);
            audio.play();

        } catch (Exception e) {
            System.err.println("Erreur chargement cinématique: " + e.getMessage());
            goToNext();
        }
    }

    @Override
    public void render(float delta) {
        handleInput();

        if (finished) {
            goToNext();
            return;
        }

        if (frames != null && frames.size > 0) {
            frameTimer += delta;
            if (frameTimer >= frameDuration) {
                currentFrame = (currentFrame + 1) % frames.size;
                frameTimer = 0;
            }
        }

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();

        if (frames != null && frames.size > 0) {
            game.batch.draw(frames.get(currentFrame), 0, 0, 800, 600);
        }

        drawSkipButton();

        game.batch.end();
    }

    private void handleInput() {
        Vector3 mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        mousePos.y = 600 - mousePos.y;

        hoverSkip = skipButtonBounds.contains(mousePos.x, mousePos.y);

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) ||
                Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) ||
                Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            goToNext();
            return;
        }

        if (Gdx.input.justTouched() && hoverSkip) {
            goToNext();
        }
    }

    private void drawSkipButton() {
        Color bgColor = hoverSkip ? new Color(0.3f, 0.3f, 0.3f, 0.9f) : new Color(0.2f, 0.2f, 0.2f, 0.7f);

        game.batch.setColor(bgColor);
        game.batch.draw(pixel, skipButtonBounds.x, skipButtonBounds.y, skipButtonBounds.width, skipButtonBounds.height);

        game.batch.setColor(1, 1, 1, 1);
        game.batch.draw(pixel, skipButtonBounds.x, skipButtonBounds.y + skipButtonBounds.height - 3, skipButtonBounds.width, 3);
        game.batch.draw(pixel, skipButtonBounds.x, skipButtonBounds.y, skipButtonBounds.width, 3);
        game.batch.draw(pixel, skipButtonBounds.x, skipButtonBounds.y, 3, skipButtonBounds.height);
        game.batch.draw(pixel, skipButtonBounds.x + skipButtonBounds.width - 3, skipButtonBounds.y, 3, skipButtonBounds.height);

        String text = "SKIP";
        layout.setText(font, text);
        float textX = skipButtonBounds.x + (skipButtonBounds.width - layout.width) / 2f;
        float textY = skipButtonBounds.y + (skipButtonBounds.height + layout.height) / 2f;

        font.setColor(Color.WHITE);
        font.draw(game.batch, text, textX, textY);

        game.batch.setColor(Color.WHITE);
    }

    private void goToNext() {
        dispose();
        if (type == CinematicType.INTRO) {
            game.setScreen(new HomeScreen(game));
        } else {
            game.setScreen(new HomeScreen(game));
        }
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {
        if (audio != null && audio.isPlaying()) {
            audio.pause();
        }
    }

    @Override
    public void resume() {
        if (audio != null) {
            audio.play();
        }
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        if (audio != null) {
            audio.stop();
            audio.dispose();
        }
        if (frames != null) {
            for (Texture tex : frames) {
                tex.dispose();
            }
        }
        if (pixel != null) {
            pixel.dispose();
        }
        if (font != null) {
            font.dispose();
        }
    }
}