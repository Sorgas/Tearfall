package stonering.game.view.render.stages.renderer;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import stonering.entity.job.designation.Designation;
import stonering.entity.local.building.BuildingBlock;
import stonering.entity.local.items.Item;
import stonering.entity.local.plants.PlantBlock;
import stonering.entity.local.unit.Unit;
import stonering.entity.local.unit.aspects.MovementAspect;
import stonering.entity.local.zone.Zone;
import stonering.enums.blocks.BlockTypesEnum;
import stonering.enums.blocks.BlocksTileMapping;
import stonering.enums.designations.DesignationsTileMapping;
import stonering.enums.materials.MaterialMap;
import stonering.game.GameMvc;
import stonering.game.model.GameModel;
import stonering.game.model.lists.*;
import stonering.game.model.lists.tasks.TaskContainer;
import stonering.game.model.local_map.LocalMap;
import stonering.util.geometry.Int3dBounds;
import stonering.game.view.tilemaps.LocalTileMap;
import stonering.util.geometry.Position;

/**
 * Class for rendering tiles.
 *
 * @author Alexander on 06.02.2019.
 */
public class TileRenderer extends Renderer {
    private LocalMap localMap;
    private LocalTileMap localTileMap;
    private PlantContainer plantContainer;
    private BuildingContainer buildingContainer;
    private UnitContainer unitContainer;
    private TaskContainer taskContainer;
    private ItemContainer itemContainer;
    private ZonesContainer zonesContainer;
    private Int3dBounds visibleArea;

    private Position cachePosition;
    private Vector3 cacheVector;

    public TileRenderer(DrawingUtil drawingUtil, Int3dBounds visibleArea) {
        super(drawingUtil);
        this.visibleArea = visibleArea;
        GameModel model = GameMvc.instance().getModel();
        localMap = model.get(LocalMap.class);
        localTileMap = model.get(LocalTileMap.class);
        buildingContainer = model.get(BuildingContainer.class);
        unitContainer = model.get(UnitContainer.class);
        taskContainer = model.get(TaskContainer.class);
        plantContainer = model.get(PlantContainer.class);
        itemContainer = model.get(ItemContainer.class);
        zonesContainer = model.get(ZonesContainer.class);
        cachePosition = new Position();
        cacheVector = new Vector3();
    }

    /**
     * Renders tiles in visible area.
     */
    @Override
    public void render() {
        for (int z = visibleArea.getMinZ(); z <= visibleArea.getMaxZ(); z++) {
            util.shadeByZ(visibleArea.getMaxZ() - z);
            for (int x = visibleArea.getMinX(); x <= visibleArea.getMaxX(); x++) {
                for (int y = visibleArea.getMaxY(); y >= visibleArea.getMinY(); y--) {
                    drawTile(x, y, z);
                }
            }
            for (int x = visibleArea.getMinX(); x <= visibleArea.getMaxX(); x++) {
                for (int y = visibleArea.getMaxY(); y >= visibleArea.getMinY(); y--) {
                    drawAreaLabel(x, y, z); // for debug purposes
                }
            }
        }
    }

    /**
     * Draws all content of the tile.
     * Draw order: block, water, substrate plants, plants, building, unit, items, designation.
     * //TODO refactor
     */
    private void drawTile(int x, int y, int z) {
        cachePosition.set(x, y, z);
        cacheVector.set(x, y, z); // not changed after
        //byte lightLevel = (byte) (localMap.getLight().getValue(x, y, z) + localMap.getGeneralLight().getValue(x, y, z));  //TODO limit light level
        //util.shadeByLight(lightLevel);
        drawBlock(x, y, z);
        drawSubstrate(x, y, z);
        drawWaterBlock(x, y, z);
        cachePosition.set(x, y, z);
        if (plantContainer != null) drawPlantBlock(plantContainer.getPlantBlocks().get(cachePosition));
        if (buildingContainer != null) drawBuildingBlock(buildingContainer.getBuildingBlocks().get(cachePosition));
        if (unitContainer != null) unitContainer.getUnitsInPosition(x, y, z).forEach(this::drawUnit);
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
        util.drawSprite(getSpriteForBlock(x, y, z), cacheVector);
    }

