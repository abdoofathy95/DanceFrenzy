package com.abdoofathy.progame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class GameScreen implements Screen{
	private MainGame game;
	private Texture upGreen,upPurple , downGreen,downPurple , leftGreen,leftPurple , rightGreen,rightPurple , gameOverBackground;
	private SpriteBatch batch;
	private String n,prevN;
	private boolean showStartMenu = true,showPauseMenu,sending,showGameOver;
	private volatile boolean toggle;
	private Stage startMenu, pauseMenu, background, gameOver;
	private TextButton start , pause, resume, restartButton, quit1, quit2;
	private Skin skin;
	private int fontSize;
	private final int SCREEN_WIDTH = Gdx.graphics.getWidth(),
			SCREEN_HEIGHT = Gdx.graphics.getHeight();
	
	public GameScreen(MainGame game){
		this.game = game;
		batch = new SpriteBatch();
		fontSize = (int)Gdx.graphics.getDensity() * 16;
		createBasicSkin();
	}
	@Override
	public void show() {
		startMenu = new Stage();
		pauseMenu = new Stage();
		background = new Stage();
		gameOver = new Stage();
		Gdx.input.setInputProcessor(startMenu);
		start = new TextButton("Dance!!", skin);
		pause = new TextButton("Pause", skin);
		resume = new TextButton("Resume", skin);
		quit1 = new TextButton("Quit", skin);
		quit2 = new TextButton("Quit", skin);
		restartButton = new TextButton("Play Again?", skin);
		
		start.setPosition(SCREEN_WIDTH*0.4f, SCREEN_HEIGHT*0.6f);
		pause.setPosition(SCREEN_WIDTH*0.1f, SCREEN_HEIGHT*0.1f);
		quit1.setPosition(SCREEN_WIDTH*0.4f, SCREEN_HEIGHT*0.4f);
		restartButton.setPosition(SCREEN_WIDTH*0.25f, SCREEN_HEIGHT*0.3f);
		quit2.setPosition(SCREEN_WIDTH*0.4f, SCREEN_HEIGHT*0.4f);
		resume.setPosition(SCREEN_WIDTH*0.4f, SCREEN_HEIGHT*0.6f);
		start.addListener(new InputListener(){
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				game.bluetooth.sendMessage(IBluetooth.START);
				game.bluetooth.listenForInput();
				showStartMenu = false;
				return true;
			}
		});
		quit1.addListener(new InputListener(){
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				game.iActivity.exit();
				return true;
			}
		});
		quit2.addListener(new InputListener(){
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				game.iActivity.exit();
				return true;
			}
		});
		pause.addListener(new InputListener(){
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				game.bluetooth.stopSending();
				game.bluetooth.sendMessage(IBluetooth.PAUSE);
				sending = true;
				showPauseMenu = true;
				return true;
			}
		});
		resume.addListener(new InputListener(){
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				game.bluetooth.sendMessage(IBluetooth.START);
				game.bluetooth.resumeSending();
				sending = false;
				showPauseMenu = false;
				return true;
			}
		});
		restartButton.addListener(new InputListener(){
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				game.bluetooth.sendMessage(IBluetooth.START);
				game.bluetooth.resumeSending();
				sending = false;
				showGameOver = false;
				return true;
			}
		});
		
		startMenu.addActor(start);
		startMenu.addActor(quit1);
		pauseMenu.addActor(resume);
		pauseMenu.addActor(quit1);
		background.addActor(pause);
		gameOver.addActor(restartButton);
		gameOver.addActor(quit2);
		upGreen = new Texture(Gdx.files.internal("up-green.png"));
		upPurple = new Texture(Gdx.files.internal("up-purple.png"));
		downGreen = new Texture(Gdx.files.internal("down-green.png"));
		downPurple = new Texture(Gdx.files.internal("down-purple.png"));
		leftGreen = new Texture(Gdx.files.internal("left-green.png"));
		leftPurple = new Texture(Gdx.files.internal("left-purple.png"));
		rightGreen = new Texture(Gdx.files.internal("right-green.png"));
		rightPurple = new Texture(Gdx.files.internal("right-purple.png"));
		gameOverBackground = new Texture(Gdx.files.internal("game-over.png"));
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		if(showStartMenu){
			startMenu.draw();
		}
		if(showPauseMenu){
			pauseMenu.draw();
			Gdx.input.setInputProcessor(pauseMenu);
			// stop sending
		}
		if(showGameOver){
			Gdx.gl.glClearColor(0, 0, 0, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			batch.begin();
				batch.draw(gameOverBackground, 0,0,SCREEN_WIDTH,SCREEN_HEIGHT);
			batch.end();
			gameOver.draw();
			Gdx.input.setInputProcessor(gameOver);
		}
		if(!showPauseMenu && !showStartMenu && !showGameOver){
			background.draw();
			Gdx.input.setInputProcessor(background);
			n = game.bluetooth.getGuessedNumber();
			prevN = game.bluetooth.getPrevNumber();
			Thread color = new Thread(){
				public void run(){
					try {
						if(n == prevN){
							toggle = true;
							sleep(IBluetooth.sendingRate);
							toggle = false;
							sleep(IBluetooth.sendingRate);
						}else
							toggle = false;
						
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			};
			color.start();
			if(IBluetooth.QUIT.equals(game.bluetooth.getInput())){ // in case GAME OVER
				game.bluetooth.stopSending();
				game.bluetooth.clearRecvData();
				sending = true;
				showGameOver = true;
			}

			batch.begin();
			if(n == IBluetooth.UP){
				if(toggle){
					batch.draw(upGreen,0,0,SCREEN_WIDTH,SCREEN_HEIGHT);	
				}
				else{
					batch.draw(upPurple,0,0,SCREEN_WIDTH,SCREEN_HEIGHT);
				}
			}
			if(n == IBluetooth.DOWN){
				if(toggle){
					batch.draw(downGreen,0,0,SCREEN_WIDTH,SCREEN_HEIGHT);
				}
				else{
					batch.draw(downPurple,0,0,SCREEN_WIDTH,SCREEN_HEIGHT);
				}
			}
			if(n == IBluetooth.LEFT){
				if(toggle){
					batch.draw(leftGreen,0,0,SCREEN_WIDTH,SCREEN_HEIGHT);
				}
				else{
					batch.draw(leftPurple,0,0,SCREEN_WIDTH,SCREEN_HEIGHT);
				}
			}
			if(n == IBluetooth.RIGHT){
				if(toggle){
					batch.draw(rightGreen,0,0,SCREEN_WIDTH,SCREEN_HEIGHT);	
				}
				else{
					batch.draw(rightPurple,0,0,SCREEN_WIDTH,SCREEN_HEIGHT);	
				}
			}
			batch.end();
			//start sending
			startSending();
		}
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		upGreen.dispose();
		upPurple.dispose();
		downGreen.dispose();
		downPurple.dispose();
		leftGreen.dispose();
		leftPurple.dispose();
		rightGreen.dispose();
		rightPurple.dispose();
	}

	@Override
	public void dispose() {
		batch.dispose();
		upGreen.dispose();
		upPurple.dispose();
		downGreen.dispose();
		downPurple.dispose();
		leftGreen.dispose();
		leftPurple.dispose();
		rightGreen.dispose();
		rightPurple.dispose();
		background.dispose();
		pauseMenu.dispose();
		startMenu.dispose();
		game.dispose();
	}
	
	private void createBasicSkin(){
		  //Create a font
		  FreeTypeFontGenerator fontGen = new FreeTypeFontGenerator(Gdx.files.internal("fonts/cool-font1.ttf"));
		  FreeTypeFontParameter fontParameter = new FreeTypeFontParameter();
		  fontParameter.size = fontSize;
		  BitmapFont font = fontGen.generateFont(fontParameter);
		  fontGen.dispose();
		  skin = new Skin();
		  skin.add("default", font);

		  //Create a texture
		  Pixmap pixmap = new Pixmap((int)Gdx.graphics.getWidth()/4,(int)Gdx.graphics.getHeight()/10, Pixmap.Format.RGB888);
		  pixmap.setColor(Color.WHITE);
		  pixmap.fill();
		  skin.add("background",new Texture(pixmap));

		  
		  //Create a button style
		  TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
		  textButtonStyle.font = skin.getFont("default");
		  skin.add("default", textButtonStyle);

		}

	private void startSending(){
		if(!sending){
		sending = true;
		game.bluetooth.sendEvery(IBluetooth.sendingRate);
		}
	}
}
