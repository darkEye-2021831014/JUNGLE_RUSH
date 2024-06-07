package com.junglerush;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;

import java.math.BigInteger;
import java.util.TreeSet;

public class GameScreen implements Screen {

    private final JungleRush game;
    private final MainMenuScreen menu;
    private Texture tRoad,tRiver,tMainCharacter;
    private Array<Texture> trees,backgrounds,animals,enemies;
    private Array<Rectangle> roadRect, treeRectLeft, treeRectRight,backgroundRect,roadBoarder,animalRect;
    private Rectangle riverRect,mcRect,enemyRect,currentAnimalRect;

    private final int TOTAL_TILES = 20,treeRandomFactor = 6;
    private int ELEMENT_WIDTH,ELEMENT_HEIGHT,JUNGLE_FACTOR,ROAD_FACTOR,RIVER_FACTOR,JUNGLE_WIDTH,ROAD_WIDTH,
            BOARDER_WIDTH;
    private int SPEED = 5,animalIndex,enemyIndex,animalSpeed=3,enemySpeed=2,mcScoreFactor,enemyScoreFactor,animalScoreFactor,isPositiveAnimal;
    private long enemyScore,animalScore;
    private BigInteger mcScore;
    private boolean leftMove = false,rightMove = false,upMove = false,downMove = false,GAME_OVER = false;
    private TreeSet<BigInteger> scoreTree;
    private ShapeRenderer shapeRenderer;
//    private BlinkingLight redLight,greenLight;
    private Array<Particle> particlesLeft,particlesRight;
    private final int totalParticle = 1000;
    private float centerLeftX,centerLeftY,centerRightX,centerRightY;
    private final Player player;
    private final Enemy enemyCar,enemyAnimal;
    private final Score enemyScoreText,playerScoreText;
    private final Background background;
    private final Trees forest;
    private final Indicator indicator;

    public GameScreen(final JungleRush game,final MainMenuScreen menu)
    {
        this.game = game;
        this.menu = menu;
        this.forest = new Trees();

        this.background = new Background();
        this.playerScoreText = new Score("Fonts/robotoMonoRegular.ttf",20);
        this.player = new Player(this.playerScoreText,5,1,"Background/mainCar.png");

        this.enemyScoreText = new Score("Fonts/robotoMonoRegular.ttf",18);
        enemyCar = new Enemy(2,MathUtils.random(0,3),this.enemyScoreText,1);

        enemyAnimal = new Enemy(3,MathUtils.random(0,1),this.enemyScoreText,1);

        indicator = new Indicator(1000,player.getScore(),BigInteger.valueOf(enemyCar.getScore()));
        shapeRenderer = new ShapeRenderer();

        initializeBackground();


    }

    private void initializeBackground() {
        initializeVariables();
        //load all available trees

        loadBackground();
        loadTrees();
        loadEnemyCars();
        loadEnemyAnimals();
        loadPlayer();
        loadIndicator();


    }

    private void loadEnemyAnimals() {
        for(int i=1;i<=22;i++)
            enemyAnimal.addTexture(new Texture("Enemy/Living/animal"+i+".png"));

        //spawn animal
        spawnEnemyAnimal();
    }


    private void loadIndicator() {
        indicator.setCenterX(background.getBoarderRect().get(0).x + background.getBoarderRect().get(0).width/2,
                background.getBoarderRect().get(1).x + background.getBoarderRect().get(1).width/2);
        indicator.setCenterY(enemyCar.getRectangle().y+enemyCar.getRectangle().height/2);


        int maxRadius = 16,minSpeed = 50;
        for (int i = 0; i < indicator.getTotalParticles(); i++) {
            float initialRadius = (float) (Math.random() * maxRadius); // Adjust max radius
            float initialAngle = (float) (Math.random() * 360);
            float speed = (float) (Math.random() * 50 + minSpeed); // Adjust speed range
            indicator.addParticleLeft(new Particle(indicator.getCenterLeftX(),indicator.getCenterLeftY(),initialRadius, initialAngle, speed));
            indicator.addParticleRight(new Particle(indicator.getCenterRightX(),indicator.getCenterRightY(),initialRadius, initialAngle, speed));
        }
    }

