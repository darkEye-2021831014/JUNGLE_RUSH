package com.junglerush;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import org.w3c.dom.css.Rect;

import java.awt.*;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.TreeSet;

public class GameScreen implements Screen {

    private final JungleRush game;
    private final MainMenuScreen menu;
    private Texture tRoad,tRiver,tMainCharacter;
    private Array<Texture> trees,backgrounds,animals,enemies;
    private Array<Rectangle> roadRect, treeRectLeft, treeRectRight,backgroundRect,roadBoarder,animalRect;
    private Rectangle riverRect,mcRect,enemyRect,currentAnimalRect;

    private final int TOTAL_TILES = 20,treeRandomFactor = 6;
    private int ELEMENT_WIDTH,ELEMENT_HEIGHT,JUNGLE_FACTOR,ROAD_FACTOR,RIVER_FACTOR,JUNGLE_WIDTH,ROAD_WIDTH,BOARDER_WIDTH;
    private int SPEED = 5,animalIndex,enemyIndex,animalSpeed=3,enemySpeed=2,mcScoreFactor,enemyScoreFactor,animalScoreFactor,isPositiveAnimal;
    private long enemyScore,animalScore;
    private BigInteger mcScore;
    private boolean leftMove = false,rightMove = false,upMove = false,downMove = false,GAME_OVER = false;
    private TreeSet<BigInteger> scoreTree;

    public GameScreen(final JungleRush game,final MainMenuScreen menu)
    {
        this.game = game;
        this.menu = menu;
        tRoad = new Texture("Background/Road.jpeg");

        initializeBackground();


    }

    private void initializeBackground() {
        initializeVariables();
        //load all available trees
        loadTrees();
        loadBackground();
        loadAnimals();
        loadEnemies();
        loadCars();
        initializeRectangles();


    }

    private void loadEnemies() {
        enemies = new Array<>();
        for(int i=1;i<=14;i++)
            enemies.add(new Texture("Enemy/NonLiving/enemyCar"+i+".png"));

        //set random enemy texture selector
        this.enemyIndex =  MathUtils.random(0,enemies.size-1);
    }

    private void loadCars() {
        tMainCharacter = new Texture("Background/mainCar.png");
    }

    private void loadAnimals() {
        animals = new Array<>();
        for(int i=1;i<=22;i++)
            animals.add(new Texture("Enemy/Living/animal"+i+".png"));

        //set random texture selector
        this.animalIndex =  MathUtils.random(0,animals.size-1);
    }


    @Override
    public void render(float v) {
        ScreenUtils.clear(0, 0, 0.3f, 1);

        game.batch.begin();
        drawBackground();
        drawTrees();
        drawAnimals();
        drawEnemies();
        drawMainCharacter();
        drawScoreTree();
        game.batch.end();

        update();
    }

    private void drawScoreTree() {
        Iterator<BigInteger> descendingIterator = scoreTree.descendingIterator();
        int scoreHeight = 25;
        Rectangle scoreRect = new Rectangle(game.SCREEN_WIDTH-this.riverRect.width,game.SCREEN_HEIGHT-2*scoreHeight,this.riverRect.width,scoreHeight);
        drawScore(scoreRect,game.fontBold,"Score\nTree");
        scoreRect.y-=2*scoreHeight;
        while (descendingIterator.hasNext())
        {
            String curScore = descendingIterator.next().toString();
            drawScore(scoreRect,game.fontRegular,curScore);
            scoreRect.y-=scoreHeight;
            if(scoreRect.y <=0)break;
        }

    }

    private void drawEnemies() {
        game.batch.draw(enemies.get(this.enemyIndex),this.enemyRect.x,this.enemyRect.y);
        drawScore(this.enemyRect,game.fontRegular,Long.toString(this.enemyScore));
    }

    private void drawMainCharacter() {
        game.batch.draw(this.tMainCharacter,mcRect.x,mcRect.y,mcRect.width,mcRect.height);

        drawScore(this.mcRect,game.fontRegular,mcScore.toString());
    }

    private void drawScore(Rectangle rect, BitmapFont font,String value) {
        menu.blinkingEffect(0.3f,font,0.02f,0.01f);

        GlyphLayout g1 = new GlyphLayout(font, value);
        float textX = rect.x+ ( rect.width- g1.width)/2f;
        float textY = rect.y + (rect.height+g1.height)/2f;
        font.draw(game.batch,g1,textX,textY);
    }

