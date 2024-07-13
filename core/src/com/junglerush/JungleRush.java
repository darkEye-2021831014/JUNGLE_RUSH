package com.junglerush;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;


public class JungleRush extends Game {
	final int SCREEN_WIDTH = 1280, SCREEN_HEIGHT = 800;
	public SpriteBatch batch;
	public ShapeRenderer shapeRenderer;
	public static String playerName = null;
	public static ScoreManager scoreManager;


	@Override
	public void create () {
		batch = new SpriteBatch();
		shapeRenderer =new ShapeRenderer();
		scoreManager = new ScoreManager();
		this.setScreen(new MainMenuScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		shapeRenderer.dispose();
		scoreManager.disconnect();
	}
}
