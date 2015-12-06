package com.abdoofathy.progame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class MainGame extends Game {
	
	public OrthographicCamera camera;
	public int SCENE_WIDTH = 480, SCENE_HEIGHT = 320;
	public IBluetooth bluetooth;
	public String macAddress;
	public IActivity iActivity;
	public MainGame(IBluetooth bluetooth,String macAddress,IActivity iActivity){
		this.bluetooth = bluetooth;
		this.macAddress = macAddress;
		this.iActivity = iActivity;
	}
	@Override
	public void create() {
		camera = new OrthographicCamera();
		camera.setToOrtho(false,SCENE_WIDTH,SCENE_HEIGHT);
		camera.translate(camera.viewportWidth/2, camera.viewportHeight/2);
		setScreen(new SplashScreen(this));
	}
	
	@Override
	public void dispose(){
		super.dispose();
		iActivity.exit();
	}
	

}
