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
    private final Score enemyScoreText,playerScoreText,enemyAnimalScoreText;
    private final Background background;
    private final Trees forest;
    private final Indicator indicatorCarLeft,indicatorCarRight,indicatorAnimal;
    private final Collision collision;

    public GameScreen(final JungleRush game)
    {
        this.game = game;
        this.forest = new Trees();

        this.background = new Background();
        this.playerScoreText = new Score("Fonts/robotoMonoBold.ttf",24,1);
        this.player = new Player(this.playerScoreText,5,1,"Background/mainCar.png",10000L);

        this.enemyScoreText = new Score("Fonts/robotoMonoBold.ttf",22,1);
        enemyCar = new Enemy(2,MathUtils.random(0,3),this.enemyScoreText,false,10000L);

        this.enemyAnimalScoreText = new Score("Fonts/robotoMonoBold.ttf",22,1);
        enemyAnimal = new Enemy(3,MathUtils.random(0,1),this.enemyAnimalScoreText,false,1000L);

        indicatorCarLeft = new Indicator(1000,player.getScore(),enemyCar.getScore());
        indicatorCarRight = new Indicator(1000,player.getScore(),enemyCar.getScore());
        indicatorAnimal = new Indicator(200,player.getScore(),enemyCar.getScore());

        collision = new Collision(player,background,enemyCar,enemyAnimal,this,game);
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
        loadCarIndicator();
        loadAnimalIndicator();


    }

    private void loadAnimalIndicator() {
        setAnimalIndicatorCenter();

        loadParticles(indicatorAnimal);
    }

    private void loadEnemyAnimals() {
        for(int i=1;i<=22;i++)
            enemyAnimal.addTexture(new Texture("Enemy/Living/animal"+i+".png"));

        //spawn animal
        spawnEnemyAnimal();
    }


    private void loadCarIndicator() {
        indicatorCarLeft.setCenterX(background.getBoarderRect().get(0).x + background.getBoarderRect().get(0).width/2);
        indicatorCarRight.setCenterX(background.getBoarderRect().get(1).x + background.getBoarderRect().get(1).width/2);

        indicatorCarLeft.setCenterY(enemyCar.getRectangle().y+enemyCar.getRectangle().height/2);
        indicatorCarRight.setCenterY(indicatorCarLeft.getCenterY());

        loadParticles(indicatorCarLeft);
        loadParticles(indicatorCarRight);

    }

    private void loadParticles(Indicator indicator)
    {
        int maxRadius = 16,minSpeed = 50;
        for (int i = 0; i < indicatorCarLeft.getTotalParticles(); i++) {
            float initialRadius = (float) (Math.random() * maxRadius); // Adjust max radius
            float initialAngle = (float) (Math.random() * 360);
            float speed = (float) (Math.random() * 50 + minSpeed); // Adjust speed range
            indicator.addParticle(new Particle(indicator.getCenterX(),indicator.getCenterY(),initialRadius, initialAngle, speed));
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

    public void spawnEnemyCar()
    {
        enemyCar.setIndex();
        enemyCar.spawnEnemy(this.JUNGLE_WIDTH+this.BOARDER_WIDTH +
                        MathUtils.random(0, this.ROAD_WIDTH-enemyCar.getTexture().getWidth()), 20*this.ELEMENT_HEIGHT,
                        Math.max(0,player.getScoreFactor()-3),player.getScoreFactor()+3);
        indicatorCarLeft.setCenterY(enemyCar.getRectangle().y+enemyCar.getRectangle().height/2);
        indicatorCarRight.setCenterY(indicatorCarLeft.getCenterY());
    }

    public void spawnEnemyAnimal() {
        enemyAnimal.setIndex();

        int scoreLowerLimit = Math.max(0,player.getScoreFactor()-5),
                scoreUpperLimit = player.getScoreFactor()+1;
        int value = MathUtils.random(0,1);
        enemyAnimal.setDivide(value != 0);

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
        indicatorCarLeft.draw(shapeRenderer,0.7f);
        indicatorCarRight.draw(shapeRenderer,0.7f);
        indicatorAnimal.draw(shapeRenderer,0.5f);
        this.shapeRenderer.end();

        update();
    }



    private void update()
    {
        updateEnemyCar();
        updatePlayer();
        forest.update(player.getSpeed(),50);
        background.update(game.SCREEN_WIDTH,game.SCREEN_HEIGHT,player.getSpeed());
        updateCarIndicator();
        updateAnimalIndicator();
        updateEnemyAnimal();

        collision.update();

    }

    private void updateAnimalIndicator() {
        setAnimalIndicatorCenter();
        if(enemyAnimal.isDivide()) indicatorAnimal.setIndicator(-1);
        else indicatorAnimal.setIndicator(1);
        indicatorAnimal.update();
    }

    private void updateCarIndicator() {
        indicatorCarLeft.updateCenterY(enemyCar.getSpeed());
        indicatorCarRight.updateCenterY(enemyCar.getSpeed());
        indicatorCarLeft.update();
        indicatorCarRight.update();

        indicatorCarLeft.setIndicator(player.getScore(),enemyCar.getScore());
        indicatorCarRight.setIndicator(indicatorCarLeft.getIndicator());
    }

    private void updateEnemyAnimal() {
        if(enemyAnimal.isDivide())
            enemyAnimal.setTextColor(Color.RED);
        else enemyAnimal.setTextColor(Color.GREEN);
        // i= 0(left to right), 1 ( right to left), 2 (static)
        int i = checkDirection(enemyAnimal.getIndex());

        enemyAnimal.getRectangle().y -= player.getSpeed();
        if(i==0) enemyAnimal.getRectangle().x += enemyAnimal.getSpeed();
        else if(i==1) enemyAnimal.getRectangle().x -= enemyAnimal.getSpeed();

        if(enemyAnimal.getRectangle().y <= -enemyAnimal.getTexture().getHeight() ||
                enemyAnimal.getRectangle().x <= background.getBoarderRect().get(0).x+this.BOARDER_WIDTH ||
                enemyAnimal.getRectangle().x >= (background.getBoarderRect().get(1).x - (enemyAnimal.getTexture().getWidth())))
        {
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





    private int checkDirection(int value)
    {

        // i= 0(left to right), 1 ( right to left), 2 (static)
        int i = 0;
        if(value >= 8 && value < 15) i = 1;
        else if(value >= 15 && value < 22) i = 2;
        return i;
    }

    private void setAnimalIndicatorCenter()
    {
        indicatorAnimal.setCenterX(enemyAnimal.getRectangle().x+ enemyAnimal.getRectangle().width/2);
        indicatorAnimal.setCenterY(enemyAnimal.getRectangle().y+enemyAnimal.getRectangle().height/2);
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
