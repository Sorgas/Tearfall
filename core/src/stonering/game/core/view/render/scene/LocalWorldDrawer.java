package stonering.game.core.view.render.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import stonering.enums.designations.DesignationsTileMapping;
import stonering.enums.materials.MaterialMap;
import stonering.game.core.GameMvc;
import stonering.game.core.model.EntitySelector;
import stonering.game.core.model.GameContainer;
import stonering.game.core.model.LocalMap;
import stonering.game.core.view.tilemaps.LocalTileMap;
import stonering.entity.local.building.BuildingBlock;
import stonering.entity.local.items.Item;
import stonering.entity.local.plants.PlantBlock;
import stonering.entity.local.unit.UnitBlock;
import stonering.global.utils.Position;

import java.util.ArrayList;

/**
 * Draws LocalMap. Blocks and plants are taken from LocalTileMap,
 * Buildings, units, and items are taken from LocalMap.
 * //TODO move color from batch to sprites
 *
 * @author Alexander Kuzyakov on 13.06.2017.
 */
public class LocalWorldDrawer {
    private GameMvc gameMvc;
    private GameContainer container;
    private LocalTileMap localTileMap;
    private SpriteBatch batch;
    private Texture[] atlases;
    private EntitySelector selector;
    //TODO add SelectionFrame (see EntitySelector & PlaceSelectComponent)
    private LocalMap localMap;
    private int viewAreaWidth;              // radius
    private int viewAreDepth;
    private float shadingStep = 0.06f;
    private Color batchColor;               // default batch color without light or transparency

