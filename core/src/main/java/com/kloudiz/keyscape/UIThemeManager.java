package com.kloudiz.keyscape;

import com.badlogic.gdx.graphics.Color;

public class UIThemeManager {

    public static final int THEME_BLUE_VIOLET       = 0; // bleu → violet
    public static final int THEME_RED_VIOLET_BLUE   = 1; // rouge → bleu/violet
    public static final int THEME_FUCHSIA_PINK      = 2; // fuchsia → rose pâle
    public static final int THEME_VIOLET_ROSE_RED   = 3; // violet → rose/rouge
    public static final int THEME_YELLOW_ORANGE     = 4; // jaune → orange
    public static final int THEME_GREEN_PINK        = 5; // vert → rose
    public static final int THEME_RED_SIMPLE        = 6; // rouge uni
    public static final int THEME_RAINBOW           = 7; // style arc-en-ciel simplifié
    public static final int THEME_VIOLET_SIMPLE     = 8; // violet uni

    private static UIThemeManager instance;

    private int currentTheme = THEME_BLUE_VIOLET;

    public static UIThemeManager getInstance() {
        if (instance == null) {
            instance = new UIThemeManager();
        }
        return instance;
    }

    private UIThemeManager() {}

    public int getCurrentTheme() {
        return currentTheme;
    }

    public void setCurrentTheme(int themeIndex) {
        if (themeIndex < 0) themeIndex = 0;
        if (themeIndex > 8) themeIndex = 8;
        this.currentTheme = themeIndex;
    }

    public void nextTheme() {
        currentTheme = (currentTheme + 1) % 9;
    }

    public Color getStartColor(boolean hover) {
        switch (currentTheme) {

            case THEME_RED_VIOLET_BLUE:
                return hover
                    ? new Color(0.95f, 0.3f, 0.3f, 1f)
                    : new Color(0.85f, 0.15f, 0.15f, 1f);

            case THEME_FUCHSIA_PINK:
                return hover
                    ? new Color(1.0f, 0.2f, 0.6f, 1f)
                    : new Color(0.95f, 0.15f, 0.55f, 1f);

            case THEME_VIOLET_ROSE_RED:
                return hover
                    ? new Color(0.7f, 0.3f, 0.9f, 1f)
                    : new Color(0.6f, 0.2f, 0.8f, 1f);

            case THEME_YELLOW_ORANGE:
                return hover
                    ? new Color(1.0f, 0.95f, 0.3f, 1f)
                    : new Color(1.0f, 0.9f, 0.2f, 1f);

            case THEME_GREEN_PINK:
                return hover
                    ? new Color(0.4f, 0.9f, 0.5f, 1f)
                    : new Color(0.3f, 0.8f, 0.4f, 1f);

            case THEME_RED_SIMPLE:
                return hover
                    ? new Color(0.98f, 0.35f, 0.35f, 1f)
                    : new Color(0.9f, 0.2f, 0.2f, 1f);

            case THEME_RAINBOW:
                return hover
                    ? new Color(1.0f, 0.4f, 0.4f, 1f)
                    : new Color(1.0f, 0.2f, 0.4f, 1f);

            case THEME_VIOLET_SIMPLE:
                return hover
                    ? new Color(0.7f, 0.4f, 1.0f, 1f)
                    : new Color(0.55f, 0.3f, 0.95f, 1f);

            case THEME_BLUE_VIOLET:
            default:
                return hover
                    ? new Color(0.53f, 0.86f, 1f, 1f)
                    : new Color(0.43f, 0.79f, 1f, 1f);
        }
    }

    public Color getEndColor(boolean hover) {
        switch (currentTheme) {

            case THEME_RED_VIOLET_BLUE:
                return hover
                    ? new Color(0.3f, 0.5f, 1f, 1f)
                    : new Color(0.1f, 0.3f, 0.9f, 1f);

            case THEME_FUCHSIA_PINK:
                return hover
                    ? new Color(1.0f, 0.7f, 0.9f, 1f)
                    : new Color(0.98f, 0.8f, 0.9f, 1f);

            case THEME_VIOLET_ROSE_RED:
                return hover
                    ? new Color(0.95f, 0.3f, 0.5f, 1f)
                    : new Color(0.9f, 0.2f, 0.4f, 1f);

            case THEME_YELLOW_ORANGE:
                return hover
                    ? new Color(1.0f, 0.7f, 0.2f, 1f)
                    : new Color(0.98f, 0.55f, 0.1f, 1f);

            case THEME_GREEN_PINK:
                return hover
                    ? new Color(1.0f, 0.55f, 0.8f, 1f)
                    : new Color(0.95f, 0.4f, 0.7f, 1f);

            case THEME_RED_SIMPLE:
                return hover
                    ? new Color(0.8f, 0.1f, 0.1f, 1f)
                    : new Color(0.7f, 0.05f, 0.05f, 1f);

            case THEME_RAINBOW:
                return hover
                    ? new Color(0.4f, 0.7f, 1.0f, 1f)
                    : new Color(0.35f, 0.4f, 1.0f, 1f);

            case THEME_VIOLET_SIMPLE:
                return hover
                    ? new Color(0.85f, 0.6f, 1.0f, 1f)
                    : new Color(0.7f, 0.45f, 1.0f, 1f);

            case THEME_BLUE_VIOLET:
            default:
                return hover
                    ? new Color(0.70f, 0.25f, 1f, 1f)
                    : new Color(0.61f, 0.18f, 1f, 1f);
        }
    }
}
