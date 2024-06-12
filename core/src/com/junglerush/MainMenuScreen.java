package com.junglerush;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import javax.swing.*;
import java.util.Scanner;

public class MainMenuScreen implements Screen {

    private final JungleRush game;
    private float opacityControl = 0.3f;
    private boolean increase = true;
    private Score mainMenuText;

    public MainMenuScreen(JungleRush game)
    {
        this.game = game;
        mainMenuText = new Score("Fonts/robotoMonoRegular.ttf",32,1);
    }

    @Override
    public void render(float v) {
        ScreenUtils.clear(0,0,0.2f,1);

        String welcome = "Welcome To Jungle Rush!",
                play = "Tap Anywhere Or Press Space To Play";
        mainMenuText.blinkingEffect(0.04f,  0.03f, 0.3f);
        mainMenuText.setColor(new Color(mainMenuText.getOpacity(),1,1,mainMenuText.getOpacity()));

        game.batch.begin();
        int width = 400,height = 200;
        mainMenuText.drawXY(game.batch,welcome,game.SCREEN_WIDTH/2,game.SCREEN_HEIGHT/2+50);
        mainMenuText.drawXY(game.batch,play,game.SCREEN_WIDTH/2,game.SCREEN_HEIGHT/2);

        game.batch.end();

        // Check for touch or space key press
        if (Gdx.input.justTouched() || Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            game.setScreen(new GameScreen(game));
            dispose();
        }
    }

    @Override
    public void show() {

    }

    @Override
    public void resize(int i, int i1) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
