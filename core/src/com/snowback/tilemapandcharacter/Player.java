package com.snowback.tilemapandcharacter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

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
    private float mMovingTime = 3.0f;
    private boolean mIsMainPlayer = false;

    private Rectangle hitBox;
    private Integer health;
    private Integer weaponPower;
    private MovingPosition posPrev = null;


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
        if (mlstMovingPosition.size() <= 0) {
            mlstMovingPosition.add(new MovingPosition(X, Y, angle));
        }
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

        if (mIsPlayingAnimation) {
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
                mTimePassed += Gdx.graphics.getDeltaTime();

                if (mlstMovingPosition.size() >= 1) {
                    posPrev = mlstMovingPosition.get(0);
                }
                else {
                    spritebatch.draw((TextureRegion) mAnimation.getKeyFrame(mTimePassed, true), getX(), getY());
                    posPrev = null;
                }

                if (posPrev != null) {
                    changeDirection(posPrev.fAngle);

                    float dx = posPrev.fX - getX();
                    float dy = posPrev.fY - getY();
                    double length = Math.sqrt(dx * dx + dy * dy);
                    if (length <= 0.5) {
                        mlstMovingPosition.remove(0);
                    }
                    else {
                        //float delta = Gdx.graphics.getDeltaTime();
                        Vector2 normal = new Vector2(dx / (float) length, dy / (float) length);
                        float x = getX();
                        x += normal.x;
                        float y = getY();
                        y += normal.y;

                        setX(x);
                        setY(y);

                        spritebatch.draw((TextureRegion) mAnimation.getKeyFrame(mTimePassed, true), getX(), getY());
                        Gdx.app.log("Moving Position: ", "PrevPos X : " + posPrev.fX + ", Y: " + posPrev.fY + ", length: " + length);
                        Gdx.app.log("Moving Position: ", "CurrPos X : " + getX() + ", Y: " + getY() + ", Angle: " + posPrev.fAngle);
                        Gdx.app.log("Moving Position: ", "MovingTime : " + mMovingTime + ", TimePassed: " + mTimePassed);
                    }
                }
            }
//            else {
//                if (mlstMovingPosition.size() >= 1) {
//                    if(mIsPlayingAnimation) {
//                        mTimePassed += Gdx.graphics.getDeltaTime();
//                    }
//
//                    MovingPosition posPrev = mlstMovingPosition.get(0);
//                    changeDirection(posPrev.fAngle);
//                    float dx = posPrev.fX - getX();
//                    float dy = posPrev.fY - getY();
//                    float length = (float) Math.sqrt(dx*dx + dy*dy);
//                    //float delta = Gdx.graphics.getDeltaTime();
//                    setX(getX()+(dx/length));
//                    setY(getY()+(dy/length));
//
//                    spritebatch.draw((TextureRegion) mAnimation.getKeyFrame(mTimePassed, true), getX(), getY());
//                    Gdx.app.log("Moving Position: ", "PrevPos X : " +  posPrev.fX + ", Y: " + posPrev.fY + ", length: " + length);
//                    Gdx.app.log("Moving Position: ", "CurrPos X : " +  getX() + ", Y: " + getY() + ", Angle: " + posPrev.fAngle);
//                    Gdx.app.log("Moving Position: ", "MovingTime : " +  mMovingTime + ", TimePassed: " + mTimePassed);
//
//                    if ((mMovingTime -= mTimePassed) <= 0) {
//                        mMovingTime = 1.0f;
//                        mlstMovingPosition.remove(0);
//                    }
//                    else {
//                        Gdx.app.log("mMovingTime ", "time: " + mMovingTime);
//                    }
//                }
//                else {
//                    mIsPlayingAnimation = false;
//                    spritebatch.draw((TextureRegion) mAnimation.getKeyFrame(mTimePassed, true), getX(), getY());
//                }
//            }
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
