package com.junglerush;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.math.Rectangle;

public class Enemy {
    private Rectangle rectangle;
    private final Array<Texture> textures;
    private float speed;
    private long score;
    private int scoreFactor,Index;
    private final Score text;
    private int isPositive;

    public Enemy(float speed,int scoreFactor, Score text,int isPositive){
        this.text = text;
        this.textures = new Array<>();
        this.rectangle = new Rectangle();
        this.speed = speed;
        this.scoreFactor = scoreFactor;
        this.isPositive =isPositive;
    }


    public void addTexture(Texture texture){
        this.textures.add(texture);
    }

    public void spawnEnemy(float x, float y, int scoreFactorLower, int scoreFactorUpper)
    {
        this.rectangle.x = x;
        this.rectangle.y = y;
        this.rectangle.width = getTexture().getWidth();
        this.rectangle.height = getTexture().getHeight();
        //set score
        text.setRectangle(this.rectangle);
        setScore(scoreFactorLower,scoreFactorUpper);
    }

    public Texture getTexture(){
        return this.textures.get(this.Index);
    }

    public void draw(SpriteBatch batch){
        batch.draw(getTexture(),rectangle.x,rectangle.y,rectangle.width,rectangle.height);
        //draw score
        text.setRectangle(this.rectangle);
        text.draw(batch,Long.toString(this.score));
    }

    public void update(){
        rectangle.y -= speed;
    }

    public void setTextColor(Color color){
        text.setColor(color);
    }






    //setters

    public void setIsPositive(int isPositive) {
        this.isPositive =isPositive;
    }

    public void setRectangle(Rectangle rectangle) {
        this.rectangle = rectangle;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public void setScore(int scoreFactorLower, int scoreFactorUpper) {
        setScoreFactor(MathUtils.random(scoreFactorLower,scoreFactorUpper));
        this.score = isPositive * (1L << this.scoreFactor);
    }

    public void setScoreFactor(int scoreFactor) {
        this.scoreFactor = scoreFactor;
    }

    public void setIndex() {
        this.Index = MathUtils.random(0,this.textures.size-1);
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
        this.score = 1L << scoreFactor;
        return this.score;
    }

    public int getScoreFactor() {
        return scoreFactor;
    }

    public int getIndex() {
        return Index;
    }
}
