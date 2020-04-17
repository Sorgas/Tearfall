package stonering.stage.renderer;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import stonering.entity.RenderAspect;
import stonering.entity.job.designation.Designation;
import stonering.entity.item.Item;
import stonering.entity.plant.PlantBlock;
import stonering.entity.zone.Zone;
import stonering.enums.blocks.BlockTypeEnum;
import stonering.enums.blocks.BlockTileMapping;
import stonering.enums.designations.DesignationsTileMapping;
import stonering.enums.materials.MaterialMap;
import stonering.game.GameMvc;
import stonering.game.model.GameModel;
import stonering.game.model.system.*;
import stonering.game.model.system.item.ItemContainer;
import stonering.game.model.system.plant.PlantContainer;
import stonering.game.model.system.substrate.SubstrateContainer;
import stonering.game.model.system.task.TaskContainer;
import stonering.game.model.local_map.LocalMap;
import stonering.stage.localworld.MovableCamera;
import stonering.game.model.tilemaps.LocalTileMap;
import stonering.util.geometry.CoordFunction;
import stonering.util.geometry.Int2dBounds;
import stonering.util.geometry.Position;

import static com.badlogic.gdx.graphics.Pixmap.Format.RGBA8888;
import static stonering.stage.renderer.AtlasesEnum.*;

/**
 * Class for drawing tiles. Contains renderers for different entities. (todo)
 * TODO add render order for buildings, to render flat carpets, pressure plates etc.
 * TODO draw local light spots.
 *
 * @author Alexander on 06.02.2019.
 */
public class TileDrawer extends Drawer {
    private UnitDrawer unitDrawer;
    private BuildingDrawer buildingDrawer;
    
    private LocalMap localMap;
    private LocalTileMap localTileMap;
    private PlantContainer plantContainer;
    private SubstrateContainer substrateContainer;
    private TaskContainer taskContainer;
    private ItemContainer itemContainer;
    private ZoneContainer zoneContainer;

    private MovableCamera camera;
    private boolean disabled = false;

    private Position cachePosition;
    private Vector3 cacheVector;
    private Int2dBounds cacheBounds;
    private TextureRegion blackTile;

    public TileDrawer(SpriteDrawingUtil spriteDrawingUtil, ShapeDrawingUtil shapeDrawingUtil, MovableCamera camera) {
        super(spriteDrawingUtil, shapeDrawingUtil);
        this.camera = camera;
        GameModel model = GameMvc.model();
        localMap = model.get(LocalMap.class);
        localTileMap = model.get(LocalTileMap.class);
        unitDrawer = new UnitDrawer(spriteDrawingUtil, shapeDrawingUtil);
        buildingDrawer = new BuildingDrawer(spriteDrawingUtil, shapeDrawingUtil);
        taskContainer = model.get(TaskContainer.class);
        plantContainer = model.get(PlantContainer.class);
        substrateContainer = model.get(SubstrateContainer.class);
        itemContainer = model.get(ItemContainer.class);
        zoneContainer = model.get(ZoneContainer.class);
        cachePosition = new Position();
        cacheVector = new Vector3();
        cacheBounds = new Int2dBounds();
        Pixmap pixmap = new Pixmap(1, 1, RGBA8888);
        pixmap.setColor(Color.BLACK);
        pixmap.drawPixel(0, 0);
        blackTile = new TextureRegion(new Texture(pixmap));
    }

    public void render() {
        if (disabled) return;
        int maxZ = camera.getCameraZ();
        for (int z = Math.max(maxZ - spriteUtil.maxZLevels, 0); z <= maxZ; z++) {
            spriteUtil.shadeByZ(maxZ - z);
            defineLayerBounds(z);
            iterateLayer(z, this::renderFlatTile);
            iterateLayer(z, this::drawBlockTiles);
            iterateLayer(z, this::drawAreaLabel);
        }
    }

