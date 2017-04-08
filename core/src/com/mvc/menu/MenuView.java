package com.mvc.menu;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mvc.GameView;

/**
 * Created by Alexander on 02.04.2017.
 */
public class MenuView implements GameView {
	private MenuModel model;
	private MenuController controller;
	private ShapeRenderer shapeRenderer;
	private SpriteBatch spriteBatch;
	private BitmapFont font;

	@Override
	public void init() {

	}

	@Override
	public void showFrame() {
		shapeRenderer.setColor(1,1,0,1);
		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		shapeRenderer.rect(0,0,10,10);
		shapeRenderer.end();
		font.draw(spriteBatch,"qwer",200,200);
	}

	@Override
	public void setTileset(Texture img) {

	}

	@Override
	public void setSpriteBatch(SpriteBatch batch) {
		if (this.spriteBatch != null) {
			this.spriteBatch.dispose();
		}
		this.spriteBatch = batch;
	}

	@Override
	public void setShapeRenderer(ShapeRenderer shapeRenderer) {
		if (this.shapeRenderer != null) {
			this.shapeRenderer.dispose();
		}
		this.shapeRenderer = shapeRenderer;
	}

	@Override
	public void setFont(BitmapFont font) {
		this.font = font;
	}

	@Override
	public void setWindowSize(int width, int height) {

	}

	public void setModel(MenuModel model) {
		this.model = model;
	}

	private void qwer() {
		Stage stage = new Stage(new Viewport() {
		});
	}
}
