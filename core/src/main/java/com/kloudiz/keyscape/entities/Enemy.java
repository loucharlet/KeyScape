package com.kloudiz.keyscape.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Enemy extends Entity {

    private Texture spritesheet;
    private TextureRegion[][] frames;
    private TextureRegion currentFrame;
    private Texture fallbackTexture;
    private boolean useFallback;

    private static final float ENEMY_SPEED = 80f;
    private static final float PATROL_DISTANCE = 150f;

    private float velocityX;
    private float startX;
    private boolean movingRight;

    private int frameIndex;
    private float animationTimer;
    private static final float FRAME_DURATION = 0.15f;

    public Enemy(String spritesheetPath, float x, float y, int spriteWidth, int spriteHeight,
                 float displayWidth, float displayHeight) {
        super(x, y, displayWidth, displayHeight);

        this.startX = x;
        this.movingRight = true;
        this.velocityX = ENEMY_SPEED;
        this.frameIndex = 0;
        this.animationTimer = 0;
        this.useFallback = false;

        try {
            spritesheet = new Texture(Gdx.files.internal(spritesheetPath));
            frames = TextureRegion.split(spritesheet, spriteWidth, spriteHeight);
            currentFrame = frames[0][0];
        } catch (Exception e) {
            createFallbackTexture();
        }
    }

    private void createFallbackTexture() {
        useFallback = true;
        Pixmap pixmap = new Pixmap((int) width, (int) height, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.RED);
        pixmap.fill();
        pixmap.setColor(Color.BLACK);
        pixmap.drawRectangle(0, 0, (int) width, (int) height);
        pixmap.drawLine(0, 0, (int) width, (int) height);
        pixmap.drawLine((int) width, 0, 0, (int) height);
        fallbackTexture = new Texture(pixmap);
        pixmap.dispose();
    }

    @Override
    public void update(float delta) {
        x += velocityX * delta;

        if (movingRight && x >= startX + PATROL_DISTANCE) {
            movingRight = false;
            velocityX = -ENEMY_SPEED;
        } else if (!movingRight && x <= startX) {
            movingRight = true;
            velocityX = ENEMY_SPEED;
        }

        updateBounds();

        if (!useFallback) {
            updateAnimation(delta);
        }
    }

    private void updateAnimation(float delta) {
        if (frames == null) return;

        animationTimer += delta;

        if (animationTimer >= FRAME_DURATION) {
            frameIndex++;
            if (frameIndex >= frames[0].length) {
                frameIndex = 0;
            }
            animationTimer = 0;
            currentFrame = frames[0][frameIndex];
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        if (!active) return;

        if (useFallback) {
            batch.draw(fallbackTexture, x, y, width, height);
        } else if (currentFrame != null) {
            if (movingRight) {
                batch.draw(currentFrame, x, y, width, height);
            } else {
                batch.draw(currentFrame, x + width, y, -width, height);
            }
        }
    }

    public void reverseDirection() {
        movingRight = !movingRight;
        velocityX = -velocityX;
    }

    public void kill() {
        active = false;
    }

    public void reset() {
        active = true;
        x = startX;
        movingRight = true;
        velocityX = ENEMY_SPEED;
        updateBounds();
    }

    @Override
    public void dispose() {
        if (spritesheet != null) {
            spritesheet.dispose();
        }
        if (fallbackTexture != null) {
            fallbackTexture.dispose();
        }
    }
}
