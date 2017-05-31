package com.snowback.tilemapandcharacter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import java.util.ArrayList;

/**
 * Created by bback99 on 2017-05-14.
 */

public class Player extends Sprite {

    private String mUserName;
    private Animation animation;
    private float timePassed = 0;
    private boolean bIsPlayingAnimation = false;
    private ArrayList<Bullet> lstBullet = new ArrayList<Bullet>();
    private float textureSize = 0.0f;
    private float saveShootingAngle = 0.0f;

    public Player(String username, double posX, double posY) {
        mUserName = username;
        animation = AssetManager.getInstance().getAniCharRight();

        // just for getting image length
        Texture img1 = new Texture("players/character10/09.png");
        textureSize = img1.getWidth();

        this.setX((float) posX);
        this.setY((float) posY);
    }

    public String getUserName() { return mUserName; }

    public float getTextureSize() {
        return textureSize;
    }

    public void AddBullet(float x, float y, float angle) {
        //lstBullet.add(new Bullet(getX(), getY(), angle));
        lstBullet.add(new Bullet(x, y, angle));
        //Gdx.app.log("Position: ", "X : " +  x + ", Y: " + y + ", Angle: " + angle);
    }

    public void draw(SpriteBatch spritebatch) {
        if(bIsPlayingAnimation) {
            timePassed += Gdx.graphics.getDeltaTime();
        }

        if (animation == null) {
            animation = AssetManager.getInstance().getAniCharRight();
        }
        else {
            spritebatch.draw((TextureRegion) animation.getKeyFrame(timePassed, true), getX(), getY());
        }

        for(Bullet bullet: lstBullet) {
            bullet.draw(spritebatch);
        }
    }

    public Boolean updateBullets(ArrayList<Zombie> lstZombie) {
        for(Bullet bullet: lstBullet) {
            bullet.update(Gdx.graphics.getDeltaTime());

            // check to collide any zombies
            for(Zombie zombie: lstZombie) {
                if(zombie.getHitBox().overlaps(bullet.getHitBox())) {
                    lstZombie.remove(zombie);
                    lstBullet.remove(bullet);
                    return true;
                }
            }

            // check bounds and remove it in lstBullet
            float bottomLeftX = 0.0f, bottomLeftY = 0.0f, topRightX = (float) World.width*15, topRightY = (float) World.height*15;
            if (bullet.getHitBox().getX() <= bottomLeftX || bullet.getHitBox().getX() >= topRightX) {
                lstBullet.remove(bullet);
                return false;
            }
            else if (bullet.getHitBox().getY() <= bottomLeftY || bullet.getHitBox().getY() >= topRightY) {
                lstBullet.remove(bullet);
                return false;
            }
        }
        return false;
    }

    public float getShootingAngle() { return saveShootingAngle; }

    public void changeDirection(float angle) {

        this.saveShootingAngle = angle;

        if (angle < 0) {
            bIsPlayingAnimation = false;
            return;
        }

        bIsPlayingAnimation = true;

        // for example using texture from files
        if (angle >= 25 && angle <= 70) {  // right-down
            animation = AssetManager.getInstance().getAniCharRightDown();
        }
        else if (angle >= 70 && angle <= 115) { // down
            animation = AssetManager.getInstance().getAniCharDown();
        }
        else if (angle >= 115 && angle <= 160) { // down-left
            animation = AssetManager.getInstance().getAniCharDownLeft();
        }
        else if (angle >= 160 && angle <= 205) { // left
            animation = AssetManager.getInstance().getAniCharLeft();
        }
        else if (angle >= 205 && angle <= 250) { // left-up
            animation = AssetManager.getInstance().getAniCharLeftUp();
        }
        else if (angle >= 250 && angle <= 295) { // up
            animation = AssetManager.getInstance().getAniCharUp();
        }
        else if (angle >= 295 && angle <= 340) {  // up-right
            animation = AssetManager.getInstance().getAniCharUpRight();
        }
        else {  // right        // for example using atlas
            animation = AssetManager.getInstance().getAniCharRight();
        }
    }
}
