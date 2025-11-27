package com.kloudiz.keyscape.world;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.kloudiz.keyscape.entities.Coin;
import com.kloudiz.keyscape.entities.Enemy;
import com.kloudiz.keyscape.entities.Player;
import com.kloudiz.keyscape.effects.ExplosionEffect;

import java.util.ArrayList;
import java.util.List;

public class Level {
    private String name;
    private Texture background;
    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer mapRenderer;
    private CollisionHandler collisionHandler;
    private List<Coin> coins;
    private Texture coinTexture;
    private List<Enemy> enemies;
    private List<ExplosionEffect> explosions;
    private int totalCoins;
    private int requiredCoins;
    private float mapWidth;
    private float mapHeight;
    private float tileScale;
    private Rectangle keyBounds;
    private boolean keyUnlocked;
    private boolean keyCollected;
    private int initialCoinCount;

    public Level(String name, String tmxPath, String backgroundPath, float tileScale) {
        this.name = name;
        this.tileScale = tileScale;
        this.requiredCoins = 20;
        background = new Texture(backgroundPath);
        background.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        tiledMap = new TmxMapLoader().load(tmxPath);
        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap, tileScale);
        int mapW = tiledMap.getProperties().get("width", Integer.class);
        int mapH = tiledMap.getProperties().get("height", Integer.class);
        int tileW = tiledMap.getProperties().get("tilewidth", Integer.class);
        int tileH = tiledMap.getProperties().get("tileheight", Integer.class);
        mapWidth = mapW * tileW * tileScale;
        mapHeight = mapH * tileH * tileScale;
        collisionHandler = new CollisionHandler(tileScale);
        collisionHandler.loadCollisionsFromMap(tiledMap, mapWidth);
        coinTexture = new Texture("ui/coin.png");
        coins = new ArrayList<>();
        enemies = new ArrayList<>();
        explosions = new ArrayList<>();
        keyUnlocked = false;
        keyCollected = false;
        loadCoins();
        loadEnemies();
        loadKey();
    }

    private void loadCoins() {
        if (tiledMap.getLayers().get("Coins") == null) {
            System.err.println("Pas de layer 'Coins' !");
            return;
        }
        for (MapObject obj : tiledMap.getLayers().get("Coins").getObjects()) {
            if (obj instanceof RectangleMapObject) {
                Rectangle rect = ((RectangleMapObject) obj).getRectangle();
                Coin coin = new Coin(
                    rect.x * tileScale,
                    rect.y * tileScale,
                    coinTexture
                );
                coins.add(coin);
            }
        }
        totalCoins = coins.size();
        initialCoinCount = coins.size();
    }

    private void loadEnemies() {
        if (tiledMap.getLayers().get("Enemy") == null) {
            System.err.println("Pas de layer 'Enemy' !");
            return;
        }

        for (MapObject obj : tiledMap.getLayers().get("Enemy").getObjects()) {
            if (obj instanceof RectangleMapObject) {
                Rectangle rect = ((RectangleMapObject) obj).getRectangle();

                String spritePath = obj.getProperties().get("sprite", String.class);

                if (spritePath == null || spritePath.isEmpty()) {
                    System.err.println("Ennemi ignoré : pas de propriété 'sprite' définie");
                    continue;
                }

                Enemy enemy = new Enemy(
                    spritePath,
                    rect.x * tileScale,
                    rect.y * tileScale,
                    256,
                    341,
                    64,
                    64
                );
                enemies.add(enemy);
            }
        }
    }

    private void loadKey() {
        if (tiledMap.getLayers().get("Key") == null) {
            System.err.println("Pas de layer 'Key' !");
            return;
        }

        for (MapObject obj : tiledMap.getLayers().get("Key").getObjects()) {
            if (obj instanceof RectangleMapObject) {
                Rectangle rect = ((RectangleMapObject) obj).getRectangle();
                keyBounds = new Rectangle(
                    rect.x * tileScale,
                    rect.y * tileScale,
                    rect.width * tileScale,
                    rect.height * tileScale
                );
                System.out.println("Clé trouvée à la position : " + keyBounds.x + ", " + keyBounds.y);
                break;
            }
        }
    }

    public void update(float delta) {
        for (Coin coin : coins) {
            coin.update(delta);
        }

        for (Enemy enemy : enemies) {
            enemy.update(delta);
        }

        for (int i = explosions.size() - 1; i >= 0; i--) {
            ExplosionEffect explosion = explosions.get(i);
            explosion.update(delta);

            if (!explosion.isActive()) {
                explosion.dispose();
                explosions.remove(i);
            }
        }
    }

    public void checkCoinCollisions(Player player) {
        for (Coin coin : coins) {
            if (!coin.isCollected() && player.getBounds().overlaps(coin.getBounds())) {
                coin.collect();
                player.addCoin();
            }
        }
    }

    public void checkEnemyCollisions(Player player) {
        if (player.isInvincible()) return;

        for (Enemy enemy : enemies) {
            if (!enemy.isActive()) continue;

            if (player.getBounds().overlaps(enemy.getBounds())) {
                float playerBottom = player.getY();
                float enemyTop = enemy.getY() + enemy.getHeight();

                if (player.getVelocityY() < 0 && playerBottom > enemyTop - 10) {
                    ExplosionEffect killExplosion = new ExplosionEffect("déco+elements/tetedemortt.png");
                    killExplosion.setSize(96, 96);
                    killExplosion.setDuration(0.5f);

                    float explosionX = enemy.getX() + enemy.getWidth() / 2 - 48;
                    float explosionY = enemy.getY() + enemy.getHeight() / 2 - 48;
                    killExplosion.trigger(explosionX, explosionY);

                    explosions.add(killExplosion);

                    Coin coin = new Coin(
                        enemy.getX() + enemy.getWidth() / 2 - 16,
                        enemy.getY() + enemy.getHeight() / 2 - 16,
                        coinTexture
                    );
                    coins.add(coin);
                    totalCoins++;

                    enemy.kill();
                    player.jump();
                    System.out.println("Ennemi écrasé ! Pièce obtenue !");
                } else {
                    player.takeDamage();

                    ExplosionEffect explosion = new ExplosionEffect("déco+elements/explosionrouge.png");
                    explosion.setSize(96, 96);
                    explosion.setDuration(0.5f);

                    float explosionX = player.getX() + player.getWidth() / 2 - 48;
                    float explosionY = player.getY() + player.getHeight() / 2 - 48;
                    explosion.trigger(explosionX, explosionY);

                    explosions.add(explosion);
                }
                break;
            }
        }
    }

    public boolean checkKeyCollision(Player player) {
        if (keyBounds == null || keyCollected) {
            return false;
        }

        if (!keyUnlocked && player.getCoinsCollected() >= requiredCoins) {
            keyUnlocked = true;
            System.out.println("Clé débloquée ! Tu peux maintenant la récupérer !");
        }

        if (keyUnlocked && player.getBounds().overlaps(keyBounds)) {
            keyCollected = true;
            System.out.println("Clé récupérée ! Niveau complété !");
            return true;
        }

        return false;
    }

    public boolean isKeyUnlocked() {
        return keyUnlocked;
    }

    public boolean isKeyCollected() {
        return keyCollected;
    }

    public Rectangle getKeyBounds() {
        return keyBounds;
    }

    public void renderBackground(SpriteBatch batch) {
        int bgWidth = background.getWidth();
        int repeats = (int) Math.ceil(mapWidth / bgWidth) + 1;
        float targetHeight = 600f;
        for (int i = 0; i < repeats; i++) {
            batch.draw(background, i * bgWidth, 0, bgWidth, targetHeight);
        }
    }

    public void renderTiledMap(OrthographicCamera camera) {
        mapRenderer.setView(camera);
        mapRenderer.render();
    }

    public void renderCoins(SpriteBatch batch) {
        for (Coin coin : coins) {
            coin.render(batch);
        }
    }

    public void renderEnemies(SpriteBatch batch) {
        for (Enemy enemy : enemies) {
            enemy.render(batch);
        }
    }

    public void renderExplosions(SpriteBatch batch) {
        for (ExplosionEffect explosion : explosions) {
            explosion.render(batch);
        }
    }

    public boolean isCompleted(Player player) {
        return player.getCoinsCollected() >= requiredCoins;
    }

    public void dispose() {
        background.dispose();
        tiledMap.dispose();
        mapRenderer.dispose();
        coinTexture.dispose();
        for (Coin coin : coins) {
            coin.dispose();
        }
        for (Enemy enemy : enemies) {
            enemy.dispose();
        }
        for (ExplosionEffect explosion : explosions) {
            explosion.dispose();
        }
    }

    public String getName() {
        return name;
    }

    public float getMapWidth() {
        return mapWidth;
    }

    public float getMapHeight() {
        return mapHeight;
    }

    public CollisionHandler getCollisionHandler() {
        return collisionHandler;
    }

    public int getTotalCoins() {
        return totalCoins;
    }

    public int getRequiredCoins() {
        return requiredCoins;
    }

    public void resetCoins() {
        while (coins.size() > initialCoinCount) {
            Coin removed = coins.remove(coins.size() - 1);
            removed.dispose();
        }
        for (Coin coin : coins) {
            coin.reset();
        }
        totalCoins = coins.size();
    }

    public void resetEnemies() {
        for (Enemy enemy : enemies) {
            enemy.reset();
        }
    }
}
