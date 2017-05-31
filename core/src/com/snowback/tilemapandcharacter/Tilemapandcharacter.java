package com.snowback.tilemapandcharacter;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import org.json.JSONException;
import org.json.JSONObject;

public class Tilemapandcharacter extends Game {

    public interface Listener {
        void example(String data);
    }

    private Listener mListener;

    public Tilemapandcharacter(Listener listener) {
        mListener = listener;
    }

    @Override
    public void create() {
        //set our GameScreen as our active screen
        setScreen(new GameScreen());
    }

    public class GameScreen implements Screen, ISocketListener {

        private OrthographicCamera camera; //2D camera
        private World world;
        private Play mPlay;
        private JoyStick joyStick;
        private SocketManager mSocketManager;

        SpriteBatch batch;

        public static final String CHAT_SERVER_URL = "http://10.0.2.2:13337/";
        private int currentLoad;

        public SocketManager getSocketManager() {
            return mSocketManager;
        }

        @Override
        public void show() {

            AssetManager.getInstance().Init();

            batch = new SpriteBatch();
            this.camera = new OrthographicCamera();
            this.camera.setToOrtho(false, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
            this.camera.update();

            mSocketManager = new SocketManager(this);
            mSocketManager.connect(CHAT_SERVER_URL);
        }

        private void CreateObjects() {
            world = new World();
            mPlay = new Play();
            joyStick = new JoyStick(mPlay.getPlayer(), this.camera, this);
        }

        @Override
        public void render(float delta) {

            // this sequence is very important for using libGDX AssetManager
            if (AssetManager.getInstance().getAssetManager().update()) {        // waiting for loading resources
                if (!AssetManager.getInstance().isbIsLoaded()) {                // if done?
                    AssetManager.getInstance().makeResource();                  // make a resources like animations that was needed
                    CreateObjects();                                            // create objects like world, player, etc.
                }

                //clear screen
                Gdx.gl.glClearColor(1, 1, 1, 1);
                Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

                this.camera.position.x = mPlay.getPlayer().getX();
                this.camera.position.y = mPlay.getPlayer().getY();
                camera.update();

                batch.setProjectionMatrix(camera.combined);

                world.render(camera);
                joyStick.render(camera);

                batch.begin();
                generalUpdate();
                mPlay.render(camera, batch);
                batch.end();

                // update bullet
                mPlay.updateBullets();
            }
            else{
                Gdx.app.log("Loading Resources", Float.toString(AssetManager.getInstance().getAssetManager().getProgress()));

                // later on display loading bar here
            }
        }

        @Override
        public void pause() {

        }

        @Override
        public void resume() {

        }

        @Override
        public void hide() {

        }

        @Override
        public void dispose() {
            world.dispose();
            joyStick.dispose();
            //SocketManager.getInstance().disconnect();
            mSocketManager.disconnect();
        }

        public void generalUpdate(){
            if(Gdx.input.isKeyPressed(Input.Keys.D) || (Gdx.input.isKeyPressed(Input.Keys.LEFT)))
            {
                mPlay.setPlayPositionLEFT(joyStick);
            }

            if(Gdx.input.isKeyPressed(Input.Keys.A) || (Gdx.input.isKeyPressed(Input.Keys.RIGHT)))
            {
                mPlay.setPlayPositionRIGHT(joyStick);
            }

            if(Gdx.input.isKeyPressed(Input.Keys.S) || (Gdx.input.isKeyPressed(Input.Keys.DOWN)))
            {
                mPlay.setPlayPositionDOWN(joyStick);
            }

            if(Gdx.input.isKeyPressed(Input.Keys.W) || (Gdx.input.isKeyPressed(Input.Keys.UP)))
            {
                mPlay.setPlayPositionUP(joyStick);
            }
        }

        @Override public void resize(int width, int height) {
            if (mListener != null) {
                mListener.example("resize is called.");
            }
        }

        public void socketHandler(String type, Object... args) {
            if (type == "notify spawn zombie") {
                JSONObject data = (JSONObject) args[0];
                try {
                    JSONObject position = data.getJSONObject("zombiePosition");
                    float fX = position.getInt("X");
                    float fY = position.getInt("Y");
                    Gdx.app.log("SOCKET.IO", "position: " + fX + ", " + fY);
                    this.mPlay.addZombie(fX, fY);
                } catch (JSONException e) {
                    return;
                }
            }
            else if (type == "notify login") {
                JSONObject data = (JSONObject) args[0];
                try {
                    String username = data.getString("username");
                    int numbers = data.getInt("numUsers");
                    Gdx.app.log("SOCKET.IO", "username: " + username + ", usercount: " + numbers);
                    this.mPlay.addPlayers(username);
                } catch (JSONException e) {
                    return;
                }
            }
            else if (type == "notify moving") {
                JSONObject data = (JSONObject) args[0];
                String username;
                float fX, fY;
                try {
                    username = data.getString("username");
                    fX = Float.valueOf(String.valueOf(data.getString("X")));
                    fY = Float.valueOf(String.valueOf(data.getString("Y")));
                    Gdx.app.log("SOCKET.IO", "name: " + username + ", position X: " + fX + ", position Y: " + fY);
                    this.mPlay.setPlayerPosition(username, fX, fY);
                } catch (JSONException e) {
                    return;
                }
            }
        }
    }
}
