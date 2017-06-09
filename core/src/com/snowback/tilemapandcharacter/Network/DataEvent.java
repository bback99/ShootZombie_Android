package com.snowback.tilemapandcharacter.Network;

import org.json.JSONObject;

import java.util.EventObject;

/**
 * Created by shoong on 2017-06-06.
 */

public class DataEvent extends EventObject {
    private JSONObject message;

    public JSONObject getMessage() {
        return message;
    }

    public void setMessage(JSONObject message) {
        this.message = message;
    }

    public DataEvent(Object source, JSONObject message) {
        super(source);
        this.message = message;
    }
}