    private void loadBackground()
    {
        int totalBg = 2;
        for(int i=0;i < totalBg;i++)
            background.addTexture(new Texture("Background/water.bmp"));
        background.addTexture(new Texture("Background/River.bmp"));
        background.addTexture(new Texture("Background/Road.jpeg"));

        //background water rectangle
        background.addRectangle(new Rectangle(0,0,game.SCREEN_WIDTH,game.SCREEN_HEIGHT));
        background.addRectangle(new Rectangle(0,game.SCREEN_HEIGHT,game.SCREEN_WIDTH,game.SCREEN_HEIGHT));
        background.addRectangle(new Rectangle(0,0,game.SCREEN_WIDTH,game.SCREEN_HEIGHT));
        background.addRectangle(new Rectangle(-game.SCREEN_WIDTH,0,game.SCREEN_WIDTH,game.SCREEN_HEIGHT));
        //background river rectangle
        background.addRiverRectangle(
                new Rectangle(this.JUNGLE_WIDTH*2+this.ROAD_WIDTH+this.BOARDER_WIDTH*2,0,
                        this.ELEMENT_WIDTH*this.RIVER_FACTOR,game.SCREEN_HEIGHT));
        //background boarder rectangle
        background.addBoarderRectangle(new Rectangle(this.JUNGLE_WIDTH,0,this.BOARDER_WIDTH,game.SCREEN_HEIGHT));
        background.addBoarderRectangle(new Rectangle(this.JUNGLE_WIDTH+this.BOARDER_WIDTH+this.ROAD_WIDTH,0,this.BOARDER_WIDTH,game.SCREEN_HEIGHT));
        //background road rectangle
        background.addRoadRectangle(new Rectangle(this.JUNGLE_WIDTH+this.BOARDER_WIDTH, 0, this.ROAD_WIDTH, game.SCREEN_HEIGHT));
        background.addRoadRectangle(new Rectangle(this.JUNGLE_WIDTH+this.BOARDER_WIDTH, game.SCREEN_HEIGHT, this.ROAD_WIDTH, game.SCREEN_HEIGHT));
    }

    private void loadTrees()
    {
        for(int i =1; i <= 123; i++)
            forest.addTexture(new Texture("Trees/jungleTree"+i+".png"));

        for (int i = 0; i < 16; i++)
        {
            for (int j = 0; j < 6; j++)
            {
                forest.addRectangleLeft(new Rectangle(this.ELEMENT_WIDTH*j,50*i,this.ELEMENT_WIDTH,50));
                forest.addRectangleRight(new Rectangle(this.JUNGLE_WIDTH + this.BOARDER_WIDTH*2+this.ROAD_WIDTH+
                        this.ELEMENT_WIDTH*j,50*i,this.ELEMENT_WIDTH,50));
            }
        }

        for (int i = 0; i < forest.getRectanglesLeft().size; i++) {
            forest.addTreeIndexLeft(MathUtils.random(0,forest.getTextures().size-1));
            forest.addTreeIndexRight(MathUtils.random(0,forest.getTextures().size-1));
        }
    }

    private void loadPlayer() {
        player.spawnPlayer(this.JUNGLE_WIDTH+this.BOARDER_WIDTH+(this.ROAD_WIDTH-player.getTexture().getWidth())/2f,
                0,player.getTexture().getWidth(),player.getTexture().getHeight());
    }

    private void loadEnemyCars() {
        int totalEnemyCars = 13;
        for (int i= 1;i <=totalEnemyCars; i++)
            enemyCar.addTexture(new Texture("Enemy/NonLiving/enemyCar"+i+".png"));
        spawnEnemyCar();
    }

    private void spawnEnemyCar()
    {
        enemyCar.setIndex();
        enemyCar.spawnEnemy(this.JUNGLE_WIDTH+this.BOARDER_WIDTH +
                        MathUtils.random(0, this.ROAD_WIDTH-enemyCar.getTexture().getWidth()), 20*this.ELEMENT_HEIGHT,
                        Math.max(0,player.getScoreFactor()-3),Math.min(62,player.getScoreFactor()+3));
        indicator.setCenterY(enemyCar.getRectangle().y+enemyCar.getRectangle().height/2);
        indicator.setIndicator(player.getScore(),BigInteger.valueOf(enemyCar.getScore()));
    }

