package com.junglerush;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

public class ProcessInput implements InputProcessor {

    private final Player player;
    private final GameScreen gameScreen;

    public ProcessInput(Player player,GameScreen gameScreen) {
        this.player = player;
        this.gameScreen = gameScreen;
    }

    private void setKeyValues(int keyCode, boolean value)
    {
        switch (keyCode)
        {
            case Input.Keys.LEFT:
            case Input.Keys.A:
                this.player.setLeftMove(value);
                break;
            case Input.Keys.RIGHT:
            case Input.Keys.D:
                this.player.setRightMove(value);
                break;
            case Input.Keys.UP:
            case Input.Keys.W:
                this.player.setUpMove(value);
                break;
            case Input.Keys.DOWN:
            case Input.Keys.S:
                this.player.setDownMove(value);
                break;
        }
    }

    @Override
    public boolean keyDown(int keyCode) {
        this.setKeyValues(keyCode, true);
        switch (keyCode)
        {
            case Input.Keys.SPACE:
                if(!gameScreen.isOnHold()) {
                    gameScreen.setPaused(!gameScreen.isPaused());
                    if(gameScreen.isPaused()) {
                        gameScreen.getRoadSound().pause();

                        gameScreen.getPauseSound().stop();
                        gameScreen.getResumeSound().stop();
                        gameScreen.getPauseSound().play();
                    }
                    else {
                        gameScreen.getRoadSound().resume();

                        gameScreen.getPauseSound().stop();
                        gameScreen.getResumeSound().stop();
                        gameScreen.getResumeSound().play();
                    }
                }
                break;
            case Input.Keys.UP:
            case Input.Keys.W:
                if(!gameScreen.isOnHold() && !gameScreen.isPaused() && !gameScreen.isGameOver()) {
                    gameScreen.getCarSpeedUpSound().stop();
                    gameScreen.getCarSpeedUpSound().play();
                }
                break;
        }
        return true;
    }

    @Override
    public boolean keyUp(int keyCode) {
        this.setKeyValues(keyCode, false);
        switch (keyCode)
        {
            case Input.Keys.UP:
            case Input.Keys.W:
//                gameScreen.getCarSpeedUpSound().pause();
                break;
        }
        return true;
    }

    @Override
    public boolean keyTyped(char c) {
        return false;
    }

    @Override
    public boolean touchDown(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchUp(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchCancelled(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchDragged(int i, int i1, int i2) {
        return false;
    }

    @Override
    public boolean mouseMoved(int i, int i1) {
        return false;
    }

    @Override
    public boolean scrolled(float v, float v1) {
        return false;
    }
}
