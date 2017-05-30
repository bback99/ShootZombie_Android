package com.snowback.tilemapandcharacter;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by shoong on 2017-05-18.
 */

public class Zombie extends Sprite {

    private Rectangle hitBox;

    public Zombie(float x, float y) {
        hitBox = new Rectangle(x, y, 50, 100);
    }

    public void draw(SpriteBatch spritebatch) {
        spritebatch.draw(AssetManager.getInstance().getZombie(), hitBox.getX(), hitBox.getY(), hitBox.getWidth(), hitBox.getHeight());
    }

    public Rectangle getHitBox() {
        return hitBox;
    }

    public void dispose() {
        //textureZombie.dispose();
    }

    public void update(float delta) {
//        hitBox.x += speed * (float)Math.cos(a*Math.PI/180) * delta;
//        hitBox.y += speed * (float)Math.sin(a*Math.PI/180) * delta;
//        time -= delta;
    }
}
