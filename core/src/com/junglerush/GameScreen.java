package com.junglerush;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;

import java.awt.*;

public class GameScreen implements Screen {

    private final JungleRush game;
    private Texture tRoad;
    private Array<Rectangle> roadRect;

    private final int ELEMENT_WIDTH = 30,ELEMENT_HEIGHT = 20;

    public GameScreen(final JungleRush game)
    {
        this.game = game;
        tRoad = new Texture("Road.jpeg");

        roadRect = new Array<>();
        roadRect.add(new Rectangle(12*this.ELEMENT_WIDTH,0,12*this.ELEMENT_WIDTH,game.SCREEN_HEIGHT));
        roadRect.add(new Rectangle(12*this.ELEMENT_WIDTH,game.SCREEN_HEIGHT,12*this.ELEMENT_WIDTH,game.SCREEN_HEIGHT));
    }

    @Override
    public void render(float v) {
        ScreenUtils.clear(0,0,0.3f,1);
        game.batch.begin();
        game.batch.draw(tRoad,roadRect.get(0).x,roadRect.get(0).y,roadRect.get(0).width,roadRect.get(0).height);
        game.batch.draw(tRoad,roadRect.get(1).x,roadRect.get(1).y,roadRect.get(1).width,roadRect.get(1).height);
        game.batch.end();

        update();
    }

    private void update()
    {
        this.roadRect.get(0).y -= 10;
        this.roadRect.get(1).y -= 10;
        if(this.roadRect.get(0).y <= -game.SCREEN_HEIGHT) this.roadRect.get(0).y = game.SCREEN_HEIGHT;
        if(this.roadRect.get(1).y <= -game.SCREEN_HEIGHT) this.roadRect.get(1).y = game.SCREEN_HEIGHT;
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
