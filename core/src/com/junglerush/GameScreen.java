package com.junglerush;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;

import java.awt.*;

public class GameScreen implements Screen {

    private final JungleRush game;
    private Texture tRoad;
    private Array<Texture> trees;
    private Array<Rectangle> roadRect, treeRectLeft, treeRectRight;

    private final int TOTAL_TILES = 20,aniSpeed = 20,SPEED = 5,treeRandomFactor = 6;
    private int ELEMENT_WIDTH,ELEMENT_HEIGHT,JUNGLE_FACTOR,ROAD_FACTOR,JUNGLE_WIDTH,ROAD_WIDTH;
    private int aniTick = 0,aniIndex = 0;

    public GameScreen(final JungleRush game)
    {
        this.game = game;
        tRoad = new Texture("Road.jpeg");

        initializeBackground();


    }

    private void initializeBackground() {
        initializeVariables();
        //load all available trees
        loadTrees();
        initializeRectangles();

    }




    @Override
    public void render(float v) {
        ScreenUtils.clear(0,0,0.3f,1);

        game.batch.begin();
        game.batch.draw(tRoad,roadRect.get(0).x,roadRect.get(0).y,roadRect.get(0).width,roadRect.get(0).height);
        game.batch.draw(tRoad,roadRect.get(1).x,roadRect.get(1).y,roadRect.get(1).width,roadRect.get(1).height);

        for (int i = 0; i < trees.size; i++)
            game.batch.draw(trees.get(i),this.treeRectLeft.get(i).x,this.treeRectLeft.get(i).y,this.treeRectLeft.get(i).width,this.treeRectLeft.get(i).height);
        for (int i = 0; i < trees.size; i++)
            game.batch.draw(trees.get(i),this.treeRectRight.get(i).x,this.treeRectRight.get(i).y,this.treeRectRight.get(i).width,this.treeRectRight.get(i).height);

        game.batch.end();

        update();
    }

    private void update()
    {
        //simulate road movement
        for(int i=0; i < this.roadRect.size;i++)
        {
            this.roadRect.get(i).y -= this.SPEED;
            if(this.roadRect.get(i).y <= -game.SCREEN_HEIGHT) this.roadRect.get(i).y = game.SCREEN_HEIGHT;
        }

        //simulate random tree spawn and tree movement
        for (int i = 0; i < this.treeRectLeft.size; i++)
        {
            this.treeRectLeft.get(i).y -= this.SPEED;
            this.treeRectRight.get(i).y -= this.SPEED;
            if(this.treeRectLeft.get(i).y <= -this.treeRectLeft.get(i).getHeight())
            {
                this.treeRectLeft.get(i).y = MathUtils.random(0,game.SCREEN_HEIGHT+this.treeRandomFactor*this.treeRectLeft.get(i).height);
                this.treeRectLeft.get(i).x = MathUtils.random(0,this.JUNGLE_WIDTH-this.treeRectLeft.get(i).width);
            }
            if(this.treeRectRight.get(i).y <= -this.treeRectRight.get(i).getHeight())
            {
                this.treeRectRight.get(i).y = MathUtils.random(0,game.SCREEN_HEIGHT+this.treeRandomFactor*this.treeRectLeft.get(i).height);
                this.treeRectRight.get(i).x = this.JUNGLE_WIDTH + this.ROAD_WIDTH + MathUtils.random(0, this.JUNGLE_WIDTH - trees.get(i).getWidth());
            }
        }

    }

    private void animateTrees(Array<Texture> component) {
        this.aniTick++;
        if(this.aniTick >= aniSpeed)
        {
            this.aniTick = 0;
            this.aniIndex++;
            if(this.aniIndex >= component.size)
                this.aniIndex = 0;
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
        this.ROAD_FACTOR = 8;
        this.JUNGLE_WIDTH = this.JUNGLE_FACTOR * this.ELEMENT_WIDTH;
        this.ROAD_WIDTH = this.ROAD_FACTOR * this.ELEMENT_WIDTH;
    }

    private void initializeRectangles() {
        //road control rectangle
        roadRect = new Array<>();
        roadRect.add(new Rectangle(this.JUNGLE_WIDTH, 0, this.ROAD_WIDTH, game.SCREEN_HEIGHT));
        roadRect.add(new Rectangle(this.JUNGLE_WIDTH, game.SCREEN_HEIGHT, this.ROAD_WIDTH, game.SCREEN_HEIGHT));

        //tree control rectangle initialization
        treeRectLeft = new Array<>();
        treeRectRight = new Array<>();
        for (int i = 0; i < trees.size; i++)
        {
            treeRectLeft.add(new Rectangle(MathUtils.random(0, this.JUNGLE_WIDTH - trees.get(i).getWidth()), MathUtils.random(0, game.SCREEN_HEIGHT + this.treeRandomFactor * this.trees.get(i).getHeight()), trees.get(i).getWidth(), trees.get(i).getHeight()));
            treeRectRight.add(new Rectangle(this.JUNGLE_WIDTH + this.ROAD_WIDTH + MathUtils.random(0, this.JUNGLE_WIDTH - trees.get(i).getWidth()), MathUtils.random(0, game.SCREEN_HEIGHT + this.treeRandomFactor * this.trees.get(i).getHeight()), trees.get(i).getWidth(), trees.get(i).getHeight()));
        }
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
    }



}
