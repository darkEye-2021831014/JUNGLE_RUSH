package com.junglerush;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import sun.nio.cs.ext.MacHebrew;


public class GameScreen implements Screen {

    private final JungleRush game;

    private final int TOTAL_TILES = 20;
    private int ELEMENT_WIDTH,ELEMENT_HEIGHT,JUNGLE_FACTOR,RIVER_FACTOR,JUNGLE_WIDTH,ROAD_WIDTH,
            BOARDER_WIDTH;
    private boolean isPaused = true,onHold = true,gameOver = false;
    private float elapsedTime = 0;
    private int totalCount = 3;
    private Rectangle centerRectangle;

    private final Player player;
    private final Enemy enemyCar,enemyAnimal;
    private final Score enemyScoreText,playerScoreText,enemyAnimalScoreText;
    private final Background background;
    private final Trees forestLeft,forestRight;
    private final Indicator indicatorCarLeft,indicatorCarRight,indicatorAnimal;
    private final Collision collision;
    private Sound countdownSound,carStartingSound,roadSound,boarderCollisionSound,carSpeedUpSound,pauseSound,resumeSound,
            collisionWithStrongCar,collisionWithStrongAnimal,collisionWithGreenAnimal,animalCollisionGameOver,
            collisionWithNormalCar;
    private Array<Sound> forestBirdSounds;
    private Array<Sound> soundArray;



    public GameScreen(final JungleRush game)
    {

        this.game = game;
        this.forestLeft = new Trees(50);
        this.forestRight = new Trees(50);

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

        initializeBackground();

        startPlayingSounds();
    }

    private void startPlayingSounds() {
        countdownSound.play();
        carStartingSound.play();
        roadSound.loop();
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
        loadSounds();


    }

    private void loadSounds() {
        soundArray = new Array<>();
        forestBirdSounds = new Array<>();
        countdownSound = Gdx.audio.newSound(Gdx.files.internal("Sounds/gameCountdown.wav"));
        carStartingSound = Gdx.audio.newSound(Gdx.files.internal("Sounds/carStarting1.mp3"));
        roadSound = Gdx.audio.newSound(Gdx.files.internal("Sounds/roadAmbience1.mp3"));
        boarderCollisionSound = Gdx.audio.newSound(Gdx.files.internal("Sounds/fallingInWater.wav"));
        carSpeedUpSound = Gdx.audio.newSound(Gdx.files.internal("Sounds/carSpeedUp.wav"));
        pauseSound = Gdx.audio.newSound(Gdx.files.internal("Sounds/pauseGame.wav"));
        resumeSound = Gdx.audio.newSound(Gdx.files.internal("Sounds/resumeGame.wav"));
        collisionWithStrongCar = Gdx.audio.newSound(Gdx.files.internal("Sounds/dashingWithCar3.wav"));
        collisionWithStrongAnimal = Gdx.audio.newSound(Gdx.files.internal("Sounds/dashingWithAnimal1.wav"));
        collisionWithGreenAnimal = Gdx.audio.newSound(Gdx.files.internal("Sounds/collisionWithGreenAnimal.wav"));
        animalCollisionGameOver = Gdx.audio.newSound(Gdx.files.internal("Sounds/dashingWithAnimal4.mp3"));
        collisionWithNormalCar = Gdx.audio.newSound(Gdx.files.internal("Sounds/dashingWithCar1.wav"));
        forestBirdSounds.add(Gdx.audio.newSound(Gdx.files.internal("Sounds/forestBirdsChirpingSound.wav")));
        forestBirdSounds.add(Gdx.audio.newSound(Gdx.files.internal("Sounds/forestBirdsChirpingSound1.wav")));

        soundArray.add(countdownSound);
        soundArray.add(carStartingSound);
        soundArray.add(roadSound);

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
        updateIndicatorXY();

        loadParticles(indicatorCarLeft);
        loadParticles(indicatorCarRight);

    }

