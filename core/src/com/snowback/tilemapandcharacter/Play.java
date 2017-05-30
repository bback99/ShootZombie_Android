package com.snowback.tilemapandcharacter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by bback99 on 2017-05-14.
 */

public class Play {

    private Player mPlayer;     // for main character
    private ArrayList<Player> mlstPlayers;       // for other players
    private ArrayList<Zombie> mlstZombie;
    private Random random;

    public Play() {
        mPlayer = new Player("Snow");
        mlstZombie = new ArrayList<Zombie>();
        mlstPlayers = new ArrayList<Player>();
        random = new Random();

        for (int i=0; i<3; i++) {
            mlstZombie.add(new Zombie(Math.abs(random.nextInt() % World.width * World.TILESIZE), Math.abs(random.nextInt() % World.height * World.TILESIZE)));
        }
    }

    public void addZombie(float fX, float fY) {
           mlstZombie.add(new Zombie(fX, fY));
    }

    public void addPlayers(String username) {
        mlstPlayers.add(new Player(username));
    }

    public void render(OrthographicCamera camera, SpriteBatch batch) {

        for(Zombie zombie: mlstZombie) {
            zombie.draw(batch);
        }

        mPlayer.draw(batch);

        for(Player player: mlstPlayers) {
            player.draw(batch);
        }
    }

    public Player getPlayer() {
        return mPlayer;
    }

    public void setPlayerPosition(String username, float X, float Y) {

        for(Player player: mlstPlayers) {
            if (player.getUserName() == username) {
                //Gdx.app.log("X: ", Integer.toString(X));//, ", Y: " , Integer.toString(Y));
                mPlayer.setX(X);
                mPlayer.setY(Y);
                //mPlayer.changeDirection(1);
                break;
            }
        }
    }

    public void setPlayPositionLEFT(JoyStick stick) {
        mPlayer.setX(mPlayer.getX()-1);
        if (!stick.bIsShooting())
            mPlayer.changeDirection(180);
        else
            mPlayer.changeDirection(mPlayer.getShootingAngle());
    }

    public void setPlayPositionRIGHT(JoyStick stick) {
        mPlayer.setX(mPlayer.getX()+1);
        if (!stick.bIsShooting())
            mPlayer.changeDirection(0);
        else
            mPlayer.changeDirection(mPlayer.getShootingAngle());
    }

    public void setPlayPositionDOWN(JoyStick stick) {
        mPlayer.setY(mPlayer.getY()-1);
        if (!stick.bIsShooting())
            mPlayer.changeDirection(270);
        else
            mPlayer.changeDirection(mPlayer.getShootingAngle());
    }

    public void setPlayPositionUP(JoyStick stick) {
        mPlayer.setY(mPlayer.getY()+1);
        if (!stick.bIsShooting())
            mPlayer.changeDirection(90);
        else
            mPlayer.changeDirection(mPlayer.getShootingAngle());
    }

    public void dispose () {
        mPlayer.dispose();
        for(Zombie zombie: mlstZombie) {
            zombie.dispose();
        }
    }

    public void updateBullets() {
        if (mPlayer.updateBullets(mlstZombie)) {
            //addZombie();
        }
    }
}
