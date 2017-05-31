package com.snowback.tilemapandcharacter;

import com.badlogic.gdx.Gdx;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * Created by bback99 on 2017-05-27.
 */

interface ISocketListener {
    void socketHandler(String type, Object... args);
}

public class SocketManager {

    //private static SocketManager mInstance = null;
    private Socket mSocket;
    private Boolean isConnected = true;
    private List<Message> mMessages = new ArrayList<Message>();
    private String mUsername;
    private Tilemapandcharacter.GameScreen mMain;

    public interface CallbackFunc{
        void callback(Object... args);
    }

//    protected SocketManager() {
//        // Exists only to defeat instantiation.
//    }

//    public static SocketManager getInstance() {
//        if(mInstance == null) {
//            mInstance = new SocketManager();
//        }
//        return mInstance;
//    }

    public SocketManager(Tilemapandcharacter.GameScreen main) {
        mMain = main;
    }

    public void connect(String address) {
        try {
            //mSocket = IO.socket("https://socketio-chat.now.sh/");
            mSocket = IO.socket(address);//"http://10.0.2.2:3000/");

            mSocket.on(Socket.EVENT_CONNECT, onConnect);
            mSocket.on(Socket.EVENT_DISCONNECT, onDisconnect);
            mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
            mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
            mSocket.on("notify login", onNotifyLogin);
            mSocket.on("notify moving", onNotifyMoving);
            mSocket.on("notify spawn zombie", onNotifySpawnZombie);
            mSocket.connect();

            mSocket.on("answer login", onAnswerLogin);

            attempLogin();

        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public void disconnect() {
        mSocket.disconnect();
        mSocket.off(Socket.EVENT_CONNECT, onConnect);
        mSocket.off(Socket.EVENT_DISCONNECT, onDisconnect);
        mSocket.off(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.off(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        mSocket.off("notify login", onNotifyLogin);
        mSocket.off("notify moving", onNotifyMoving);
        mSocket.off("notify spawn zombie", onNotifySpawnZombie);
    }

//    public addCallbackFunc(String socketEvent, new CallbackFunc(} ) {
//        Emitter.Listener listener = new Emitter.Listener() {
//            @Override
//            public void call(Object... args) {
//
//            }
//        };
//        mSocket.on(socketEvent, listener);
//    }
//
//    public removeCallbackFunc(String socketEvent, Emitter.Listener listener) {
//        mSocket.off(socketEvent, listener);
//    }

    public Socket getSocket() {
        return mSocket;
    }
    public Boolean isConnected() { return isConnected; }

    public void attempLogin() {
        mSocket.emit("request login", "Snow");
    }
    public void sendPlayerPosition(float fX, float fY) {
        JSONObject position = new JSONObject();
        try {
            position.put("X", fX);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            position.put("Y", fY);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mSocket.emit("request moving", position);
    }

    // When Server is running again
    private Emitter.Listener onConnect = new Emitter.Listener() {

        @Override
        public void call(Object... args) {
            if (!isConnected) {
                mSocket.emit("request login", mUsername);
                isConnected = true;
            }
        }
    };

    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            isConnected = false;
        }
    };

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            int i = 0;
            i = 100;
        }
    };

    private Emitter.Listener onAnswerLogin = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject data = (JSONObject) args[0];

            int numUsers;
            try {
                numUsers = data.getInt("numUsers");
                JSONObject position = data.getJSONObject("zombiePosition");
                float fX = position.getInt("X");
                float fY = position.getInt("Y");
                Gdx.app.log("SOCKET.IO", "users: " + numUsers);
                Gdx.app.log("SOCKET.IO", "position: " + fX + ", " + fY);
            } catch (JSONException e) {
                Gdx.app.log("SOCKET.IO", "Exception: " + e.toString());
                return;
            }
        }
    };

    private Emitter.Listener onNotifyMoving = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            mMain.socketHandler("notify moving", args);
        }
    };

    private Emitter.Listener onNotifyLogin = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            mMain.socketHandler("notify login", args);
        }
    };

    private Emitter.Listener onNotifySpawnZombie = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            mMain.socketHandler("notify spawn zombie", args);
        }
    };
}
