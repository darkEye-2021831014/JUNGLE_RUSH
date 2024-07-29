package com.junglerush;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.Texture;

import javax.swing.*;

public class MainMenuScreen implements Screen {

    private final JungleRush game;
    private final Texture bgImage;
    private final Texture newBgImage;
    private final Texture logoImage;
    private final Texture tigImage;
    private final Texture lioImage;
    private final Texture deerImage;
    private final Score mainMenuText;


    public MainMenuScreen(JungleRush game) {
        this.game = game;
        this.bgImage = new Texture(Gdx.files.internal("Background/fin3.jpg"));
        this.newBgImage = new Texture(Gdx.files.internal("Background/bgImage1.png"));
        this.logoImage = new Texture(Gdx.files.internal("Background/logoImage.png"));
        this.tigImage = new Texture(Gdx.files.internal("Background/tigf.png"));
        this.lioImage = new Texture(Gdx.files.internal("Background/lionf.png"));
        this.deerImage = new Texture(Gdx.files.internal("Background/deer.png"));
        this.mainMenuText = new Score("Fonts/robotoMonoRegular.ttf", 32, 1);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);

        String welcome = "Welcome To Jungle Rush!",
                play = "Tap Anywhere Or Press Space To Play";
        mainMenuText.blinkingEffect(0.04f, 0.03f, 0.3f);
        mainMenuText.setColor(new Color(mainMenuText.getOpacity(), 1, 1, mainMenuText.getOpacity()));

        game.batch.begin();

        //  background images
        game.batch.draw(bgImage, 0, 0, game.SCREEN_WIDTH, game.SCREEN_HEIGHT);
        game.batch.draw(newBgImage, 0, 0, game.SCREEN_WIDTH, game.SCREEN_HEIGHT - 300);

        // logo
        float logoY = (game.SCREEN_HEIGHT - logoImage.getHeight()) / 2 + 50;
        float logoWidth = ((game.SCREEN_WIDTH +150)/ 2)  ;
        game.batch.draw(logoImage, (game.SCREEN_WIDTH - logoWidth) / 2, logoY - 5, logoWidth, logoImage.getHeight());

        // Scaling factors for the images
        float scaleTig = 1.4f;
        float scaleLio = 0.75f;
        float scaleDeer = 1.3f;

        //  tig, lio, and deer images
        game.batch.draw(tigImage, 500, -30, tigImage.getWidth() * scaleTig, tigImage.getHeight() * scaleTig);
        game.batch.draw(lioImage, 1000, 0, lioImage.getWidth() * scaleLio, lioImage.getHeight() * scaleLio);
        game.batch.draw(deerImage, -200, -100, deerImage.getWidth() * scaleDeer, deerImage.getHeight() * scaleDeer);

        //  blinking text
        mainMenuText.drawXY(game.batch, welcome, game.SCREEN_WIDTH / 2, game.SCREEN_HEIGHT / 2 + 30);
        mainMenuText.drawXY(game.batch, play, game.SCREEN_WIDTH / 2, game.SCREEN_HEIGHT / 2-30);

        game.batch.end();

        // Check for touch or space key press
        if (Gdx.input.justTouched() || Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            UserNamePrompt();
            game.setScreen(new GameScreen(game));
            dispose();
        }
    }

    private void UserNamePrompt() {
        while (JungleRush.playerName == null) {
            JungleRush.playerName = JOptionPane.showInputDialog(null, "Enter Your Name (Max 15 Characters): ", "Input Dialog", JOptionPane.QUESTION_MESSAGE);

            if(JungleRush.playerName != null && (JungleRush.playerName.isEmpty() || JungleRush.playerName.length() > 15))
                JungleRush.playerName = null;

            for (int i = 0; JungleRush.playerName !=null && i < JungleRush.playerName.length(); i++) {
                int ok = 0;
                if(i!=0)
                {
                    if(JungleRush.playerName.charAt(i)==' ') ok |= 1;
                }
                if(Character.toUpperCase(JungleRush.playerName.charAt(i)) >= 'A' && Character.toUpperCase(JungleRush.playerName.charAt(i)) <= 'Z')
                    ok|=1;

                if(ok != 1)
                {
                    JungleRush.playerName = null;
                    break;
                }
            }

            if(JungleRush.playerName == null)
                JOptionPane.showMessageDialog(null, "UserName Cannot Contain Anything Besides A-Z and SPACE!\nUsername Cannot Contain More Than 15 Characters!", "WARNING!", JOptionPane.WARNING_MESSAGE);

        }
    }

    @Override
    public void show() {
    }

    @Override
    public void resize(int w, int h) {
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
        bgImage.dispose();
        newBgImage.dispose();
        logoImage.dispose();
        tigImage.dispose();
        lioImage.dispose();
        deerImage.dispose();
        mainMenuText.cleanUp();
    }
}