package stonering.game.view.render.stages.renderer;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import stonering.entity.job.designation.Designation;
import stonering.entity.local.PositionAspect;
import stonering.entity.local.building.BuildingBlock;
import stonering.entity.local.item.Item;
import stonering.entity.local.plants.PlantBlock;
import stonering.entity.local.unit.Unit;
import stonering.entity.local.unit.aspects.MovementAspect;
import stonering.entity.local.zone.Zone;
import stonering.enums.blocks.BlockTypesEnum;
import stonering.enums.designations.DesignationsTileMapping;
import stonering.enums.materials.MaterialMap;
import stonering.game.GameMvc;
import stonering.game.model.GameModel;
import stonering.game.model.lists.*;
import stonering.game.model.lists.tasks.TaskContainer;
import stonering.game.model.local_map.LocalMap;
import stonering.game.view.MovableCamera;
import stonering.game.view.tilemaps.LocalTileMap;
import stonering.util.geometry.Int2dBounds;
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
    private SubstrateContainer substrateContainer;
    private BuildingContainer buildingContainer;
    private UnitContainer unitContainer;
    private TaskContainer taskContainer;
    private ItemContainer itemContainer;
    private ZonesContainer zonesContainer;
    private MovableCamera camera;

    private Position cachePosition;
    private Vector3 cacheVector;
    private Int2dBounds cacheBounds;

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
    }

    /**
     * Renders tiles in visible area.
     */
    @Override
    public void render() {
        int maxZ = camera.getCameraZ();
        int minZ = (int) Math.max(maxZ - util.maxZLevels, 0);
        for (int z = minZ; z <= maxZ; z++) {
            util.shadeByZ(maxZ - z);
            defineLayerBounds(z);
            for (int y = cacheBounds.getMaxY(); y >= cacheBounds.getMinY(); y--) {
                for (int x = cacheBounds.getMinX(); x <= cacheBounds.getMaxX(); x++) {
//                    drawTile(x, y, z);
                }
            }
            for (int y = cacheBounds.getMaxY(); y >= cacheBounds.getMinY(); y--) {
                for (int x = cacheBounds.getMinX(); x <= cacheBounds.getMaxX(); x++) {
                    drawAreaLabel(x, y, z); // for debug purposes
                }
            }
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
        //byte lightLevel = (byte) (localMap.getLight().getValue(x, y, z) + localMap.getGeneralLight().getValue(x, y, z));  //TODO limit light level
        //util.shadeByLight(lightLevel);
        drawBlock(x, y, z);
        if (substrateContainer != null) drawSubstrate(x, y, z);
        drawWaterBlock(x, y, z);
        cachePosition.set(x, y, z);
        if (plantContainer != null) drawPlantBlock(plantContainer.getPlantBlock(cachePosition));
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
        TextureRegion region = selectSpriteForBlock(x, y, z);
        if (region != null) util.drawSprite(region, cacheVector);
    }

    /**
     * Selects sprite to draw in given position, block or toping.
     */
    private TextureRegion selectSpriteForBlock(int x, int y, int z) {
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
        return localTileMap.get(x, y, z).getVal1();
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
        cachePosition.set(x, y, z);
        PlantBlock block = substrateContainer.getSubstrateBlock(cachePosition);
        if (block != null)
            return util.selectSprite(6, localTileMap.get(cachePosition).getVal1(), block.getAtlasXY()[1]);
        if (z == 0) return null;
        cachePosition.set(x, y, z - 1);
        block = substrateContainer.getSubstrateBlock(cachePosition);
        if (block != null)
            return util.selectToping(6, localTileMap.get(cachePosition).getVal1(), block.getAtlasXY()[1]);
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
        util.drawSprite(util.selectSprite(5, item.getType().atlasXY[0], item.getType().atlasXY[1]), item.getAspect(PositionAspect.class).position);
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
