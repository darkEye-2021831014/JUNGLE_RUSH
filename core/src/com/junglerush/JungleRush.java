package com.junglerush;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.ScreenUtils;

public class JungleRush extends Game {
	final int SCREEN_WIDTH = 1280, SCREEN_HEIGHT = 800;
	private FreeTypeFontGenerator generator;
	private FreeTypeFontGenerator.FreeTypeFontParameter parameter;
	SpriteBatch batch;
	BitmapFont fontBold,fontRegular;


	@Override
	public void create () {
		batch = new SpriteBatch();
		//create font
		generator = new FreeTypeFontGenerator(Gdx.files.internal("Fonts/robotoMonoRegular.ttf"));
		parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.size = 28;
		fontRegular = generator.generateFont(parameter);
		//bold font
		generator = new FreeTypeFontGenerator(Gdx.files.internal("Fonts/robotoMonoBold.ttf"));
		parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.size = 20;
		fontBold = generator.generateFont(parameter);




		this.setScreen(new MainMenuScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		generator.dispose();

	}
}
