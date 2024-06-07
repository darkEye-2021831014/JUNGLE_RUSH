package com.junglerush;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import java.math.BigInteger;
import java.util.Iterator;
import java.util.TreeSet;

public class Player {
    private BigInteger score;
    private int scoreFactor;
    private final Texture texture;
    private final Rectangle rectangle;
    private int speed;
    private final Score text;
    private TreeSet<BigInteger> scoreTree;
    private boolean leftMove,rightMove,upMove,downMove;

    public Player(Score text, int speed, int scoreFactor, String filePath) {
        this.text = text;
        this.speed = speed;
        this.scoreFactor = scoreFactor;
        this.rectangle = new Rectangle();
        this.texture = new Texture(filePath);
        scoreTree =new TreeSet<>();
        setScore(this.scoreFactor);
    }

    public void spawnPlayer(float x, float y, float width, float height)
    {
        this.rectangle.x = x;
        this.rectangle.y = y;
        this.rectangle.width = width;
        this.rectangle.height = height;

        text.setRectangle(this.rectangle);
        setScore(this.scoreFactor);
    }

    public Texture getTexture()
    {
        return texture;
    }

    public void draw(SpriteBatch batch,int screenWidth,int screenHeight,Background background) {
        batch.draw(texture, rectangle.x, rectangle.y,rectangle.width, rectangle.height);
        text.setRectangle(this.rectangle);
        text.draw(batch,this.score.toString());

        //draw score tree
        text.setColor(new Color(text.getOpacity(), 0, 0, text.getOpacity()));

        Iterator<BigInteger> descendingIterator = scoreTree.descendingIterator();
        int scoreHeight = 25;
        Rectangle scoreRect = new Rectangle(screenWidth-background.getRiverRect().width,
                screenHeight-2*scoreHeight,background.getRiverRect().width,scoreHeight);
        text.setRectangle(scoreRect);
        text.draw(batch,"Score\nTree");
        scoreRect.y-=2*scoreHeight;
        while (descendingIterator.hasNext())
        {
            String curScore = descendingIterator.next().toString();
            text.setRectangle(scoreRect);
            text.draw(batch,curScore);
            scoreRect.y-=scoreHeight;
            if(scoreRect.y <=0)break;
        }
    }

    public void update(Enemy enemyCar,Background background) {
        Gdx.input.setInputProcessor(new ProcessInput(this));
        if(this.leftMove) this.rectangle.x -= this.speed;
        if(this.rightMove) this.rectangle.x += this.speed;

        //handle road movement
        if(this.upMove) {
            this.speed =10;
            enemyCar.setSpeed(4);
        }
        else if(this.downMove) {
            this.speed = 3;
            enemyCar.setSpeed(0);
        }
        else {
            this.speed = 5;
            enemyCar.setSpeed(2);
        }

        //avoid gap in the road;
        if((background.getRoadRect().get(0).y+background.getRoadRect().get(0).height) != background.getRoadRect().get(1).y
                && (background.getRoadRect().get(1).y+background.getRoadRect().get(1).height) != background.getRoadRect().get(0).y)
        {
            if(background.getRoadRect().get(0).y <= 0)
                background.getRoadRect().get(1).y = background.getRoadRect().get(0).y+background.getRoadRect().get(0).height;
            else if(background.getRoadRect().get(1).y <= 0)
                background.getRoadRect().get(0).y = background.getRoadRect().get(1).y+background.getRoadRect().get(1).height;
        }
        //set rectangle to draw score
        text.setRectangle(this.rectangle);
    }

    public void setTextColor(Color color){
        text.setColor(color);
    }

    private int setBits(long value)
    {
        int sz = 0;
        while (value != 0L)
        {
            sz++;
            value >>= 1L;
        }
        if(sz!=0)sz--;
        return sz;
    }

    public void setScore(int factor)
    {
        BigInteger curScore = BigInteger.valueOf(1L << factor);
        while(this.scoreTree.contains(curScore))
        {
            this.scoreTree.remove(curScore);
            curScore = curScore.add(curScore);
        }
        this.scoreTree.add(curScore);
        this.score = this.scoreTree.last();

        System.out.println(this.score);
        //set score factor
        BigInteger highest = BigInteger.valueOf((1L << 63)-1L);
        int result = this.score.compareTo(highest); // result = - 1 ( str1 is less than str 2), 0 ( both str are equal),
        // 1 (st1 is greater than str2)
        if(result <= 0)
        {
            long playerScore = this.score.longValue();
            this.scoreFactor = setBits(playerScore);
        }
        else this.scoreFactor = 62; // maximum allowed
    }


    public void setLeftMove(boolean leftMove) {
        this.leftMove = leftMove;
    }

    public void setRightMove(boolean rightMove) {
        this.rightMove = rightMove;
    }

    public void setUpMove(boolean upMove) {
        this.upMove = upMove;
    }

    public void setDownMove(boolean downMove) {
        this.downMove = downMove;
    }

    public BigInteger getScore() {
        return score;
    }

    public int getScoreFactor() {
        return scoreFactor;
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public int getSpeed() {
        return speed;
    }

    public Score getText() {
        return text;
    }

    public TreeSet<BigInteger> getScoreTree() {
        return scoreTree;
    }
}
