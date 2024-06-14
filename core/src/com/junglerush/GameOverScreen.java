package com.junglerush;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import org.w3c.dom.css.Rect;

import java.math.BigInteger;
import java.util.Iterator;
import java.util.Objects;
import java.util.TreeSet;

public class GameOverScreen implements Screen {
    private final Player player;
    private final GameScreen gameScreen;
    private final JungleRush game;
    private final Score gameOverText;
    private Rectangle gameOverRectangle,scoreTreeRect;
    private final ScoreManager scoreManager;

    public GameOverScreen(Player player, JungleRush game, GameScreen gameScreen) {
        this.player = player;
        this.game = game;
        this.gameScreen = gameScreen;
        scoreManager = new ScoreManager();
        gameOverText = new Score("Fonts/robotoMonoRegular.ttf",32,1);
        gameOverText.setColor(Color.WHITE);

        scoreManager.addScore(JungleRush.playerName,player.getMaxScore(),14);


        int rectWidth = 800,rectHeight = 500;

        gameOverRectangle = new Rectangle((game.SCREEN_WIDTH-rectWidth)/2f,(game.SCREEN_HEIGHT-rectHeight)/2f,rectWidth,rectHeight);
    }

    @Override
    public void render(float v) {
        ScreenUtils.clear(0,0,0.2f,1);
        game.batch.begin();
        gameOverText.blinkingEffect(0.04f,0.03f,0.3f);
        gameOverText.setColor(new Color(gameOverText.getOpacity(),1,1,gameOverText.getOpacity()));
        gameOverText.drawXY(game.batch,"Tap Anywhere or Press Space To Restart",game.SCREEN_WIDTH/2-180,game.SCREEN_HEIGHT/2);

        gameOverText.setColor(new Color(gameOverText.getOpacity(),1,1,1));
        String highestScore = player.getMaxScore().toString();
        if(player.getMaxScore().compareTo(BigInteger.valueOf(100000))>=0) highestScore = NumberFormatter.formatBigInteger(player.getMaxScore());
        gameOverText.drawXY(game.batch,"        Game Over\nHighest Score Achieved: "+highestScore,game.SCREEN_WIDTH/2-180,game.SCREEN_HEIGHT/2+200);



        Array<PlayerData> players = scoreManager.getPlayersData();
        Rectangle nameRect = new Rectangle(game.SCREEN_WIDTH-410,game.SCREEN_HEIGHT-100,300,50);
        Rectangle scoreRect = new Rectangle(nameRect.x+nameRect.width,nameRect.y, game.SCREEN_WIDTH-(nameRect.x+nameRect.width), 50);

        gameOverText.setRectangle( new Rectangle(game.SCREEN_WIDTH-410,game.SCREEN_HEIGHT-100,410,100));
        gameOverText.setColor(new Color(gameOverText.getOpacity(),1,1,1));
        gameOverText.draw(game.batch,"All Time Best Scores");
        int cnt = 0;
        for(PlayerData player:players)
        {
            gameOverText.setColor(new Color(1,gameOverText.getOpacity(),1,1));
            if(cnt == 0 && Objects.equals(player.getPlayerName(), JungleRush.playerName) && Objects.equals(player.getScore(), this.player.getMaxScore()))
            {
                cnt++;
                gameOverText.setColor(new Color(0,1,gameOverText.getOpacity(),1));
            }
            String scoreValue = player.getScore().toString();
            if(player.getScore().compareTo(BigInteger.valueOf(10000))>=0) scoreValue = NumberFormatter.formatBigInteger(player.getScore());

            gameOverText.setRectangle(nameRect);
            gameOverText.draw(game.batch,player.getPlayerName(),false);
            gameOverText.setRectangle(scoreRect);
            gameOverText.draw(game.batch,": " + scoreValue,false);
            nameRect.y -= 50;
            scoreRect.y = nameRect.y;
        }


        game.batch.end();

        update();
    }

    private void update()
    {
        if(Gdx.input.isTouched() || Gdx.input.isKeyPressed(Input.Keys.SPACE))
        {
            game.setScreen(new GameScreen(game));
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
