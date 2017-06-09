package com.snowback.tilemapandcharacter.Network;

import java.util.EventListener;

/**
 * Created by shoong on 2017-06-06.
 */

public interface DataListener extends EventListener {
    void receiveData(DataEvent event);
}
