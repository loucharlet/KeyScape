package com.kloudiz.keyscape.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.kloudiz.keyscape.entities.Player;
import com.kloudiz.keyscape.world.Level;

public class Hud {

    private BitmapFont font;
    private OrthographicCamera hudCamera;
    private Texture coinIcon;
    private Texture lifeIcon;

    public Hud() {
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(2f);

        coinIcon = new Texture("ui/coin.png");
        lifeIcon = new Texture("déco+elements/gingembre.png");

        hudCamera = new OrthographicCamera();
        hudCamera.setToOrtho(false, 800, 600);
    }

    public void render(SpriteBatch batch, Player player, Level level) {
        hudCamera.update();
        batch.setProjectionMatrix(hudCamera.combined);

        batch.begin();

        batch.draw(coinIcon, 675, 562, 32, 32);

        String coinsText = player.getCoinsCollected() + " / " + level.getRequiredCoins();
        font.draw(batch, coinsText, 715, 590);

        batch.draw(lifeIcon, 730, 512, 32, 32);

        String livesText = String.valueOf(player.getLives());
        font.draw(batch, livesText, 770, 540);

        batch.end();
    }

    public void dispose() {
        font.dispose();
        coinIcon.dispose();
        lifeIcon.dispose();
    }
}
