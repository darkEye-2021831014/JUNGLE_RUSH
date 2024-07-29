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
    private float fontScale;

    public Score(String filePath, int fontSize,float fontScale){
        this.filePath =filePath;
        this.fontSize = fontSize;
        this.fontScale = fontScale;
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
        setFontScale(this.fontScale);
        generator.dispose();
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
        setFontScale(this.fontScale) ;
        GlyphLayout g1 = new GlyphLayout();
        g1.setText(font,text);
        if((g1.width+g1.width/4) > rectangle.width)
        {
            float scale = rectangle.width/(g1.width+g1.width/4);
            font.getData().setScale(scale);
            g1.setText(font,text);
        }
        float textX = rectangle.x+ ( rectangle.width- g1.width)/2f;
        float textY = rectangle.y + (rectangle.height+g1.height)/2f;
        font.draw(batch,g1,textX,textY);
    }

    public void draw(Batch batch,String text,boolean isMid){
        setFontScale(this.fontScale) ;
        GlyphLayout g1 = new GlyphLayout();
        g1.setText(font,text);
        if(g1.width > rectangle.width)
        {
            float scale = rectangle.width/g1.width;
            font.getData().setScale(scale);
            g1.setText(font,text);
        }
        float textX = rectangle.x;
        float textY = rectangle.y;
        font.draw(batch,text,textX,textY);
    }

    public void drawXY(Batch batch,String text,int width,int height){
        setFontScale(this.fontScale) ;
        GlyphLayout g1 = new GlyphLayout();
        g1.setText(font,text);
        float textX = width - g1.width/2f;
        float textY = height + g1.height/2f;
        font.draw(batch,g1,textX,textY);
    }

    public void setColor(Color color){
        font.setColor(color);
    }

    public void setRectangle(Rectangle rectangle){
        this.rectangle = rectangle;
    }

    public void setFontScale(float fontScale){
        this.fontScale = fontScale;
        font.getData().setScale(this.fontScale);
    }

    public float getOpacity() {
        return opacity;
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public String getFilePath() {
        return filePath;
    }

    public float getFontScale() {
        return fontScale;
    }

    public void cleanUp()
    {
        font.dispose();
    }
}
