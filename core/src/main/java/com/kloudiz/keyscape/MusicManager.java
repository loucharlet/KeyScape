package com.kloudiz.keyscape;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

public class MusicManager {

    private static MusicManager instance;
    private Music backgroundMusic;
    private boolean isEnabled = true;
    private float volume = 0.5f;


    public static MusicManager getInstance() {
        if (instance == null) {
            instance = new MusicManager();
        }
        return instance;
    }

    private MusicManager() {

    }


    public void loadAndPlay(String musicPath) {
        try {

            if (backgroundMusic != null) {
                backgroundMusic.stop();
                backgroundMusic.dispose();
            }


            backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal(musicPath));
            backgroundMusic.setLooping(true);
            backgroundMusic.setVolume(volume);

            if (isEnabled) {
                backgroundMusic.play();
                System.out.println("Musique lancée : " + musicPath);
            }
        } catch (Exception e) {
            System.err.println("Erreur chargement musique : " + e.getMessage());
        }
    }


    public void toggle() {
        isEnabled = !isEnabled;

        if (backgroundMusic != null) {
            if (isEnabled) {
                backgroundMusic.play();
            } else {
                backgroundMusic.pause();
            }
        }
    }


    public void setVolume(float volume) {
        this.volume = Math.max(0f, Math.min(1f, volume));
        if (backgroundMusic != null) {
            backgroundMusic.setVolume(this.volume);
        }
    }

    public void increaseVolume() {
        setVolume(volume + 0.1f);
    }


    public void decreaseVolume() {
        setVolume(volume - 0.1f);
    }


    public void pause() {
        if (backgroundMusic != null && backgroundMusic.isPlaying()) {
            backgroundMusic.pause();
        }
    }


    public void resume() {
        if (backgroundMusic != null && isEnabled) {
            backgroundMusic.play();
        }
    }


    public void dispose() {
        if (backgroundMusic != null) {
            backgroundMusic.stop();
            backgroundMusic.dispose();
            backgroundMusic = null;
        }
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public float getVolume() {
        return volume;
    }
}
