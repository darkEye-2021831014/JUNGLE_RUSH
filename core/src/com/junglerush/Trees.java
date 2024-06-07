package com.junglerush;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class Trees {
    private Array<Rectangle> rectanglesLeft,rectanglesRight;
    private Array<Texture> textures;
    private Array<Integer> treeIndexLeft,treeIndexRight;
    public Trees() {
        rectanglesLeft = new Array<>();
        rectanglesRight = new Array<>();
        textures = new Array<>();
        treeIndexLeft = new Array<>();
        treeIndexRight = new Array<>();
    }

    public void addTexture(Texture texture) {
        textures.add(texture);
    }

    public void addRectangleLeft(Rectangle rectangle) {
        rectanglesLeft.add(rectangle);
    }
    public void addRectangleRight(Rectangle rectangle) {
        rectanglesRight.add(rectangle);
    }

    public void addTreeIndexLeft(int treeIndex) {
        this.treeIndexLeft.add(treeIndex);
    }

    public void addTreeIndexRight(int treeIndex) {
        this.treeIndexRight.add(treeIndex);
    }

    public void draw(SpriteBatch batch)
    {
        for (int i = 0; i < rectanglesLeft.size; i++) {
            batch.draw(textures.get(treeIndexLeft.get(i)), rectanglesLeft.get(i).x, rectanglesLeft.get(i).y,rectanglesLeft.get(i).width,rectanglesLeft.get(i).height);
            batch.draw(textures.get(treeIndexRight.get(i)), rectanglesRight.get(i).x, rectanglesRight.get(i).y,rectanglesRight.get(i).width,rectanglesRight.get(i).height);
        }
    }

    public void update(int speed,int treeHeight)
    {
        for (int i = 0; i < rectanglesLeft.size; i++) {
            rectanglesLeft.get(i).y -= speed;
            if(rectanglesLeft.get(i).y <= -treeHeight)
            {
                rectanglesLeft.get(i).y = 15 * treeHeight;
                treeIndexLeft.set(i,MathUtils.random(0,textures.size-1));
            }

            rectanglesRight.get(i).y -= speed;
            if(rectanglesRight.get(i).y <= -treeHeight)
            {
                rectanglesRight.get(i).y = 15*treeHeight;
                treeIndexRight.set(i,MathUtils.random(0,textures.size-1));
            }
        }
    }





    //getters & setters


    public Array<Rectangle> getRectanglesLeft() {
        return rectanglesLeft;
    }

    public Array<Rectangle> getRectanglesRight() {
        return rectanglesRight;
    }

    public Array<Texture> getTextures() {
        return textures;
    }

    public Array<Integer> getTreeIndexLeft() {
        return treeIndexLeft;
    }

    public Array<Integer> getTreeIndexRight() {
        return treeIndexRight;
    }
}
