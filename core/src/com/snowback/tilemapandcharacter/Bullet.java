package com.snowback.tilemapandcharacter;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by shoong on 2017-05-17.
 */

public class Bullet {
    Rectangle hitBox;
    float a, time;
    int speed;

    public Bullet(float x, float y, float angle) {
        a = angle;
        time = 2;
        speed = 300;
        hitBox = new Rectangle(x, y, 10, 10);
    }

    public Rectangle getHitBox() {
        return hitBox;
    }

    public void update(float delta) {
        hitBox.x += speed * (float)Math.cos(a*Math.PI/180) * delta;
        hitBox.y += speed * (float)Math.sin(a*Math.PI/180) * delta;
        time -= delta;
    }

    public boolean isDead() {
        if (time < 0) return true;
        return false;
    }

    public void draw(SpriteBatch batch) {
        batch.draw(AssetManager.getInstance().getBullet(), hitBox.x, hitBox.y, 50, 50);
    }
}