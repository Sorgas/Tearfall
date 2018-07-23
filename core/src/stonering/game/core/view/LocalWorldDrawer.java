package stonering.game.core.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import stonering.enums.designations.DesignationsTileMapping;
import stonering.enums.materials.MaterialMap;
import stonering.game.core.GameMvc;
import stonering.game.core.model.GameContainer;
import stonering.game.core.model.LocalMap;
import stonering.game.core.view.tilemaps.LocalTileMap;
import stonering.global.utils.Position;
import stonering.objects.local_actors.building.BuildingBlock;
import stonering.objects.local_actors.items.Item;
import stonering.objects.local_actors.plants.PlantBlock;
import stonering.objects.local_actors.unit.UnitBlock;
import java.util.ArrayList;

/**
 * Draws LocalMap. Blocks and plants are taken from LocalTileMap,
 * Buildings, units, and items are taken from LocalMap.
 *
 * @author Alexander Kuzyakov on 13.06.2017.
 */
public class LocalWorldDrawer {
    private GameMvc gameMvc;
    private GameContainer container;
    private LocalTileMap localTileMap;
    private SpriteBatch batch;
    private Texture[] atlases;
    private Position camera;
    private LocalMap localMap;
    private int viewAreaWidth;
    private int viewAreDepth;
    private float shadingStep = 0.06f;

    private int tileWidth = 64;
    private int tileHeight = 96;
    private int tileDepth = 64;
    private int topingTileHeight = 70;
    private int blockTileHeight = 166;

    private int screenCenterX;
    private int screenCenterY;

    private int maxX;
    private int maxY;
    private int maxZ;
    private int minX;
    private int minY;
    private int minZ;
    private MaterialMap materialMap;

    public LocalWorldDrawer(GameMvc gameMvc) {
        this.gameMvc = gameMvc;
        materialMap = MaterialMap.getInstance();
        initAtlases();
    }

    public void init() {
        container = gameMvc.getModel();
        localMap = container.getLocalMap();
        setScreenCenterX(Gdx.graphics.getWidth() / 2);
        setScreenCenterY(Gdx.graphics.getHeight() / 2);
        setViewAreaWidth(50);
        setViewAreDepth(15);
    }

    public void drawWorld() {
        if (localTileMap == null)
            localTileMap = container.getLocalTileMap();
        this.camera = container.getCamera().getPosition();
        defineframe();
        batch.begin();
        for (int z = minZ; z <= maxZ; z++) {
            float shading = (camera.getZ() - z) * shadingStep;
            batch.setColor(1 - shading, 1 - shading, 1 - shading, 1);
            for (int x = minX; x <= maxX; x++) {
                for (int y = maxY; y >= minY; y--) {
                    drawTile(x, y, z);
                }
            }
        }
        drawCamera();
        batch.end();
    }

    private void drawTile(int x, int y, int z) {
        drawBlock(x, y, z);
        PlantBlock plantBlock = localMap.getPlantBlock(x, y, z);
        if (plantBlock != null) {
            drawSprite(1, x, y, z, plantBlock.getAtlasX(), plantBlock.getAtlasY());
        }
        BuildingBlock buildingBlock = localMap.getBuildingBlock(x, y, z);
        if (buildingBlock != null) {
            drawSprite(3, x, y, z, 0, 0);
        }
        UnitBlock unitBlock = localMap.getUnitBlock(x, y, z);
        if (unitBlock != null) {
            drawSprite(2, x, y, z, 0, 0);
        }
        ArrayList<Item> items = container.getItemContainer().getItems(x, y, z);
        if (!items.isEmpty()) {
            items.forEach((item) -> drawSprite(5, x, y, z, item.getType().getAtlasX(), item.getType().getAtlasY()));
        }
        if (localMap.getDesignatedBlockType(x, y, z) > 0) {
            drawSprite(4, x, y, z, DesignationsTileMapping.getAtlasX(localMap.getDesignatedBlockType(x, y, z)), 0);
        }
    }

    private void drawSprite(int atlas, int x, int y, int z, int spriteX, int spriteY) {
        batch.draw(new TextureRegion(atlases[atlas],
                        spriteX * tileWidth,
                        spriteY * tileHeight,
                        tileWidth, tileHeight),
                getScreenPosX(x - camera.getX()),
                getScreenPosY(y - camera.getY(), z - camera.getZ()));
    }

    private void drawBlock(int x, int y, int z) {
        int atlas = localTileMap.getAtlasNum(x, y, z);
        if (atlas >= 0) { // not empty cell
            batch.draw(new TextureRegion(atlases[atlas],
                            localTileMap.getAtlasX(x, y, z) * tileWidth,
                            localTileMap.getAtlasY(x, y, z) * (blockTileHeight) + topingTileHeight,
                            tileWidth, tileHeight),
                    getScreenPosX(x - camera.getX()),
                    getScreenPosY(y - camera.getY(), z - camera.getZ()));
        } else {
            int lowerAtlas;
//            if (z > 0 && (lowerAtlas = localTileMap.getAtlasNum(x, y, z - 1)) >= 0) {// not empty cell lower
//                batch.draw(new TextureRegion(atlases[lowerAtlas],
//                                localTileMap.getAtlasX(x, y, z - 1) * tileWidth,
//                                localTileMap.getAtlasY(x, y, z - 1) * (blockTileHeight),
//                                tileWidth, topingTileHeight),
//                        getScreenPosX(x - camera.getX(), y - camera.getY()),
//                        getScreenPosY(x - camera.getX(), y - camera.getY(), z - camera.getZ()));
//            }
        }
    }

    private void drawCamera() {
        batch.draw(new TextureRegion(atlases[4], 0, tileHeight, tileWidth, tileHeight),
                getScreenPosX(0), getScreenPosY(0, 0));
    }

    private void initAtlases() {
        atlases = new Texture[6];
        atlases[0] = new Texture("sprites/blocks4.png");
        atlases[1] = new Texture("sprites/plants.png");
        atlases[2] = new Texture("sprites/units.png");
        atlases[3] = new Texture("sprites/buildings.png");
        atlases[4] = new Texture("sprites/ui_tiles.png");
        atlases[5] = new Texture("sprites/items.png");
    }

    private void defineframe() {
        maxX = camera.getX() + viewAreaWidth;
        if (maxX > localTileMap.getxSize() - 1) {
            maxX = localTileMap.getxSize() - 1;
        }
        minX = camera.getX() - viewAreaWidth;
        if (minX < 0) {
            minX = 0;
        }
        maxY = camera.getY() + viewAreaWidth;
        if (maxY > localTileMap.getySize() - 1) {
            maxY = localTileMap.getySize() - 1;
        }
        minY = camera.getY() - viewAreaWidth;
        if (minY < 0) {
            minY = 0;
        }
        maxZ = camera.getZ();
        if (maxZ > localTileMap.getzSize() - 1) {
            maxZ = localTileMap.getzSize() - 1;
        }
        minZ = camera.getZ() - viewAreDepth;
        if (minZ < 0) {
            minZ = 0;
        }
    }

    private int getScreenPosX(int x) {
        return x * tileWidth + screenCenterX;
    }

    private int getScreenPosY(int y, int z) {
        return y * tileDepth + z * (tileHeight - tileDepth) + screenCenterY;
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