package com.snowback.tilemapandcharacter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;

/**
 * Created by bback99 on 2017-05-14.
 */

public class Player extends Sprite {

    public class MovingPosition {
        public float fX;
        public float fY;
        public float fAngle;

        MovingPosition(float x, float y, float angle) {
            fX = x;
            fY = y;
            fAngle = angle;
        }
    }

    private String mUserName;
    private Animation mAnimation;
    private float mTimePassed = 0;
    private boolean mIsPlayingAnimation = false;
    private ArrayList<Bullet> mlstBullet = new ArrayList<Bullet>();
    private float mTextureSize = 0.0f;
    private float mSaveShootingAngle = 0.0f;
    private ArrayList<MovingPosition> mlstMovingPosition = new ArrayList<MovingPosition>();
    private float mMovingTime = 5.0f;
    private boolean mIsMainPlayer = false;

    private Rectangle hitBox;
    private Integer health;
    private Integer weaponPower;


    public Player(boolean isMainPlayer, String username, double posX, double posY) {
        mIsMainPlayer = isMainPlayer;
        mUserName = username;
        mAnimation = AssetManager.getInstance().getAniCharRight();

        // just for getting image length
        Texture img1 = new Texture("players/character10/09.png");
        mTextureSize = img1.getWidth();

        this.setX((float) posX);
        this.setY((float) posY);

        health = 100;
        weaponPower = 2;
        hitBox = new Rectangle(getX(),getY(),100.f,100.f);
    }

    public void addMovingPosition(float X, float Y, float angle) {
        mlstMovingPosition.add(new MovingPosition(X, Y, angle));
    }

    public String getUserName() { return mUserName; }

    public float getTextureSize() {
        return mTextureSize;
    }

    public void AddBullet(float x, float y, float angle) {
        //lstBullet.add(new Bullet(getX(), getY(), angle));
        mlstBullet.add(new Bullet(x, y, angle));
        //Gdx.app.log("Position: ", "X : " +  x + ", Y: " + y + ", Angle: " + angle);
    }

    public void draw(SpriteBatch spritebatch) {
        //update hitbox's position.
        hitBox.x = getX();
        hitBox.y = getY();

        //Debug purpose.
        if(health<=0){
            health = 100;
        }

        if(mIsPlayingAnimation) {
            mTimePassed += Gdx.graphics.getDeltaTime();
        }

        if (mAnimation == null) {
            mAnimation = AssetManager.getInstance().getAniCharRight();
        }
        else {
            if (mIsMainPlayer) {
                spritebatch.draw((TextureRegion) mAnimation.getKeyFrame(mTimePassed, true), getX(), getY());
            }
            else {
                if (mlstMovingPosition.size() >= 1) {
                    MovingPosition posPrev = mlstMovingPosition.get(0);
                    float dx = posPrev.fX - getX();
                    float dy = posPrev.fX - getY();
                    float length = (float) Math.sqrt(dx*dx + dy*dy);
//                    setX(getX() + 300 * (float)Math.cos(pos.fAngle*Math.PI/180) * mTimePassed);
//                    setY(getY() + 300 * (float)Math.cos(pos.fAngle*Math.PI/180) * mTimePassed);
                    setX(getX()+(dx/length));
                    setY(getY()+(dy/length));
                    changeDirection(posPrev.fAngle);
                    spritebatch.draw((TextureRegion) mAnimation.getKeyFrame(mTimePassed, true), getX(), getY());

                    if ((mMovingTime -= Gdx.graphics.getDeltaTime()) <= 0) {
                        mMovingTime = 5.0f;
                        mlstMovingPosition.remove(0);
                    }
                }
            }
        }

        for(Bullet bullet: mlstBullet) {
            bullet.draw(spritebatch);
        }
    }

    public Boolean updateBullets(ArrayList<Zombie> lstZombie) {
        for(Bullet bullet: mlstBullet) {
            bullet.update(Gdx.graphics.getDeltaTime());

            // check to collide any zombies
            for(Zombie zombie: lstZombie) {
                if(zombie.getHitBox().overlaps(bullet.getHitBox())) {
                    zombie.hit(weaponPower);
                    if (zombie.getHealth()<=0) {
                        lstZombie.remove(zombie);
                        return true;
                    }
                    mlstBullet.remove(bullet);
                    return false;
                }
            }

            // check bounds and remove it in lstBullet
            float bottomLeftX = 0.0f, bottomLeftY = 0.0f, topRightX = (float) World.width*15, topRightY = (float) World.height*15;
            if (bullet.getHitBox().getX() <= bottomLeftX || bullet.getHitBox().getX() >= topRightX) {
                mlstBullet.remove(bullet);
                return false;
            }
            else if (bullet.getHitBox().getY() <= bottomLeftY || bullet.getHitBox().getY() >= topRightY) {
                mlstBullet.remove(bullet);
                return false;
            }
        }
        return false;
    }

    public float getShootingAngle() { return mSaveShootingAngle; }

    public void changeDirection(float angle) {

        this.mSaveShootingAngle = angle;

        if (angle < 0) {
            mIsPlayingAnimation = false;
            return;
        }

        mIsPlayingAnimation = true;

        // for example using texture from files
        if (angle >= 25 && angle <= 70) {  // right-down
            mAnimation = AssetManager.getInstance().getAniCharRightDown();
        }
        else if (angle >= 70 && angle <= 115) { // down
            mAnimation = AssetManager.getInstance().getAniCharDown();
        }
        else if (angle >= 115 && angle <= 160) { // down-left
            mAnimation = AssetManager.getInstance().getAniCharDownLeft();
        }
        else if (angle >= 160 && angle <= 205) { // left
            mAnimation = AssetManager.getInstance().getAniCharLeft();
        }
        else if (angle >= 205 && angle <= 250) { // left-up
            mAnimation = AssetManager.getInstance().getAniCharLeftUp();
        }
        else if (angle >= 250 && angle <= 295) { // up
            mAnimation = AssetManager.getInstance().getAniCharUp();
        }
        else if (angle >= 295 && angle <= 340) {  // up-right
            mAnimation = AssetManager.getInstance().getAniCharUpRight();
        }
        else {  // right        // for example using atlas
            mAnimation = AssetManager.getInstance().getAniCharRight();
        }
    }

    public Integer getHealth(){
        return health;
    }
    public Rectangle getHitBox(){
        return hitBox;
    }

    public void Hit(int i) {
        health-=i;
    }
}
