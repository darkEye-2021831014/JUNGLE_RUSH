package com.junglerush;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.StringBuilder;

import java.math.BigInteger;

public class Enemy {
    private Rectangle rectangle;
    private final Array<Texture> textures;
    private float speed;
    private BigInteger score;
    private int scoreFactor,Index;
    private final Score text;
    private boolean isDivide;
    private long maxAllowed;

    public Enemy(float speed,int scoreFactor, Score text,boolean isDivide,long maxAllowed){
        this.text = text;
        this.textures = new Array<>();
        this.rectangle = new Rectangle();
        this.speed = speed;
        this.scoreFactor = scoreFactor;
        this.isDivide = isDivide;
        this.maxAllowed = maxAllowed;
        this.score = power2(this.scoreFactor);
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
        BigInteger maxValue = BigInteger.valueOf(maxAllowed);

        String curScore = this.score.toString();
        if(this.score.compareTo(maxValue) >=0)
            curScore = NumberFormatter.formatBigInteger(this.score);
        //set divide by value
        if(isDivide)
            curScore = "/"+curScore;

        text.setRectangle(this.rectangle);
        text.draw(batch,curScore);
    }

    public void update(){
        rectangle.y -= speed;
    }

    public void setTextColor(Color color){
        text.setColor(color);
    }






    //setters


    public void setDivide(boolean divide) {
        isDivide = divide;
    }

    public void setRectangle(Rectangle rectangle) {
        this.rectangle = rectangle;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public static BigInteger power2(int value)
    {
        if(value == 0) return BigInteger.valueOf(1);

        BigInteger res = BigInteger.valueOf(1);
        if(value%2!=0)
            res = res.multiply(BigInteger.valueOf(2));
        BigInteger half = power2(value/2);
        res = res.multiply(half);
        res = res.multiply(half);

        return res;
    }

    public void setScore(int scoreFactorLower, int scoreFactorUpper) {
        setScoreFactor(MathUtils.random(scoreFactorLower,scoreFactorUpper));
        this.score = power2(this.scoreFactor);
    }

    public void setScore(BigInteger score)
    {
        this.score = score;
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

    public BigInteger getScore() {
        return this.score;
    }

    public int getScoreFactor() {
        return scoreFactor;
    }

    public int getIndex() {
        return Index;
    }

    public Score getText() {
        return text;
    }

    public boolean isDivide() {
        return isDivide;
    }

    public long getMaxAllowed() {
        return maxAllowed;
    }
}
