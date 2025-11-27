package com.kloudiz.keyscape.entities;

public class Player extends Character {
    private static final float PLAYER_SPEED = 200f;
    private static final float PLAYER_JUMP_FORCE = 500f;
    private static final float PLAYER_DOUBLE_JUMP_FORCE = 650f;
    private static final float INVINCIBILITY_DURATION = 1.5f;
    private int coinsCollected;
    private int lives;
    private float startX;
    private float startY;
    private float checkpointX;
    private float checkpointY;
    private boolean isInvincible;
    private float invincibilityTimer;

    public Player(String spritesheetPath, float x, float y, int spriteWidth, int spriteHeight) {
        super(
            spritesheetPath,
            x,
            y,
            spriteWidth,
            spriteHeight,
            64,
            64,
            PLAYER_SPEED,
            PLAYER_JUMP_FORCE,
            PLAYER_DOUBLE_JUMP_FORCE
        );
        this.coinsCollected = 0;
        this.lives = 2;
        this.startX = x;
        this.startY = y;
        this.checkpointX = x;
        this.checkpointY = y;
        this.isInvincible = false;
        this.invincibilityTimer = 0;
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        if (isInvincible) {
            invincibilityTimer -= delta;
            if (invincibilityTimer <= 0) {
                isInvincible = false;
                invincibilityTimer = 0;
            }
        }
    }

    @Override
    public void jump() {
        float currentTime = System.currentTimeMillis() / 1000f;
        if (lastJumpTime >= 0 && timeSinceLastJump < DOUBLE_JUMP_WINDOW && canDoubleJump) {
            velocityY = PLAYER_DOUBLE_JUMP_FORCE;
            isGrounded = false;
            canDoubleJump = false;
            lastJumpTime = -1f;
            return;
        }
        if (isGrounded) {
            velocityY = jumpForce;
            isGrounded = false;
            lastJumpTime = currentTime;
            timeSinceLastJump = 0f;
            canDoubleJump = true;
        }
    }

    public void updateCheckpoint() {
        if (isGrounded) {
            checkpointX = x;
            checkpointY = y;
        }
    }

    public void takeDamage() {
        if (isInvincible) return;

        lives--;
        isInvincible = true;
        invincibilityTimer = INVINCIBILITY_DURATION;
        System.out.println("Vies restantes : " + lives);

        if (lives > 0) {
            respawnAtCheckpoint();
        }
    }

    private void respawnAtCheckpoint() {
        x = checkpointX;
        y = checkpointY;
        velocityX = 0;
        velocityY = 0;
        updateBounds();
    }

    public void resetLevel() {
        x = startX;
        y = startY;
        velocityX = 0;
        velocityY = 0;
        lives = 2;
        coinsCollected = 0;
        checkpointX = startX;
        checkpointY = startY;
        updateBounds();
    }

    public void addCoin() {
        coinsCollected++;
        System.out.println("Pièce collectée ! Total : " + coinsCollected);
    }

    public int getCoinsCollected() {
        return coinsCollected;
    }

    public int getLives() {
        return lives;
    }

    public boolean isAlive() {
        return lives > 0;
    }

    public void resetCoins() {
        coinsCollected = 0;
    }

    public boolean isInvincible() {
        return isInvincible;
    }

    public float getInvincibilityTimer() {
        return invincibilityTimer;
    }
}
