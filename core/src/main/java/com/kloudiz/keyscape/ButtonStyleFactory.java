package com.kloudiz.keyscape;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class ButtonStyleFactory {
    public static TextButton.TextButtonStyle createColoredButtonStyle(
            int width, int height,
            Color normalColor, Color hoverColor, Color pressedColor) {

        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();

        style.up = new TextureRegionDrawable(createRoundedRectTexture(width, height, normalColor));
        style.over = new TextureRegionDrawable(createRoundedRectTexture(width, height, hoverColor));
        style.down = new TextureRegionDrawable(createRoundedRectTexture(width, height, pressedColor));

        style.font = new BitmapFont();
        style.fontColor = Color.WHITE;

        return style;
    }

    public static TextButton.TextButtonStyle createPlayButtonStyle() {
        return createColoredButtonStyle(
            200, 60,
            new Color(0.2f, 0.6f, 0.2f, 1),
            new Color(0.3f, 0.7f, 0.3f, 1),
            new Color(0.15f, 0.5f, 0.15f, 1)
        );
    }

    private static TextureRegion createRoundedRectTexture(int width, int height, Color color) {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fillRectangle(0, 0, width, height);

        pixmap.setColor(color.r * 0.7f, color.g * 0.7f, color.b * 0.7f, 1);
        pixmap.drawRectangle(0, 0, width, height);
        pixmap.drawRectangle(1, 1, width - 2, height - 2);

        Texture texture = new Texture(pixmap);
        pixmap.dispose();

        return new TextureRegion(texture);
    }

    public static Texture createColorTexture(int width, int height, Color color) {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fill();
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }
}
