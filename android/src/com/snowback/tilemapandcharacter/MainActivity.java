package com.snowback.tilemapandcharacter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    private Button btnStartGame;
    private Button btnConnect;
    private LinearLayout layout;
    //public static final String CHAT_SERVER_URL = "https://socketio-chat.now.sh/";
    public static final String CHAT_SERVER_URL = "http://10.0.2.2:13337/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layout = (LinearLayout) findViewById(R.id.ID_LAYOUT);
        btnStartGame = new Button(this);
        layout.addView(btnStartGame);
        btnStartGame.setText("Start Game");
        btnStartGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), AndroidLauncher.class));
            }
        });

        layout = (LinearLayout) findViewById(R.id.ID_LAYOUT);
        btnConnect = new Button(this);
        layout.addView(btnConnect);
        btnConnect.setText("Connect to Server");
        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //SocketManager.getInstance().attempLogin();
            }
        });

        //SocketManager.getInstance().connect(CHAT_SERVER_URL);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //SocketManager.getInstance().disconnect();
    }
}