    private void drawAnimals() {
        checkDirection(this.animalIndex);
        game.batch.draw(animals.get(this.animalIndex),currentAnimalRect.x,currentAnimalRect.y);

        drawScore(this.currentAnimalRect,game.fontRegular, Long.toString(this.animalScore));
    }

    private void update()
    {
        Gdx.input.setInputProcessor(new ProcessInput(this));
        //update main characters position based on input
        updateMainCharacter();
        //update animals
        updateAnimals();
        //update Enemies
        updateEnemies();
        //simulate road movement
        updateRoad();
        //simulate random tree spawn and tree movement
        updateTrees();
        //simulate background image movement
        updateBackground();
        //detect collision
        detectCollision();



    }

    private void detectCollision() {
        this.mcScoreFactor = calculateMcScoreFactor();

        //detect collision with road boarder
        for(Rectangle rect: roadBoarder)
            if(this.mcRect.overlaps(rect))
            {
                this.GAME_OVER = true;
                return;
            }

        //detect collision with enemies
        if(this.mcRect.overlaps(this.enemyRect))
        {
            if(this.enemyScoreFactor > this.mcScoreFactor)
            {
                this.GAME_OVER = true;
                return;
            }
            else
            {
                //mc score will increase
                setMainCharacterScore(this.enemyScoreFactor);
                spawnNewEnemy();
            }
        }

    }

    private void spawnNewEnemy() {
        this.enemyIndex =  MathUtils.random(0,enemies.size-1);
        enemyRect.x = this.JUNGLE_WIDTH + this.BOARDER_WIDTH + MathUtils.random(0,
                this.ROAD_WIDTH - this.enemies.get(this.enemyIndex).getWidth());
        enemyRect.y = 20 * this.ELEMENT_HEIGHT;
        enemyRect.width = this.enemies.get(this.enemyIndex).getWidth();
        enemyRect.height = this.enemies.get(this.enemyIndex).getHeight();
        //set enemy score
        setEnemyScore(Math.max(0,this.mcScoreFactor-3),Math.min(62,this.mcScoreFactor+5));
    }

    private int calculateMcScoreFactor() {
        String highest = Long.toString((1L << 63)-1L);
        int result = this.mcScore.toString().compareTo(highest); // result = - 1 ( str1 is less than str 2), 0 ( both str are equal),
        // 1 (st1 is greater than str2)
        if(result <= 0)
        {
            long curScore = this.mcScore.longValue();
            return (int)Math.floor(Math.log(curScore)/Math.log(2));
        }
        return 62;
    }

    private void updateEnemies() {
        enemyRect.y -= this.enemySpeed;
        if(enemyRect.y <= -enemies.get(this.enemyIndex).getHeight()) {
            spawnNewEnemy();
        }
    }

    private int checkDirection(int value)
    {
        int i = 0;
        if(value >= 8 && value < 15) i = 1;
        else if(value >= 15 && value < 22) i = 2;
        currentAnimalRect = this.animalRect.get(i);
        return i;
    }

    private void updateAnimals() {
        int i = 0;// i= 0(left to right), 1 ( right to left), 2 (static)
        i = checkDirection(this.animalIndex);

        animalRect.get(i).y -= this.SPEED;
        if(i==0) animalRect.get(i).x += this.animalSpeed;
        else if(i==1) animalRect.get(i).x -= this.animalSpeed;

        if(animalRect.get(i).y <= -animals.get(this.animalIndex).getHeight() ||
        animalRect.get(i).x <= (this.JUNGLE_WIDTH+this.BOARDER_WIDTH) ||
        animalRect.get(i).x >= (this.JUNGLE_WIDTH + this.BOARDER_WIDTH +this.ROAD_WIDTH - (this.animals.get(this.animalIndex).getWidth()))) {
            this.animalIndex =  MathUtils.random(0,animals.size-1);

            i = checkDirection(this.animalIndex);
            animalRect.get(i).y = MathUtils.random(18 * this.ELEMENT_HEIGHT, 19 * this.ELEMENT_HEIGHT);

            if(i==0)animalRect.get(i).x = this.JUNGLE_WIDTH + this.BOARDER_WIDTH;
            else if(i==1)animalRect.get(i).x = this.JUNGLE_WIDTH + this.BOARDER_WIDTH+this.ROAD_WIDTH -
                    (this.animals.get(this.animalIndex).getWidth());
            else animalRect.get(i).x = this.JUNGLE_WIDTH + this.BOARDER_WIDTH + MathUtils.random(0,
                        this.ROAD_WIDTH - (this.animals.get(this.animalIndex).getWidth()));
            //set new animal score
            setAnimalScore(Math.max(0,this.mcScoreFactor-5),Math.min(62,this.mcScoreFactor+2));
        }

        currentAnimalRect = animalRect.get(i);
    }

