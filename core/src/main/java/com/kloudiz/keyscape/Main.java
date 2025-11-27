package com.kloudiz.keyscape;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Main extends Game {

    public SpriteBatch batch;

    @Override
    public void create() {
        System.out.println("=== KEYSCAPE START ===");

        batch = new SpriteBatch();

        com.kloudiz.keyscape.managers.ProgressManager.getInstance().resetProgress();

        setScreen(new CinematicScreen(this, CinematicScreen.CinematicType.INTRO));

        System.out.println("=== PRÊT À JOUER ===");
    }

    @Override
    public void dispose() {
        if (batch != null) batch.dispose();
        MusicManager.getInstance().dispose();
        super.dispose();
    }
}