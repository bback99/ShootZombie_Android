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
    private TextureAtlas walking;
    private Animation animation;
    private float timePassed = 0;
    private boolean bIsPlayingAnimation = false;
    private ArrayList<Bullet> lstBullet = new ArrayList<Bullet>();
    private float textureSize = 0.0f;
    private float saveShootingAngle = 0.0f;

    public Player(String username) {
        mUserName = username;
        walking = new TextureAtlas(Gdx.files.internal("players/character10/right/walkingRight.atlas"));
        animation = new Animation(1/10f, walking.getRegions());
        Texture img1 = new Texture("players/character10/09.png");
        textureSize = img1.getWidth();
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
        spritebatch.draw((TextureRegion) animation.getKeyFrame(timePassed, true), getX(), getY());
        //Gdx.app.log("X: ", Float.toString(getX()) + ", Y: " + Float.toString(getY()));

        for(Bullet bullet: lstBullet) {
            bullet.draw(spritebatch);
        }

        //Gdx.app.log("Bullet Count: ", Integer.toString(lstBullet.size()));
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

    public void dispose () {
        walking.dispose();
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
            Texture img1 = new Texture("players/character10/09.png");
            Texture img2 = new Texture("players/character10/10.png");
            Texture img3 = new Texture("players/character10/11.png");
            Texture img4 = new Texture("players/character10/12.png");
            TextureRegion[] frames = new TextureRegion[4];
            frames[0] = new TextureRegion(img1);
            frames[1] = new TextureRegion(img2);
            frames[2] = new TextureRegion(img3);
            frames[3] = new TextureRegion(img4);
            animation = new Animation(1/10f, frames);
        }
        else if (angle >= 70 && angle <= 115) { // down
            Texture img1 = new Texture("players/character10/20.png");
            Texture img2 = new Texture("players/character10/21.png");
            Texture img3 = new Texture("players/character10/22.png");
            TextureRegion[] frames = new TextureRegion[3];
            frames[0] = new TextureRegion(img1);
            frames[1] = new TextureRegion(img2);
            frames[2] = new TextureRegion(img3);
            animation = new Animation(1/10f, frames);
        }
        else if (angle >= 115 && angle <= 160) { // down-left
            Texture img1 = new Texture("players/character10/13.png");
            Texture img2 = new Texture("players/character10/14.png");
            Texture img3 = new Texture("players/character10/15.png");
            Texture img4 = new Texture("players/character10/16.png");
            TextureRegion[] frames = new TextureRegion[4];
            frames[0] = new TextureRegion(img1);
            frames[1] = new TextureRegion(img2);
            frames[2] = new TextureRegion(img3);
            frames[3] = new TextureRegion(img4);
            animation = new Animation(1/10f, frames);
        }
        else if (angle >= 160 && angle <= 205) { // left
            Texture img1 = new Texture("players/character10/05.png");
            Texture img2 = new Texture("players/character10/06.png");
            Texture img3 = new Texture("players/character10/07.png");
            Texture img4 = new Texture("players/character10/08.png");
            TextureRegion[] frames = new TextureRegion[4];
            frames[0] = new TextureRegion(img1);
            frames[1] = new TextureRegion(img2);
            frames[2] = new TextureRegion(img3);
            frames[3] = new TextureRegion(img4);
            animation = new Animation(1/10f, frames);
        }
        else if (angle >= 205 && angle <= 250) { // left-up
            Texture img1 = new Texture("players/character10/05.png");
            Texture img2 = new Texture("players/character10/06.png");
            Texture img3 = new Texture("players/character10/07.png");
            Texture img4 = new Texture("players/character10/08.png");
            TextureRegion[] frames = new TextureRegion[4];
            frames[0] = new TextureRegion(img1);
            frames[1] = new TextureRegion(img2);
            frames[2] = new TextureRegion(img3);
            frames[3] = new TextureRegion(img4);
            animation = new Animation(1/10f, frames);
        }
        else if (angle >= 250 && angle <= 295) { // up
            Texture img1 = new Texture("players/character10/17.png");
            Texture img2 = new Texture("players/character10/18.png");
            Texture img3 = new Texture("players/character10/19.png");
            TextureRegion[] frames = new TextureRegion[3];
            frames[0] = new TextureRegion(img1);
            frames[1] = new TextureRegion(img2);
            frames[2] = new TextureRegion(img3);
            animation = new Animation(1/10f, frames);
        }
        else if (angle >= 295 && angle <= 340) {  // up-right
            Texture img1 = new Texture("players/character10/01.png");
            Texture img2 = new Texture("players/character10/02.png");
            Texture img3 = new Texture("players/character10/03.png");
            Texture img4 = new Texture("players/character10/04.png");
            TextureRegion[] frames = new TextureRegion[4];
            frames[0] = new TextureRegion(img1);
            frames[1] = new TextureRegion(img2);
            frames[2] = new TextureRegion(img3);
            frames[3] = new TextureRegion(img4);
            animation = new Animation(1/10f, frames);
        }
        else {  // right        // for example using atlas
            walking = new TextureAtlas(Gdx.files.internal("players/character10/right/walkingRight.atlas"));
            animation = new Animation(1/10f, walking.getRegions());
        }
    }
}