    private void setAnimalScore(int lowerLimit,int upperLimit) {
        this.isPositiveAnimal = (MathUtils.random(0,1)==0)?-1:1;
        this.animalScoreFactor = MathUtils.random(lowerLimit,upperLimit);
        this.animalScore = this.isPositiveAnimal * (1L << this.animalScoreFactor);
    }
    private void setEnemyScore(int lowerLimit,int upperLimit) {
        this.enemyScoreFactor = MathUtils.random(lowerLimit,upperLimit);
        this.enemyScore = 1L << this.enemyScoreFactor;
    }
    private void setMainCharacterScore(int value) {
        long curValue = 1L << value;
        while(this.scoreTree.contains(new BigInteger(Long.toString(curValue))))
        {
            this.scoreTree.remove(new BigInteger(Long.toString(curValue)));
            curValue += curValue;
        }
        this.scoreTree.add(new BigInteger(Long.toString(curValue)));

        this.mcScore = this.scoreTree.last();
    }

    private void updateMainCharacter() {
        if(this.leftMove) this.mcRect.x -= this.SPEED;
        if(this.rightMove) this.mcRect.x += this.SPEED;

        //handle road movement
        if(this.upMove) {
            this.SPEED =10;
            this.enemySpeed = 4;
        }
        else if(this.downMove) {
            this.SPEED = 3;
            this.enemySpeed = 0;
        }
        else {
            this.SPEED = 5;
            this.enemySpeed = 2;
        }

        //avoid gape in the road;
        if((roadRect.get(0).y+roadRect.get(0).height) != roadRect.get(1).y &&
                (roadRect.get(1).y+roadRect.get(1).height) != roadRect.get(0).y)
        {
            if(roadRect.get(0).y <= 0)
                roadRect.get(1).y = roadRect.get(0).y+roadRect.get(0).height;
            else if(roadRect.get(1).y <= 0)
                roadRect.get(0).y = roadRect.get(1).y+roadRect.get(1).height;
        }
    }





    //all draw methods implementation
    private void drawTrees() {
        for (int i = 0; i < trees.size; i++)
            game.batch.draw(trees.get(i),this.treeRectLeft.get(i).x,this.treeRectLeft.get(i).y,this.treeRectLeft.get(i).width,this.treeRectLeft.get(i).height);
        for (int i = 0; i < trees.size; i++)
            game.batch.draw(trees.get(i),this.treeRectRight.get(i).x,this.treeRectRight.get(i).y,this.treeRectRight.get(i).width,this.treeRectRight.get(i).height);
    }

    private void drawBackground() {
        for(int i = 0; i < 2; i++)
            game.batch.draw(backgrounds.get(0), this.backgroundRect.get(i).x,this.backgroundRect.get(i).y,this.backgroundRect.get(i).width,this.backgroundRect.get(i).height);

        game.batch.setBlendFunction(GL20.GL_DST_COLOR, GL20.GL_ZERO);
        for(int i = 2; i < this.backgroundRect.size; i++)
            game.batch.draw(backgrounds.get(1), this.backgroundRect.get(i).x,this.backgroundRect.get(i).y,this.backgroundRect.get(i).width,this.backgroundRect.get(i).height);

        game.batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
        //draw river
        game.batch.draw(tRiver,riverRect.x,riverRect.y,riverRect.width,riverRect.height);

        //draw boarder
        for(Rectangle rect:roadBoarder)
            game.batch.draw(tRiver,rect.x,rect.y,rect.width,rect.height);

        game.batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        //draw road
        for(Rectangle rect:roadRect)
            game.batch.draw(tRoad,rect.x,rect.y,rect.width,rect.height);
    }



