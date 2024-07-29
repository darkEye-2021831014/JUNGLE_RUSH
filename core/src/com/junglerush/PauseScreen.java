package com.junglerush;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class PauseScreen {
    private Stage stage;
    private Array<PauseElement> pauseElements;

    public PauseScreen()
    {
        stage=new Stage();
        pauseElements = new Array<>();
    }

    public void addPauseElement(PauseElement pauseElement)
    {
        pauseElements.add(pauseElement);
        stage.addActor(pauseElement.getButton());
    }

    public void draw()
    {
        Gdx.input.setInputProcessor(stage);
        stage.draw();
    }

    public Stage getStage() {
        return stage;
    }

    public Array<PauseElement> getPauseElements() {
        return pauseElements;
    }
}
