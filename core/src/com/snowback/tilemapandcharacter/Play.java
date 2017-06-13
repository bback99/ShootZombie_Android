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

    private Player mPlayer;
    private ArrayList<Player> mlstPlayers;       // for other players
    private ArrayList<Zombie> mlstZombie;
    private Random random;

    public Play(Tilemapandcharacter.GameScreen main) {
        mPlayer = new Player(true, main.UserName, 0, 0);       // for main player
        mlstZombie = new ArrayList<Zombie>();
        mlstPlayers = new ArrayList<Player>();
        random = new Random();

        for (int i=0; i<3; i++) {
            addZombie(Math.abs(random.nextInt() % World.width * World.TILESIZE), Math.abs(random.nextInt() % World.height * World.TILESIZE));
        }
    }

    public void addZombie(float fX, float fY) {
        mlstZombie.add(new Zombie(fX, fY));
    }

    public void addPlayers(Player newPlayer) {
        if (newPlayer.getUserName() != mPlayer.getUserName())
            mlstPlayers.add(newPlayer);
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

    public void setPlayerPosition(String username, float X, float Y, float angle) {

        for(Player player: mlstPlayers) {
            if (player.getUserName().equals(username)) {
                player.addMovingPosition(X, Y, angle);
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

    public void updateBullets() {
        if (mPlayer.updateBullets(mlstZombie)) {
            //addZombie();
        }
    }

    public void updateEnemy(float dt) {
        for(Zombie zombie: mlstZombie){
            zombie.update(dt, mPlayer);
        }
    }

    public ArrayList<Zombie> getZombie() {
        return mlstZombie;
    }
}