    //all update methods implementation
    private void updateRoad() {
        for(int i=0; i < this.roadRect.size;i++)
        {
            this.roadRect.get(i).y -= this.SPEED;
            if(this.roadRect.get(i).y <= -game.SCREEN_HEIGHT) this.roadRect.get(i).y = game.SCREEN_HEIGHT;
        }
    }

    private void updateBackground() {
        for(int i=0;i < this.backgroundRect.size/2;i++)
        {
            this.backgroundRect.get(i).y--;
            if(this.backgroundRect.get(i).y <= -game.SCREEN_HEIGHT) this.backgroundRect.get(i).y = game.SCREEN_HEIGHT;
        }
        for(int i=this.backgroundRect.size/2;i < this.backgroundRect.size;i++)
        {
            this.backgroundRect.get(i).x++;
            if(this.backgroundRect.get(i).x >= game.SCREEN_WIDTH) this.backgroundRect.get(i).x = -game.SCREEN_WIDTH;
        }
    }

    private void updateTrees() {
        for (int i = 0; i < this.treeRectLeft.size; i++)
        {
            this.treeRectLeft.get(i).y -= this.SPEED;
            this.treeRectRight.get(i).y -= this.SPEED;
            if(this.treeRectLeft.get(i).y <= -this.treeRectLeft.get(i).getHeight())
            {
                this.treeRectLeft.get(i).y = MathUtils.random(0,game.SCREEN_HEIGHT+this.treeRandomFactor*this.treeRectLeft.get(i).height);
                this.treeRectLeft.get(i).x = MathUtils.random(0, this.JUNGLE_WIDTH -trees.get(i).getWidth());
            }
            if(this.treeRectRight.get(i).y <= -this.treeRectRight.get(i).getHeight())
            {
                this.treeRectRight.get(i).y = MathUtils.random(0,game.SCREEN_HEIGHT+this.treeRandomFactor*this.treeRectLeft.get(i).height);
                this.treeRectRight.get(i).x = this.JUNGLE_WIDTH + this.ROAD_WIDTH + 2*this.BOARDER_WIDTH+ MathUtils.random(0, this.JUNGLE_WIDTH - trees.get(i).getWidth());
            }
        }
    }


    @Override
    public void show() {

    }

