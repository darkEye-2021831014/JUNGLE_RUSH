package com.junglerush;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;

import java.math.BigInteger;

public class Indicator {
    private Array<Particle> particles;
    private int indicator,totalParticles;
    private float centerX,centerY;
    public Indicator(int totalParticles, BigInteger playerScore, BigInteger enemyScore){
        particles = new Array<>();
        this.totalParticles = totalParticles;
        setIndicator(playerScore,enemyScore);
    }

    public void addParticle(Particle particle){
        particles.add(particle);
    }

    public void setIndicator(BigInteger playerScore, BigInteger enemyScore){
        // indicator = - 1 ( first is less than second), 0 ( both str are equal), 1 (first is greater than second)
        this.indicator = playerScore.compareTo(enemyScore);
    }

    public void setIndicator(int indicator)
    {
        this.indicator = indicator;
    }

    public void setCenterX(float centerX)
    {
        this.centerX = centerX;
    }

    public void setCenterY(float centerY){
        this.centerY = centerY;
    }



    public void draw(ShapeRenderer shapeRenderer,float radius){
        Color color;
        if(this.indicator < 0) color =Color.RED;
        else if(this.indicator > 0) color =Color.GREEN;
        else color = Color.BLACK;

        for(int i=0; i < totalParticles; i++) {
            particles.get(i).draw(shapeRenderer, color, radius);
        }
    }

    public void updateCenterY(float enemySpeed, Enemy enemy)
    {
        this.centerY = enemy.getRectangle().y+enemy.getRectangle().height/2;
    }

    public void update()
    {
        float deltaTime = Gdx.graphics.getDeltaTime();
        for (int i=0; i < totalParticles; i++) {
            particles.get(i).update(deltaTime, centerX,centerY);
        }
    }




    //getters & setters
    public int getTotalParticles() {
        return totalParticles;
    }

    public int getIndicator() {
        return indicator;
    }

    public Array<Particle> getParticles() {
        return particles;
    }

    public float getCenterX() {
        return centerX;
    }

    public float getCenterY() {
        return centerY;
    }

}
