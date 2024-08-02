package com.junglerush;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class PauseElement {
    private ImageButton button;
    private ButtonType type;
    private final GameScreen gameScreen;

    public PauseElement(String texturePath, int RIVER_WIDTH,float y, ButtonType type, GameScreen gameScreen)
    {
        this.gameScreen = gameScreen;
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
            case MUSIC:
                GameScreen.musicState = (GameScreen.musicState == GameState.MUSIC_OFF)?GameState.MUSIC_ON:GameState.MUSIC_OFF;
                break;
            case SOUND:
                changeMusicVolume();
                break;
            case QUIT:
                Gdx.app.exit();
                break;

        }
    }

    private void changeMusicVolume() {
        int newVolumePercent = gameScreen.getMUSIC_VOLUME();

        JSlider slider = new JSlider(JSlider.HORIZONTAL, 0, 100, newVolumePercent);
        slider.setPaintTicks(false);
        slider.setPaintLabels(false);

        // Create a label to show the current value
        JLabel valueLabel = new JLabel(Integer.toString(newVolumePercent));

        // Add a ChangeListener to update the label as the slider moves
        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int value = slider.getValue();
                valueLabel.setText(value + "%");
            }
        });

        // Create a panel to hold the slider and the label
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(slider, BorderLayout.CENTER);
        panel.add(valueLabel, BorderLayout.SOUTH);

        // Show the slider dialog
        int result = JOptionPane.showConfirmDialog(null, panel, "Select Volume Percentage", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        // Process the result
        if (result == JOptionPane.OK_OPTION) {
            newVolumePercent = slider.getValue();
        }

        gameScreen.setMUSIC_VOLUME(newVolumePercent);
    }

    public ImageButton getButton() {
        return button;
    }

    public ButtonType getType() {
        return type;
    }

}
