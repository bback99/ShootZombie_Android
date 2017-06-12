package com.snowback.tilemapandcharacter.Network;
import com.badlogic.gdx.Gdx;
import com.snowback.tilemapandcharacter.Tilemapandcharacter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shoong on 2017-06-06.
 * For define packet message between clients and servers
 * For reqeust and answer message to use request() method on SocketManager class
 * For notify message to use notifyMessage() method on SocketManager class
 */

public class MessageHandler extends SocketManager{

    public MessageHandler(Tilemapandcharacter.GameScreen main) {
        super(main);
        initOnReviceDataHandler();
    }

    public void initOnReviceDataHandler() {
        onNotifyLogin();
        onNotifyPlayerLocation();
        onUserLeaveFromRoom();
    }

    public void onNotifyLogin() {
        List<DataListener> lstListener = new ArrayList<DataListener>();
        lstListener.add(new DataListener() {
            @Override
            public void receiveData(DataEvent event) {
                MessageHandler.super.mMain.socketHandler("notify login", event.getMessage());
            }
        });
        super.mMapListeners.put("onNotifyLogin", lstListener);
    }

    public void onUserLeaveFromRoom() {
        List<DataListener> lstListener = new ArrayList<DataListener>();
        lstListener.add(new DataListener() {
            @Override
            public void receiveData(DataEvent event) {
                MessageHandler.super.mMain.socketHandler("notify moving", event.getMessage());
            }
        });
        super.mMapListeners.put("onUserLeaveFromRoom", lstListener);
    }

    public void onNotifyPlayerLocation() {
        List<DataListener> lstListener = new ArrayList<DataListener>();
        lstListener.add(new DataListener() {
            @Override
            public void receiveData(DataEvent event) {
                MessageHandler.super.mMain.socketHandler("notify moving", event.getMessage());
            }
        });
        super.mMapListeners.put("onNotifyPlayerLocation", lstListener);
    }

    public void requestAttempLogin(String userName, float X, float Y, DataCallback func) {

        JSONObject requestLogin = new JSONObject();
        try {
            requestLogin.put("rid", 1);
            requestLogin.put("username", userName);
            requestLogin.put("X", X);
            requestLogin.put("Y", Y);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request("connector.entryHandler.entry", requestLogin, func);
    }

    public void notifyPlayerPosition(float fX, float fY, float angle) {
        JSONObject ntfUserPosition = new JSONObject();
        try {
            ntfUserPosition.put("X", fX);
            ntfUserPosition.put("Y", fY);
            ntfUserPosition.put("angle", angle);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // if use this way to send just for notification, then will get RPC error on server side. How to solve?
        // notifyMessage("room.roomHandler.notifyPlayerLocation", ntfUserPosition);
        request("room.roomHandler.notifyPlayerLocation", ntfUserPosition, new DataCallback() {

            @Override
            public void responseData(JSONObject message) {
                //Gdx.app.log("GDX", "got a message");
            }
        });
    }
}
