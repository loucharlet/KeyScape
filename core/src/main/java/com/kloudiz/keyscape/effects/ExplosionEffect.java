package com.kloudiz.keyscape.effects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ExplosionEffect {
    private Texture texture;
    private float x;
    private float y;
    private float width;
    private float height;
    private float duration;
    private float timer;
    private boolean active;
    private float alpha;
    private float scale;

    public ExplosionEffect(String texturePath) {
        this.texture = new Texture(texturePath);
        this.duration = 0.5f;
        this.width = 64;
        this.height = 64;
        this.active = false;
        this.scale = 1.0f;
    }

    public void trigger(float x, float y) {
        this.x = x;
        this.y = y;
        this.timer = 0;
        this.alpha = 1.0f;
        this.scale = 0.3f;
        this.active = true;
    }

    public void update(float delta) {
        if (!active) return;

        timer += delta;

        float progress = timer / duration;

        if (progress < 0.3f) {
            scale = 0.3f + (progress / 0.3f) * 1.2f;
            alpha = 1.0f;
        } else {
            scale = 1.5f + ((progress - 0.3f) / 0.7f) * 0.2f;
            alpha = 1.0f - ((progress - 0.3f) / 0.7f);
        }

        if (timer >= duration) {
            active = false;
        }
    }

    public void render(SpriteBatch batch) {
        if (!active) return;

        float oldAlpha = batch.getColor().a;

        batch.setColor(1, 1, 1, alpha);

        float scaledWidth = width * scale;
        float scaledHeight = height * scale;

        float centeredX = x - (scaledWidth - width) / 2;
        float centeredY = y - (scaledHeight - height) / 2;

        batch.draw(texture, centeredX, centeredY, scaledWidth, scaledHeight);

        batch.setColor(1, 1, 1, oldAlpha);
    }

    public boolean isActive() {
        return active;
    }

    public void dispose() {
        if (texture != null) {
            texture.dispose();
        }
    }

    public void setSize(float width, float height) {
        this.width = width;
        this.height = height;
    }

    public void setDuration(float duration) {
        this.duration = duration;
    }
}
