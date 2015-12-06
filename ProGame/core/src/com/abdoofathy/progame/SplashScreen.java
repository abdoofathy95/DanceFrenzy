package com.abdoofathy.progame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.TimeUtils;

public class SplashScreen implements Screen{

	private MainGame game;
	private Texture about;
	private Image text;
	private SpriteBatch batch;
	private long startTime;
	private float FADE_TIME = 2.5f; // in milsec
	
	public SplashScreen(MainGame game){
		this.game = game;
		batch = new SpriteBatch();
	}
	@Override
	public void show() {
		startTime = TimeUtils.millis();
		about = new Texture(Gdx.files.internal("product-by.png"));
		text = new Image(about);
		text.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		text.addAction(Actions.forever(Actions.sequence(Actions.fadeIn(FADE_TIME),Actions.fadeOut(FADE_TIME))));
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
        text.act(Gdx.graphics.getDeltaTime());
        text.draw(batch, 1);
        batch.end();
        if(TimeUtils.millis() > (startTime + 5000)){
        	game.setScreen(new PostSplashScreen(game));
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
		about.dispose();
		batch.dispose();
	}

	@Override
	public void dispose() {
		about.dispose();
		batch.dispose();
	}

}
