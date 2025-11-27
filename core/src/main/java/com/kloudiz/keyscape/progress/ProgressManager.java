package com.kloudiz.keyscape.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class ProgressManager {
    private static ProgressManager instance;
    private Preferences prefs;

    private static final String PREFS_NAME = "keyscape_progress";
    private static final String LEVEL_CASSIDY = "level_cassidy_completed";
    private static final String LEVEL_LOU = "level_lou_completed";
    private static final String LEVEL_LOUISE = "level_louise_completed";

    private ProgressManager() {
        prefs = Gdx.app.getPreferences(PREFS_NAME);
    }

    public static ProgressManager getInstance() {
        if (instance == null) {
            instance = new ProgressManager();
        }
        return instance;
    }

    public void markLevelCompleted(int levelIndex) {
        String key = getLevelKey(levelIndex);
        if (key != null) {
            prefs.putBoolean(key, true);
            prefs.flush();
            System.out.println("Niveau " + levelIndex + " marqué comme complété !");
        }
    }

    public boolean isLevelCompleted(int levelIndex) {
        String key = getLevelKey(levelIndex);
        return key != null && prefs.getBoolean(key, false);
    }

    public int getCompletedLevelsCount() {
        int count = 0;
        for (int i = 0; i < 3; i++) {
            if (isLevelCompleted(i)) {
                count++;
            }
        }
        return count;
    }

    public boolean areAllLevelsCompleted() {
        return getCompletedLevelsCount() == 3;
    }

    private String getLevelKey(int levelIndex) {
        switch (levelIndex) {
            case 0: return LEVEL_CASSIDY;
            case 1: return LEVEL_LOU;
            case 2: return LEVEL_LOUISE;
            default: return null;
        }
    }

    public void resetProgress() {
        prefs.clear();
        prefs.flush();
        System.out.println("Progression réinitialisée !");
    }
}
