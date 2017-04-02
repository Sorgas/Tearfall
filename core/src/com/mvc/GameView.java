package com.mvc;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public interface GameView {

	void init();

    void showFrame();

    void setTileset(Texture img);

    void setSpriteBatch(SpriteBatch batch);

    void setShapeRenderer(ShapeRenderer shapeRenderer);

    void setFont(BitmapFont font);

	void setWindowSize(int width, int height);
}