    private int tileWidth = 64;
    private int tileHeight = 96;
    private int tileDepth = 64;
    private int topingTileHeight = 70;
    private int blockTileHeight = 166;
    private float scale = 0.5f;

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
        batchColor = new Color();
    }

    public void drawWorld() {
        if (localTileMap == null)
            localTileMap = container.getLocalTileMap();
        this.selector = container.getCamera();
        defineframe();
        batch.enableBlending();
        batch.begin();
        for (int z = minZ; z <= maxZ; z++) {
            shadeByZ(selector.getPosition().getZ() - z);
            for (int x = minX; x <= maxX; x++) {
                for (int y = maxY; y >= minY; y--) {
                    drawTile(x, y, z);
                }
            }
        }
        drawSelector();
        drawFrame();
        batch.end();
    }

    /**
     * Draws all content of the tile.
     * Draw order: block, plants, building, unit, items, designation.
     *
     * @param x
     * @param y
     * @param z
     */
    private void drawTile(int x, int y, int z) {
        byte lightLevel = (byte) (localMap.getLight().getValue(x, y, z) + localMap.getGeneralLight().getValue(x, y, z));  //TODO limit light level
        shadeByLight(lightLevel);
        drawBlock(x, y, z);
        updateColorA(0.6f);
        drawWaterBlock(x, y, z);
        updateColorA(1f);
        PlantBlock plantBlock = localMap.getPlantBlock(x, y, z);
        if (plantBlock != null) {
            drawSprite(selectSprite(1, plantBlock.getAtlasX(), plantBlock.getAtlasY()), x, y, z);
        }
        BuildingBlock buildingBlock = localMap.getBuildingBlock(x, y, z);
        if (buildingBlock != null) {
            drawSprite(selectSprite(3, 0, 0), x, y, z);
        }
        UnitBlock unitBlock = localMap.getUnitBlock(x, y, z);
        if (unitBlock != null) {
            drawSprite(selectSprite(2, 0, 0), x, y, z);
        }
        ArrayList<Item> items = container.getItemContainer().getItems(x, y, z);
        if (!items.isEmpty()) {
            items.forEach((item) -> drawSprite(selectSprite(5, item.getType().getAtlasX(), item.getType().getAtlasY()), x, y, z));
        }
        if (localMap.getDesignatedBlockType(x, y, z) > 0) {
            drawSprite(selectSprite(4, DesignationsTileMapping.getAtlasX(localMap.getDesignatedBlockType(x, y, z)), 0), x, y, z);
        }
        resetColor();
    }

    /**
     * Draws block parts.
     *
     * @param x
     * @param y
     * @param z
     */
    private void drawBlock(int x, int y, int z) {
        int atlas = localTileMap.getAtlasNum(x, y, z);
        if (atlas >= 0) { // not empty cell
            drawSprite(selectSprite(atlas, localTileMap.getAtlasX(x, y, z), localTileMap.getAtlasY(x, y, z)), x, y, z);
        } else {
            int lowerAtlas;
            if (z > 0 && (lowerAtlas = localTileMap.getAtlasNum(x, y, z - 1)) >= 0) {// not empty cell lower
                drawTopping(lowerAtlas, x, y, z, localTileMap.getAtlasX(x, y, z - 1), localTileMap.getAtlasY(x, y, z - 1));
            }
        }
    }

    /**
     * Draws water in this tile.
     *
     * @param x
     * @param y
     * @param z
     */
    private void drawWaterBlock(int x, int y, int z) {
        if (localMap.getFlooding(x, y, z) != 0) {
            drawSprite(selectSprite(0, 13 + localMap.getFlooding(x, y, z), 0), x, y, z);
        } else {
            if (z > 0 && localMap.getFlooding(x, y, z - 1) >= 7) {// not empty cell lower
                drawTopping(0, x, y, z, 20, 0);
            }
        }
    }


    /**
     * Draws sprite on position.
     */
    private void drawSprite(TextureRegion sprite, int x, int y, int z) {
        batch.draw(sprite,
                getScreenPosX(x - selector.getPosition().getX()),
                getScreenPosY(y - selector.getPosition().getY(), z - selector.getPosition().getZ()));
    }

    private void drawSprite(TextureRegion sprite, Position position) {
        drawSprite(sprite, position.getX(), position.getY(), position.getZ());
    }

    /**
     * Cuts standard tile from x y position in specified atlas.
     */
    private TextureRegion selectSprite(int atlas, int x, int y) {
        return new TextureRegion(atlases[atlas],
                x * tileWidth,
                y * (blockTileHeight) + topingTileHeight,
                tileWidth, tileHeight);
    }

    /**
     * Draws tile topping part.
     *
     * @param atlas   atlas index to draw from.
     * @param x       screen position
     * @param y
     * @param z
     * @param spriteX sprite coordinates in atlas
     * @param spriteY
     */
    private void drawTopping(int atlas, int x, int y, int z, int spriteX, int spriteY) {
        batch.draw(new TextureRegion(atlases[atlas],
                        spriteX * tileWidth,
                        spriteY * blockTileHeight,
                        tileWidth, topingTileHeight),
                getScreenPosX(x - selector.getPosition().getX()),
                getScreenPosY(y - selector.getPosition().getY(), z - selector.getPosition().getZ()));
    }

    private void drawSelector() {
        drawSprite(selector.getSelectorSprite(), selector.getPosition());
        if (selector.getStatusSprite() != null) {
            drawSprite(selector.getStatusSprite(), selector.getPosition());
        }
    }

    //TODO externalize
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
        maxX = Math.min(selector.getPosition().getX() + viewAreaWidth, localTileMap.getxSize() - 1);
        minX = Math.max(selector.getPosition().getX() - viewAreaWidth, 0);
        maxY = Math.min(selector.getPosition().getY() + viewAreaWidth, localTileMap.getySize() - 1);
        minY = Math.max(selector.getPosition().getY() - viewAreaWidth, 0);
        maxZ = Math.min(selector.getPosition().getZ(), localTileMap.getzSize() - 1);
        minZ = Math.max(selector.getPosition().getZ() - viewAreDepth, 0);
    }

    /**
     * Draws selection frame.
     */
    private void drawFrame() {
        //TODO add landscape dependant rendering
        if (selector.getFrameStart() != null) {
            int minX = Math.min(selector.getFrameStart().getX(), selector.getPosition().getX());
            int maxX = Math.max(selector.getFrameStart().getX(), selector.getPosition().getX());
            int minY = Math.min(selector.getFrameStart().getY(), selector.getPosition().getY());
            int maxY = Math.max(selector.getFrameStart().getY(), selector.getPosition().getY());
            int minZ = Math.min(selector.getFrameStart().getZ(), selector.getPosition().getZ());
            int maxZ = selector.getPosition().getZ();
            for (int x = minX; x <= maxX; x++) {
                for (int y = minY; y <= maxY; y++) {
                    for (int z = minZ; z <= maxZ; z++) {
                        if (y == maxY && z == maxZ) drawSprite(selectSprite(4, 0, 1), x, y, z);
                        if (y == minY && z == maxZ) drawSprite(selectSprite(4, 1, 1), x, y, z);
                        if (x == minX && z == maxZ) drawSprite(selectSprite(4, 2, 1), x, y, z);
                        if (x == maxX && z == maxZ) drawSprite(selectSprite(4, 3, 1), x, y, z);
                        if (y == minY && z == minZ) drawSprite(selectSprite(4, 4, 1), x, y, z);
                        if (y == minY && x == minX) drawSprite(selectSprite(4, 5, 1), x, y, z);
                        if (y == minY && x == maxX) drawSprite(selectSprite(4, 6, 1), x, y, z);
                        if (y == maxY && z == minZ) drawSprite(selectSprite(4, 7, 1), x, y, z);
                        if (x == minX && z > minZ && y == minY) drawSprite(selectSprite(4, 8, 1), x, y, z);
                        if (x == maxX && z > minZ && y == minY) drawSprite(selectSprite(4, 9, 1), x, y, z);
                        updateColorA(0.5f);
                        if (z == maxZ) drawSprite(selectSprite(4, 10, 1), x, y, z);
                        if (y == minY) drawSprite(selectSprite(4, 11, 1), x, y, z);
                        if (z > minZ && y == minY) drawSprite(selectSprite(4, 12, 1), x, y, z);
                        updateColorA(1f);
                    }
                }
            }
        }
    }

    /**
     * Used to determine tile was clicked on.
     *
     * @param screenPos
     * @return
     */
    public Vector2 translateScreenPositionToModel(Vector2 screenPos) {
        Vector2 vector = screenPos.sub(new Vector2(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2)); // to click point from center
        vector.set(vector.x / tileWidth, -vector.y / tileDepth);
        return new Vector2((float) Math.floor(selector.getPosition().getX() + vector.x), (float) Math.floor(selector.getPosition().getY() + vector.y));
    }

    /**
     * Makes color transparent.
     *
     * @param a
     */
    private void updateColorA(float a) {
        Color color = batch.getColor();
        batch.setColor(color.r, color.g, color.b, a);
    }

    /**
     * Shades batch color to correspond lowering z coord.
     *
     * @param dz
     */
    private void shadeByZ(int dz) {
        float shadedColorChannel = 1 - (dz) * shadingStep;
        batchColor.set(shadedColorChannel, shadedColorChannel, shadedColorChannel, 1f);
        resetColor();
    }

    private void resetColor() {
        batch.setColor(batchColor);
    }

    private void shadeByLight(byte lightLevel) {
        float mod = lightLevel / (float) Byte.MAX_VALUE;
        batch.setColor(batchColor.r * mod, batchColor.g * mod, batchColor.b * mod, batchColor.a);
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