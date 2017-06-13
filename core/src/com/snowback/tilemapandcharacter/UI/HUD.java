package com.snowback.tilemapandcharacter.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.snowback.tilemapandcharacter.AssetManager;
import com.snowback.tilemapandcharacter.Player;
import com.snowback.tilemapandcharacter.Tilemapandcharacter;
import com.snowback.tilemapandcharacter.World;
import com.snowback.tilemapandcharacter.Zombie;

import java.util.ArrayList;

/**
 * Created by Barry on 2017/5/31.
 */

public class HUD implements Disposable{
    public Stage stage;
    private Tilemapandcharacter.GameScreen screen;
    private Viewport viewport;
    private SpriteBatch sb;
    private Player player;

    //Mini Map
    private final float beginPosY = Gdx.graphics.getHeight()-272;
    private float worldSizeX;
    private float worldSizeY;
    private Sprite miniMap;
    private Sprite miniPlayer;
    private ArrayList<Zombie> zombies;
    private Sprite miniZombie;



    public HUD (SpriteBatch sb, Tilemapandcharacter.GameScreen screen){
        this.screen = screen;
        player = screen.getPlay().getPlayer();
        zombies = screen.getPlay().getZombie();
        this.sb = sb;
        viewport = new FitViewport(Gdx.graphics.getWidth(),Gdx.graphics.getHeight(), new OrthographicCamera());
        stage = new Stage(viewport, sb);

        //MAP
        worldSizeX = (float) World.width*15-player.getWidth();
        worldSizeY = (float) World.height*15-player.getHeight();
        Gdx.app.log("x",Float.toString(worldSizeX));
        Gdx.app.log("x",Float.toString(worldSizeY));

        miniMap = new Sprite(AssetManager.getInstance().getMap(),399,272);
        miniMap.setPosition(0,beginPosY);

        miniPlayer = new Sprite(AssetManager.getInstance().getMiniPlayer());
        miniPlayer.setScale(0.5f);
        miniPlayer.setPosition(0,beginPosY);

        miniZombie = new Sprite(AssetManager.getInstance().getZombie());
        miniZombie.setOrigin(0,0);
        miniZombie.setScale(0.06f);
        miniZombie.setPosition(0,beginPosY);

    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    public void Render() {
        sb.begin();
        miniMap.draw(sb);
        miniPlayer.draw(sb);
        drawPlayer(sb);
        drawZombie(sb);
        sb.end();
    }

    private void drawPlayer(SpriteBatch sb) {
        miniPlayer.draw(sb);
    }

    private void drawZombie(SpriteBatch sb) {
        for (Zombie zombie:zombies){
            miniZombie.draw(sb);

            float x = zombie.getHitBox().getX()/worldSizeX;
            float y = zombie.getHitBox().getY()/worldSizeY;

            miniZombie.setPosition(340*x+30,beginPosY-10+y*222);

        }
    }

    public void update(float dt) {
        updateMiniPlayer(dt);
    }

    private void updateMiniPlayer(float dt) {
        float x = player.getX()/worldSizeX;
        float y = player.getY()/worldSizeY;

        miniPlayer.setPosition(340*x, beginPosY-30+y*222);
    }
}
