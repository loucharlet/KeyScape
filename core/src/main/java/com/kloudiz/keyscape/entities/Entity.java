package com.kloudiz.keyscape.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public abstract class Entity {

    protected float x;
    protected float y;
    protected float width;
    protected float height;

    protected Rectangle bounds;
    protected boolean active;

    public Entity(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.bounds = new Rectangle(x, y, width, height);
        this.active = true;
    }

    public abstract void update(float delta);

    public abstract void render(SpriteBatch batch);

    public abstract void dispose();

    protected void updateBounds() {
        bounds.setPosition(x, y);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public boolean isActive() {
        return active;
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
        updateBounds();
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
