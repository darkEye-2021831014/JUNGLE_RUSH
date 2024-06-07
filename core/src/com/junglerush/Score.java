package com.junglerush;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Rectangle;

public class Score {
    private float opacity;
    private boolean increase;
    private BitmapFont font;
    private final int fontSize;
    private Rectangle rectangle;
    private final String filePath;

    public Score(String filePath, int fontSize){
        this.filePath =filePath;
        this.fontSize = fontSize;
        font = new BitmapFont();
        rectangle = new Rectangle();
        increase = true;
        setOpacity(0.3f);//default value
        generateFont();
    }

    private void generateFont()
    {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(this.filePath));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = this.fontSize;
        font = generator.generateFont(parameter);
    }

    public void setOpacity(float opacity) {
        this.opacity = opacity;
    }

    public void blinkingEffect(float increaseBy, float decreaseBy, float lowerLimit){
        if(increase)
            this.opacity += increaseBy;
        else
            this.opacity -= decreaseBy;
        if(this.opacity >= 1) increase = false;
        else if(this.opacity <= lowerLimit) increase = true;
    }

    public void draw(Batch batch,String text){
        GlyphLayout g1 = new GlyphLayout(font, text);
        float textX = rectangle.x+ ( rectangle.width- g1.width)/2f;
        float textY = rectangle.y + (rectangle.height+g1.height)/2f;
        font.draw(batch,g1,textX,textY);
    }

    public void setColor(Color color){
        font.setColor(color);
    }

    public void setRectangle(Rectangle rectangle){
        this.rectangle = rectangle;
    }

    public float getOpacity() {
        return opacity;
    }
}
