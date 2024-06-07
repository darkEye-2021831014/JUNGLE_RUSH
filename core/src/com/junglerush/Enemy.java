package com.junglerush;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;

import java.awt.*;

public class Enemy {
    private Rectangle rectangle;
    private final Array<Texture> textures;
    private float speed;
    private long score;
    private int scoreFactor,currentIndex;

    public Enemy(float speed,int scoreFactor){
        this.textures = new Array<>();
        this.speed = speed;
        this.scoreFactor = scoreFactor;
    }


    public void addTexture(Texture texture){
        this.textures.add(texture);
    }



    //setters
    public void setRectangle(Rectangle rectangle) {
        this.rectangle = rectangle;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public void setScore(long score) {
        this.score = score;
    }

    public void setScoreFactor(int scoreFactor) {
        this.scoreFactor = scoreFactor;
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }

    //getters
    public Rectangle getRectangle() {
        return rectangle;
    }

    public Array<Texture> getTextures() {
        return textures;
    }

    public float getSpeed() {
        return speed;
    }

    public long getScore() {
        return score;
    }

    public int getScoreFactor() {
        return scoreFactor;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }
}