    private void spawnEnemyAnimal() {
        enemyAnimal.setIndex();
        System.out.println(enemyAnimal.getIndex());
        int scoreLowerLimit = Math.max(0,player.getScoreFactor()-5),
                scoreUpperLimit = Math.min(62, player.getScoreFactor()+1);
        int value = MathUtils.random(0,1);
        if(value==0) enemyAnimal.setIsPositive(1);
        else enemyAnimal.setIsPositive(-1);

        // i= 0(left to right), 1 ( right to left), 2 (static)
        int direction = checkDirection(enemyAnimal.getIndex());
        if(direction==0)
            enemyAnimal.spawnEnemy(background.getRoadRect().get(0).x,
                    MathUtils.random(15 * ELEMENT_HEIGHT,18*ELEMENT_HEIGHT),scoreLowerLimit,scoreUpperLimit);
        else if(direction==1)
            enemyAnimal.spawnEnemy(background.getRoadRect().get(0).x+this.ROAD_WIDTH-enemyAnimal.getTexture().getWidth(),
                    MathUtils.random(15 * ELEMENT_HEIGHT,18*ELEMENT_HEIGHT),scoreLowerLimit,scoreUpperLimit);
        else
            enemyAnimal.spawnEnemy(background.getRoadRect().get(0).x+MathUtils.random(0,this.ROAD_WIDTH-enemyAnimal.getTexture().getWidth()),
                    MathUtils.random(15 * ELEMENT_HEIGHT,18*ELEMENT_HEIGHT),scoreLowerLimit,scoreUpperLimit);
    }




    @Override
    public void render(float v) {
        ScreenUtils.clear(0, 0, 0.3f, 1);

        game.batch.begin();
        background.drawBackground(game.batch);
        enemyAnimal.draw(game.batch);
        enemyCar.draw(game.batch);
        forest.draw(game.batch);
        player.draw(game.batch,game.SCREEN_WIDTH,game.SCREEN_HEIGHT,background);
        game.batch.end();

        this.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        indicator.draw(shapeRenderer);
        this.shapeRenderer.end();

        update();
    }



    private void update()
    {
        updateEnemyCar();
        updatePlayer();
        forest.update(player.getSpeed(),50);
        background.update(game.SCREEN_WIDTH,game.SCREEN_HEIGHT,player.getSpeed());
        indicator.update(enemyCar.getSpeed());
        updateEnemyAnimal();
        detectCollision();


    }

    private void updateEnemyAnimal() {
        int i = 0;// i= 0(left to right), 1 ( right to left), 2 (static)
        i = checkDirection(enemyAnimal.getIndex());

        enemyAnimal.getRectangle().y -= player.getSpeed();
        if(i==0) enemyAnimal.getRectangle().x += enemyAnimal.getSpeed();
        else if(i==1) enemyAnimal.getRectangle().x -= enemyAnimal.getSpeed();

        if(enemyAnimal.getRectangle().y <= -enemyAnimal.getTexture().getHeight() ||
                enemyAnimal.getRectangle().x <= background.getBoarderRect().get(0).x+this.BOARDER_WIDTH ||
                enemyAnimal.getRectangle().x >= (background.getBoarderRect().get(1).x - (enemyAnimal.getTexture().getWidth())))
        {
            System.out.println(enemyAnimal.getIndex() + " here");
            spawnEnemyAnimal();
        }
    }

    private void updatePlayer() {
        playerScoreText.blinkingEffect(0.02f,0.01f,0.5f);
        player.setTextColor(new Color(1,1,1,playerScoreText.getOpacity()));
        player.update(enemyCar,background);
    }

    private void updateEnemyCar() {
        enemyScoreText.blinkingEffect(0.02f,0.01f,0.5f);
        enemyCar.setTextColor(new Color(1,1,1,enemyScoreText.getOpacity()));
        enemyCar.update();
        if(enemyCar.getRectangle().y <= -enemyCar.getTexture().getHeight())
            spawnEnemyCar();
    }



    private void detectCollision() {

        //detect collision with road boarder
        for(Rectangle rect: background.getBoarderRect())
            if(player.getRectangle().overlaps(rect))
            {
                this.GAME_OVER = true;
                return;
            }

        //detect collision with enemies
        if(player.getRectangle().overlaps(enemyCar.getRectangle()))
        {
            if(enemyCar.getScoreFactor() > player.getScoreFactor())
            {
                this.GAME_OVER = true;
                return;
            }
            else
            {
                //mc score will increase
                player.setScore(enemyCar.getScoreFactor());
                spawnEnemyCar();
            }
        }

    }


    private int checkDirection(int value)
    {
        // i= 0(left to right), 1 ( right to left), 2 (static)
        int i = 0;
        if(value >= 8 && value < 15) i = 1;
        else if(value >= 15 && value < 22) i = 2;
        return i;
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

        //initialize indicators
        particlesLeft = new Array<>();
        particlesRight = new Array<>();
        this.centerLeftX = this.JUNGLE_WIDTH+this.BOARDER_WIDTH/2f;
        this.centerRightX = this.JUNGLE_WIDTH+this.BOARDER_WIDTH+this.ROAD_WIDTH + this.BOARDER_WIDTH/2f;
    }


}
