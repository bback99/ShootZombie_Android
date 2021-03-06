package com.snowback.tilemapandcharacter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.snowback.tilemapandcharacter.Network.DataCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

/**
 * Created by bback99 on 2017-05-14.
 */

public class Play {

    private Player mPlayer;
    private Player mCopyPlayer;
    private ArrayList<Player> mlstPlayers;       // for other players
    private ArrayList<Zombie> mlstZombie;
    private Random random;
    private Tilemapandcharacter.GameScreen mMain;

    public Play(Tilemapandcharacter.GameScreen main) {
        mMain = main;
        mPlayer = new Player(true, main.UserName, 0, 0);       // for main player
        mCopyPlayer = new Player(true, main.UserName, 300, 300);       // for main player
        mlstZombie = new ArrayList<Zombie>();
        mlstPlayers = new ArrayList<Player>();
        random = new Random();

//        for (int i=0; i<3; i++) {
//            addZombie(i, Math.abs(random.nextInt() % World.width * World.TILESIZE), Math.abs(random.nextInt() % World.height * World.TILESIZE), 5);
//        }
    }

    public void removeMonster(int index) {
        for(Zombie zombie: mlstZombie) {
            if (index == zombie.getMonsterIndex()) {
                Gdx.app.log("ERROR", "Zombie killed: " + index);
                zombie.setDead();
                break;
            }
        }
    }

    public void addZombie(int index, float fX, float fY, int health) {
        mlstZombie.add(new Zombie(index, fX, fY, health));
    }

    public void chasePlayer(int mobIndex, float fX, float fY) {
        for(Zombie zombie: mlstZombie) {
            if (zombie.getMonsterIndex() == mobIndex) {
                zombie.setHitBox(fX, fY);
                return;
            }
        }
    }

    public void addBullets(String username, float x, float y, float angle) {
        for(Player player: mlstPlayers) {
            if (player.getUserName().equals(username)) {
                player.AddBullet(x, y, angle);
            }
        }
    }

    public void addPlayers(Player newPlayer) {
        if (newPlayer.getUserName().equals(mPlayer.getUserName()) == false)
            mlstPlayers.add(newPlayer);
    }

    public void removePlayers(String username) {
        for(Player player: mlstPlayers) {
            if (player.getUserName().equals(username)) {
                player.setDead();
                break;
            }
        }
    }

    public void render(OrthographicCamera camera, SpriteBatch batch) {
        // in order to remove safely, need to use iterator
//        Iterator<Zombie> ite = mlstZombie.iterator();
//        while(ite.hasNext()) {
//            Zombie zombie = ite.next();
//            if (!zombie.isAlive()) {
//                ite.remove();
//            }
//            else {
//                zombie.draw(batch);
//            }
//        }
        for(int i=0; i<mlstZombie.size(); i++) {
            if (!mlstZombie.get(i).isAlive()) {
                mlstZombie.remove(i);
            }
            else
                mlstZombie.get(i).draw(batch);
        }

        mPlayer.draw(batch);
        //mCopyPlayer.draw(batch);

//        // in order to remove safely, need to use iterator
//        Iterator<Player> ite2 = mlstPlayers.iterator();
//        while(ite2.hasNext()) {
//            Player player = ite2.next();
//            if (!player.isAlive()) {
//                ite2.remove();
//            }
//            else {
//                player.draw(batch);
//            }
//        }

        for(int i=0; i<mlstPlayers.size(); i++) {
            if (!mlstPlayers.get(i).isAlive()) {
                mlstPlayers.remove(i);
            }
            else
                mlstPlayers.get(i).draw(batch);
        }
    }

    public Player getPlayer() {
        return mPlayer;
    }

    public Player getCopyPlayer() {
        return mCopyPlayer;
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
        int monsterIndex = mPlayer.updateBullets(mlstZombie);
        if (monsterIndex >= 0) {
            removeMonster(monsterIndex);
            mMain.getMessageHandler().requestKilledMonster(monsterIndex, new DataCallback() {
                @Override
                public void responseData(JSONObject message) {
                }
            });
        }

        for(Player player: mlstPlayers) {
            player.updateBullets(mlstZombie);
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
