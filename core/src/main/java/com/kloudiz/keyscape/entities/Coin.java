package com.kloudiz.keyscape.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Coin extends Entity {
    private Texture texture;
    private boolean collected;
    private float animationTimer;
    private float bobOffset;

    public Coin(float x, float y, Texture sharedTexture) {
        super(x, y, 32, 32);
        this.texture = sharedTexture;
        this.collected = false;
        this.animationTimer = 0;
        this.bobOffset = 0;
    }

    @Override
    public void update(float delta) {
        if (collected) return;
        animationTimer += delta * 2;
        bobOffset = (float) Math.sin(animationTimer) * 3;
    }

    @Override
    public void render(SpriteBatch batch) {
        if (!collected && active) {
            batch.draw(texture, x, y + bobOffset, width, height);
        }
    }

    public void collect() {
        collected = true;
        active = false;
    }

    public boolean isCollected() {
        return collected;
    }

    public void reset() {
        collected = false;
        active = true;
    }

    @Override
    public void dispose() {
    }
}
