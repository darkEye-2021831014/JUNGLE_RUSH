package com.junglerush;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;


public class Background {
    private final Array<Texture> texture;
    private final Array<Rectangle> rectangles,roadRect,boarderRect;
    private Rectangle riverRect;
    public Background() {
        texture = new Array<>();
        rectangles = new Array<>();
        roadRect =new Array<>();
        boarderRect = new Array<>();
    }

    public void addTexture(Texture texture) {
        this.texture.add(texture);
    }


    public void addRectangle(Rectangle rectangle) {
        this.rectangles.add(rectangle);
    }
    public void addRoadRectangle(Rectangle rectangle) {
        this.roadRect.add(rectangle);
    }
    public void addBoarderRectangle(Rectangle rectangle) {
        this.boarderRect.add(rectangle);
    }
    public void addRiverRectangle(Rectangle rectangle) {
        this.riverRect = rectangle;
    }


    public void drawBackground(Batch batch) {
        for(int i = 0; i < 2; i++)
            batch.draw(texture.get(0), this.rectangles.get(i).x,this.rectangles.get(i).y,this.rectangles.get(i).width,
                    this.rectangles.get(i).height);

        batch.setBlendFunction(GL20.GL_DST_COLOR, GL20.GL_ZERO);
        for(int i = 2; i < this.rectangles.size; i++)
            batch.draw(texture.get(1), this.rectangles.get(i).x,this.rectangles.get(i).y,this.rectangles.get(i).width,
                    this.rectangles.get(i).height);

        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
        //draw river
        batch.draw(texture.get(2),riverRect.x,riverRect.y,riverRect.width,riverRect.height);

        //draw boarder
        for(Rectangle rect:boarderRect)
            batch.draw(texture.get(2),rect.x,rect.y,rect.width,rect.height);

        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        //draw road
        for(Rectangle rect:roadRect)
            batch.draw(texture.get(3),rect.x,rect.y,rect.width,rect.height);
    }


    public void update(int screenWidth, int screenHeight,int speed)
    {
        for(int i=0;i < this.rectangles.size/2;i++)
        {
            this.rectangles.get(i).y--;
            if(this.rectangles.get(i).y <= -screenHeight) this.rectangles.get(i).y = screenHeight;
        }
        for(int i=this.rectangles.size/2;i < this.rectangles.size;i++)
        {
            this.rectangles.get(i).x++;
            if(this.rectangles.get(i).x >= screenWidth) this.rectangles.get(i).x = -screenWidth;
        }

        //update roads
    }

    public void updateRoads(int screenWidth, int screenHeight,int speed)
    {
        for(int i=0; i < this.roadRect.size;i++)
        {
            this.roadRect.get(i).y -= speed;
            if(this.roadRect.get(i).y <= -screenHeight) this.roadRect.get(i).y = screenHeight;
        }
    }


    //getters & setters

    public Array<Texture> getTexture() {
        return texture;
    }

    public Array<Rectangle> getRectangles() {
        return rectangles;
    }

    public Array<Rectangle> getRoadRect() {
        return roadRect;
    }

    public Array<Rectangle> getBoarderRect() {
        return boarderRect;
    }

    public Rectangle getRiverRect() {
        return riverRect;
    }

    public  void cleanUp()
    {
        for(Texture texture:texture)
            texture.dispose();
    }
}
