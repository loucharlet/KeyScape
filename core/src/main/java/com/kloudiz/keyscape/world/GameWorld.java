package com.kloudiz.keyscape.world;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.kloudiz.keyscape.entities.Player;
import com.kloudiz.keyscape.ui.Hud;

public class GameWorld {

    private Level currentLevel;
    private Player player;
    private Hud hud;
    private int levelIndex;

    private static final float DEATH_Y = -100f;

    public GameWorld(String tmxPath, String bgPath, String playerSprite, int levelIndex) {
        currentLevel = new Level("Main Level", tmxPath, bgPath, 0.75f);
        player = new Player(playerSprite, 20, 150, 256, 341);
        hud = new Hud();
        this.levelIndex = levelIndex;
    }

    public void update(float delta) {
        player.update(delta);
        currentLevel.update(delta);

        currentLevel.getCollisionHandler().handleCollisions(player);
        currentLevel.checkCoinCollisions(player);
        currentLevel.checkEnemyCollisions(player);

        if (currentLevel.checkKeyCollision(player)) {
            System.out.println("Clé collectée pour le niveau " + levelIndex);
        }

        player.updateCheckpoint();

        checkPlayerDeath();

        if (!player.isAlive()) {
            System.out.println("GAME OVER - Redémarrage du niveau...");
            resetLevel();
        }

        if (currentLevel.isCompleted(player)) {
            System.out.println("Niveau terminé ! Tu as collecté " + player.getCoinsCollected() + " pièces !");
        }
    }

    private void checkPlayerDeath() {
        if (player.getY() < DEATH_Y) {
            player.takeDamage();

            if (!player.isAlive()) {
                System.out.println("GAME OVER - Redémarrage du niveau...");
                resetLevel();
            }
        }
    }

    private void resetLevel() {
        player.resetLevel();
        currentLevel.resetCoins();
        currentLevel.resetEnemies();
    }

    public void render(SpriteBatch batch, OrthographicCamera camera) {
        batch.begin();
        currentLevel.renderBackground(batch);
        batch.end();

        currentLevel.renderTiledMap(camera);

        batch.begin();
        currentLevel.renderCoins(batch);
        currentLevel.renderEnemies(batch);
        currentLevel.renderExplosions(batch);
        player.render(batch);
        batch.end();

        hud.render(batch, player, currentLevel);
    }

    public void dispose() {
        currentLevel.dispose();
        player.dispose();
        hud.dispose();
    }

    public Player getPlayer() {
        return player;
    }

    public float getMapWidth() {
        return currentLevel.getMapWidth();
    }

    public float getMapHeight() {
        return currentLevel.getMapHeight();
    }

    public boolean isKeyCollected() {
        return currentLevel.isKeyCollected();
    }

    public int getLevelIndex() {
        return levelIndex;
    }
}
