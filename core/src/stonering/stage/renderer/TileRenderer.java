package stonering.stage.renderer;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import stonering.entity.job.designation.Designation;
import stonering.entity.building.BuildingBlock;
import stonering.entity.item.Item;
import stonering.entity.plants.PlantBlock;
import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.CreatureStatusIcon;
import stonering.entity.unit.aspects.MovementAspect;
import stonering.entity.unit.aspects.RenderAspect;
import stonering.entity.zone.Zone;
import stonering.enums.blocks.BlockTypesEnum;
import stonering.enums.designations.DesignationsTileMapping;
import stonering.enums.materials.MaterialMap;
import stonering.game.GameMvc;
import stonering.game.model.GameModel;
import stonering.game.model.system.*;
import stonering.game.model.system.building.BuildingContainer;
import stonering.game.model.system.tasks.TaskContainer;
import stonering.game.model.system.units.UnitContainer;
import stonering.game.model.local_map.LocalMap;
import stonering.stage.localworld.MovableCamera;
import stonering.game.model.tilemaps.LocalTileMap;
import stonering.util.geometry.Int2dBounds;
import stonering.util.geometry.Position;

import java.util.List;

import static com.badlogic.gdx.graphics.Pixmap.Format.RGBA8888;
import static stonering.stage.renderer.AtlasesEnum.*;

/**
 * Class for rendering tiles.
 *
 * @author Alexander on 06.02.2019.
 */
public class TileRenderer extends Renderer {
    private LocalMap localMap;
    private LocalTileMap localTileMap;
    private PlantContainer plantContainer;
    private SubstrateContainer substrateContainer;
    private BuildingContainer buildingContainer;
    private UnitContainer unitContainer;
    private TaskContainer taskContainer;
    private ItemContainer itemContainer;
    private ZonesContainer zonesContainer;
    private MovableCamera camera;
    private boolean disabled = false;

    private Position cachePosition;
    private Vector3 cacheVector;
    private Int2dBounds cacheBounds;
    TextureRegion blackTile;

    public TileRenderer(DrawingUtil drawingUtil, MovableCamera camera) {
        super(drawingUtil);
        this.camera = camera;
        GameModel model = GameMvc.instance().getModel();
        localMap = model.get(LocalMap.class);
        localTileMap = model.get(LocalTileMap.class);
        buildingContainer = model.get(BuildingContainer.class);
        unitContainer = model.get(UnitContainer.class);
        taskContainer = model.get(TaskContainer.class);
        plantContainer = model.get(PlantContainer.class);
        substrateContainer = model.get(SubstrateContainer.class);
        itemContainer = model.get(ItemContainer.class);
        zonesContainer = model.get(ZonesContainer.class);
        cachePosition = new Position();
        cacheVector = new Vector3();
        cacheBounds = new Int2dBounds();
        Pixmap pixmap = new Pixmap(1, 1, RGBA8888);
        pixmap.setColor(Color.BLACK);
        pixmap.drawPixel(0, 0);
        blackTile = new TextureRegion(new Texture(pixmap));
    }

    /**
     * Renders tiles in visible area.
     */
    @Override
    public void render() {
        if (disabled) return;
        int maxZ = camera.getCameraZ();
        int minZ = (int) Math.max(maxZ - util.maxZLevels, 0);
        for (int z = minZ; z <= maxZ; z++) {
            util.shadeByZ(maxZ - z);
            defineLayerBounds(z);
            for (int y = cacheBounds.getMaxY(); y >= cacheBounds.getMinY(); y--) {
                for (int x = cacheBounds.getMinX(); x <= cacheBounds.getMaxX(); x++) {
                    if (localMap.light.localLight.getValue(x, y, z) != -1) {
                        drawTile(x, y, z);
                    } else {
                        util.drawScale(blackTile, cachePosition.set(x, y, z), BatchUtil.TILE_WIDTH, BatchUtil.TILE_DEPTH);
                    }
                }
            }
            for (int y = cacheBounds.getMaxY(); y >= cacheBounds.getMinY(); y--) {
                for (int x = cacheBounds.getMinX(); x <= cacheBounds.getMaxX(); x++) {
                    drawUnits(x, y, z);
//                    drawAreaLabel(x, y, z); // for debug purposes
                }
            }
            //TODO draw local light spots.
        }
    }

    /**
     * Calculates visible part of z level.
     */
    private void defineLayerBounds(int z) {
        cacheBounds.set(BatchUtil.getModelX(camera.getFrame().getMinX()) - 5,
                BatchUtil.getModelY(z, camera.getFrame().getMinY()) - 5,
                BatchUtil.getModelX(camera.getFrame().getMaxX()) + 5,
                BatchUtil.getModelY(z, camera.getFrame().getMaxY()) + 5);
        cacheBounds.clamp(0, 0, localMap.xSize - 1, localMap.ySize - 1);
    }

