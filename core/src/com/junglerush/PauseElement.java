package com.junglerush;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class PauseElement {
    private ImageButton button;
    private ButtonType type;

    public PauseElement(String texturePath, int RIVER_WIDTH,float y, ButtonType type)
    {
        this.type = type;
        this.button = new ImageButton(new TextureRegionDrawable(new Texture(texturePath)));
        button.setPosition((Gdx.graphics.getWidth()-RIVER_WIDTH-button.getWidth())/2, y- button.getHeight());
        button.addListener(new InputListener()
        {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(button == Input.Buttons.LEFT)
                {
                    mouseClicked();
                    return true;
                }
                return false;
            }
        });
    }

    private void mouseClicked() {
        switch (type)
        {
            case PAUSE_SCREEN:
                //Nothing To Do Here
                break;
            case RESUME:
                ProcessInput.pressedSpace();
                break;
            case SCORECARD:
                //TODO
                break;
            case QUIT:
                Gdx.app.exit();
                break;

        }
    }

    public ImageButton getButton() {
        return button;
    }

    public ButtonType getType() {
        return type;
    }

}
