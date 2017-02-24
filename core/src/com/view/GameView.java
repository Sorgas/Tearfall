package com.view;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.model.GameModel;

public interface GameView {

    void showSnapshot();

    void setTileset(Texture img);

    void setModel(GameModel model);

    void setWidth(int width);

    void setHeight(int height);

    void freeResources();

    void setSpriteBatch(SpriteBatch batch);
}
