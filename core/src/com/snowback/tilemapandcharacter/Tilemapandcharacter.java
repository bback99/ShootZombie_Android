package com.snowback.tilemapandcharacter;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.snowback.tilemapandcharacter.Network.DataCallback;
import com.snowback.tilemapandcharacter.UI.HUD;

import org.json.JSONArray;
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

    public class GameScreen implements Screen {

        private OrthographicCamera camera; //2D camera
        private World world;
        private Play mPlay;
        private JoyStick joyStick;
        private com.snowback.tilemapandcharacter.Network.MessageHandler mMessageHandler;
        private HUD hud;

        SpriteBatch batch;

        public static final String CHAT_SERVER_URL = "http://10.0.2.2:3010/";
        //public static final String CHAT_SERVER_URL = "http://10.51.205.75:3010/";
        //public static final String CHAT_SERVER_URL = "http://192.168.0.103:13337/";
        public static final String UserName = "Snow John";

        public com.snowback.tilemapandcharacter.Network.MessageHandler getMessageHandler() {
            return mMessageHandler;
        }

        @Override
        public void show() {

            AssetManager.getInstance().Init();

            batch = new SpriteBatch();
            this.camera = new OrthographicCamera();
            this.camera.setToOrtho(false, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
            this.camera.update();
        }

        private void CreateObjects() {
            world = new World();
            mPlay = new Play(this);
            hud = new HUD(batch, this);
            joyStick = new JoyStick(mPlay.getPlayer(), this.camera, this);

            mMessageHandler = new com.snowback.tilemapandcharacter.Network.MessageHandler(this);
            if (mMessageHandler.connect(CHAT_SERVER_URL)) {
                Gdx.app.log("DEBUG", "Connected!!!");
                mMessageHandler.requestAttempLogin(UserName, mPlay.getPlayer().getX(), mPlay.getPlayer().getY(), new DataCallback() {
                    @Override
                    public void responseData(JSONObject message) {
                        Gdx.app.log("INFO", "RecvData : " + message.toString());
                    }
                });
            }
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
                mPlay.updateEnemy(delta);

                batch.setProjectionMatrix(hud.stage.getCamera().combined);
                hud.Render();
                hud.update(delta);
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
            mMessageHandler.disconnect();
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

            if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                Vector3 vector3 = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
                camera.unproject(vector3);
                this.mPlay.getPlayer().addMovingPosition(vector3.x, vector3.y, 0.0f);
            }
        }

        @Override public void resize(int width, int height) {
            if (mListener != null) {
                mListener.example("resize is called.");
            }
        }

        public Play getPlay(){
            return mPlay;
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
            else if (type == "answer login") {      // answer to add userlist
                JSONObject data = (JSONObject) args[0];
                try {
                    int numUsers = data.getInt("numUsers");
                    JSONArray users = data.getJSONArray("aryUsers");
                    if(users != null && users.length() > 0) {
                        for(int i=0; i<users.length(); i++) {
                            JSONObject objectInArray = users.getJSONObject(i);
                            Gdx.app.log("SOCKET.IO", "username: " + objectInArray.getString("user_name"));
                            Player newPlayer = new Player(false, objectInArray.getString("user_name"), objectInArray.getDouble("posX"), objectInArray.getDouble("posY"));
                            this.mPlay.addPlayers(newPlayer);
                        }
                    }
                } catch (JSONException e) {
                    return;
                }
            }
            else if (type == "notify login") {      // notify to add new_user
                JSONObject data = (JSONObject) args[0];
                try {
                    int numUsers = data.getInt("numUsers");
                    JSONObject newUser = data.getJSONObject("newUser");
                    Player newPlayer = new Player(false, newUser.getString("user_name"), newUser.getDouble("posX"), newUser.getDouble("posY"));
                    this.mPlay.addPlayers(newPlayer);
                } catch (JSONException e) {
                    return;
                }
            }
            else if (type == "notify moving") {
                JSONObject packet = (JSONObject) args[0];
                String username;
                float fX, fY, fAngle;
                try {
                    JSONObject data = packet.getJSONObject("body");
                    username = data.getString("username");
                    fX = Float.valueOf(String.valueOf(data.getString("X")));
                    fY = Float.valueOf(String.valueOf(data.getString("Y")));
                    fAngle = Float.valueOf(String.valueOf(data.getString("angle")));
                    //Gdx.app.log("SOCKET.IO", "name: " + username + ", position X: " + fX + ", position Y: " + fY + ", angle: " + fAngle);
                    this.mPlay.setPlayerPosition(username, fX, fY, fAngle);
                } catch (JSONException e) {
                    return;
                }
            }
        }
    }
}
