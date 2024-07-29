package com.junglerush;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;


public class Trees {
    private Array<Rectangle> rectangles;
    private Array<Texture> textures;
    private Array<Integer> treeIndex;
    private int treeHeight;
    public Trees(int treeHeight) {
        this.treeHeight = treeHeight;
        textures = new Array<>();
        rectangles = new Array<>();
        treeIndex = new Array<>();
    }

    public void addTexture(Texture texture) {
        textures.add(texture);
    }

    public void draw(SpriteBatch batch)
    {
        int i = 0;
        for (Rectangle rect:rectangles) {
            batch.draw(textures.get(treeIndex.get(i++)), rect.x, rect.y,rect.width,rect.height);
        }
    }

    public void addTreeIndex(int i)
    {
        treeIndex.add(i);
    }

    public void addRectangle(Rectangle rectangle) {
        this.rectangles.add(rectangle);
    }

    public void update(int speed,int jungleWidth,int treeWidth,float x)
    {
        for (int i=0;i<rectangles.size;i++) {
            rectangles.get(i).y -= speed;
            if(rectangles.get(i).y <= -treeHeight)
            {
                rectangles.get(i).y = 16 * treeHeight;
                rectangles.get(i).x = x+ MathUtils.random(0,jungleWidth-treeWidth);
                treeIndex.set(i,MathUtils.random(0,textures.size-1));
            }
        }
    }


    //getters & setters

    public Array<Rectangle> getRectangles() {
        return rectangles;
    }

    public Array<Texture> getTextures() {
        return textures;
    }

    public Array<Integer> getTreeIndex() {
        return treeIndex;
    }

    public int getTreeHeight() {
        return treeHeight;
    }

    public void cleanUp()
    {
        for(Texture texture:textures)
            texture.dispose();
    }
}
