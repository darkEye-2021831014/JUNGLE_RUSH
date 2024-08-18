package com.junglerush;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class collisionIndicator {
    private final Rectangle rectangle;
    private Texture texture,textureOk;
    private GameState collisionState;
    private final float blinkingSpeed;
    private float alpha;

    public collisionIndicator(float blinkingSpeed)
    {
        this.rectangle = new Rectangle();
        this.collisionState = GameState.DEFAULT;
        this.alpha = 1.0f;
        this.blinkingSpeed = blinkingSpeed;
    }


    public void setRectangle(Player player,boolean solidWidthHeight) {
        if(solidWidthHeight) {
            this.rectangle.width = 40;
            this.rectangle.height = 33;
        }
        else
        {
            this.rectangle.width = this.texture.getWidth();
            this.rectangle.height = this.rectangle.getHeight();
        }
        this.rectangle.x = player.getRectangle().x+(player.getRectangle().width-this.rectangle.width)/2.0f;
        this.rectangle.y = 30f;

    }

    public void setTexture(Texture textureAlert, Texture textureOk) {
        this.texture = textureAlert;
        this.textureOk = textureOk;
    }

    public void draw(SpriteBatch batch)
    {
        if(collisionState == GameState.COLLISION_RED || collisionState == GameState.COLLISION_GREEN) {
            batch.setColor(batch.getColor().r,batch.getColor().g,batch.getColor().b,this.alpha);

            if(collisionState == GameState.COLLISION_RED)
                batch.draw(this.texture, rectangle.x, rectangle.y,rectangle.width,rectangle.height);
            if(collisionState == GameState.COLLISION_GREEN)
                batch.draw(this.textureOk, rectangle.x, rectangle.y,rectangle.width,rectangle.height);

            batch.setColor(batch.getColor().r,batch.getColor().g,batch.getColor().b,1.0f);
        }
    }

    public void update(Enemy enemy,Enemy animal,Player player,Indicator animalIndicator, Indicator carIndicator)
    {
        Rectangle collider = new Rectangle();
        collider.x =  player.getRectangle().x;
        collider.y =  player.getRectangle().y;
        collider.width =  player.getRectangle().width;
        collider.height = Gdx.graphics.getHeight();

        if(collider.overlaps(enemy.getRectangle()) || collider.overlaps(animal.getRectangle())) {
            if(collider.overlaps(enemy.getRectangle()) && collider.overlaps(animal.getRectangle()))
            {
                if(animal.getRectangle().y < enemy.getRectangle().y)
                    Animal(animalIndicator);
                else
                    Car(carIndicator);
            }
            else if(collider.overlaps(enemy.getRectangle()))
                Car(carIndicator);

            else
                Animal(animalIndicator);

            alpha += this.blinkingSpeed *Gdx.graphics.getDeltaTime();
            alpha = (float) Math.abs(Math.sin(alpha));
        }
        else
            collisionState = GameState.DEFAULT;

        rectangle.x = player.getRectangle().x+(player.getRectangle().width-this.rectangle.width)/2.0f;
    }

    private void Car(Indicator carIndicator)
    {
        if(carIndicator.getColor() == Color.RED)
            collisionState = GameState.COLLISION_RED;
        else
            collisionState= GameState.COLLISION_GREEN;
    }

    private void Animal(Indicator animalIndicator)
    {
        if(animalIndicator.getColor() == Color.RED)
            collisionState = GameState.COLLISION_RED;
        else
            collisionState= GameState.COLLISION_GREEN;
    }




    public Rectangle getRectangle() {
        return rectangle;
    }

    public void cleanUp()
    {
        texture.dispose();
    }

}
