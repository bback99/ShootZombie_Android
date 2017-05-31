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
import com.snowback.tilemapandcharacter.Player;
import com.snowback.tilemapandcharacter.Tilemapandcharacter;

/**
 * Created by Barry on 2017/5/31.
 */

public class HUD implements Disposable{
    public Stage stage;
    private Tilemapandcharacter.GameScreen screen;
    private Viewport viewport;
    private SpriteBatch sb;
    private Player player;
    private TextureAtlas uiAtlas;

    //Hp Bar
    private Texture hpBarTexture;
    private Sprite hpBar;
    private Sprite hpBarBorder;


    public HUD (SpriteBatch sb, Tilemapandcharacter.GameScreen screen){
        this.screen = screen;
        player = screen.getPlay().getPlayer();
        this.sb = sb;
        viewport = new FitViewport(Gdx.graphics.getWidth(),Gdx.graphics.getHeight(), new OrthographicCamera());
        stage = new Stage(viewport, sb);
        uiAtlas = new TextureAtlas(Gdx.files.internal("uiAssets/uiAssets.txt"));


        hpBarTexture = new Texture(Gdx.files.internal("uiAssets/green.jpg"));
        hpBar = new Sprite(hpBarTexture,400,50);
        hpBarBorder = new Sprite(uiAtlas.findRegion("healthbart"));

        hpBar.setScale(0.55f*(player.getHealth()/100.f),0.3f);
        hpBar.setPosition(900,762);
        hpBar.setOrigin(0,0);

        hpBarBorder.setScale(0.5f,0.5f);
        hpBarBorder.setPosition(770,700);

    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    public void Render() {
        sb.begin();
        hpBar.draw(sb);
        hpBarBorder.draw(sb);
        sb.end();
    }

    public void update(float dt) {
        hpBar.setScale(0.55f*(player.getHealth()/100.f),0.3f);
    }
}
