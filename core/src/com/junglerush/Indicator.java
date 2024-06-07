package com.junglerush;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;

import java.math.BigInteger;

public class Indicator {
    private Array<Particle> particlesLeft,particlesRight;
    private int indicator,totalParticles;
    private float centerLeftX,centerLeftY,centerRightX,centerRightY;
    public Indicator(int totalParticles, BigInteger playerScore, BigInteger enemyScore){
        particlesLeft = new Array<>();
        particlesRight = new Array<>();
        this.totalParticles = totalParticles;
        setIndicator(playerScore,enemyScore);
    }

    public void addParticleLeft(Particle particle){
        particlesLeft.add(particle);
    }
    public void addParticleRight(Particle particle){
        particlesRight.add(particle);
    }

    public void setIndicator(BigInteger playerScore, BigInteger enemyScore){
        // indicator = - 1 ( first is less than second), 0 ( both str are equal), 1 (first is greater than second)
        this.indicator = playerScore.compareTo(enemyScore);
    }

    public void setCenterX(float centerLeftX,float centerRightX)
    {
        this.centerLeftX = centerLeftX;
        this.centerRightX = centerRightX;
    }

    public void setCenterY(float centerY){
        this.centerLeftY = centerY;
        this.centerRightY = this.centerLeftY;
    }



    public void draw(ShapeRenderer shapeRenderer){
        Color color;
        if(this.indicator < 0) color =Color.RED;
        else if(this.indicator > 0) color =Color.GREEN;
        else color = Color.BLACK;

        for(int i=0; i < totalParticles; i++) {
            particlesLeft.get(i).draw(shapeRenderer, color, (float) 0.7);
            particlesRight.get(i).draw(shapeRenderer, color, (float) 0.7);
        }
    }

    public void update(float enemySpeed)
    {
        this.centerLeftY -= enemySpeed;
        this.centerRightY -= enemySpeed;

        float deltaTime = Gdx.graphics.getDeltaTime();
        for (int i=0; i < totalParticles; i++) {
            particlesLeft.get(i).update(deltaTime, centerLeftX,centerLeftY);
            particlesRight.get(i).update(deltaTime, centerRightX,centerRightY);
        }
    }




    //getters & setters
    public int getTotalParticles() {
        return totalParticles;
    }

    public Array<Particle> getParticlesLeft() {
        return particlesLeft;
    }

    public Array<Particle> getParticlesRight() {
        return particlesRight;
    }

    public int getIndicator() {
        return indicator;
    }

    public float getCenterLeftX() {
        return centerLeftX;
    }

    public float getCenterLeftY() {
        return centerLeftY;
    }

    public float getCenterRightX() {
        return centerRightX;
    }

    public float getCenterRightY() {
        return centerRightY;
    }
}