    /**
     * Iterates over bounds of a single z-level and call some function for every tile.
     */
    private void iterateLayer(int z, CoordFunction function) {
        for (int y = cacheBounds.maxY; y >= cacheBounds.minY; y--) {
            for (int x = cacheBounds.minX; x <= cacheBounds.maxX; x++) {
                function.apply(x, y, z);
            }
        }
    }

    /**
     * Calculates visible part of current z-level. Upper levels are not rendered.
     * Lower levels are rendered with same x and y bounds.
     */
    private void defineLayerBounds(int z) {
        cacheBounds.set(BatchUtil.getModelX(camera.getFrame().getMinX()) - 5,
                BatchUtil.getModelY(z, camera.getFrame().getMinY()) - 5,
                BatchUtil.getModelX(camera.getFrame().getMaxX()) + 5,
                BatchUtil.getModelY(z, camera.getFrame().getMaxY()) + 5);
        cacheBounds.clamp(0, 0, localMap.xSize - 1, localMap.ySize - 1);
    }

    private void startTile(int x, int y, int z) {
        cachePosition.set(x, y, z);
        cacheVector.set(x, y, z); // not changed after
//        byte lightLevel = q(byte) (localMap.getLight().get(x, y, z) + localMap.getGeneralLight().get(x, y, z));  //TODO limit light level
//        util.shadeByLight(lightLevel);
    }

    private void renderFlatTile(int x, int y, int z) {
        if (localMap.light.localLight.get(x, y, z) == -1) return;
        startTile(x, y, z);
        drawFloor(x, y, z); // floors or toppings
        if (substrateContainer != null) drawSubstrate(x, y, z);
        spriteUtil.resetColor();
    }

    private void drawBlockTiles(int x, int y, int z) {
        if (localMap.light.localLight.get(x, y, z) == -1) { // draw black tile
            spriteUtil.drawScale(blackTile, cachePosition.set(x, y, z), BatchUtil.TILE_WIDTH, BatchUtil.TILE_DEPTH);
            return;
        }
        startTile(x, y, z);
        unitDrawer.drawUnits(x, y, z);
        drawBlock(x, y, z); // all other
        drawWaterBlock(x, y, z);
        cachePosition.set(x, y, z);
        if (plantContainer != null) drawPlantBlock(plantContainer.getPlantBlock(cachePosition));
        buildingDrawer.drawBuilding(cachePosition);
        if (itemContainer != null) itemContainer.getItemsInPosition(x, y, z).forEach(this::drawItem);
        spriteUtil.updateColorA(0.6f);
        if (taskContainer != null) drawDesignation(taskContainer.designations.get(cachePosition));
        spriteUtil.updateColorA(1f);
        if (zoneContainer != null) drawZone(zoneContainer.getZone(cachePosition));
        spriteUtil.resetColor();
    }

    private void drawAreaLabel(int x, int y, int z) {
        if (localMap.getBlockType(x, y, z) == BlockTypeEnum.SPACE.CODE) return;
        String text = localMap.passageMap.area.get(x, y, z) + " " + localMap.passageMap.getPassage(x, y, z);
        spriteUtil.writeText(text, x, y + 1, z);
    }

    private void drawFloor(int x, int y, int z) {
        BlockTypeEnum type = localMap.getBlockTypeEnumValue(x, y, z);
        if (type == BlockTypeEnum.SPACE && z > 0) { // draw topping for ramps below
            spriteUtil.drawSprite(blocks.getToppingTile(getAtlasXForBlock(x, y, z - 1), getAtlasYForBlock(x, y, z - 1)), cacheVector);
            return;
        }
        if (type == BlockTypeEnum.STAIRS) type = BlockTypeEnum.DOWNSTAIRS; // downstairs rendered under stairs.
        if (!type.FLAT) type = BlockTypeEnum.FLOOR; // floor is rendered under non-flat tiles.
        if (type == BlockTypeEnum.FLOOR || type == BlockTypeEnum.DOWNSTAIRS || type == BlockTypeEnum.FARM) {
            int atlasX = BlockTileMapping.getType(type.CODE).ATLAS_X;
            spriteUtil.drawSprite(blocks.getBlockTile(atlasX, getAtlasYForBlock(x, y, z)), cacheVector);
        }
    }

