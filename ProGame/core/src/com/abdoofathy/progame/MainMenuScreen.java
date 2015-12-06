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

public class MainMenuScreen implements Screen {

	private MainGame game;
	private Texture mainMenuBackground;
	private SpriteBatch batch;
	private Skin skin;
	private Stage stage;
	private TextButton connectButton , quitButton;
	private int fontSize;
	private final int SCREEN_WIDTH = Gdx.graphics.getWidth(),
			SCREEN_HEIGHT = Gdx.graphics.getHeight();
	
	public MainMenuScreen(MainGame game){
		this.game = game;
		batch = new SpriteBatch();
		fontSize = (int)Gdx.graphics.getDensity() * 17;
		createBasicSkin();
	}
	@Override
	public void show() {
		mainMenuBackground = new Texture(Gdx.files.internal("splash.png"));
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		connectButton = new TextButton("Connect To Arduino", skin);
		connectButton.setPosition(SCREEN_WIDTH*0.3f, SCREEN_HEIGHT*0.5f);
		quitButton = new TextButton("Quit", skin);
		quitButton.setPosition(SCREEN_WIDTH*0.5f, SCREEN_HEIGHT*0.3f);
		connectButton.addListener(new InputListener(){
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				game.bluetooth.connectToDevice(game.macAddress);
				return true;
			}
		});
		quitButton.addListener(new InputListener(){
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				game.iActivity.exit();
                return true;
            }
		});
		stage.addActor(connectButton);
		stage.addActor(quitButton);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	    batch.begin();
        batch.draw(mainMenuBackground, 0, 0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        batch.end();
        stage.draw();
        if(game.bluetooth.isConnected()){
        	// move to next screen
        	game.setScreen(new GameScreen(game));
        }
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
		stage.dispose();
		mainMenuBackground.dispose();
		batch.dispose();		
	}

	@Override
	public void dispose() {
		stage.dispose();
		mainMenuBackground.dispose();
		batch.dispose();		
		game.dispose();
	}
}
