package com.snowback.tilemapandcharacter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Timer;

import java.util.ArrayList;

/**
 * Created by bback99 on 2017-05-14.
 */

public class JoyStick {
    // for Direction
    private Stage stageDirection;
    private Touchpad tpDirection;
    private Touchpad.TouchpadStyle touchpadStyle;
    private Skin touchpadSkin;
    private Drawable touchDirectionBackground;
    private Drawable touchDirectionKnob;
    // for Shooting
    private Stage stageShooting;
    private Touchpad tpShooting;
    private Drawable touchShootingBackground;
    private Drawable touchShootingKnob;

    public static float blockSpeed = 5.0f;
    private Player player;
    private Player copyPlayer;
    private SpriteBatch spriteBatch;
    private OrthographicCamera camera; //2D camera
    InputMultiplexer multiplexer = new InputMultiplexer();

    private Timer shootingTimer = null;
    private float timeForPlayerLocation = 0.0f;
    Tilemapandcharacter.GameScreen mGameScreen;

    private boolean mIsToogle = false;


   public JoyStick(Player heroSprite, Player copy, OrthographicCamera camera, Tilemapandcharacter.GameScreen gameScreen) {
       this.player = heroSprite;
       this.copyPlayer = copy;
       create(camera);
       mGameScreen = gameScreen;
    }

    public void create (OrthographicCamera camera) {
        spriteBatch = new SpriteBatch();

        // set joystick for Direction
        //Create a touchpad skin
        touchpadSkin = new Skin();
        //Set background image
        touchpadSkin.add("touchBackground", new Texture("resources/touchBackground.png"));
        //Set knob image
        touchpadSkin.add("touchKnob", new Texture("resources/touchKnob.png"));
        //Create TouchPad Style
        touchpadStyle = new Touchpad.TouchpadStyle();
        //Create Drawable's from TouchPad skin
        touchDirectionBackground = touchpadSkin.getDrawable("touchBackground");
        touchDirectionKnob = touchpadSkin.getDrawable("touchKnob");
        //Apply the Drawables to the TouchPad Style
        touchpadStyle.background = touchDirectionBackground;
        touchpadStyle.knob = touchDirectionKnob;
        //Create new TouchPad with the created style
        tpDirection = new Touchpad(10, touchpadStyle);
        //setBounds(x,y,width,height)s
        tpDirection.setBounds(15, 15, 250, 250);

        //Create a Stage and add TouchPad
        stageDirection = new Stage();
        stageDirection.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stageDirection.addActor(tpDirection);
        multiplexer.addProcessor(stageDirection);
        //Gdx.input.setInputProcessor(stageDirection);

        // set joystick for Shooting
        //Create Drawable's from TouchPad skin
        touchShootingBackground = touchpadSkin.getDrawable("touchBackground");
        touchShootingKnob = touchpadSkin.getDrawable("touchKnob");
        //Create new TouchPad with the created style
        tpShooting = new Touchpad(10, touchpadStyle);
        //setBounds(x,y,width,height)s
        tpShooting.setBounds(Gdx.graphics.getWidth()-265, 15, 250, 250);

        //Create a Stage and add TouchPad
        stageShooting = new Stage();
        stageShooting.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stageShooting.addActor(tpShooting);
        multiplexer.addProcessor(stageShooting);
        Gdx.input.setInputProcessor(multiplexer);

        //connect to hero sprite
        //Set position to centre of the screen
        //player.setPosition(Gdx.graphics.getWidth()/2-player.getWidth()/2, Gdx.graphics.getHeight()/2-player.getHeight()/2);
        player.setPosition((World.width*15-player.getWidth())/2.0f, (World.height*15-player.getHeight())/2.0f);
        this.camera = camera;
        camera.update();
    }

