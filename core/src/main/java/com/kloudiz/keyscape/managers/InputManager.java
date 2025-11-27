package com.kloudiz.keyscape.managers;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.kloudiz.keyscape.entities.Player;

public class InputManager implements InputProcessor {

    private Player player;

    public InputManager(Player player) {
        this.player = player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (player == null) return false;

        switch (keycode) {
            case Input.Keys.A:
            case Input.Keys.LEFT:
                player.moveLeft();
                return true;

            case Input.Keys.D:
            case Input.Keys.RIGHT:
                player.moveRight();
                return true;

            case Input.Keys.SPACE:
                player.jump();
                return true;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (player == null) return false;

        switch (keycode) {
            case Input.Keys.A:
            case Input.Keys.LEFT:
            case Input.Keys.D:
            case Input.Keys.RIGHT:
                player.stopMoving();
                return true;
        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
