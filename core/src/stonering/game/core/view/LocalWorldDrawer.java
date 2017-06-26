package stonering.game.core.view;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import stonering.game.core.model.GameContainer;
import stonering.game.core.model.LocalMap;
import stonering.utils.Position;

/**
 * Created by Alexander on 13.06.2017.
 */
public class LocalWorldDrawer {
    private LocalMap map;
    private SpriteBatch batch;
    private Texture tiles;
    private int viewAreaWidth;
    private int viewAreDepth;
    private float shadingStep = 0.08f;
    private int tileWidth = 36;
    private int tileHeight = 41;
    private int floorHeight = 18;
    private int screenCenterX;
    private int screenCenterY;

    public LocalWorldDrawer() {
        tiles = new Texture("sprites/blocks.png");
    }

    public void drawWorld(GameContainer container, Position camera) {
        map = container.getMap();
        batch.begin();
        int highX = camera.getX() + viewAreaWidth;
        if(highX > map.getxSize() - 1) {
            highX = map.getxSize() - 1;
        }
        int lowX = camera.getX() - viewAreaWidth;
        if(lowX < 0) {
            lowX = 0;
        }
        int highY = camera.getY() + viewAreaWidth;
        if(highY > map.getySize() - 1) {
            highY = map.getySize() - 1;
        }
        int lowY = camera.getY() - viewAreaWidth;
        if(lowY < 0) {
            lowY = 0;
        }
        int highZ = camera.getZ();
        if(highZ > map.getzSize() - 1) {
            highZ = map.getzSize() - 1;
        }
        int lowZ = camera.getZ() - viewAreDepth;
        if(lowZ < 0) {
            lowZ = 0;
        }
        for (int z = lowZ; z <= highZ; z++) {
            float shading = (camera.getZ() - z) * shadingStep;
            batch.setColor(1 - shading, 1 - shading, 1 - shading, 1);
            for (int x = lowX; x <= highX; x++) {
                for (int y = lowY; y <= highY; y++) {
                    drawTile(getScreenPosX(x - camera.getX(), y - camera.getY()), getScreenPosY(x - camera.getX(), y - camera.getY(), z - camera.getZ()), map.getBlockType(x, y, z));
                }
            }
        }
        batch.end();
    }

    private int getScreenPosX(int x, int y) {
        return (x - y) * tileWidth / 2 + screenCenterX;
    }

    private int getScreenPosY(int x, int y, int z) {
        return -(x + y) * floorHeight / 2 + z * (tileHeight - floorHeight) + screenCenterY;
    }

    private void drawTile(int x, int y, int blockType) {
        if (blockType > 0) {
            if (blockType == 1) {
                batch.draw(new TextureRegion(tiles, 0 * tileWidth, 0 * tileHeight, tileWidth, tileHeight), x, y);
            }
        }
    }

    public void setScreenCenterX(int screenCenterX) {
        this.screenCenterX = screenCenterX;
    }

    public void setScreenCenterY(int screenCenterY) {
        this.screenCenterY = screenCenterY;
    }

    public void setViewAreaWidth(int viewAreaWidth) {
        this.viewAreaWidth = viewAreaWidth;
    }

    public void setViewAreDepth(int viewAreDepth) {
        this.viewAreDepth = viewAreDepth;
    }

    public void setBatch(SpriteBatch batch) {
        this.batch = batch;
    }
}