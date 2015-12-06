package com.abdoofathy.progame;

import org.robovm.apple.foundation.NSAutoreleasePool;
import org.robovm.apple.uikit.UIApplication;

import com.badlogic.gdx.backends.iosrobovm.IOSApplication;
import com.badlogic.gdx.backends.iosrobovm.IOSApplicationConfiguration;
import com.abdoofathy.progame.MainGame;

public class IOSLauncher extends IOSApplication.Delegate implements IBluetooth{
    @Override
    protected IOSApplication createApplication() {
        IOSApplicationConfiguration config = new IOSApplicationConfiguration();
        return new IOSApplication(new MainGame(this,"",null), config);
    }

    public static void main(String[] argv) {
        NSAutoreleasePool pool = new NSAutoreleasePool();
        UIApplication.main(argv, null, IOSLauncher.class);
        pool.close();
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