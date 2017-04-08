package com.frames;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mvc.GameController;
import com.mvc.GameView;
import com.mvc.MvcContainer;

public class GameFrame extends ApplicationAdapter {
	private GameView view;
	private GameController controller;
	private MvcContainer mvcContainer;

	private SpriteBatch batch;
	private Texture img;
	private BitmapFont font;
	private OrthographicCamera camera;
	private Viewport viewport;

	private int worldWidth = 100;
	private int worldHeigth = 100;
	private Sprite world;


	@Override
	public void create() {
		batch = new SpriteBatch();
		font = new BitmapFont();

		img = new Texture("core/assets/qwer.png");
		world = new Sprite(img);
		world.setPosition(0, 0);
		world.setSize(worldWidth, worldHeigth);

//		float aspectRatio = (float) Gdx.graphics.getHeight() / Gdx.graphics.getWidth();

		camera = new OrthographicCamera(100,100);
//		viewport = new ExtendViewport(100, 100, camera);
//		viewport.apply();


		camera.position.set(50, 50, 0);

		view.setSpriteBatch(batch);
		view.setTileset(img);
		view.setFont(font);
	}

	@Override
	public void resize(int width, int height) {
//		viewport.update(width, height);
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(world,0,0);
		world.draw(batch);
//		controller.showFrame();
		batch.end();
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {
		batch.dispose();
		img.dispose();
		font.dispose();
	}

	public void setMvcContainer(MvcContainer mvcContainer) {
		this.mvcContainer = mvcContainer;
		this.view = mvcContainer.getView();
		this.controller = mvcContainer.getController();
	}
}