    /**
     * Draws all content of the tile.
     * Draw order: block, water, substrate plants, plants, building, unit, item, designation.
     */
    private void drawTile(int x, int y, int z) {
        cachePosition.set(x, y, z);
        cacheVector.set(x, y, z); // not changed after
//        byte lightLevel = q(byte) (localMap.getLight().getValue(x, y, z) + localMap.getGeneralLight().getValue(x, y, z));  //TODO limit light level
//        util.shadeByLight(lightLevel);
        drawBlock(x, y, z);
        if (substrateContainer != null) drawSubstrate(x, y, z);
        drawWaterBlock(x, y, z);
        cachePosition.set(x, y, z);
        if (plantContainer != null) drawPlantBlock(plantContainer.getPlantBlock(cachePosition));
        if (buildingContainer != null) drawBuildingBlock(buildingContainer.getBuildingBlocks().get(cachePosition));
        if (itemContainer != null) itemContainer.getItemsInPosition(x, y, z).forEach(this::drawItem);
        if (taskContainer != null) drawDesignation(taskContainer.getDesignation(x, y, z));
        if (zonesContainer != null) drawZone(zonesContainer.getZone(cachePosition));
        util.resetColor();
    }

    private void drawAreaLabel(int x, int y, int z) {
        if (localMap.getBlockType(x, y, z) == BlockTypesEnum.SPACE.CODE) return;
        String text = localMap.getPassage().getArea().getValue(x, y, z) + " " + localMap.getPassage().getPassage(x, y, z);
        util.writeText(text, x, y + 1, z);
    }

    /**
     * Draws block parts. Rendering data is stored in {@link LocalTileMap}.
     * Also draws topping part of lower block.
     */
    private void drawBlock(int x, int y, int z) {
        TextureRegion region = selectSpriteForBlock(x, y, z);
        if (region != null) util.drawSprite(region, cacheVector);
    }

    /**
     * Selects sprite to draw in given position, block or toping.
     */
    private TextureRegion selectSpriteForBlock(int x, int y, int z) {
        int atlasX = getAtlasXForBlock(x, y, z);
        if (atlasX != -1) return blocks.getBlockTile(atlasX, getAtlasYForBlock(x, y, z));
        atlasX = getAtlasXForBlock(x, y, z - 1); // check tile lower
        if (atlasX != -1) return blocks.getToppingTile(atlasX, getAtlasYForBlock(x, y, z - 1));
        return null;
    }

    /**
     * Returns atlas x for given block. Blocks and topings have similar coordinates.
     */
    private int getAtlasXForBlock(int x, int y, int z) {
        byte blockType = localMap.getBlockType(x, y, z);
        if (blockType == BlockTypesEnum.SPACE.CODE) return -1;
        return localTileMap.get(x, y, z).getVal1();
    }

    private int getAtlasYForBlock(int x, int y, int z) {
        return MaterialMap.instance().getMaterial(localMap.getMaterial(x, y, z)).getAtlasY();
    }

    private void drawSubstrate(int x, int y, int z) {
        TextureRegion region = selectSpriteForSubstrate(x, y, z);
        if (region != null) util.drawSprite(region, cacheVector);
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
        util.updateColorA(0.6f);
        util.drawSprite(sprite, cachePosition.toVector3());
        util.updateColorA(1f);
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

    /**
     * Draws units in given cell. Calculates position to draw unit, basing on unit's movement progress.
     * Units in motion are drawn 'between' tiles.
     */
    private void drawUnits(int x, int y, int z) {
        if (unitContainer == null) return;
        for (Unit unit : unitContainer.getUnitsInPosition(x, y, z)) {
            if (!unit.hasAspect(MovementAspect.class)) continue;
            Vector3 vector = unit.getAspect(MovementAspect.class).getStepProgressVector().add(x, y, z);
            RenderAspect aspect = unit.getAspect(RenderAspect.class);
            util.drawSprite(aspect.getTile(), vector);

            List<CreatureStatusIcon> icons = aspect.icons;
            for (int i = 0; i < icons.size(); i++) {
                util.drawIcon(creature_icons.getBlockTile(icons.get(i).x, icons.get(i).y), vector, i);
            }
        }
    }

    private void drawItem(Item item) {
        util.drawSprite(items.getBlockTile(item.getType().atlasXY[0], item.getType().atlasXY[1]), items, item.position);
    }

    private void drawDesignation(Designation designation) {
        if (designation != null)
            util.drawSprite(ui_tiles.getBlockTile(DesignationsTileMapping.getAtlasX(designation.getType().CODE), 0), ui_tiles, designation.getPosition());
    }

    private void drawZone(Zone zone) {
        if (zone != null) util.drawSprite(zone.getType().sprite, cachePosition.toVector3());
    }

    private void drawBuildingBlock(BuildingBlock block) {
        if (block != null) util.drawSprite(buildings.getBlockTile(0, 0), buildings, cachePosition);
    }

    private void drawPlantBlock(PlantBlock block) {
        if (block != null)
            util.drawSprite(plants.getBlockTile(block.getAtlasXY()[0], block.getAtlasXY()[1]), cacheVector);
    }
}
