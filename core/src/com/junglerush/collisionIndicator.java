package com.junglerush;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class collisionIndicator {
//    private Array<Rectangle> rectangles;
    private Rectangle rectangle;
    private Color color;
    private int width,height;

    public collisionIndicator(Color color,int width,int height) {
        this.color = color;
        this.width = width;
        this.height = height;
    }

    public void setRectangle(float x, float y)
    {
        rectangle = new Rectangle(x,y,width,height);
    }

    public void draw(ShapeRenderer shapeRenderer)
    {
        shapeRenderer.setColor(this.color);
        shapeRenderer.rect(rectangle.x,rectangle.y,rectangle.width,rectangle.height);
    }

    public void update(float x, Enemy enemy,Enemy animal)
    {
        rectangle.x = x;
        if(rectangle.overlaps(enemy.getRectangle()) || rectangle.overlaps(animal.getRectangle()))
            color = Color.RED;
        else
            color = Color.GREEN;
    }


    public Rectangle getRectangle() {
        return rectangle;
    }

    public Color getColor() {
        return color;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
