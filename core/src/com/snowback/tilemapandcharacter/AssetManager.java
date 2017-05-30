package com.snowback.tilemapandcharacter;

import com.badlogic.gdx.graphics.Texture;

/**
 * Created by bback99 on 2017-05-30.
 */

public class AssetManager {

    private static AssetManager mInstance = null;

    protected AssetManager() {
        // Exists only to defeat instantiation.
    }

    public static AssetManager getInstance() {
        if(mInstance == null) {
            mInstance = new AssetManager();
        }
        return mInstance;
    }

    private Texture textureZombie;
    private Texture textureBullet;

    public void Init() {
        textureZombie = new Texture("resources/zombie.png");
        textureBullet = new Texture("resources/bullet.png");
    }

    public Texture getZombie() { return textureZombie; }
    public Texture getBullet() { return textureBullet; }
}
