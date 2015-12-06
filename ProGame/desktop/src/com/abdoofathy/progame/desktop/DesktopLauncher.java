package com.abdoofathy.progame.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.abdoofathy.progame.IBluetooth;
import com.abdoofathy.progame.MainGame;
import com.abdoofathy.progame.MainMenuScreen;

public class DesktopLauncher implements IBluetooth{
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new MainGame(new DesktopLauncher(),"",null), config);
	}

	@Override
	public void enableBluetooth() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void discoverDevices() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void connectToDevice(String macAddress) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isConnected() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isDiscovering() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void sendMessage(String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void listenForInput() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getInput() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void stopSending() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendEvery(long milSec) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getGuessedNumber() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPrevNumber() {
		// TODO Auto-generated method stub
		return null;
	}
}
