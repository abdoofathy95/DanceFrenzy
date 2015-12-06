package com.abdoofathy.progame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class PostSplashScreen implements Screen , InputProcessor{
	private MainGame game;
	private SpriteBatch batch, textBatch;
	private Texture splashTexture, textTexture;
	private Image text;
	private float FADE_TIME = 0.5f;
	
	
	public PostSplashScreen(MainGame game){
		this.game = game;
        batch = new SpriteBatch();
        textBatch = new SpriteBatch();
	}

	@Override
	public void show() {
		splashTexture = new Texture(Gdx.files.internal("splash.png"));
		textTexture = new Texture(Gdx.files.internal("touchToBegin.png"));
		text = new Image(textTexture);
	    text.addAction(Actions.alpha(0));
	    text.act(0);
	    text.addAction(Actions.forever(Actions.sequence(Actions.fadeIn(FADE_TIME), Actions.fadeOut(FADE_TIME))));
		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(splashTexture, 0, 0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        batch.end();
        textBatch.begin();
        text.setX(Gdx.graphics.getWidth()*0.25f);
        text.setY(Gdx.graphics.getHeight()*0.4f);
        text.act(Gdx.graphics.getDeltaTime());
        text.draw(textBatch, 1);
        textBatch.end();
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
		splashTexture.dispose();
		textTexture.dispose();
		textBatch.dispose();
        batch.dispose();
	}

	@Override
	public void dispose() {
		splashTexture.dispose();
		textTexture.dispose();
		textBatch.dispose();
        batch.dispose();
	}

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		game.setScreen(new MainMenuScreen(game));
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}
