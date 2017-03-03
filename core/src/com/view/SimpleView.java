package com.view;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.model.GameModel;
import com.model.localmap.Level;
import com.model.localmap.MapSnapshot;
import com.model.utils.Position;

public class SimpleView implements GameView {
    private GameModel model;
    private int windowWidth;
    private int windowHeight;
    private int tileWidth = 36;
    private int tileHeight = 41;
    private int floorHeight = 18;
    private SpriteBatch batch;
    private Texture img;
    private TextureRegion block;
    private float shadingStep = 0.08f;
    private BitmapFont font;

    public SimpleView() {
    }

    @Override
    public void setTileset(Texture img) {
        this.img = img;
        block = new TextureRegion(img, 3 * tileWidth, 0 * tileHeight, tileWidth, tileHeight);
    }

    @Override
    public void showSnapshot() {
        batch.begin();
        Position camera = new Position(20, 20, 105);
        MapSnapshot snapshot = model.prepareSnapshot(camera);
        int lowestLevel = camera.getZ() - snapshot.getLayerCount();
        for (int z = lowestLevel; z < camera.getZ(); z++) {
            float shading = (camera.getZ() - z ) * shadingStep;
            Level level = snapshot.getLevel(z - lowestLevel);
            batch.setColor(1 - shading,1 - shading,1 - shading, 1);
            for (int x = 0; x < snapshot.getxSize(); x++) {
                for (int y = 0; y < snapshot.getySize(); y++) {
                    Position onFramePos = centerPosition(transformPosition(new Position(x - camera.getX(), y - camera.getY(), z - camera.getZ())));
                    drawTile(onFramePos, level.getTile(x, y).getCellTypeId());
                }
            }
        }
        batch.end();
    }

    public GameModel getModel() {
        return model;
    }

    public void setModel(GameModel model) {
        this.model = model;
    }

    private void drawTile(Position onFramePos, int id) {
        if (id > 0) {
            if (id == 1) {
                batch.draw(block, onFramePos.getX(), onFramePos.getY());
            } else {
                batch.draw(new TextureRegion(img, (id - 2) * tileWidth, 7 * tileHeight, tileWidth, tileHeight), onFramePos.getX(), onFramePos.getY());
            }
        }
    }

    private Position transformPosition(Position relativePos) {
        int xOffset = (relativePos.getX() - relativePos.getY()) * tileWidth / 2;
        int yOffset = (-relativePos.getX() - relativePos.getY()) * floorHeight / 2;
        yOffset += relativePos.getZ() * (tileHeight - floorHeight);
        return new Position(xOffset, yOffset, 0);
    }

    private Position centerPosition(Position pos) {
        return new Position(pos.getX() + (windowWidth / 2), pos.getY() + (windowHeight / 2), pos.getZ());
    }

    @Override
    public void setWidth(int width) {
        windowWidth = width;
    }

    @Override
    public void setHeight(int height) {
        windowHeight = height;
    }

    @Override
    public void freeResources() {
        batch.dispose();
        img.dispose();
    }

    @Override
    public void setSpriteBatch(SpriteBatch batch) {
        this.batch = batch;
    }

	@Override
	public void setShapeRenderer(ShapeRenderer shapeRenderer) {

	}

	@Override
	public void setFont(BitmapFont font) {
		this.font = font;
	}
}