    private void drawBlock(int x, int y, int z) {
        BlockTypeEnum type = localMap.getBlockTypeEnumValue(x, y, z);
        if (!type.FLAT) spriteUtil.drawSprite(blocks.getBlockTile(getAtlasXForBlock(x, y, z), getAtlasYForBlock(x, y, z)), cacheVector);
    }

    /**
     * Returns atlas x for given block. Blocks and toppings have similar coordinates.
     */
    private int getAtlasXForBlock(int x, int y, int z) {
        byte blockType = localMap.getBlockType(x, y, z);
        if (blockType == BlockTypeEnum.SPACE.CODE) return -1;
        return localTileMap.get(x, y, z).getVal1();
    }

    private int getAtlasYForBlock(int x, int y, int z) {
        return MaterialMap.instance().getMaterial(localMap.getMaterial(x, y, z)).atlasY;
    }

    private void drawSubstrate(int x, int y, int z) {
        TextureRegion region = selectSpriteForSubstrate(x, y, z);
        if (region != null) spriteUtil.drawSprite(region, cacheVector);
    }

    /**
     * Selects substrate sprite for given tile, or toping for substrate lower.
     * Atlas X determined by form of block.
     */
    private TextureRegion selectSpriteForSubstrate(int x, int y, int z) {
        cachePosition.set(x, y, z);
        PlantBlock block = substrateContainer.getSubstrateBlock(cachePosition);
        if (block != null)
            return substrates.getBlockTile(localTileMap.get(cachePosition).getVal1(), block.getAtlasXY()[1]);
        if (z == 0) return null;
        cachePosition.set(x, y, z - 1);
        block = substrateContainer.getSubstrateBlock(cachePosition);
        if (block != null)
            return substrates.getToppingTile(localTileMap.get(cachePosition).getVal1(), block.getAtlasXY()[1]);
        return null;
    }

    /**
     * Draws water in this tile if needed.
     */
    private void drawWaterBlock(int x, int y, int z) {
        TextureRegion sprite = selectSpriteForFlooding(x, y, z);
        if (sprite == null) return;
        spriteUtil.updateColorA(0.6f);
        spriteUtil.drawSprite(sprite, cachePosition.toVector3());
        spriteUtil.updateColorA(1f);
    }

    /**
     * Selects sprite for water in given tile, or toping for water in lower cell.
     */
    private TextureRegion selectSpriteForFlooding(int x, int y, int z) {
        int flooding = localMap.getFlooding(x, y, z);
        if (flooding != 0) return liquids.getBlockTile(flooding - 1, 0);
        if (z > 0 && localMap.getFlooding(x, y, z - 1) >= 7) return liquids.getToppingTile(6, 0);
        return null;
    }

    //TODO refactor to use render aspect
    private void drawItem(Item item) {
        spriteUtil.drawSprite(items.getBlockTile(item.getType().atlasXY[0], item.getType().atlasXY[1]), items, item.position);
    }

    private void drawDesignation(Designation designation) {
        if(designation == null) return;
        RenderAspect aspect = designation.get(RenderAspect.class);
        spriteUtil.drawSprite(aspect.region, designation.position);
    }

    private void drawZone(Zone zone) {
        if (zone != null) spriteUtil.drawSprite(zone.getType().SPRITE, cachePosition.toVector3());
    }
    
    private void drawPlantBlock(PlantBlock block) {
        if (block != null)
            spriteUtil.drawSprite(plants.getBlockTile(block.getAtlasXY()[0], block.getAtlasXY()[1]), cacheVector);
    }
}