    @Override
    public void resize(int i, int i1) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        for(Texture t : trees)
            t.dispose();
    }








    //initialized methods
    private void initializeVariables() {
        this.ELEMENT_WIDTH = game.SCREEN_WIDTH/this.TOTAL_TILES;
        this.ELEMENT_HEIGHT = game.SCREEN_HEIGHT/this.TOTAL_TILES;
        this.JUNGLE_FACTOR = 6;
        this.ROAD_FACTOR = this.JUNGLE_FACTOR;
        this.RIVER_FACTOR = 1;
        this.BOARDER_WIDTH = (int)(0.5 * this.ELEMENT_WIDTH);
        this.JUNGLE_WIDTH = this.JUNGLE_FACTOR * this.ELEMENT_WIDTH;
        this.ROAD_WIDTH = this.JUNGLE_WIDTH;

        //
        scoreTree = new TreeSet<>();
        this.mcScore = new BigInteger("0");
        setMainCharacterScore(0);
        setEnemyScore(Math.max(0,this.mcScoreFactor-3),Math.min(62,this.mcScoreFactor+5));
        setAnimalScore(Math.max(0,this.mcScoreFactor-5),Math.min(62,this.mcScoreFactor+2));
    }

    private void initializeRectangles() {
        //road control rectangle
        roadRect = new Array<>();
        roadRect.add(new Rectangle(this.JUNGLE_WIDTH+this.BOARDER_WIDTH, 0, this.ROAD_WIDTH, game.SCREEN_HEIGHT));
        roadRect.add(new Rectangle(this.JUNGLE_WIDTH+this.BOARDER_WIDTH, game.SCREEN_HEIGHT, this.ROAD_WIDTH, game.SCREEN_HEIGHT));

        //tree control rectangle initialization
        treeRectLeft = new Array<>();
        treeRectRight = new Array<>();
        for (int i = 0; i < trees.size; i++)
        {
            treeRectLeft.add(new Rectangle(MathUtils.random(0, this.JUNGLE_WIDTH -trees.get(i).getWidth()), MathUtils.random(0, game.SCREEN_HEIGHT + this.treeRandomFactor * this.trees.get(i).getHeight()), trees.get(i).getWidth(), trees.get(i).getHeight()));
            treeRectRight.add(new Rectangle(this.JUNGLE_WIDTH + this.ROAD_WIDTH + 2*this.BOARDER_WIDTH+ MathUtils.random(0, this.JUNGLE_WIDTH - trees.get(i).getWidth()), MathUtils.random(0, game.SCREEN_HEIGHT + this.treeRandomFactor * this.trees.get(i).getHeight()), trees.get(i).getWidth(), trees.get(i).getHeight()));
        }

        //background control rectangle initialization
        backgroundRect = new Array<>();
        backgroundRect.add(new Rectangle(0,0,game.SCREEN_WIDTH,game.SCREEN_HEIGHT));
        backgroundRect.add(new Rectangle(0,game.SCREEN_HEIGHT,game.SCREEN_WIDTH,game.SCREEN_HEIGHT));
        backgroundRect.add(new Rectangle(0,0,game.SCREEN_WIDTH,game.SCREEN_HEIGHT));
        backgroundRect.add(new Rectangle(-game.SCREEN_WIDTH,0,game.SCREEN_WIDTH,game.SCREEN_HEIGHT));

        //river control rectangle
        riverRect = new Rectangle(this.JUNGLE_WIDTH*2+this.ROAD_WIDTH+this.BOARDER_WIDTH*2,0,this.ELEMENT_WIDTH*this.RIVER_FACTOR,game.SCREEN_HEIGHT);

        //road boarder control rectangle
        roadBoarder = new Array<>();
        roadBoarder.add(new Rectangle(this.JUNGLE_WIDTH,0,this.BOARDER_WIDTH,game.SCREEN_HEIGHT));
        roadBoarder.add(new Rectangle(this.JUNGLE_WIDTH+this.BOARDER_WIDTH+this.ROAD_WIDTH,0,this.BOARDER_WIDTH,game.SCREEN_HEIGHT));

        //enemy control rectangle
        enemyRect = new Rectangle(this.JUNGLE_WIDTH+this.BOARDER_WIDTH + MathUtils.random(0,
                this.ROAD_WIDTH-this.enemies.get(this.enemyIndex).getWidth()), 20*this.ELEMENT_HEIGHT,
                this.enemies.get(this.enemyIndex).getWidth(),this.enemies.get(this.enemyIndex).getHeight() );

        //main character control rectangle
        mcRect = new Rectangle(this.JUNGLE_WIDTH+this.BOARDER_WIDTH+(this.ROAD_WIDTH-tMainCharacter.getWidth())/2,
                0,this.tMainCharacter.getWidth(),tMainCharacter.getHeight());

        //Animal control Rectangle
        this.animalRect = new Array<>();
        //animal control for left to right animal index = 0
        this.animalRect.add(new Rectangle(this.JUNGLE_WIDTH+this.BOARDER_WIDTH,MathUtils.random(18*this.ELEMENT_HEIGHT,19*this.ELEMENT_HEIGHT), this.animals.get(this.animalIndex).getWidth(),
                this.animals.get(this.animalIndex).getHeight()));
        //animal control for right to left animal index = 1
        this.animalRect.add(new Rectangle(
                this.JUNGLE_WIDTH+this.BOARDER_WIDTH+ this.ROAD_WIDTH-this.animals.get(this.animalIndex).getWidth(),
                MathUtils.random(18*this.ELEMENT_HEIGHT,19*this.ELEMENT_HEIGHT),
                this.animals.get(this.animalIndex).getWidth(),
                this.animals.get(this.animalIndex).getHeight()));
        //animal control for static animal index = 2
        this.animalRect.add(new Rectangle(this.JUNGLE_WIDTH+this.BOARDER_WIDTH+MathUtils.random(0,
                this.ROAD_WIDTH-this.animals.get(this.animalIndex).getWidth()), MathUtils.random(18*this.ELEMENT_HEIGHT,
                19*this.ELEMENT_HEIGHT), this.animals.get(this.animalIndex).getWidth(),
                this.animals.get(this.animalIndex).getHeight()));
    }

    private void loadTrees() {
        trees = new Array<>();

        trees.add(new Texture("Trees/Luminous_tree1.png"));
        trees.add(new Texture("Trees/Luminous_tree2.png"));
        trees.add(new Texture("Trees/Luminous_tree3.png"));
        trees.add(new Texture("Trees/Luminous_tree4.png"));

        trees.add(new Texture("Trees/Mega_tree1.png"));
        trees.add(new Texture("Trees/Mega_tree2.png"));

        trees.add(new Texture("Trees/Swirling_tree1.png"));
        trees.add(new Texture("Trees/Swirling_tree2.png"));
        trees.add(new Texture("Trees/Swirling_tree3.png"));

        trees.add(new Texture("Trees/Willow1.png"));
        trees.add(new Texture("Trees/Willow2.png"));
        trees.add(new Texture("Trees/Willow3.png"));

        trees.add(new Texture("Trees/Curved_tree1.png"));
        trees.add(new Texture("Trees/Curved_tree2.png"));
        trees.add(new Texture("Trees/Curved_tree3.png"));

        trees.add(new Texture("Trees/Blue-green_balls_tree1.png"));
        trees.add(new Texture("Trees/Blue-green_balls_tree2.png"));
        trees.add(new Texture("Trees/Blue-green_balls_tree3.png"));

        trees.add(new Texture("Trees/Beige_green_mushroom1.png"));
        trees.add(new Texture("Trees/Beige_green_mushroom2.png"));
        trees.add(new Texture("Trees/Beige_green_mushroom3.png"));

        trees.add(new Texture("Trees/Chanterelles1.png"));
        trees.add(new Texture("Trees/Chanterelles2.png"));
        trees.add(new Texture("Trees/Chanterelles3.png"));

        trees.add(new Texture("Trees/Light_balls_tree1.png"));
        trees.add(new Texture("Trees/Light_balls_tree2.png"));
        trees.add(new Texture("Trees/Light_balls_tree3.png"));

        trees.add(new Texture("Trees/White_tree1.png"));
        trees.add(new Texture("Trees/White_tree2.png"));

        trees.add(new Texture("Trees/White-red_mushroom1.png"));
        trees.add(new Texture("Trees/White-red_mushroom2.png"));
        trees.add(new Texture("Trees/White-red_mushroom3.png"));

        //bushes
        trees.add(new Texture("Trees/Fern1_1.png"));
        trees.add(new Texture("Trees/Fern1_2.png"));
        trees.add(new Texture("Trees/Fern1_3.png"));

        trees.add(new Texture("Trees/Fern2_1.png"));
        trees.add(new Texture("Trees/Fern2_2.png"));
        trees.add(new Texture("Trees/Fern2_3.png"));

        trees.add(new Texture("Trees/Snow_bush1.png"));
        trees.add(new Texture("Trees/Snow_bush2.png"));
        trees.add(new Texture("Trees/Snow_bush3.png"));

        trees.add(new Texture("Trees/Bush_red_flowers1.png"));
        trees.add(new Texture("Trees/Bush_red_flowers2.png"));
        trees.add(new Texture("Trees/Bush_red_flowers3.png"));

        trees.add(new Texture("Trees/Bush_simple1_1.png"));
        trees.add(new Texture("Trees/Bush_simple1_2.png"));
        trees.add(new Texture("Trees/Bush_simple1_3.png"));

        trees.add(new Texture("Trees/Bush_simple2_1.png"));
        trees.add(new Texture("Trees/Bush_simple2_2.png"));
        trees.add(new Texture("Trees/Bush_simple2_3.png"));

        trees.add(new Texture("Trees/Bush_blue_flowers1.png"));
        trees.add(new Texture("Trees/Bush_blue_flowers2.png"));
        trees.add(new Texture("Trees/Bush_blue_flowers3.png"));

        trees.add(new Texture("Trees/Bush_orange_flowers1.png"));
        trees.add(new Texture("Trees/Bush_orange_flowers2.png"));
        trees.add(new Texture("Trees/Bush_orange_flowers3.png"));

        trees.add(new Texture("Trees/Bush_pink_flowers1.png"));
        trees.add(new Texture("Trees/Bush_pink_flowers2.png"));
        trees.add(new Texture("Trees/Bush_pink_flowers3.png"));

        trees.add(new Texture("Trees/Cactus1_1.png"));
        trees.add(new Texture("Trees/Cactus1_2.png"));
        trees.add(new Texture("Trees/Cactus1_3.png"));

        trees.add(new Texture("Trees/Cactus2_1.png"));
        trees.add(new Texture("Trees/Cactus2_2.png"));
        trees.add(new Texture("Trees/Cactus2_3.png"));

        trees.add(new Texture("Trees/Palm_tree1_1.png"));
        trees.add(new Texture("Trees/Palm_tree1_2.png"));
        trees.add(new Texture("Trees/Palm_tree1_3.png"));

        trees.add(new Texture("Trees/Palm_tree2_1.png"));
        trees.add(new Texture("Trees/Palm_tree2_2.png"));
        trees.add(new Texture("Trees/Palm_tree2_3.png"));

        trees.add(new Texture("Trees/Snow_christmass_tree1.png"));
        trees.add(new Texture("Trees/Snow_christmass_tree2.png"));
        trees.add(new Texture("Trees/Snow_christmass_tree3.png"));

        trees.add(new Texture("Trees/Snow_tree1.png"));
        trees.add(new Texture("Trees/Snow_tree2.png"));
        trees.add(new Texture("Trees/Snow_tree3.png"));

        trees.add(new Texture("Trees/Tree1.png"));
        trees.add(new Texture("Trees/Tree2.png"));
        trees.add(new Texture("Trees/Tree3.png"));

        trees.add(new Texture("Trees/Moss_tree1.png"));
        trees.add(new Texture("Trees/Moss_tree2.png"));
        trees.add(new Texture("Trees/Moss_tree3.png"));

        trees.add(new Texture("Trees/Fruit_tree1.png"));
        trees.add(new Texture("Trees/Fruit_tree2.png"));
        trees.add(new Texture("Trees/Fruit_tree3.png"));

        trees.add(new Texture("Trees/Autumn_tree1.png"));
        trees.add(new Texture("Trees/Autumn_tree2.png"));
        trees.add(new Texture("Trees/Autumn_tree3.png"));

        trees.add(new Texture("Trees/Christmas_tree1.png"));
        trees.add(new Texture("Trees/Christmas_tree2.png"));
        trees.add(new Texture("Trees/Christmas_tree3.png"));

        trees.add(new Texture("Trees/Flower_tree1.png"));
        trees.add(new Texture("Trees/Flower_tree2.png"));
        trees.add(new Texture("Trees/Flower_tree3.png"));

        trees.add(new Texture("Trees/Blue-gray_ruins1.png"));
        trees.add(new Texture("Trees/Blue-gray_ruins2.png"));
        trees.add(new Texture("Trees/Blue-gray_ruins4.png"));

        trees.add(new Texture("Trees/Yellow_ruins1.png"));
        trees.add(new Texture("Trees/Yellow_ruins2.png"));
        trees.add(new Texture("Trees/Yellow_ruins3.png"));
        trees.add(new Texture("Trees/Yellow_ruins4.png"));
        trees.add(new Texture("Trees/Yellow_ruins5.png"));

        trees.add(new Texture("Trees/Blue_coral1_shadow1.png"));
        trees.add(new Texture("Trees/Blue_coral1_shadow2.png"));
        trees.add(new Texture("Trees/Blue_coral1_shadow3.png"));

        trees.add(new Texture("Trees/Blue_coral2_shadow1.png"));
        trees.add(new Texture("Trees/Blue_coral2_shadow2.png"));
        trees.add(new Texture("Trees/Blue_coral2_shadow3.png"));

        trees.add(new Texture("Trees/Blue-beige_coral1_shadow1.png"));
        trees.add(new Texture("Trees/Blue-beige_coral1_shadow2.png"));
        trees.add(new Texture("Trees/Blue-beige_coral1_shadow3.png"));

        trees.add(new Texture("Trees/Blue-beige_coral2_shadow1.png"));
        trees.add(new Texture("Trees/Blue-beige_coral2_shadow2.png"));
        trees.add(new Texture("Trees/Blue-beige_coral2_shadow3.png"));

        for(int i = 1; i <= 8; i++)
            trees.add(new Texture("Trees/Animal"+i+".png"));












    }
    private void loadBackground() {
        backgrounds = new Array<>();
        for(int i=0;i < 2;i++)
            backgrounds.add(new Texture("Background/water.bmp"));

        tRiver = new Texture("Background/River.bmp");
    }




    //getters & setters
    public void setLeftMove(boolean value) {
        this.leftMove = value;
    }

    public void setRightMove(boolean value) {
        this.rightMove = value;
    }

    public void setUpMove(boolean value) {
        this.upMove = value;
    }

    public void setDownMove(boolean value)
    {
        this.downMove = value;
    }


}
