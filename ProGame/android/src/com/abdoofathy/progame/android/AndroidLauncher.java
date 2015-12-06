package com.abdoofathy.progame.android;

import com.abdoofathy.progame.IActivity;
import com.abdoofathy.progame.MainGame;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import android.os.Bundle;

public class AndroidLauncher extends AndroidApplication implements IActivity {
	BTManager btManager;
	
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		btManager = new BTManager();
		String macAddress = getIntent().getStringExtra(BTManager.MAC_ADDRESS);
		initialize(new MainGame(btManager,macAddress,this), config);
	}
	
	public void exit(){
		btManager.stop();
		finish();
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		btManager.stop();
	}
}
