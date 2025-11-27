package com.kloudiz.keyscape.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public abstract class Character extends Entity {

    protected Texture spritesheet;
    protected TextureRegion[][] frames;
    protected TextureRegion currentFrame;

    protected int currentRow;
    protected int frameIndex;
    protected boolean facingRight;

    protected float velocityX;
    protected float velocityY;

    protected static final float GRAVITY = -800f;
    protected static final float MAX_FALL_SPEED = -600f;

    protected boolean isGrounded;
    protected float speed;
    protected float jumpForce;

    protected float lastJumpTime;
    protected float timeSinceLastJump;
    protected boolean canDoubleJump;
    protected static final float DOUBLE_JUMP_WINDOW = 0.3f;

    protected float animationTimer;
    protected static final float FRAME_DURATION = 0.15f;

    public Character(String spritesheetPath, float x, float y, int spriteWidth, int spriteHeight,
                     float displayWidth, float displayHeight, float speed, float jumpForce, float doubleJumpForce) {
        super(x, y, displayWidth, displayHeight);

        this.speed = speed;
        this.jumpForce = jumpForce;
        this.velocityX = 0;
        this.velocityY = 0;
        this.currentRow = 1;
        this.frameIndex = 0;
        this.facingRight = true;
        this.isGrounded = false;
        this.lastJumpTime = -1f;
        this.timeSinceLastJump = 0f;
        this.canDoubleJump = true;
        this.animationTimer = 0;

        spritesheet = new Texture(spritesheetPath);
        frames = TextureRegion.split(spritesheet, spriteWidth, spriteHeight);
        currentFrame = frames[currentRow][frameIndex];
    }

    @Override
    public void update(float delta) {
        if (lastJumpTime >= 0) {
            timeSinceLastJump += delta;
            if (timeSinceLastJump > DOUBLE_JUMP_WINDOW) {
                lastJumpTime = -1f;
            }
        }

        velocityY += GRAVITY * delta;

        if (velocityY < MAX_FALL_SPEED) {
            velocityY = MAX_FALL_SPEED;
        }

        x += velocityX * delta;
        y += velocityY * delta;

        updateBounds();
        updateAnimation(delta);
    }

    protected void updateAnimation(float delta) {
        if (velocityX != 0) {
            animationTimer += delta;

            if (animationTimer >= FRAME_DURATION) {
                frameIndex++;
                if (frameIndex >= frames[currentRow].length) {
                    frameIndex = 0;
                }
                animationTimer = 0;
            }
        } else {
            frameIndex = 0;
            animationTimer = 0;
        }
        currentFrame = frames[currentRow][frameIndex];
    }

    @Override
    public void render(SpriteBatch batch) {
        if (facingRight) {
            batch.draw(currentFrame, x, y, width, height);
        } else {
            batch.draw(currentFrame, x + width, y, -width, height);
        }
    }

    public void moveLeft() {
        velocityX = -speed;
        facingRight = false;
        currentRow = 0;
    }

    public void moveRight() {
        velocityX = speed;
        facingRight = true;
        currentRow = 0;
    }

    public void stopMoving() {
        velocityX = 0;
        currentRow = 1;
    }

    public abstract void jump();

    public void stopFalling(float groundY) {
        y = groundY;
        velocityY = 0;
        isGrounded = true;
        canDoubleJump = true;
        lastJumpTime = -1f;
    }

    public void hitCeiling(float ceilingY) {
        y = ceilingY;
        velocityY = 0;
    }

    public void hitWall(float wallX) {
        x = wallX;
        velocityX = 0;
    }

    @Override
    public void dispose() {
        spritesheet.dispose();
    }

    public float getVelocityX() {
        return velocityX;
    }

    public float getVelocityY() {
        return velocityY;
    }

    public boolean isGrounded() {
        return isGrounded;
    }

    public void setGrounded(boolean grounded) {
        this.isGrounded = grounded;
    }
}
