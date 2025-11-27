package com.kloudiz.keyscape.world;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.kloudiz.keyscape.entities.Player;

import java.util.ArrayList;
import java.util.List;

public class CollisionHandler {

    private List<Rectangle> platforms;
    private float tileScale;

    public CollisionHandler(float tileScale) {
        this.platforms = new ArrayList<>();
        this.tileScale = tileScale;
    }

    public void loadCollisionsFromMap(TiledMap tiledMap, float mapWidth) {
        if (tiledMap.getLayers().get("Collisions") == null) {
            System.err.println("Pas de layer 'Collisions' !");
            platforms.add(new Rectangle(0, 0, mapWidth * tileScale, 50 * tileScale));
            System.out.println("Sol par défaut créé");
            return;
        }

        for (MapObject obj : tiledMap.getLayers().get("Collisions").getObjects()) {
            if (obj instanceof RectangleMapObject) {
                Rectangle rect = ((RectangleMapObject) obj).getRectangle();

                Rectangle scaledRect = new Rectangle(
                    rect.x * tileScale,
                    rect.y * tileScale,
                    rect.width * tileScale,
                    rect.height * tileScale
                );

                platforms.add(scaledRect);
            }
        }

        System.out.println("✅ " + platforms.size() + " plateformes chargées");
    }

    public void handleCollisions(Player player) {
        player.setGrounded(false);

        Rectangle playerBounds = player.getBounds();
        float playerVelY = player.getVelocityY();

        for (Rectangle platform : platforms) {
            if (playerBounds.overlaps(platform)) {
                float playerBottom = playerBounds.y;
                float playerTop = playerBounds.y + playerBounds.height;
                float playerLeft = playerBounds.x;
                float playerRight = playerBounds.x + playerBounds.width;

                float platformTop = platform.y + platform.height;
                float platformBottom = platform.y;
                float platformLeft = platform.x;
                float platformRight = platform.x + platform.width;

                float overlapTop = playerTop - platformBottom;
                float overlapBottom = platformTop - playerBottom;
                float overlapLeft = playerRight - platformLeft;
                float overlapRight = platformRight - playerLeft;

                float minOverlap = Math.min(
                    Math.min(overlapTop, overlapBottom),
                    Math.min(overlapLeft, overlapRight)
                );

                if (minOverlap == overlapBottom && playerVelY <= 0) {
                    player.stopFalling(platformTop);
                    return;
                } else if (minOverlap == overlapTop && playerVelY > 0) {
                    player.hitCeiling(platformBottom - playerBounds.height);
                    return;
                } else if (minOverlap == overlapLeft) {
                    player.hitWall(platformLeft - playerBounds.width);
                    return;
                } else if (minOverlap == overlapRight) {
                    player.hitWall(platformRight);
                    return;
                }
            }
        }
    }

    public List<Rectangle> getPlatforms() {
        return platforms;
    }
}