    /**
     * Selects sprite to draw in given position, block or toping.
     */
    private TextureRegion getSpriteForBlock(int x, int y, int z) {
        int atlasX = getAtlasXForBlock(x, y, z);
        if (atlasX != -1) return util.selectSprite(0, atlasX, getAtlasYForBlock(x, y, z));
        atlasX = getAtlasXForBlock(x, y, z - 1);
        if (atlasX != -1) return util.selectToping(0, atlasX, getAtlasYForBlock(x, y, z - 1));
        return null;
    }

    /**
     * Returns atlas x for given block. Blocks and topings have similar coordinates.
     */
    private int getAtlasXForBlock(int x, int y, int z) {
        byte blockType = localMap.getBlockType(x, y, z);
        if (blockType == BlockTypesEnum.SPACE.CODE) return -1;
        if (blockType == BlockTypesEnum.RAMP.CODE) return localTileMap.getMappedRamp(x, y, z).getVal1();
        return BlocksTileMapping.getType(blockType).ATLAS_X;
    }

    private int getAtlasYForBlock(int x, int y, int z) {
        return MaterialMap.getInstance().getMaterial(localMap.getMaterial(x, y, z)).getAtlasY();
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
        PlantBlock block = plantContainer.getSubstrateBlocks().get(cachePosition.set(x, y, z));
        if (block != null)
            return util.selectSprite(6, localTileMap.getAtlasX(x, y, z), block.getAtlasXY()[1]);
        if (z == 0) return null;
        block = plantContainer.getSubstrateBlocks().get(cachePosition.set(x, y, z - 1));
        if (block != null)
            return util.selectToping(6, localTileMap.getAtlasX(x, y, z - 1), block.getAtlasXY()[1]);
        return null;
    }

    /**
     * Draws water in this tile if needed.
     */
    private void drawWaterBlock(int x, int y, int z) {
        TextureRegion sprite = selectSpriteForFlooding(x, y, z);
        if (sprite == null) return;
        util.updateColorA(0.6f);
        util.drawSprite(sprite, cachePosition);
        util.updateColorA(1f);
    }

    /**
     * Selects sprite for water in given tile, or toping for water in lower cell.
     */
    private TextureRegion selectSpriteForFlooding(int x, int y, int z) {
        int flooding = localMap.getFlooding(x, y, z);
        if (flooding != 0) return util.selectSprite(0, 13 + flooding, 0);
        if (z > 0 && localMap.getFlooding(x, y, z - 1) >= 7) return util.selectToping(0, 20, 0);
        return null;
    }

    /**
     * Draws unit. Calculates position to draw unit, basing on units movement progress.
     * Units in motion are drawn 'between' tiles.
     */
    private void drawUnit(Unit unit) {
        Vector3 vector = new Vector3(unit.getPosition().x, unit.getPosition().y, unit.getPosition().z);
        MovementAspect aspect = unit.getAspect(MovementAspect.class);
        if (aspect != null) vector.add(aspect.getStepProgressVector());
        util.drawSprite(util.selectSprite(2, 0, 0), vector);
    }

    private void drawItem(Item item) {
        util.drawSprite(util.selectSprite(5, item.getType().atlasXY[0], item.getType().atlasXY[1]), item.getPosition());
    }

    private void drawDesignation(Designation designation) {
        if (designation != null)
            util.drawSprite(util.selectSprite(4, DesignationsTileMapping.getAtlasX(designation.getType().CODE), 0), designation.getPosition());
    }

    private void drawZone(Zone zone) {
        if (zone != null) util.drawSprite(zone.getType().sprite, cachePosition);
    }

    private void drawBuildingBlock(BuildingBlock block) {
        if (block != null) util.drawSprite(util.selectSprite(3, 0, 0), cachePosition);
    }

    private void drawPlantBlock(PlantBlock block) {
        if (block != null)
            util.drawSprite(util.selectSprite(1, block.getAtlasXY()[0], block.getAtlasXY()[1]), cacheVector);
    }
}