    public void render (OrthographicCamera camera) {

        //Move blockSprite with TouchPad
        checkBounds();

        // for character moving
        if (tpDirection.isTouched()) {
            if (!tpShooting.isTouched()) {
                Vector2 v = new Vector2(tpDirection.getKnobPercentX(), tpDirection.getKnobPercentY());
                player.changeDirection(v.angle());
                copyPlayer.changeDirection(v.angle());
            }
        }
        else {
            player.changeDirection(-1);     // means stop animation
            copyPlayer.changeDirection(-1);     // means stop animation
        }

        // for shooting
        if (tpShooting.isTouched()) {
            if (shootingTimer == null) {
                shootingTimer = new Timer();
                shootingTimer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        Vector2 v = new Vector2(tpShooting.getKnobPercentX(), tpShooting.getKnobPercentY());
                        player.AddBullet(player.getX()+tpShooting.getKnobPercentX()+player.getTextureSize()/2, player.getY()+tpShooting.getKnobPercentY()+player.getTextureSize()/5, v.angle());
                        player.changeDirection(v.angle());

                        // send to message
                        mGameScreen.getMessageHandler().notifyPlayerShooting(player.getX()+tpShooting.getKnobPercentX()+player.getTextureSize()/2, player.getY()+tpShooting.getKnobPercentY()+player.getTextureSize()/5, v.angle());
                    }
                }, 0.0f, 0.2f);
            }
        }
        else {
            shootingTimer = null;
            Timer.instance().clear();
        }

        stageDirection.act(Gdx.graphics.getDeltaTime());
        stageDirection.draw();

        stageShooting.act(Gdx.graphics.getDeltaTime());
        stageShooting.draw();
    }

    public Boolean bIsShooting() {
        if (tpShooting.isTouched())
            return true;
        return false;
    }

    public void dispose () {
        spriteBatch.dispose();
    }

    public void checkBounds() {

//        if (tpDirection.isTouched()) {
//            copyPlayer.addMoving(tpDirection.getKnobPercentX(), tpDirection.getKnobPercentY());
//            Gdx.app.log("List size: ", Integer.toString(copyPlayer.getList().size()));
//        }
//        else {
//            ArrayList<Player.MovingPosition> lstTest = copyPlayer.getList();
//            if (lstTest.size() > 0) {
//                Player.MovingPosition data = lstTest.get(0);
//                copyPlayer.setX(copyPlayer.getX() + data.fX*blockSpeed);
//                copyPlayer.setY(copyPlayer.getY() + data.fY*blockSpeed);
//                lstTest.remove(0);
//            }
//        }

        //float bottomLeftX = 0.0f, bottomLeftY = 0.0f, topRightX = (float) Gdx.graphics.getWidth()-player.getWidth(), topRightY = (float) Gdx.graphics.getHeight()-player.getHeight();
        float bottomLeftX = 0.0f, bottomLeftY = 0.0f, topRightX = (float) World.width*15-player.getWidth(), topRightY = (float) World.height*15-player.getHeight();

        if (player.getX() <= bottomLeftX || player.getX() >= topRightX) {
            player.setX(player.getX());
        }
        else {
            float positionX = player.getX() + tpDirection.getKnobPercentX()*blockSpeed;
            if (positionX >= 0 && positionX <= topRightX) {
                player.setX(positionX);
            }
        }

        if (player.getY() <= bottomLeftY || player.getY()+90 >= topRightY) {
            player.setY(player.getY());
        }
        else {
            float positionY = player.getY() + tpDirection.getKnobPercentY()*blockSpeed;
            if (positionY >= 0 && positionY +90 <= topRightY) {
                player.setY(positionY);
            }
        }

        if (tpDirection.isTouched())
            mGameScreen.getMessageHandler().notifyPlayerPosition(tpDirection.getKnobPercentX(), tpDirection.getKnobPercentY(), 0);

//        // send player's position to server
//        if (tpDirection.isTouched()) {
//            timeForPlayerLocation += Gdx.graphics.getDeltaTime();
//            mIsToogle = true;
//        }
//        else {
//            if (mIsToogle) {
//                Vector2 v = new Vector2(tpDirection.getKnobPercentX(), tpDirection.getKnobPercentY());
//                mGameScreen.getMessageHandler().notifyPlayerPosition(player.getX(), player.getY(), v.angle());
//                mIsToogle = false;
//            }
//        }
//
//        if (timeForPlayerLocation >= 0.0001 && tpDirection.isTouched()) {
//            Vector2 v = new Vector2(tpDirection.getKnobPercentX(), tpDirection.getKnobPercentY());
//            mGameScreen.getMessageHandler().notifyPlayerPosition(player.getX(), player.getY(), v.angle());
//            timeForPlayerLocation = 0;
//        }
    }
}
