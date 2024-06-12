package com.junglerush;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import jdk.internal.org.jline.utils.ShutdownHooks;
import org.w3c.dom.css.Rect;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

import java.math.BigInteger;

public class Collision {
    private final Player player;
    private final Background background;
    private final Enemy enemyCar,enemyAnimal;
    private final GameScreen gameScreen;
    private final JungleRush game;

    public Collision(Player player, Background background, Enemy enemyCar, Enemy enemyAnimal, GameScreen gameScreen, JungleRush game) {
        this.player = player;
        this.background = background;
        this.enemyCar = enemyCar;
        this.enemyAnimal = enemyAnimal;
        this.gameScreen = gameScreen;
        this.game = game;
    }

    public boolean detectCollision(Rectangle rectangle1, Rectangle rectangle2){
        return rectangle1.overlaps(rectangle2);
    }

    public void update()
    {
        //collision with road boarder
        for(Rectangle rect:background.getBoarderRect())
            if(detectCollision(rect,player.getRectangle()))
            {
                gameScreen.getBoarderCollisionSound().play();
                gameOver();
                return;
            }

        //collision with enemy car
        if(detectCollision(player.getRectangle(),enemyCar.getRectangle())) {

            if(enemyCar.getScore().compareTo(player.getScore()) > 0)
            {
                gameScreen.getCollisionWithStrongCar().play();
                gameOver();
                return;
            }
            else
            {
                gameScreen.getCollisionWithNormalCar().stop();
                gameScreen.getCollisionWithGreenAnimal().stop();
                gameScreen.getCollisionWithStrongAnimal().stop();
                gameScreen.getCollisionWithNormalCar().play(0.9f);
                player.setScore(enemyCar.getScore(),false);
                gameScreen.spawnEnemyCar();
            }
        }

        //collision with enemy animal
        if(detectCollision(player.getRectangle(),enemyAnimal.getRectangle()))
        {
            if(enemyAnimal.isDivide())
            {
                gameScreen.getCollisionWithNormalCar().stop();
                gameScreen.getCollisionWithGreenAnimal().stop();
                gameScreen.getCollisionWithStrongAnimal().stop();
                gameScreen.getCollisionWithStrongAnimal().play(0.8f);
            }
            else
            {
                gameScreen.getCollisionWithNormalCar().stop();
                gameScreen.getCollisionWithGreenAnimal().stop();
                gameScreen.getCollisionWithStrongAnimal().stop();
                gameScreen.getCollisionWithGreenAnimal().play(0.8f);
            }
            player.setScore(enemyAnimal.getScore(),enemyAnimal.isDivide());
            if(player.getScore().compareTo(BigInteger.valueOf(0)) <= 0)
            {
                gameScreen.getAnimalCollisionGameOver().play();
                gameOver();
                return;
            }
            else
                gameScreen.spawnEnemyAnimal();
        }

        //collision between enemyCar and enemy enemyAnimal
        if(detectCollision(enemyAnimal.getRectangle(),enemyCar.getRectangle()))
        {
            if(enemyAnimal.isDivide())
            {
                enemyCar.setScore(enemyCar.getScore().divide(enemyAnimal.getScore()));
                if(enemyCar.getScore().compareTo(BigInteger.valueOf(0))==0) {
                    gameScreen.spawnEnemyCar();
                }
            }
            else
                enemyCar.setScore(enemyCar.getScore().add(enemyAnimal.getScore()));
            gameScreen.spawnEnemyAnimal();
        }
    }

    public void gameOver()
    {
        gameScreen.setGameOver(true);
        for(Sound sound:gameScreen.getSoundArray())
            sound.stop();
        for(Sound sound:gameScreen.getForestBirdSounds())
            sound.stop();
        game.setScreen(new GameOverScreen(player,game,gameScreen));

    }
}
