package com.snowback.tilemapandcharacter;

import android.os.Bundle;
import android.util.Log;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class AndroidLauncher extends AndroidApplication implements Tilemapandcharacter.Listener {

	private static final String TAG = "AndroidLauncher";

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new Tilemapandcharacter(this), config);
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	public void example(String data) {
		Log.d(TAG, "example: " + data);
	}
}
