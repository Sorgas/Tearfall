package com.view;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.model.GameModel;

public interface GameView {

    void showSnapshot();

    void setTileset(Texture img);

    void setModel(GameModel model);

    void setWidth(int width);

    void setHeight(int height);

    void freeResources();

    void setSpriteBatch(SpriteBatch batch);

    void setShapeRenderer(ShapeRenderer shapeRenderer);

    void setFont(BitmapFont font);
}
