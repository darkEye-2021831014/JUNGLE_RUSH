package com.junglerush;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import org.w3c.dom.css.Rect;

import java.math.BigInteger;
import java.util.Iterator;
import java.util.TreeSet;

public class Player {
    private BigInteger score,maxScore;
    private int scoreFactor;
    private final Texture texture;
    private final Rectangle rectangle;
    private int speed;
    private final Score text;
    private TreeSet<BigInteger> scoreTree;
    private boolean leftMove,rightMove,upMove,downMove;
    private long maxAllowed;

    public Player(Score text, int speed, int scoreFactor, String filePath,long maxAllowed) {
        this.text = text;
        this.speed = speed;
        this.scoreFactor = scoreFactor;
        this.rectangle = new Rectangle();
        this.texture = new Texture(filePath);
        this.maxAllowed = maxAllowed;
        scoreTree =new TreeSet<>();
        this.score = Enemy.power2(this.scoreFactor);
        setScore(this.score,false);
    }

    public void spawnPlayer(float x, float y, float width, float height)
    {
        this.rectangle.x = x;
        this.rectangle.y = y;
        this.rectangle.width = width;
        this.rectangle.height = height;

        text.setRectangle(this.rectangle);
    }

    public Texture getTexture()
    {
        return texture;
    }

    public void draw(SpriteBatch batch,int screenWidth,int screenHeight,Background background) {
        batch.draw(texture, rectangle.x, rectangle.y,rectangle.width, rectangle.height);
        text.setRectangle(this.rectangle);
        BigInteger maxValue = BigInteger.valueOf(maxAllowed);
        String curScore = this.score.toString();
        //transform to have suffix
        if(this.score.compareTo(maxValue) >= 0)
            curScore = NumberFormatter.formatBigInteger(this.score);
        text.draw(batch,curScore);

        //draw score tree
        int scoreHeight = 25;
        Rectangle scoreRect = new Rectangle(screenWidth-background.getRiverRect().width,
                screenHeight-2*scoreHeight,background.getRiverRect().width,scoreHeight);
        drawScoreTree(batch,scoreRect,Color.BLACK);
    }

    public void drawScoreTree(SpriteBatch batch,Rectangle scoreRect,
                              Color color) {
        BigInteger maxValue = BigInteger.valueOf(maxAllowed);
        int scoreHeight = 25;
        text.setColor(color);
        Iterator<BigInteger> descendingIterator = scoreTree.descendingIterator();

        text.setRectangle(scoreRect);
        text.draw(batch,"Score\nTree");
        scoreRect.y -= scoreHeight;
        text.draw(batch,"\n----------");
        scoreRect.y-=scoreHeight;
        while (descendingIterator.hasNext())
        {
            BigInteger curValue = descendingIterator.next();
            String curScore = curValue.toString();
            if(curValue.compareTo(maxValue) >= 0)
                curScore = NumberFormatter.formatBigInteger(curValue);
            text.setRectangle(scoreRect);
            text.draw(batch,curScore);
            scoreRect.y-=scoreHeight;
            if(scoreRect.y <= 0) break;
        }
    }

    public void update(Enemy enemyCar,Enemy enemyAnimal,Background background) {
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
            enemyAnimal.setSpeed(4);
        }
        else {
            this.speed = 5;
            enemyCar.setSpeed(2);
            enemyAnimal.setSpeed(3);
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

    public static int setBits(BigInteger value)
    {
        int sz = 0;
        while (value.compareTo(BigInteger.valueOf(0L)) > 0)
        {
            sz++;
            value = value.divide(BigInteger.valueOf(2L));
        }
        if(sz!=0)sz--;
        return sz;
    }

    public void setScore(BigInteger curScore,boolean isDivide)
    {
        //always take the power 2 value
        curScore = Enemy.power2(setBits(curScore));
        if(isDivide) {
            assert this.score != null;
            curScore = this.score.divide(curScore);
            scoreTree.remove(this.score);
        }

        while(this.scoreTree.contains(curScore))
        {
            this.scoreTree.remove(curScore);
            curScore = curScore.add(curScore);
        }

        if(curScore.compareTo(BigInteger.valueOf(0)) > 0)
            this.scoreTree.add(curScore);
        if(this.scoreTree.isEmpty())
            this.score = BigInteger.valueOf(0);
        else
            this.score = this.scoreTree.last();
        //set maximum score achieved
        setMaxScore();

        //set score factor
        assert this.score != null;
        this.scoreFactor = setBits(this.score);
    }

    private void setMaxScore() {
        if(maxScore == null)
            maxScore = this.score;
        else
            maxScore = maxScore.max(score);
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

    public BigInteger getMaxScore() {
        return maxScore;
    }
}