    private void updateIndicatorXY() {
        indicatorCarLeft.setCenterX(background.getBoarderRect().get(0).x + background.getBoarderRect().get(0).width/2);
        indicatorCarRight.setCenterX(background.getBoarderRect().get(1).x + background.getBoarderRect().get(1).width/2);

        indicatorCarLeft.setCenterY(enemyCar.getRectangle().y+enemyCar.getRectangle().height/2);
        indicatorCarRight.setCenterY(indicatorCarLeft.getCenterY());
    }

    private void loadParticles(Indicator indicator) {
        int maxRadius = 16,minSpeed = 50;
        for (int i = 0; i < indicatorCarLeft.getTotalParticles(); i++) {
            float initialRadius = (float) (Math.random() * maxRadius); // Adjust max radius
            float initialAngle = (float) (Math.random() * 360);
            float speed = (float) (Math.random() * 50 + minSpeed); // Adjust speed range
            indicator.addParticle(new Particle(indicator.getCenterX(),indicator.getCenterY(),initialRadius, initialAngle, speed));
        }
    }

    private void loadBackground() {
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

    private void loadTrees() {
        for(int i =1; i <= 123; i++) {
            forestLeft.addTexture(new Texture("Trees/jungleTree" + i + ".png"));
            forestRight.addTexture(new Texture("Trees/jungleTree" + i + ".png"));
        }

        for (int i = 0; i < 25; i++)
        {
            for (int j = 0; j < 12; j++)
            {
                forestLeft.addRectangle(new Rectangle(MathUtils.random(0,this.JUNGLE_WIDTH-this.ELEMENT_WIDTH),
                        (forestLeft.getTreeHeight()-MathUtils.random(10,20))*i, this.ELEMENT_WIDTH, forestLeft.getTreeHeight()));
                forestRight.addRectangle(new Rectangle(this.JUNGLE_WIDTH + this.BOARDER_WIDTH*2+this.ROAD_WIDTH+
                        MathUtils.random(0,this.JUNGLE_WIDTH-this.ELEMENT_WIDTH),
                        (forestRight.getTreeHeight()- MathUtils.random(10,20))*i, this.ELEMENT_WIDTH, forestRight.getTreeHeight()));
            }
        }

        for (int i = 0; i < forestLeft.getRectangles().size; i++) {
            forestLeft.addTreeIndex(MathUtils.random(0,forestLeft.getTextures().size-1));
            forestRight.addTreeIndex(MathUtils.random(0,forestRight.getTextures().size-1));
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

    public void spawnEnemyCar() {
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
        if(!gameOver) {
            Gdx.input.setInputProcessor(new ProcessInput(player, this));
            ScreenUtils.clear(0, 0, 0.3f, 1);

            game.batch.begin();
            background.drawBackground(game.batch);
            enemyAnimal.draw(game.batch);
            enemyCar.draw(game.batch);
            forestLeft.draw(game.batch);
            forestRight.draw(game.batch);
            player.draw(game.batch, game.SCREEN_WIDTH, game.SCREEN_HEIGHT, background);
            game.batch.end();

            game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            indicatorCarLeft.draw(game.shapeRenderer, 0.7f);
            indicatorCarRight.draw(game.shapeRenderer, 0.7f);
            indicatorAnimal.draw(game.shapeRenderer, 0.5f);
            game.shapeRenderer.end();

            game.batch.begin();
            PauseResume();
            game.batch.end();
        }
    }

    private void PauseResume() {
        updateAlways();
        if(onHold)
            countDown();
        else {
            if (isPaused) {
                playerScoreText.blinkingEffect(0.04f,0.03f,0.3f);
                playerScoreText.setRectangle(centerRectangle);
                playerScoreText.setColor(new Color(playerScoreText.getOpacity(), 1, 1, playerScoreText.getOpacity()));
                playerScoreText.draw(game.batch, "Press Space To Resume");
            } else {
                update();
            }
        }
    }

    private void updateAlways() {
        background.update(game.SCREEN_WIDTH,game.SCREEN_HEIGHT,player.getSpeed());
        updateAnimalIndicator();
        updateCarIndicator();
    }

    private void countDown() {
        playerScoreText.setRectangle(this.centerRectangle);
        playerScoreText.setColor(new Color(0,1,1,1));
        if(totalCount <= 0) {
            if(totalCount == 0)
                playerScoreText.draw(game.batch,"Go!");
            else
            {
                setOnHold(false);
                setPaused(false);
                return;
            }
        }
        else
            playerScoreText.draw(game.batch,"Getting Started In\n        "+totalCount);

        elapsedTime += Gdx.graphics.getDeltaTime();
        if(elapsedTime >= 1f) {
            elapsedTime = 0;
            totalCount--;
        }
    }


    private void update()
    {
        elapsedTime += Gdx.graphics.getDeltaTime();
        if(elapsedTime >= MathUtils.random(3,8)) {
            forestBirdSounds.get(MathUtils.random(0, forestBirdSounds.size - 1)).play(0.95f);
            elapsedTime = 0;
        }
        forestLeft.update(player.getSpeed(),this.JUNGLE_WIDTH,this.ELEMENT_WIDTH,0);
        forestRight.update(player.getSpeed(),this.JUNGLE_WIDTH,this.ELEMENT_WIDTH,
                this.JUNGLE_WIDTH+this.ROAD_WIDTH+this.BOARDER_WIDTH*2);
        background.updateRoads(game.SCREEN_WIDTH,game.SCREEN_HEIGHT,player.getSpeed());
        updateEnemyCar();
        updatePlayer();
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
        updateIndicatorXY();
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





    private int checkDirection(int value) {
        // i= 0(left to right), 1 ( right to left), 2 (static)
        int i = 0;
        if(value >= 8 && value < 15) i = 1;
        else if(value >= 15 && value < 22) i = 2;
        return i;
    }

    private void setAnimalIndicatorCenter() {
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
    }



    //initialized methods
    private void initializeVariables() {
        this.ELEMENT_WIDTH = game.SCREEN_WIDTH/this.TOTAL_TILES;
        this.ELEMENT_HEIGHT = game.SCREEN_HEIGHT/this.TOTAL_TILES;
        this.JUNGLE_FACTOR = 6;
        this.RIVER_FACTOR = 1;
        this.BOARDER_WIDTH = (int)(0.5 * this.ELEMENT_WIDTH);
        this.JUNGLE_WIDTH = this.JUNGLE_FACTOR * this.ELEMENT_WIDTH;
        this.ROAD_WIDTH = this.JUNGLE_WIDTH;

        //center rectangle to show text
        centerRectangle = new Rectangle(this.JUNGLE_WIDTH+this.BOARDER_WIDTH,300,ROAD_WIDTH,game.SCREEN_HEIGHT-300);
    }



    //getters & setters
    public void setPaused(boolean paused) {
        isPaused = paused;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public boolean isOnHold() {
        return onHold;
    }

    public void setOnHold(boolean onHold) {
        this.onHold = onHold;
    }

    public Array<Sound> getSoundArray() {
        return soundArray;
    }

    public Sound getRoadSound() {
        return roadSound;
    }

    public Sound getBoarderCollisionSound() {
        return boarderCollisionSound;
    }

    public Sound getCarSpeedUpSound() {
        return carSpeedUpSound;
    }

    public Sound getPauseSound() {
        return pauseSound;
    }

    public Sound getResumeSound() {
        return resumeSound;
    }

    public Sound getCollisionWithStrongCar() {
        return collisionWithStrongCar;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public Sound getCollisionWithStrongAnimal() {
        return collisionWithStrongAnimal;
    }

    public Sound getCollisionWithGreenAnimal() {
        return collisionWithGreenAnimal;
    }

    public Sound getAnimalCollisionGameOver() {
        return animalCollisionGameOver;
    }

    public Sound getCollisionWithNormalCar() {
        return collisionWithNormalCar;
    }

    public Array<Sound> getForestBirdSounds() {
        return forestBirdSounds;
    }
}