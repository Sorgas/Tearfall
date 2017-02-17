package com.frames;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.view.GameView;

public class GameFrame implements ApplicationListener {
    private GameView view;
    private SpriteBatch batch;
    private Texture img;

    @Override
	public void create () {
        batch = new SpriteBatch();
        img = new Texture("core/assets/blocks.png");
        view.setSpriteBatch(batch);
        view.setTileset(img);
	}

    @Override
    public void resize(int width, int height) {
        view.setWidth(width);
        view.setHeight(height);
        batch = new SpriteBatch();
        view.setSpriteBatch(batch);
    }

    @Override
	public void render () {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        view.showSnapshot();
	}

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        view.freeResources();
    }

    public void setView(com.view.GameView view) {
        this.view = view;
    }

}